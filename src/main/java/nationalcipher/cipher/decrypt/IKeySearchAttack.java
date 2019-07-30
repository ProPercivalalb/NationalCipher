package nationalcipher.cipher.decrypt;

import java.util.Arrays;

import nationalcipher.api.IAttackMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;

/**
 * Used to solve ciphers with a string key where each character in the key
 * independently affects a large proportional of the output text. This is used
 * to attack periodic ciphers such as Vigenere, Beaufort, Porta etc.
 * 
 * @author Alex
 *
 */
public interface IKeySearchAttack<K> extends IAttackMethod<K> {

    default DecryptionTracker tryKeySearch(DecryptionTracker tracker, int length) {
        tracker.getProgress().setIndeterminate(true);
        
        char[] parent = new char[length];
        Arrays.fill(parent, 'A');

        Solution currentBestSolution = Solution.WORST_SOLUTION;

        while (true) {
            boolean change = false;
            for (int i = 0; i < length; i++) {
                for (char j = 'A'; j <= 'Z'; j += this.getCharStep()) {
                    if (tracker.shouldStop()) {
                        return tracker;
                    }
                    
                    //TODO if (!this.hasDuplicateLetters() && )
                        
                    char previous = parent[i];
                    parent[i] = j;

                    tracker.lastSolution = this.toSolution(tracker, this.useStringGetKey(tracker, new String(parent)));
                    tracker.addSolution(tracker.lastSolution);

                    if (tracker.lastSolution.score >= currentBestSolution.score) {
                        currentBestSolution = tracker.lastSolution;
                        currentBestSolution.bake();
                        change = previous != j;
                    } else { // Last solution is worst so revert key
                        parent[i] = previous;
                    }

                    this.onPostIteration(tracker, tracker.iteration);
                }
            }

            // No change to the key resulted in a better solution so we must have found the solution
            if (!change) {
                break;
            }
        }

        this.updateIfBetterThanBest(tracker, currentBestSolution, () -> this.useStringGetKey(tracker, new String(parent)));
        
        tracker.finish();
        return tracker;
    }

    K useStringGetKey(DecryptionTracker tracker, String periodicKeyPart);

    // TODO
    default boolean hasDuplicateLetters() {
        return true;
    }

    default int getCharStep() {
        return 1;
    }

    default void onPostIteration(DecryptionTracker tracker, long iteration) {
        tracker.increaseIteration();
    }
}
