package nationalcipher.cipher.decrypt;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.api.ICipher;
import nationalcipher.api.IKeySearchAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class PeriodicKeyAttack extends CipherAttack<String> implements IKeySearchAttack<String> {

    public JSpinner[] rangeSpinner;

    public PeriodicKeyAttack(ICipher<String> cipher, String displayName) {
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
        switch (method) {
        case PERIODIC_KEY:
            int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
            this.tryKeySearch(new DecryptionTracker(text, app), app.getProgress(), periodRange[0], periodRange[1]);
            break;
        default:
            super.attemptAttack(text, method, app);
            break;
        }
    }

    @Override
    public String getKey(String periodicPart) {
        return periodicPart;
    }
}
