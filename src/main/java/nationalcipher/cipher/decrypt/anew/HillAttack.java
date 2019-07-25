package nationalcipher.cipher.decrypt.anew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.algebra.SimultaneousEquations;
import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.fitness.ChiSquared;
import javalibrary.list.DynamicResultList;
import javalibrary.list.ResultPositive;
import javalibrary.math.MathUtil;
import javalibrary.math.matrics.Matrix;
import javalibrary.streams.FileReader;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.anew.HillCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.anew.HillAttack.HillSection;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.setting.SettingTypes;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class HillAttack extends CipherAttack<Matrix, HillCipher> {

    public JSpinner gramSearchRange;
    public JComboBox<String> trigramSets;
    public final String[][] BI_GRAM = new String[][] { new String[] { "TH", "HE" } };
    public final String[][] TRI_GRAM = new String[][] { new String[] { "ENT", "LES", "ION" }, new String[] { "THE", "ING", "ENT" }, new String[] { "THE", "ING", "ION" }, new String[] { "THE", "ENT", "ION" }, new String[] { "ING", "ENT", "HER" }, new String[] { "ING", "ENT", "FOR" }, new String[] { "ENT", "ION", "HER" }, new String[] { "ING", "ENT", "THA" }, new String[] { "ING", "ENT", "NTH" } };
    // public final String[][] TRI_GRAM = new String[][] {new String[] {"THE",
    // "AND", "ING"}, new String[] {"THE", "ING", "ENT"}, new String[] {"THE",
    // "ING", "ION"}, new String[] {"THE", "ENT", "ION"}, new String[] {"ING",
    // "ENT", "HER"}, new String[] {"ING", "ENT", "FOR"}, new String[] {"ENT",
    // "ION", "HER"}, new String[] {"ING", "ENT", "THA"}, new String[] {"ING",
    // "ENT", "NTH"}};
    public final String[] TRI_GRAM_DISPLAY = new String[] { "THE AND ING", "THE ING ENT", "THE ING ION", "THE ENT ION", "ING ENT HER", "ING ENT FOR", "ENT ION HER", "ING ENT THA", "ING ENT NTH" };
    private int[] sizeRange = new int[] { 2, 4 };
    
    public HillAttack() {
        super(new HillCipher(), "Hill");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED, DecryptionMethod.PERIODIC_KEY, DecryptionMethod.SIMULATED_ANNEALING);
        this.addSetting(SettingTypes.createIntRange(2, 4, 2, 100, 1, (values, cipher) -> {HillAttack.this.sizeRange = values;}));
        this.gramSearchRange = JSpinnerUtil.createSpinner(20, 3, 100, 1);
        this.trigramSets = new JComboBox<String>(this.TRI_GRAM_DISPLAY);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        super.createSettingsUI(dialog, panel);
        panel.add(new SubOptionPanel("N-Gram Search Range:", this.gramSearchRange));
        panel.add(new SubOptionPanel("3x3 Trigram Sets:", this.trigramSets));
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        HillTracker tracker = new HillTracker(text, app);

        // Settings grab
        int gramSearchRange = SettingParse.getInteger(this.gramSearchRange);
        String[][] commonGrams = new String[][] { BI_GRAM[0], TRI_GRAM[this.trigramSets.getSelectedIndex()] };

        switch (method) {
        case CALCULATED:
            this.readLatestSettings();
            for (int size = this.sizeRange[0]; size <= this.sizeRange[1]; size++) {
                Map<String, Integer> chars = StringAnalyzer.getEmbeddedStrings(text, size, size, false);
                List<String> sorted = new ArrayList<String>(chars.keySet());
                Collections.sort(sorted, new StringAnalyzer.SortStringInteger(chars));
                Collections.reverse(sorted);
                this.output(tracker, "" + chars.size());
                int[][] pickPattern = this.generatePickPattern(size, Math.min(gramSearchRange, sorted.size()));

                if (size == 2) {
                    for (int i = 0; i < pickPattern.length; i++) {
                        int[] matrixData = new int[0];
                        for (int k = 0; k < size; k++) {
                            int[][] equations = new int[size][size + 1];
                            for (int l = 0; l < size; l++)
                                equations[l] = this.createEquationFrom(commonGrams[size - 2][l], sorted.get(pickPattern[i][l]), k);
                            int[] solution = SimultaneousEquations.solveSimEquationsMod(equations, 26);
                            matrixData = ArrayUtil.concat(matrixData, solution);
                        }

                        this.decryptAndUpdate(tracker, new Matrix(matrixData, size, size));
                    }
                } else if (size == 3) {
                    List<String> list2 = FileReader.compileTextFromResource("/resources/commontrigrampairings.txt");

                    for (String line : list2) {
                        String[] str = StringTransformer.splitInto(line, 3);

                        for (int i = 0; i < pickPattern.length; i++) {
                            int[] matrixData = new int[0];
                            for (int k = 0; k < size; k++) {
                                int[][] equations = new int[size][size + 1];
                                for (int l = 0; l < size; l++)
                                    equations[l] = this.createEquationFrom(str[l], sorted.get(pickPattern[i][l]), k);
                                int[] solution = SimultaneousEquations.solveSimEquationsMod(equations, 26);
                                matrixData = ArrayUtil.concat(matrixData, solution);
                            }

                            this.decryptAndUpdate(tracker, new Matrix(matrixData, size, size));
                        }
                    }
                }
            }
            return tracker;
        case PERIODIC_KEY:
            this.readLatestSettings();
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
                    KeyIterator.iterateObject(row -> this.iteratePossibleRows(tracker, row), size, tracker.resultList.toArray(new HillSection[0]));
                }
            }
            return tracker;
        default:
            return super.attemptAttack(text, method, app);
        }
    }
    
    @Override
    public void decryptAndUpdate(DecryptionTracker tracker, Matrix key) {
        try {
            super.decryptAndUpdate(tracker, key);
        } catch (MatrixNoInverse | MatrixNotSquareException e) {
            return;
        }
    }

    public int[][] generatePickPattern(int size, int times) { // MathUtil.factorial(times)
        int[][] patterns = new int[(int) Math.pow(times, size)][size];

        int count = 0;

        if (size == 2) {
            for (int i = 0; i < times; i++)
                for (int j = 0; j < times; j++) {
                    if (i == j)
                        continue;
                    patterns[count++] = new int[] { i, j };
                }

            return patterns;
        } else if (size == 3) {
            for (int i = 0; i < times; i++)
                for (int j = 0; j < times; j++) {
                    for (int k = 0; k < times; k++) {
                        if (i == j || i == k || j == k)
                            continue;
                        patterns[count++] = new int[] { i, j, k };
                    }
                }

            return patterns;
        }

        return new int[0][0];
    }

    
    public void iterateMatrixRows(HillTracker tracker, Integer[] row) {
        if (MathUtil.allDivisibleBy(row, 0, tracker.size, 2, 13)) {
            return;
        }

        char[] decrypted = new char[tracker.lengthSub];

        for (int i = 0; i < tracker.getCipherText().length(); i += tracker.size) {
            int total = 0;
            for (int s = 0; s < tracker.size; s++)
                total += row[s] * (tracker.getCipherText().charAt(i + s) - 'A');

            decrypted[i / tracker.size] = (char) (total % 26 + 'A');
        }

        double score = ChiSquared.calculate(decrypted, tracker.getLanguage());

        if (tracker.resultList.add(new HillSection(score, decrypted, Arrays.copyOf(row, row.length)))) {
            if (score < 80D) {
                this.output(tracker, "%s, %f, %s", Arrays.toString(row), score, new String(decrypted));
            }
        }

    }
    
    public void iteratePossibleRows(HillTracker tracker, HillSection[] data) {
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
            return;
        }
        
        for (int s = 0; s < tracker.size; s++) {
            HillSection hillResult = data[s];
            for (int i = 0; i < tracker.lengthSub; i++) {
                tracker.getPlainTextHolder(false)[i * tracker.size + s] = (char) hillResult.decrypted[i];
            }
        }

        tracker.lastSolution = new Solution(tracker.getPlainTextHolder(false), tracker.getLanguage());

        this.updateIfBetterThanBest(tracker, tracker.lastSolution, () -> inverseMatrix.inverseMod(26));
    }
    
    public int[] createEquationFrom(String plainText, String cipherText, int index) {
        int[] equation = new int[plainText.length() + 1];
        for (int i = 0; i < equation.length - 1; i++)
            equation[i] = plainText.charAt(i) - 'A';
        equation[equation.length - 1] = cipherText.charAt(index) - 'A';
        return equation;
    }

    public class HillTracker extends DecryptionTracker {

        private int size;
        private int lengthSub;
        private DynamicResultList<HillSection> resultList;

        public HillTracker(CharSequence text, IApplication app) {
            super(text, app);
            this.resultList = new DynamicResultList<HillSection>(8);
        }
    }

    public static class HillSection extends ResultPositive {
        public char[] decrypted;
        public Integer[] inverseCol;

        public HillSection(double score, char[] decrypted, Integer[] inverseCol) {
            super(score);
            this.decrypted = decrypted;
            this.inverseCol = inverseCol;
        }
    }
}
