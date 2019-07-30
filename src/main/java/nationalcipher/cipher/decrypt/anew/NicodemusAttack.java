package nationalcipher.cipher.decrypt.anew;

import javax.swing.JSpinner;

import nationalcipher.cipher.base.anew.NicodemusCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.IDictionaryAttack;
import nationalcipher.cipher.decrypt.IKeySearchAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.setting.SettingTypes;
import nationalcipher.ui.IApplication;

public class NicodemusAttack extends CipherAttack<BiKey<String, Integer>, NicodemusCipher> implements IKeySearchAttack<BiKey<String, Integer>>, IDictionaryAttack<BiKey<String, Integer>> {

    public JSpinner[] rangeSpinner;
    public JSpinner blockSpinner;

    public NicodemusAttack(NicodemusCipher cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE, DecryptionMethod.PERIODIC_KEY);
        this.addSetting(SettingTypes.createIntRange("period_range", 2, 4, 2, 100, 1, (values, cipher2) -> {cipher2.setFirstKeyLimit(builder -> builder.setRange(values));}));
        this.addSetting(SettingTypes.createIntSpinner("block_size", 5, 1, 100, 1, (values, cipher2) -> {cipher2.setSecondKeyDomain(builder -> builder.setSize(values));}));
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        DecryptionTracker tracker = this.createTracker(text, app);
        switch (method) {
        case PERIODIC_KEY:
            app.getProgress().setIndeterminate(true);
            this.getCipher().getSecondKeyType().iterateKeys(period -> {
                this.tryKeySearch(tracker, period);
                return true;
            });
            return tracker;
        case DICTIONARY:
            return this.tryDictionaryAttack(tracker);
        default:
            return super.attemptAttack(text, method, app);
        }
    }

    @Override
    public BiKey<String, Integer> useStringGetKey(DecryptionTracker tracker, String periodicPart) {
        return this.getCipher().randomiseKey(tracker.getLength()).setFirst(periodicPart);
    }

    @Override
    public BiKey<String, Integer> useWordToGetKey(DecryptionTracker tracker, String word) {
        return this.useStringGetKey(tracker, word);
    }
}
