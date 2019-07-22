package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.lib.BooleanLib;
import javalibrary.list.DynamicResultList;
import javalibrary.list.ResultPositive;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.anew.ColumnarTranspositionCipher;
import nationalcipher.cipher.base.anew.KeywordCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.SACipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class ADFGXAttack extends CipherAttack {

    public final char[] alphaChar;
    public final int alphaCount;
    public final Character[] alphabet;
    public JSpinner[] rangeSpinner;
    public JComboBox<Boolean> directionOption;

    public ADFGXAttack(String displayName, String alphaChar, Character[] alphabet) {
        super(displayName);
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY);
        this.alphaChar = alphaChar.toCharArray();
        this.alphaCount = this.alphaChar.length;
        this.alphabet = alphabet;
        this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 12, 1);
        this.directionOption = new JComboBox<Boolean>(BooleanLib.OBJECT_REVERSED);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
        panel.add(new SubOptionPanel("Read down (T) - Read across (F)", this.directionOption));
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        ADFGXTracker task = new ADFGXTracker(text, app);

        // Settings grab
        int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
        task.readDefault = SettingParse.getBooleanValue(this.directionOption);

        if (method == DecryptionMethod.PERIODIC_KEY) {
            for (int length = periodRange[0]; length <= periodRange[1]; length++)
                KeyIterator.permuteIntegerOrderedKey(task::onPermute, length);

            if (task.best.size() < 1) {
                app.out().println("No transposition order with good digraph %cIC found.", (char) 916);
                return;
            }

            app.out().println("Found %d transposition orders with good digraph %cIC.", task.best.size(), (char) 916);
            task.best.sort();

            for (int i = 0; i < task.best.size(); i++) {
                ADFGXResult section = task.best.get(i);

                char[] tempText = new char[section.decrypted.length / 2];

                for (int r = 0; r < this.alphaCount; r++)
                    for (int c = 0; c < this.alphaCount; c++)
                        for (int d = 0; d < section.decrypted.length; d += 2)
                            if (section.decrypted[d] == this.alphaChar[r] && section.decrypted[d + 1] == this.alphaChar[c])
                                tempText[d / 2] = this.alphabet[r * this.alphaCount + c];

                SACipherAttack<String, KeywordCipher> substitutionHack = new SACipherAttack<>(new KeywordCipher(KeyGeneration.ALL_25_CHARS), "");
                DecryptionTracker keywordTracker = substitutionHack.attemptAttack(text, DecryptionMethod.SIMULATED_ANNEALING, app);
                
                if (this.isBetterThanBest(task, keywordTracker.getBestSolution())) {
                    this.updateBestSolution(task, keywordTracker.getBestSolution(), null);
                }
            }
        }

        app.out().println(task.getBestSolution());
    }

    public class ADFGXTracker extends DecryptionTracker {

        public final ColumnarTranspositionCipher transCipher;
        public boolean readDefault;
        private DynamicResultList<ADFGXResult> best;

        public ADFGXTracker(String text, IApplication app) {
            super(text, app);
            this.best = new DynamicResultList<ADFGXResult>(256);
            transCipher = new ColumnarTranspositionCipher();
        }

        public void onPermute(Integer[] data) {
            byte[] decrypted = new byte[this.cipherText.length];
            decrypted = this.transCipher.decodeEfficently(cipherText, plainText, key).decode(this.cipherText, decrypted, BiKey.of(data, this.readDefault));

            double currentSum = Math.abs(StatCalculator.calculateIC(decrypted, 2, false) - this.getLanguage().getNormalCoincidence()) * 1000;

            ADFGXResult section = new ADFGXResult(decrypted, currentSum, Arrays.copyOf(data, data.length));

            if (this.best.add(section))
                if (currentSum < 10D)
                    this.out().println(section.toString());

        }
    }

    public static class ADFGXResult extends ResultPositive {

        public byte[] decrypted;
        public Integer[] order;

        public ADFGXResult(byte[] decrypted, double score, Integer[] inverseCol) {
            super(score);
            this.decrypted = decrypted;
            this.order = inverseCol;
        }

        @Override
        public String toString() {
            if (this.decrypted.length > 100) {
                char[] printDecrypt = new char[105];
                for (int i = 0; i < 50; i++)
                    printDecrypt[i] = this.decrypted[i];
                printDecrypt[50] = ' ';
                printDecrypt[51] = '.';
                printDecrypt[52] = '.';
                printDecrypt[53] = '.';
                printDecrypt[54] = ' ';
                for (int i = 0; i < 50; i++)
                    printDecrypt[i + 55] = this.decrypted[this.decrypted.length - 51 + i];

                return String.format("%s, %f, %s", Arrays.toString(this.order), this.score, new String(printDecrypt));
            } else
                return String.format("%s, %f, %s", Arrays.toString(this.order), this.score, new String(this.decrypted));
        }
    }
}
