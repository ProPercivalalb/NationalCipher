package nationalcipher.cipher.decrypt;

import java.math.BigInteger;

import javalibrary.lib.Timer;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IAttackMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.ui.NationalCipherUI;

public interface ISimulatedAnnealingAttack<K> extends IAttackMethod<K> {

    default DecryptionTracker trySimulatedAnnealing(DecryptionTracker tracker, int iterations) {
        Timer timer = new Timer();
        double lastDF = 0;

        K bestKey, lastKey;
        
        tracker.getProgress().addMaxValue(BigInteger.valueOf((long) Math.floor((double) tracker.getSettings().getSATempStart() / tracker.getSettings().getSATempStep()) + 1).multiply(BigInteger.valueOf(tracker.getSettings().getSACount())));
        int ite = 0;
        
        stop:
        while (iterations < 0 || ite++ < iterations) {
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

                for (int count = 0; count < tracker.getSettings().getSACount(); count++) {
                    if (tracker.shouldStop()) {
                        break stop;
                    }
                    
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
            
            tracker.getProgress().finish();
            // TODO if(this.iterationTimer)
            // this.output(tracker, "Iteration Time: %f",
            // timer.getTimeRunning(Units.Time.MILLISECOND));
            if (this.endIteration(tracker, tracker.bestSolution)) {
                break;
            }
            
            this.output(tracker, "============================");
        }
        
        return tracker;
    }

    default K generateIntialKey(DecryptionTracker tracker) {
        return this.getCipher().randomiseKey();
    }

    default K modifyKey(DecryptionTracker tracker, K bestMaximaKey, double temp, int count, double lastDF) {
        return this.getCipher().alterKey(bestMaximaKey, temp, count, lastDF);
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
        this.output(tracker, bestSolution.toString());
        NationalCipherUI.BEST_SOULTION = bestSolution.getText();
        return false;
    }
}
