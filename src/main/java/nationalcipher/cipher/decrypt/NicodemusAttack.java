package nationalcipher.cipher.decrypt;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.dict.Dictionary;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.api.ICipher;
import nationalcipher.api.IKeySearchAttack;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class NicodemusAttack extends CipherAttack<BiKey<String, Integer>> implements IKeySearchAttack<BiKey<String, Integer>> {

    public JSpinner[] rangeSpinner;

    public NicodemusAttack(ICipher<BiKey<String, Integer>> cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE, DecryptionMethod.PERIODIC_KEY);
        this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {

        // Settings grab
        int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
        switch (method) {
        case PERIODIC_KEY:
            app.getProgress().setIndeterminate(true);
            this.periodicKey.setSecond(5);
            this.tryKeySearch(new DecryptionTracker(text, app), app.getProgress(), periodRange[0], periodRange[1]);
            break;
        case DICTIONARY:
            DecryptionTracker tracker = new DecryptionTracker(text, app);
            app.getProgress().addMaxValue(Dictionary.wordCount());
            BiKey<String, Integer> key = BiKey.of("", 5);
            for (String word : Dictionary.WORDS) {
                tracker.lastSolution = this.toSolution(tracker, key.setFirst(word));
                if (this.isBetterThanBest(tracker, tracker.lastSolution)) {
                    this.updateBestSolution(tracker, tracker.lastSolution, key);
                }
            }
            break;
        default:
            super.attemptAttack(text, method, app);
            break;
        }
    }

    private BiKey<String, Integer> periodicKey = BiKey.empty();

    @Override
    public BiKey<String, Integer> getKey(String periodicPart) {
        return this.periodicKey.setFirst(periodicPart);
    }
}
