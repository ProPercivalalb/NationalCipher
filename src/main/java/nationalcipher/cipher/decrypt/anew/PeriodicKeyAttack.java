package nationalcipher.cipher.decrypt.anew;

import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.IKeySearchAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.setting.SettingTypes;
import nationalcipher.ui.IApplication;

public class PeriodicKeyAttack<C extends UniKeyCipher<String, ? extends IRangedKeyBuilder<String>>> extends CipherAttack<String, C> implements IKeySearchAttack<String> {

    public int[] periodRange;
    private int charStep = 1;
    
    public PeriodicKeyAttack(C cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY, DecryptionMethod.BRUTE_FORCE);
        this.addSetting(SettingTypes.createIntRange("period_range", 2, 15, 2, 100, 1, (range, cipher2) -> {periodRange = range; cipher2.setDomain(builder -> builder.setRange(range)); }));
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        DecryptionTracker tracker = this.createTracker(text, app);
        switch (method) {
        case PERIODIC_KEY:
            for (int period = this.periodRange[0]; period < this.periodRange[1]; period++) {
                this.tryKeySearch(tracker, period);
            }
            return tracker;
        default:
            return super.attemptAttack(text, method, app);
        }
    }

    @Override
    public String useStringGetKey(DecryptionTracker tracker, String periodicPart) {
        return periodicPart;
    }
    
    @Override
    public int getCharStep() {
        return this.charStep;
    }
    
    public PeriodicKeyAttack<C> setCharStep(int charStep) {
        this.charStep = charStep;
        return this;
    }
}
