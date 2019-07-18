package nationalcipher.cipher.decrypt.anew;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.anew.CadenusCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.IDictionaryAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class CadenusAttack extends CipherAttack<String, CadenusCipher> implements IDictionaryAttack<String> {

    public JSpinner[] rangeSpinner;
    
    public CadenusAttack() {
        super(new CadenusCipher(), "Cadenus");
        this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE);
        this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 100, 1);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
    }
    
    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
        this.getCipher().setDomain(builder -> builder.setRange(periodRange));
        
        switch (method) {
        case DICTIONARY:
            return this.tryDictionaryAttack(new DecryptionTracker(text, app));
        default:
            return super.attemptAttack(text, method, app);
        }
    }
    
    @Override
    public Supplier<Predicate<String>> getWordFilter(DecryptionTracker tracker) {
        final List<Integer> factors = MathUtil.getFactors(tracker.getCipherText().length() / 25);
        return () -> key -> factors.contains(key.length());
    }
    
    @Override
    public String useWordToGetKey(DecryptionTracker tracker, String word) {
        return word;
    }
    
    @Override
    public Solution toSolution(DecryptionTracker tracker, String key) {
        int blockSize = key.length() * 25;
        char[] plainText = new char[0];
        for (int i = 0; i < tracker.getCipherText().length() / blockSize; i++)
            plainText = ArrayUtil.concat(plainText, CadenusAttack.this.getCipher().decodeEfficently(tracker.getCipherText().subSequence(i * blockSize, (i + 1) * blockSize), key));
        
        return new Solution(plainText, tracker.getLanguage());
    }
}
