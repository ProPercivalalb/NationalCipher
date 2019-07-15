package nationalcipher.cipher.decrypt;

import java.math.BigInteger;

import javalibrary.lib.Timer;
import javalibrary.swing.ProgressValue;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IAttackMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.ui.NationalCipherUI;

public interface ISimulatedAnnealingAttack<K> extends IAttackMethod<K> {

    default void trySimulatedAnnealing(DecryptionTracker tracker, ProgressValue progBar) {
        Timer timer = new Timer();
        double lastDF = 0;

        K bestKey, lastKey;
        
        progBar.addMaxValue(BigInteger.valueOf((long) Math.floor((double) tracker.getSettings().getSATempStart() / tracker.getSettings().getSATempStep()) + 1).multiply(BigInteger.valueOf(tracker.getSettings().getSACount())));
        
        
        while (true) {
            timer.restart();
            this.startIteration(tracker);
            lastKey = this.generateIntialKey(tracker);
            tracker.lastSolution = this.toSolution(tracker, lastKey);

            K bestMaximaKey = this.generateIntialKey(tracker);
            Solution bestMaximaSolution = tracker.lastSolution;

            if (this.isBetterThanBest(tracker, bestMaximaSolution)) {
                this.updateBestSolution(tracker, bestMaximaSolution, bestMaximaKey);
                bestKey = bestMaximaKey;
            }
            double TEMP = tracker.getSettings().getSATempStart();
            do {
                TEMP = Math.max(0.0D, TEMP - tracker.getSettings().getSATempStep());
                
                
                //tracker.out().println("TEMP: " + TEMP);
                for (int count = 0; count < tracker.getSettings().getSACount(); count++) {
                    this.onPreIteration(tracker);
                    lastKey = this.modifyKey(tracker, bestMaximaKey, TEMP, count, lastDF);
                    tracker.lastSolution = this.toSolution(tracker, lastKey);
                    tracker.addSolution(tracker.lastSolution);

                    lastDF = tracker.lastSolution.score - bestMaximaSolution.score;

                    if (lastDF >= 0) {
                        bestMaximaSolution = tracker.lastSolution;
                        bestMaximaKey = lastKey;
                    } else if (TEMP > 0) {
                        if (TEMP == 0.0D || Math.exp(lastDF / TEMP) > RandomUtil.pickDouble()) {
                            bestMaximaSolution = tracker.lastSolution;
                            bestMaximaKey = lastKey;
                        }
                    }

                    if (this.isBetterThanBest(tracker, bestMaximaSolution)) {
                        this.updateBestSolution(tracker, bestMaximaSolution, bestMaximaKey);
                        bestKey = bestMaximaKey;
                    }

                    this.onPostIteration(tracker);
                }
            } while (TEMP > 0);
            
            progBar.finish();
            // TODO if(this.iterationTimer)
            // tracker.out().println("Iteration Time: %f",
            // timer.getTimeRunning(Units.Time.MILLISECOND));
            if (this.endIteration(tracker, tracker.bestSolution)) {
                break;
            }
            
            tracker.out().println("============================");
        }
    }

    default K generateIntialKey(DecryptionTracker tracker) {
        return this.getCipher().randomiseKey();
    }

    default K modifyKey(DecryptionTracker tracker, K bestMaximaKey, double temp, int count, double lastDF) {
        return this.getCipher().alterKey(bestMaximaKey);
    }

    default void onPreIteration(DecryptionTracker tracker) {
 
    }

    default void onPostIteration(DecryptionTracker tracker) {
        tracker.increaseIteration();
    }

    default void startIteration(DecryptionTracker tracker) {
        tracker.getProgress().setValue(0);
    }
    
    default boolean endIteration(DecryptionTracker tracker, Solution bestSolution) {
        tracker.out().println(bestSolution.toString());
        NationalCipherUI.BEST_SOULTION = bestSolution.getText();
        return false;
    }
}
