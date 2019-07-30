package nationalcipher.cipher.decrypt.anew;

import nationalcipher.cipher.base.anew.ProgressiveCipher;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.IKeySearchAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.setting.SettingTypes;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.ui.IApplication;

public class ProgressiveKeyAttack extends CipherAttack<TriKey<String, Integer, Integer>, ProgressiveCipher> implements IKeySearchAttack<TriKey<String, Integer, Integer>> {

    private int[] periodRange = new int[] { 2, 15 };
    
    public ProgressiveKeyAttack(ProgressiveCipher cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY);
        this.addSetting(SettingTypes.createIntRange("period_range", 2, 15, 2, 100, 1, (values, cipher2) -> {periodRange = values; cipher2.setFirstKeyDomain(builder -> builder.setRange(values));}));
        this.addSetting(SettingTypes.createIntRange("prog_period", 1, 30, 1, 100, 1, (values, cipher2) -> {cipher2.setSecondKeyDomain(builder -> builder.setRange(values));}));
        this.addSetting(SettingTypes.createIntRange("prog_key", 1, 25, 1, 100, 1, (values, cipher2) -> {cipher2.setThirdKeyDomain(builder -> builder.setRange(values));}));
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {     
        switch (method) {
        case PERIODIC_KEY:
            // Settings grab
            app.getProgress().setIndeterminate(true);
            DecryptionTracker tracker = new DecryptionTracker(text, app);
            this.getCipher().getSecondKeyType().iterateKeys(progPeriod -> {
                this.getCipher().getThirdKeyType().iterateKeys(progKey -> {
                    this.periodicKey.setSecond(progPeriod);
                    this.periodicKey.setThird(progKey);
                    for (int period = this.periodRange[0]; period < this.periodRange[1]; period++) {
                        this.tryKeySearch(tracker, period);
                    }
                    return true;
                });
                return true;
            });
            return tracker;
        default:
            return super.attemptAttack(text, method, app);
        }
    }

    private TriKey<String, Integer, Integer> periodicKey = TriKey.empty();

    @Override
    public TriKey<String, Integer, Integer> useStringGetKey(DecryptionTracker tracker, String periodicPart) {
        return CipherUtils.optionalParallel(this.periodicKey::clone, ()->this.periodicKey, tracker).setFirst(periodicPart);
    }
}
