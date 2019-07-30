package nationalcipher.cipher.decrypt.anew;

import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.list.DynamicResultList;
import javalibrary.math.MathUtil;
import javalibrary.math.matrics.Matrix;
import nationalcipher.cipher.base.anew.HillExtendedCipher;
import nationalcipher.cipher.base.anew.KeywordCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.anew.HillAttack.HillSection;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.setting.SettingTypes;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.ui.IApplication;
import nationalcipher.util.CharArrayWrapper;

public class HillSubsitutionAttack extends CipherAttack<BiKey<Matrix, Matrix>, HillExtendedCipher> {

    private int[] sizeRange = new int[] { 2, 4 };
    
    public HillSubsitutionAttack() {
        super(new HillExtendedCipher(), "Hill Subsitution");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED, DecryptionMethod.PERIODIC_KEY, DecryptionMethod.SIMULATED_ANNEALING);
        this.addSetting(SettingTypes.createIntRange("size_range", 2, 4, 2, 100, 1, (values, cipher) -> {HillSubsitutionAttack.this.sizeRange = values;}));
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        super.createSettingsUI(dialog, panel);
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        HillTracker tracker = new HillTracker(text, app);

        switch (method) {
        case PERIODIC_KEY:
            for (int size = this.sizeRange[0]; size <= this.sizeRange[1]; size++) {
                tracker.resultList.clear();
                if (tracker.getCipherText().length() % size != 0) {
                    this.output(tracker, "Matrix size of %d is not possible, length of text is not a multiple.", size);
                    continue;
                }
                tracker.size = size;
                tracker.lengthSub = tracker.getCipherText().length() / size;

                KeyIterator.iterateIntegerArray(row -> this.iterateMatrixRows(tracker, row), size, 26, true);
                tracker.resultList.sort();
                
                if (tracker.resultList.size() < size) {
                    this.output(tracker, "Did not find enought key columns that produces good characters %d/%d", tracker.resultList.size(), size);
                } else {
                    app.out().println("Trying all combinations...");
                    app.out().println("Removing trials that have no inverse...");
                    app.out().println("Removing trials with %cIC less than 10...", (char) 916);
                    KeyIterator.iterateObject(row -> this.iteratePossibleRows(tracker, row), size, tracker.resultList.toArray(new HillSection[0]));
                    tracker.bestNext.sort();
                    
                    for (HillSection section : tracker.bestNext) {
                        CipherAttack<String, KeywordCipher> substitutionHack = new CipherAttack<>(new KeywordCipher(KeyGeneration.ALL_26_CHARS), "Hill Sub").setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING).setIterations(5).mute();
                        DecryptionTracker keywordTracker = substitutionHack.attemptAttack(new CharArrayWrapper(section.decrypted), DecryptionMethod.SIMULATED_ANNEALING, app);
                        
                        this.updateIfBetterThanBest(tracker, keywordTracker.getBestSolution());
                    }
                }
            }
            return tracker;
        default:
            return super.attemptAttack(text, method, app);
        }
    }
    
    @Override
    public void decryptAndUpdate(DecryptionTracker tracker, BiKey<Matrix, Matrix> key) {
        try {
            super.decryptAndUpdate(tracker, key);
        } catch (MatrixNoInverse | MatrixNotSquareException e) {
            return;
        }
    }
    
    public boolean iterateMatrixRows(HillTracker tracker, Integer[] row) {
        if (MathUtil.allDivisibleBy(row, 0, tracker.size, 2, 13)) {
            return true;
        }

        char[] decrypted = new char[tracker.lengthSub];

        for (int i = 0; i < tracker.getCipherText().length(); i += tracker.size) {
            int total = 0;
            for (int s = 0; s < tracker.size; s++) 
                total += row[s] * (tracker.getCipherText().charAt(i + s) - 'A');

            decrypted[i / tracker.size] =  (char) (total % 26 + 'A');
        }

        double score = Math.abs(StatCalculator.calculateMonoIC(decrypted) - tracker.getLanguage().getNormalCoincidence()) * 1000;

        if (tracker.resultList.add(new HillSection(score, decrypted, Arrays.copyOf(row, row.length)))) {
            if (score < 10D) {
                this.output(tracker, "%s, %f, %s", Arrays.toString(row), score, new String(decrypted));
            }
        }
        return true;
    }

    public boolean iteratePossibleRows(HillTracker tracker, HillSection[] data) {
        // Create key matrix from data
        Integer[] inverseData = new Integer[tracker.size * tracker.size];
        for (int s = 0; s < tracker.size; s++) {
            HillSection hillResult = data[s];
            for (int n = 0; n < tracker.size; n++) {
                inverseData[s * tracker.size + n] = hillResult.inverseCol[n];
            }
        }
        
        Matrix inverseMatrix = new Matrix(inverseData, tracker.size);
        
        if (!inverseMatrix.hasInverseMod(26)) {
            return true;
        }
        
        char[] combinedDecrypted = new char[tracker.getCipherText().length()];

        for (int s = 0; s < tracker.size; s++) {
            HillSection hillSection = data[s];
            for (int i = 0; i < tracker.lengthSub; i++) {
                combinedDecrypted[i * tracker.size + s] = hillSection.decrypted[i];
            }
        }

        double score = Math.abs(StatCalculator.calculateMonoIC(combinedDecrypted) - tracker.getLanguage().getNormalCoincidence()) * 1000;

        if (score < 10D) {
            HillSection section = new HillSection(score, combinedDecrypted, inverseData);
            if (tracker.bestNext.add(section)) {
                this.output(tracker, section.toString());
            }
        }
        return true;
    }

    public class HillTracker extends DecryptionTracker {

        private int size;
        private int lengthSub;
        private DynamicResultList<HillSection> resultList;
        private DynamicResultList<HillSection> bestNext;

        public HillTracker(CharSequence text, IApplication app) {
            super(text, app);
            this.resultList = new DynamicResultList<HillSection>(8);
            this.bestNext = new DynamicResultList<HillSection>(8);
        }
    }
}
