package nationalcipher.cipher.decrypt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javalibrary.swing.ProgressValue;
import nationalcipher.api.IAttackMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.util.CipherUtils;

public interface IBruteForceAttack<K> extends IAttackMethod<K> {

    default DecryptionTracker tryBruteForce(DecryptionTracker tracker) {
        BigInteger totalKeys = this.getCipher().getNumOfKeys();
        this.output(tracker.out(), CipherUtils.formatBigInteger(totalKeys) + " Keys to search");
        tracker.getProgress().addMaxValue(totalKeys);

        // Run in parallel if option is enabled and there are more than 1000 keys to test, overhead is not worth it otherwise
        Consumer<Consumer<K>> handler = CipherUtils.optionalParallel(b -> b && totalKeys.compareTo(BigInteger.valueOf(1000)) > 0, ()-> {
            if (totalKeys.compareTo(BigInteger.valueOf(Integer.MAX_VALUE - 5)) > 0) {
                throw new UnsupportedOperationException("Too many keys to search in parallel - too many in general brute force is not a recommmened attack method");
            }
            
            this.output(tracker.out(), "Running in parallel");

            List<K> keys = new ArrayList<>(totalKeys.intValue());
            this.getCipher().iterateKeys(keys::add);
            return keys.parallelStream()::forEach;
        }, ()-> {
            return this.getCipher()::iterateKeys;
        }, tracker);
        
        handler.accept(key -> { 
            if (!tracker.shouldStop()) {
                this.decryptAndUpdate(tracker, key);  
            }
        });
        tracker.getProgress().finish();
        return tracker;
    }

    @Override
    default void decryptAndUpdate(DecryptionTracker tracker, K key) {
        IAttackMethod.super.decryptAndUpdate(tracker, key);

        tracker.increaseIteration();
    }
}
