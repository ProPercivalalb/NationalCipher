package nationalcipher.api;

import javalibrary.lib.Timer;
import javalibrary.swing.ProgressValue;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.ui.NationalCipherUI;

public interface ISimulatedAnnealingAttack<K> extends IAttackMethod<K> {

    default void trySimulatedAnnealing(DecryptionTracker tracker, ProgressValue progBar) {
        Timer timer = new Timer();
        double lastDF = 0;
        
        K bestKey, lastKey;
        
        while(true) {
            timer.restart();
            lastKey = this.generateIntialKey(tracker);
            tracker.lastSolution = this.toSolution(tracker, lastKey);
            
            K bestMaximaKey = this.generateIntialKey(tracker);
            Solution bestMaximaSolution = tracker.lastSolution;
            
            if(this.isBetterThanBest(tracker, bestMaximaSolution)) {
                this.updateBestSolution(tracker, bestMaximaSolution, bestMaximaKey);
                bestKey = bestMaximaKey;
            }

            for(double TEMP = tracker.getSettings().getSATempStart(); TEMP >= 0; TEMP -= tracker.getSettings().getSATempStep()) {
                for(int count = 0; count < tracker.getSettings().getSACount(); count++) { 
                    this.onPreIteration(tracker);
                    lastKey = this.modifyKey(tracker, bestMaximaKey, TEMP, count, lastDF);
                    tracker.lastSolution = this.toSolution(tracker, lastKey);
                    tracker.addSolution(tracker.lastSolution);
                    
                    lastDF = tracker.lastSolution.score - bestMaximaSolution.score;
                    
                    if(lastDF >= 0) {
                        bestMaximaSolution = tracker.lastSolution;
                        bestMaximaKey = lastKey;
                    }
                    else if(TEMP > 0) { 
                        double prob = Math.exp(lastDF / TEMP);
                        if(prob > RandomUtil.pickDouble()) {
                            bestMaximaSolution = tracker.lastSolution;
                            bestMaximaKey = lastKey;
                        }
                    }
                    
                    if(this.isBetterThanBest(tracker, bestMaximaSolution)) {
                        this.updateBestSolution(tracker, bestMaximaSolution, bestMaximaKey);
                        bestKey = bestMaximaKey;
                    }
                    
                    this.onPostIteration(tracker);
                }
            }
            //TODO if(this.iterationTimer)
            //    tracker.out().println("Iteration Time: %f", timer.getTimeRunning(Units.Time.MILLISECOND));
            
            if(this.endIteration(tracker, tracker.bestSolution))
                break;
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
        tracker.getProgress().increase();
        tracker.getKeyPanel().updateIteration(tracker.iteration++);
    }
    
    default boolean endIteration(DecryptionTracker tracker, Solution bestSolution) {
        tracker.out().println(bestSolution.toString());
        NationalCipherUI.BEST_SOULTION = bestSolution.getText();
        tracker.getProgress().setValue(0);
        return false;
    }
}
