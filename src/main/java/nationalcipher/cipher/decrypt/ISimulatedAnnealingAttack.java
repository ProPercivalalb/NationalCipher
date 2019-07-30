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

        tracker.getProgress().addMaxValue(BigInteger.valueOf((long) Math.floor((double) tracker.getSettings().getSATempStart() / tracker.getSettings().getSATempStep()) + 1).multiply(BigInteger.valueOf(tracker.getSettings().getSACount())));
        int ite = 0;

        stop:
        while (iterations < 0 || ite++ < iterations) {
            timer.restart();
            this.startIteration(tracker);

            K bestMaximaKey = this.generateIntialKey(tracker);
            Solution bestMaximaSolution = this.toSolution(tracker, bestMaximaKey);
            this.updateIfBetterThanBest(tracker, bestMaximaSolution, bestMaximaKey);

            double TEMP = tracker.getSettings().getSATempStart();
            do {
                TEMP = Math.max(0.0D, TEMP - tracker.getSettings().getSATempStep());

                for (int count = 0; count < tracker.getSettings().getSACount(); count++) {
                    if (tracker.shouldStop()) {
                        break stop;
                    }

                    this.onPreIteration(tracker);
                    K lastKey = this.modifyKey(tracker, bestMaximaKey, TEMP, count);
                    tracker.lastSolution = this.toSolution(tracker, lastKey);
                    tracker.addSolution(tracker.lastSolution);
                    
                    if (this.shouldAcceptSolution(TEMP, tracker.lastSolution, bestMaximaSolution)) {
                        bestMaximaSolution = tracker.lastSolution;
                        bestMaximaKey = lastKey;
                        this.updateIfBetterThanBest(tracker, bestMaximaSolution, bestMaximaKey);
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
        return this.getCipher().randomiseKey(tracker.getLength());
    }

    default K modifyKey(DecryptionTracker tracker, K bestMaximaKey, double temp, int count) {
        return this.getCipher().alterKey(bestMaximaKey, temp, count);
    }
    
    default boolean shouldAcceptSolution(double TEMP, Solution lastSolution, Solution bestSolution) {
        double lastDF = lastSolution.score - bestSolution.score;
        return lastDF >= 0 || (TEMP > 0 && Math.exp(lastDF / TEMP) > RandomUtil.pickDouble());
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
