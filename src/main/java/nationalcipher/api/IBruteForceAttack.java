package nationalcipher.api;

import javalibrary.swing.ProgressValue;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;

public interface IBruteForceAttack<K> extends IAttackMethod<K> {

    default void tryBruteForce(DecryptionTracker tracker, ProgressValue progBar) {
        progBar.addMaxValue(this.getCipher().getNumOfKeys());
        this.getCipher().iterateKeys(key -> this.onIteration(tracker, key));
    }
    
    default void onIteration(DecryptionTracker tracker, K key) {
        tracker.lastSolution = this.toSolution(tracker, key);
        
        if(this.isBetterThanBest(tracker, tracker.lastSolution)) {
            this.updateBestSolution(tracker, tracker.lastSolution, key);
        }
        
        tracker.getKeyPanel().updateIteration(tracker.iteration++);
        tracker.getProgress().increase();
    }
}
