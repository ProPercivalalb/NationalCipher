package nationalcipher.cipher.decrypt;

import java.math.BigInteger;

import nationalcipher.api.IAttackMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.parallel.MasterThread;

public interface IBruteForceAttack<K> extends IAttackMethod<K> {

    default DecryptionTracker tryBruteForce(DecryptionTracker tracker) {
        BigInteger totalKeys = this.getCipher().getNumOfKeys();
        this.output(tracker, CipherUtils.formatBigInteger(totalKeys) + " Keys to search");
        tracker.getProgress().addMaxValue(totalKeys);

        if (tracker.getSettings().useParallel()) {
            MasterThread thread = new MasterThread((control) -> {
                this.getCipher().iterateKeys(key -> {
                    Runnable job = () -> {
                        this.decryptAndUpdate(tracker, key);
                        tracker.increaseIteration();
                    };
                    while(!control.addJob(job)) {
                        if (control.isFinishing()) { return false; }
                    }
                    
                    return true;
                });
            });
            thread.start();
            
            thread.waitTillCompleted(tracker::shouldStop);
        } else {
            this.getCipher().iterateKeys(key -> {
                if (tracker.shouldStop()) {
                    return false;
                }
                
                this.decryptAndUpdate(tracker, key);
                tracker.increaseIteration();
                
                return true;
            });
        }

        tracker.finish();
        return tracker;
    }
}
