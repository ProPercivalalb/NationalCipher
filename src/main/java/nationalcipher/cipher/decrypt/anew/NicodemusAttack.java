package nationalcipher.cipher.decrypt.anew;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.anew.NicodemusCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.IDictionaryAttack;
import nationalcipher.cipher.decrypt.IKeySearchAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.ui.IApplication;

public class NicodemusAttack extends CipherAttack<BiKey<String, Integer>, NicodemusCipher> implements IKeySearchAttack<BiKey<String, Integer>>, IDictionaryAttack<BiKey<String, Integer>> {

    public JSpinner[] rangeSpinner;
    public JSpinner blockSpinner;

    public NicodemusAttack(NicodemusCipher cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE, DecryptionMethod.PERIODIC_KEY);
        this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 4, 2, 100, 1);
        this.blockSpinner = JSpinnerUtil.createSpinner(5, 1, 100, 1);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
        panel.add(new SubOptionPanel("Block Size:", this.blockSpinner));
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
        int blockSize = SettingParse.getInteger(this.blockSpinner);
        this.getCipher().setFirstKeyLimit(builder -> builder.setRange(periodRange));
        this.getCipher().setSecondKeyLimit(builder -> builder.setSize(blockSize));
        
        // Settings grab
        switch (method) {
        case PERIODIC_KEY:
            app.getProgress().setIndeterminate(true);
            this.periodicKey.setSecond(blockSize);
            this.tryKeySearch(new DecryptionTracker(text, app), app.getProgress(), periodRange[0], periodRange[1]);
            break;
        case DICTIONARY:
            DecryptionTracker tracker = new DecryptionTracker(text, app);
            this.periodicKey.setSecond(blockSize);
            this.tryDictionaryAttack(tracker, app.getProgress());
            break;
        default:
            super.attemptAttack(text, method, app);
            break;
        }
    }

    private BiKey<String, Integer> periodicKey = BiKey.empty();

    @Override
    public BiKey<String, Integer> useStringGetKey(DecryptionTracker tracker, String periodicPart) {
        return CipherUtils.optionalParallel(this.periodicKey::clone, ()->this.periodicKey, tracker).setFirst(periodicPart);
    }

    @Override
    public BiKey<String, Integer> useWordToGetKey(DecryptionTracker tracker, String word) {
        return this.useStringGetKey(tracker, word);
    }
}
