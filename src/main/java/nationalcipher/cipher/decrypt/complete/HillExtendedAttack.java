package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.fitness.ChiSquared;
import javalibrary.math.matrics.Matrix;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class HillExtendedAttack extends CipherAttack {

    public JSpinner[] rangeSpinner;

    public HillExtendedAttack() {
        super("Hill Extended");
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY);
        this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 3, 2, 5, 1);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Size Range:", this.rangeSpinner));
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        HillExtendedTask task = new HillExtendedTask(text, app);

        // Settings grab
        int[] sizeRange = SettingParse.getIntegerRange(this.rangeSpinner);
        if (method == DecryptionMethod.PERIODIC_KEY) {
            for (int size = sizeRange[0]; size <= sizeRange[1]; size++) {
                if (task.cipherText.length % size != 0) {
                    app.out().println("Matrix size of %d is not possible, length of text is not a multiple.", size);
                    continue;
                }
                task.size = size;
                task.lengthSub = task.cipherText.length / size;

                KeyIterator.iterateIntegerArray(task::onList, size + 1, 26, true);

                if (task.best.size() < size)
                    app.out().println("Did not find enought key columns that produces good characters %d/%d", task.best.size(), size);
                else
                    KeyIterator.iterateIntegerArray(task::onList2, size, task.best.size(), false);
            }
        }

        app.out().println(task.getBestSolution());
    }

    public class HillExtendedTask extends DecryptionTracker {

        private int size;
        private int lengthSub;
        private List<HillSection> best = new ArrayList<HillSection>();

        public HillExtendedTask(String text, IApplication app) {
            super(text.toCharArray(), app);
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

            byte[] decrypted = new byte[this.lengthSub];

            for (int i = 0; i < this.cipherText.length; i += this.size) {
                int total = 0;
                for (int s = 0; s < this.size; s++)
                    total += data[s] * (this.cipherText[i + s] - 'A');

                decrypted[i / this.size] = (byte) ((total + (26 - data[this.size])) % 26 + 'A');
            }

            double currentSum = ChiSquared.calculate(decrypted, this.app.getLanguage());

            if (currentSum < 200) {
                this.app.out().println("%s, %f, %s", Arrays.toString(data), currentSum, Arrays.toString(decrypted));
                this.best.add(new HillSection(decrypted, Arrays.copyOf(data, data.length)));
            }
        }

        public void onList2(Integer[] data) {
            for (int s = 0; s < this.size; s++) {
                HillSection hillSection = this.best.get(data[s]);
                for (int i = 0; i < this.lengthSub; i++)
                    this.plainText[i * this.size + s] = hillSection.decrypted[i];
            }

            this.lastSolution = new Solution(this.plainText, this.getLanguage());

            if (this.lastSolution.score >= this.bestSolution.score) {
                this.bestSolution = this.lastSolution;
                int[] inverseMatrix = new int[this.size * this.size];
                int[] extendedMatrix = new int[this.size];
                for (int s = 0; s < this.size; s++) {
                    HillSection hillSection = this.best.get(data[s]);
                    for (int n = 0; n < this.size; n++)
                        inverseMatrix[s * this.size + n] = hillSection.inverseCol[n];
                    extendedMatrix[s] = hillSection.inverseCol[this.size];
                }

                try {
                    this.bestSolution.setKeyString("%s, %s", new Matrix(inverseMatrix, this.size).inverseMod(26), Arrays.toString(extendedMatrix));
                } catch (MatrixNoInverse e) {
                }

                this.bestSolution.bakeSolution();
                this.out().println("%s", this.bestSolution);
                this.getKeyPanel().updateSolution(this.bestSolution);
            }
        }
    }

    public static class HillSection {
        public byte[] decrypted;
        public Integer[] inverseCol;

        public HillSection(byte[] decrypted, Integer[] inverseCol) {
            this.decrypted = decrypted;
            this.inverseCol = inverseCol;
        }
    }
}
