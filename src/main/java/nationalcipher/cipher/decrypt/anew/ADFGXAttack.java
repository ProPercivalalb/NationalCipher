package nationalcipher.cipher.decrypt.anew;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javalibrary.language.ILanguage;
import javalibrary.list.DynamicResultList;
import javalibrary.list.Result;
import javalibrary.list.ResultPositive;
import javalibrary.streams.PrimTypeUtil;
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
import nationalcipher.cipher.setting.SettingTypes;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.parallel.MasterThread;
import nationalcipher.ui.IApplication;
import nationalcipher.util.CharArrayWrapper;

public class ADFGXAttack extends CipherAttack<QuadKey<String, Integer[], String, ReadMode>, ADFGXCipher> {

    public final Character[] alphabet; //Key square characters
    public final Character[] charHolders; //ADFGX characters
    
    public ADFGXAttack() {
        super(new ADFGXCipher(), "ADFGX");
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY);
        this.alphabet = KeyGeneration.ALL_25_CHARS;
        this.charHolders = PrimTypeUtil.toCharacterArray("ADFGX");
        this.addSetting(SettingTypes.createIntRange("period_range", 2, 5, 2, 100, 1, (values, cipher) -> {cipher.setSecondKeyLimit(builder -> builder.setRange(values));}));
        this.addSetting(SettingTypes.createCombo("read_mode", ReadMode.values(), (value, cipher) -> {cipher.setFourthKeyLimit(builder -> builder.setUniverse(value));}));
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        ADFGXTracker tracker = new ADFGXTracker(text, app);

        switch (method) {
        case PERIODIC_KEY:
            IKeyType<Integer[]> orderedKey = this.getCipher().getSecondKeyType();
            
            app.getProgress().addMaxValue(orderedKey.getNumOfKeys());
            
            if (tracker.getSettings().useParallel()) {
                MasterThread thread = new MasterThread((control) -> {
                    orderedKey.iterateKeys(key -> {
                        Integer[] keyCopy = ArrayUtil.copy(key);
                        Runnable job = () -> {
                            tracker.onPermute(keyCopy);
                        };
                        while(!control.addJob(job)) {
                            if (control.isFinishing()) { return false; }
                        }
                        
                        return true;
                    });
                });
                thread.start();
                
                thread.waitTillCompleted(tracker::shouldStop);
                
            } else {
                orderedKey.iterateKeys(tracker::onPermute);
            }
            tracker.finish();
            
            if (tracker.resultsList.size() < 1) {
                this.output(tracker, "No transposition order with good digraph %cIC found.", (char) 916);
                return null;
            }

            this.output(tracker, "Found %d transposition orders with good digraph %cIC.", tracker.resultsList.size(), (char) 916);
            tracker.resultsList.sort();

            Iterator<ADFGXResult> iterator = tracker.resultsList.iterator();
            
            while (iterator.hasNext()) {
                if (tracker.shouldStop()) { break; }
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

                CipherAttack<String, KeywordCipher> substitutionHack = new CipherAttack<>(new KeywordCipher(KeyGeneration.ALL_25_CHARS), "ADFGX Sub").setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING).setIterations(3).mute();
                DecryptionTracker keywordTracker = substitutionHack.attemptAttack(new CharArrayWrapper(tempText), DecryptionMethod.SIMULATED_ANNEALING, app);

                this.updateIfBetterThanBest(tracker, keywordTracker.getBestSolution());
                keywordTracker.getProgress().reset();
            }
            return tracker;
        default:
            return super.attemptAttack(text, method, app);
        }
    }
    
    public class ADFGXTracker extends DecryptionTracker {

        public final ColumnarTranspositionCipher transCipher;
        private DynamicResultList<ADFGXResult> resultsList;

        public ADFGXTracker(CharSequence text, IApplication app) {
            super(text, app);
            this.resultsList = new DynamicResultList<ADFGXResult>(256);
            this.transCipher = new ColumnarTranspositionCipher();
        }

        public boolean onPermute(Integer[] data) {
            if (this.shouldStop()) { return false; }
            char[] decrypted = new char[this.getOutputTextLength(this.getCipherText().length())];
            ADFGXAttack.this.getCipher().getFourthKeyType().iterateKeys(readMode -> {
                if (this.shouldStop()) { return false; }
                this.transCipher.decodeEfficently(this.getCipherText(), decrypted, BiKey.of(data, readMode));

                ADFGXResult section = new ADFGXResult(decrypted, this.getLanguage(), Arrays.copyOf(data, data.length));

                if (this.resultsList.add(section)) {
                    if (section.score < 5D) {
                        ADFGXAttack.this.output(this, section.toString());
                    }
                }
                
                this.increaseIteration();
                return true;
            });
            return true;
        }
    }

    public static class ADFGXResult extends ResultPositive {

        public char[] decrypted;
        public Integer[] order;

        public ADFGXResult(char[] decrypted, ILanguage language, Integer[] inverseCol) {
            super(Math.abs(StatCalculator.calculateIC(new CharArrayWrapper(decrypted), 2, false) - language.getNormalCoincidence()) * 1000);
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
