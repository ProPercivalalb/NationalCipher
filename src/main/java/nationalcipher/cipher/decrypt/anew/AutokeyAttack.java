package nationalcipher.cipher.decrypt.anew;

import nationalcipher.cipher.base.anew.AutokeyCipher;
import nationalcipher.cipher.decrypt.IDictionaryAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.ui.IApplication;

public class AutokeyAttack extends PeriodicKeyAttack<AutokeyCipher> implements IDictionaryAttack<String> {

    public AutokeyAttack(AutokeyCipher cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY, DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE);
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
        this.getCipher().setDomain(builder -> builder.setRange(periodRange));
        
        // Settings grab
        switch (method) {
        case PERIODIC_KEY:
            app.getProgress().setIndeterminate(true);
            return this.tryKeySearch(new DecryptionTracker(text, app), periodRange[0], periodRange[1]);
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
