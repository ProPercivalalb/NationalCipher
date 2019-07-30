package nationalcipher.api;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;

public interface IAttackMethod<K> {

    ICipher<K> getCipher();

    /**
     * Decrypts the cipher using the given key checks if it is better than the
     * the best solution and if so updates it.
     * @param tracker The results tracker
     * @param key The key to decrypt using
     */
    default void decryptAndUpdate(DecryptionTracker tracker, K key) {
        Solution solution = this.toSolution(tracker, key);

        synchronized (tracker) {
            this.updateIfBetterThanBest(tracker, solution, key);
        }
    }
    
    default Solution toSolution(DecryptionTracker tracker, K key) {
        return new Solution(this.getCipher().decodeEfficently(tracker.getCipherText(), tracker.getPlainTextHolder(), key), tracker.getLanguage());
    }

    default boolean updateIfBetterThanBest(DecryptionTracker tracker, Solution solution) {
        return this.updateIfBetterThanBest(tracker, solution, (K) null);
    }
    
    default boolean updateIfBetterThanBest(DecryptionTracker tracker, Solution solution, K key) {
        if (this.isBetterThanBest(tracker, solution)) {
            this.updateBestSolution(tracker, solution, key);
            return true;
        }
        
        return false;
    }
    
    default boolean updateIfBetterThanBest(DecryptionTracker tracker, Solution solution, Supplier<K> key) {
        if (this.isBetterThanBest(tracker, solution)) {
            this.updateBestSolution(tracker, solution, key.get());
            return true;
        }
        
        return false;
    }
    
    default boolean isBetterThanBest(DecryptionTracker tracker, Solution solution) {
        return solution.isBetterThan(tracker.bestSolution);
    }

    /**
     * Updates the best solution. Converts the key to a readable form, copies the
     * plaintext array, prints out the solution and updates the solution panel UI.
     * 
     * @param tracker  The
     *                 {@link nationalcipher.cipher.decrypt.methods.DecryptionTracker}
     *                 instances that tracks best solutions
     * @param solution The solution to become the best solution
     * @param key      The key to generate this solution, can be null which indicates
     *                 the keystring was already set at some point
     */
    default void updateBestSolution(DecryptionTracker tracker, Solution solution, @Nullable K key) {
        tracker.bestSolution = solution;
        
        if (key != null) {
            tracker.bestSolution.setKeyString(this.getCipher().prettifyKey(key));
        }
        
        tracker.bestSolution.bake();
        tracker.addSolution(tracker.bestSolution);
        this.output(tracker, tracker.bestSolution.toString());
        tracker.getKeyPanel().updateSolution(tracker.bestSolution);
    }
    
    
    /**
     * @return If the cipher attack is muted
     */
    default boolean isMuted() {
        return false;
    }
    
    default boolean output(DecryptionTracker tracker, String text, Object... format) {
        if(!this.isMuted() && !tracker.shouldStop()) {
            tracker.out().println(text, format);
            return true;
        }
        
        return false;
    }
}
