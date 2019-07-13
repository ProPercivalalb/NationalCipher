package nationalcipher.cipher.decrypt.complete;

import java.math.BigInteger;
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
import javalibrary.math.matrics.Matrix;
import javalibrary.streams.FileReader;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.other.Hill;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.NationalCipherUI;

public class HillAttack extends CipherAttack {

    public JSpinner[] rangeSpinner;
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

    public HillAttack() {
        super("Hill");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED, DecryptionMethod.PERIODIC_KEY, DecryptionMethod.SIMULATED_ANNEALING);
        this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 3, 2, 5, 1);
        this.gramSearchRange = JSpinnerUtil.createSpinner(20, 3, 100, 1);
        this.trigramSets = new JComboBox<String>(this.TRI_GRAM_DISPLAY);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Size Range:", this.rangeSpinner));
        panel.add(new SubOptionPanel("N-Gram Search Range:", this.gramSearchRange));
        panel.add(new SubOptionPanel("3x3 Trigram Sets:", this.trigramSets));
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        HillTask task = new HillTask(text, app);

        // Settings grab
        int[] sizeRange = SettingParse.getIntegerRange(this.rangeSpinner);
        int gramSearchRange = SettingParse.getInteger(this.gramSearchRange);
        String[][] commonGrams = new String[][] { BI_GRAM[0], TRI_GRAM[this.trigramSets.getSelectedIndex()] };

        if (method == DecryptionMethod.BRUTE_FORCE) {
            BigInteger TWENTY_SIX = BigInteger.valueOf(26);

            for (int size = sizeRange[0]; size <= sizeRange[1]; ++size)
                app.getProgress().addMaxValue(TWENTY_SIX.pow((int) Math.pow(size, 2)));

            for (int size = sizeRange[0]; size <= sizeRange[1]; size++)
                KeyIterator.iterateMatrix(task::onIteration, size);
        } else if (method == DecryptionMethod.CALCULATED) {
            for (int size = sizeRange[0]; size <= sizeRange[1]; size++) {
                Map<String, Integer> chars = StringAnalyzer.getEmbeddedStrings(text, size, size, false);
                List<String> sorted = new ArrayList<String>(chars.keySet());
                Collections.sort(sorted, new StringAnalyzer.SortStringInteger(chars));
                Collections.reverse(sorted);
                app.out().println("" + chars.size());
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

                        task.onIteration(new Matrix(matrixData, size, size));
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

                            task.onIteration(new Matrix(matrixData, size, size));
                        }
                    }
                }
            }
        } else if (method == DecryptionMethod.PERIODIC_KEY) {
            for (int size = sizeRange[0]; size <= sizeRange[1]; size++) {
                if (task.cipherText.length % size != 0) {
                    app.out().println("Matrix size of %d is not possible, length of text is not a multiple.", size);
                    continue;
                }
                task.size = size;
                task.lengthSub = task.cipherText.length / size;

                KeyIterator.iterateIntegerArray(task::onList, size, 26, true);

                if (task.resultList.size() < size)
                    app.out().println("Did not find enought key columns that produces good characters %d/%d", task.resultList.size(), size);
                else
                    KeyIterator.iterateIntegerArray(task::onList2, size, task.resultList.size(), false);
            }
        } else if (method == DecryptionMethod.SIMULATED_ANNEALING) {
            task.size = sizeRange[0];
            app.getProgress().addMaxValue(app.getSettings().getSAIteration());
            task.run();
        }

        app.out().println(task.getBestSolution());
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

    public int[] createEquationFrom(String plainText, String cipherText, int index) {
        int[] equation = new int[plainText.length() + 1];
        for (int i = 0; i < equation.length - 1; i++)
            equation[i] = plainText.charAt(i) - 'A';
        equation[equation.length - 1] = cipherText.charAt(index) - 'A';
        return equation;
    }

    public class HillTask extends SimulatedAnnealing {

        private int size;
        private int lengthSub;
        private DynamicResultList<HillSection> resultList;
        public Matrix bestKey, bestMaximaKey, lastKey;

        public HillTask(String text, IApplication app) {
            super(text.toCharArray(), app);
            this.resultList = new DynamicResultList<HillSection>(8);
        }

        public void onIteration(Matrix matrix) {
            try {
                this.lastSolution = new Solution(Hill.decode(this.cipherText, this.plainText, matrix), this.getLanguage());

                if (this.lastSolution.score >= this.bestSolution.score) {
                    this.bestSolution = this.lastSolution;
                    this.bestSolution.setKeyString(matrix.toString());
                    this.bestSolution.bakeSolution();
                    this.out().println("%s", this.bestSolution);
                    this.getKeyPanel().updateSolution(this.bestSolution);
                }
            } catch (MatrixNoInverse e) {
                return;
            } catch (MatrixNotSquareException e) {
                return;
            } finally {
                this.getKeyPanel().updateIteration(this.iteration++);
                this.getProgress().increase();
            }
        }

        @Override
        public Solution generateKey() {
            this.bestMaximaKey = KeyGeneration.createMatrix(this.size, 26);
            return new Solution(Hill.decodeUsingInverse(this.cipherText, this.plainText, this.bestMaximaKey), this.getLanguage());
        }

        @Override
        public Solution modifyKey(double temp, int count, double lastDF) {
            this.lastKey = KeyManipulation.modifyMatrix(this.bestMaximaKey, count % this.size, this.size);
            return new Solution(Hill.decodeUsingInverse(this.cipherText, this.plainText, this.lastKey), this.getLanguage());
        }

        @Override
        public void storeKey() {
            this.bestMaximaKey = this.lastKey;
        }

        @Override
        public void solutionFound() {
            this.bestKey = this.bestMaximaKey;
            this.bestSolution.setKeyString(this.bestKey.toString());
            this.bestSolution.bakeSolution();
            this.getKeyPanel().updateSolution(this.bestSolution);
        }

        @Override
        public void onIteration() {
            this.getProgress().increase();
            this.getKeyPanel().updateIteration(this.iteration++);
        }

        @Override
        public boolean endIteration() {
            this.out().println("%s", this.bestSolution);
            NationalCipherUI.BEST_SOULTION = this.bestSolution.getText();
            this.getProgress().setValue(0);
            return false;
        }

        public void onList(Integer[] data) {
            boolean invalidDeterminate = false;
            for (int d : new int[] { 2, 13 }) {
                boolean divides = true;
                for (int s = 0; s < this.size; s++)
                    if (data[s] % d != 0)
                        divides = false;

                invalidDeterminate = divides;
                if (divides)
                    break;
            }

            if (invalidDeterminate)
                return;

            char[] decrypted = new char[this.lengthSub];

            for (int i = 0; i < this.cipherText.length; i += this.size) {
                int total = 0;
                for (int s = 0; s < this.size; s++)
                    total += data[s] * (this.cipherText[i + s] - 'A');

                decrypted[i / this.size] = (char) (total % 26 + 'A');
            }

            double score = ChiSquared.calculate(decrypted, this.app.getLanguage());

            this.resultList.addResult(new HillSection(score, decrypted, Arrays.copyOf(data, data.length)));

            if (score < 128D)
                this.app.out().println("%s, %f, %s", Arrays.toString(data), score, Arrays.toString(decrypted));
        }

        public void onList2(Integer[] data) {
            for (int s = 0; s < this.size; s++) {
                HillSection hillResult = this.resultList.get(data[s]);
                for (int i = 0; i < this.lengthSub; i++)
                    this.plainText[i * this.size + s] = (byte) hillResult.decrypted[i];
            }

            this.lastSolution = new Solution(this.plainText, this.getLanguage());

            if (this.lastSolution.score >= this.bestSolution.score) {
                this.bestSolution = this.lastSolution;
                int[] inverseMatrix = new int[this.size * this.size];
                for (int s = 0; s < this.size; s++) {
                    HillSection hillResult = this.resultList.get(data[s]);
                    for (int n = 0; n < this.size; n++)
                        inverseMatrix[s * this.size + n] = hillResult.inverseCol[n];

                }
                try {
                    this.bestSolution.setKeyString(new Matrix(inverseMatrix, this.size).inverseMod(26).toString());
                } catch (MatrixNoInverse e) {
                    this.bestSolution.setKeyString("Invertible: %s", Arrays.toString(inverseMatrix));
                }

                this.bestSolution.bakeSolution();
                this.out().println("%s", this.bestSolution);
                this.getKeyPanel().updateSolution(this.bestSolution);
            }
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
