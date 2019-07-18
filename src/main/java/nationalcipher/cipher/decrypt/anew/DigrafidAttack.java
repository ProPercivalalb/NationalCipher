package nationalcipher.cipher.decrypt.anew;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.anew.DigrafidCipher;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;

public class DigrafidAttack extends CipherAttack<TriKey<String, String, Integer>, DigrafidCipher> {

    public JSpinner spinner;

    public DigrafidAttack() {
        super(new DigrafidCipher(), "Digrafid");
        this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
        this.spinner = JSpinnerUtil.createSpinner(ArrayUtil.concat(new Integer[] { 0 }, ArrayUtil.createRangeInteger(2, 101)));
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Period:", this.spinner));
    }

    @Override
    public TriKey<String, String, Integer> generateIntialKey(DecryptionTracker tracker) {
        return this.getCipher().randomiseKey().setThird(SettingParse.getInteger(this.spinner));
    }
}
