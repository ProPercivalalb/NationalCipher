package nationalcipher.cipher.decrypt.anew;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.IKeySearchAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class PeriodicKeyAttack<C extends UniKeyCipher<String, ? extends IRangedKeyBuilder<String>>> extends CipherAttack<String, C> implements IKeySearchAttack<String> {

    public JSpinner[] rangeSpinner;

    public PeriodicKeyAttack(C cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY);
        this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
        this.getCipher().setDomain(builder -> builder.setRange(periodRange));
        
        switch (method) {
        case PERIODIC_KEY:
            this.tryKeySearch(new DecryptionTracker(text, app), app.getProgress(), periodRange[0], periodRange[1]);
            break;
        default:
            super.attemptAttack(text, method, app);
            break;
        }
    }

    @Override
    public String useStringGetKey(DecryptionTracker tracker, String periodicPart) {
        return periodicPart;
    }
}
