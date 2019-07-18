package nationalcipher.cipher.decrypt.anew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.language.ILanguage;
import javalibrary.lib.BooleanLib;
import javalibrary.list.DynamicResultList;
import javalibrary.list.Result;
import javalibrary.list.ResultPositive;
import javalibrary.streams.PrimTypeUtil;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.anew.ADFGXCipher;
import nationalcipher.cipher.base.anew.ColumnarTranspositionCipher;
import nationalcipher.cipher.base.anew.KeywordCipher;
import nationalcipher.cipher.base.anew.ReadMode;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.ui.IApplication;
import nationalcipher.util.CharArrayWrapper;

public class ADFGXAttack extends CipherAttack<QuadKey<String, Integer[], String, ReadMode>, ADFGXCipher> {

    public JSpinner[] rangeSpinner;
    public JComboBox<Boolean> directionOption;
    public final Character[] alphabet; //Key square characters
    public final Character[] charHolders; //ADFGX characters
    
    public ADFGXAttack() {
        super(new ADFGXCipher(), "ADFGX");
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY);
        this.alphabet = KeyGeneration.ALL_25_CHARS;
        this.charHolders = PrimTypeUtil.toCharacterArray("ADFGX");
        this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 12, 1);
        this.directionOption = new JComboBox<Boolean>(BooleanLib.OBJECT_REVERSED);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
        panel.add(new SubOptionPanel("Read down (T) - Read across (F)", this.directionOption));
    }
    
    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        ADFGXTracker tracker = new ADFGXTracker(text, app);
        
        int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
        tracker.readDefault = SettingParse.getBooleanValue(this.directionOption) ? ReadMode.DOWN : ReadMode.ACROSS;
        
        // Settings grab
        switch (method) {
        case PERIODIC_KEY:
            IKeyType<Integer[]> orderedKey = this.getCipher().setSecondKeyLimit(builder -> builder.setRange(periodRange));
            
            app.getProgress().addMaxValue(orderedKey.getNumOfKeys());
            
            Consumer<Consumer<Integer[]>> handler = CipherUtils.optionalParallel(()-> {
                List<Integer[]> keys = new ArrayList<>(orderedKey.getNumOfKeys().intValue());
                orderedKey.iterateKeys(null, order -> keys.add(ArrayUtil.copy(order)));
                return keys.parallelStream()::forEach;
            }, ()-> {
                return task -> orderedKey.iterateKeys(null, task);
            }, tracker);

            handler.accept(tracker::onPermute);
            tracker.finish();
            
            if (tracker.resultsList.size() < 1) {
                this.output(app.out(), "No transposition order with good digraph %cIC found.", (char) 916);
                return null;
            }

            this.output(app.out(), "Found %d transposition orders with good digraph %cIC.", tracker.resultsList.size(), (char) 916);
            tracker.resultsList.sort();

            Iterator<ADFGXResult> iterator = tracker.resultsList.iterator();
            
            while (iterator.hasNext()) {
                ADFGXResult section = iterator.next();

                char[] tempText = new char[section.decrypted.length / 2];

                for (int r = 0; r < this.charHolders.length; r++) {
                    for (int c = 0; c < this.charHolders.length; c++) {
                        for (int d = 0; d < section.decrypted.length; d += 2) {
                            if (section.decrypted[d] == this.charHolders[r] && section.decrypted[d + 1] == this.charHolders[c]) {
                                tempText[d / 2] = this.alphabet[r * this.charHolders.length + c];
                            }
                        }
                    }
                }
                
                CipherAttack<String, KeywordCipher> substitutionHack = new CipherAttack<>(new KeywordCipher(KeyGeneration.ALL_25_CHARS), "ADFGX Sub").setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING).setIterations(5).mute();
                DecryptionTracker keywordTracker = substitutionHack.attemptAttack(new CharArrayWrapper(tempText), DecryptionMethod.SIMULATED_ANNEALING, app);
                
                if (this.isBetterThanBest(tracker, keywordTracker.getBestSolution())) {
                    this.updateBestSolution(tracker, keywordTracker.getBestSolution(), null);
                }
            }
            return tracker;
        default:
            return super.attemptAttack(text, method, app);
        }
    }
    
    public class ADFGXTracker extends DecryptionTracker {

        public final ColumnarTranspositionCipher transCipher;
        public ReadMode readDefault;
        private DynamicResultList<ADFGXResult> resultsList;

        public ADFGXTracker(CharSequence text, IApplication app) {
            super(text, app);
            this.resultsList = new DynamicResultList<ADFGXResult>(256);
            this.transCipher = new ColumnarTranspositionCipher();
        }

        public void onPermute(Integer[] data) {
            char[] decrypted = new char[this.getOutputTextLength(this.getCipherText().length())];
            this.transCipher.decodeEfficently(this.getCipherText(), decrypted, BiKey.of(data, this.readDefault));

            ADFGXResult section = new ADFGXResult(decrypted, this.getLanguage(), Arrays.copyOf(data, data.length));

            if (this.resultsList.add(section)) {
                if (section.score < 5D) {
                    ADFGXAttack.this.output(this.out(), section.toString());
                }
            }
            
            this.increaseIteration();
        }
    }

    public static class ADFGXResult extends ResultPositive {

        public char[] decrypted;
        public Integer[] order;

        public ADFGXResult(char[] decrypted, ILanguage language, Integer[] inverseCol) {
            super(Math.abs(StatCalculator.calculateIC(decrypted, 2, false) - language.getNormalCoincidence()) * 1000);
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
        
        @Override
        public int compareTo(Result other) {
            int value = super.compareTo(other);
            
            return value == 0 ? Integer.compare(this.order.length, ((ADFGXResult)other).order.length) : value;
        }
    }
}
