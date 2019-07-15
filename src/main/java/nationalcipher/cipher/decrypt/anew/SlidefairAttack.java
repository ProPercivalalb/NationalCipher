package nationalcipher.cipher.decrypt.anew;

import nationalcipher.cipher.base.anew.SlidefairCipher;
import nationalcipher.cipher.decrypt.IDictionaryAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.ui.IApplication;

public class SlidefairAttack extends PeriodicKeyAttack<SlidefairCipher> implements IDictionaryAttack<String> {

    public SlidefairAttack(SlidefairCipher cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY, DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE);
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
        this.getCipher().setDomain(builder -> builder.setRange(periodRange));
        
        // Settings grab
        switch (method) {
        case PERIODIC_KEY:
            app.getProgress().setIndeterminate(true);
            this.tryKeySearch(new DecryptionTracker(text, app), app.getProgress(), periodRange[0], periodRange[1]);
            break;
        case DICTIONARY:
            DecryptionTracker tracker = new DecryptionTracker(text, app);
            this.tryDictionaryAttack(tracker, app.getProgress());
            break;
        default:
            super.attemptAttack(text, method, app);
            break;
        }
    }
    
    @Override
    public String useWordToGetKey(DecryptionTracker tracker, String word) {
        return this.useStringGetKey(tracker, word);
    }
}
