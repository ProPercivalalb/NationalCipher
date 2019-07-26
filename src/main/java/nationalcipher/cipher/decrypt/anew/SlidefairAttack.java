package nationalcipher.cipher.decrypt.anew;

import nationalcipher.cipher.base.anew.SlidefairCipher;
import nationalcipher.cipher.decrypt.IDictionaryAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.ui.IApplication;

public class SlidefairAttack extends PeriodicKeyAttack<SlidefairCipher> implements IDictionaryAttack<String> {

    public SlidefairAttack(SlidefairCipher cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY, DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE);
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        // Settings grab
        switch (method) {
        case DICTIONARY:
            DecryptionTracker tracker = new DecryptionTracker(text, app);
            return this.tryDictionaryAttack(tracker);
        default:
            return super.attemptAttack(text, method, app);
        }
    }
    
    @Override
    public String useWordToGetKey(DecryptionTracker tracker, String word) {
        return this.useStringGetKey(tracker, word);
    }
}
