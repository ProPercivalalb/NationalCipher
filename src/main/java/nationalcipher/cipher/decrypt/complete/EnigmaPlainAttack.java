package nationalcipher.cipher.decrypt.complete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.fitness.TextFitness;
import javalibrary.lib.Timer;
import javalibrary.list.DynamicResultList;
import javalibrary.list.ResultNegative;
import javalibrary.math.MathUtil;
import javalibrary.math.Units.Time;
import nationalcipher.cipher.base.enigma.Enigma;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class EnigmaPlainAttack extends CipherAttack {

    private JComboBox<EnigmaMachine> machineSelection;
    private JComboBox<String> reflectorSelection;

    public EnigmaPlainAttack() {
        super("Enigma - Plain");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
        this.machineSelection = new JComboBox<EnigmaMachine>();
        this.reflectorSelection = new JComboBox<String>();

        Stream.of(EnigmaLib.MACHINES).filter(m -> m.isSolvable() && !m.hasThinRotor()).forEach(m -> this.machineSelection.addItem(m));

        this.machineSelection.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                EnigmaMachine currentMachine = (EnigmaMachine) EnigmaPlainAttack.this.machineSelection.getSelectedItem();

                EnigmaPlainAttack.this.reflectorSelection.removeAllItems();
                if (currentMachine.reflectorCount > 1)
                    EnigmaPlainAttack.this.reflectorSelection.addItem("-Check all-");
                for (String reflectorName : currentMachine.reflectorNames)
                    EnigmaPlainAttack.this.reflectorSelection.addItem(reflectorName);
            }
        });

        EnigmaMachine currentMachine = (EnigmaMachine) this.machineSelection.getSelectedItem();
        if (currentMachine.reflectorCount > 1)
            this.reflectorSelection.addItem("-Check all-");
        for (String reflectorName : currentMachine.reflectorNames)
            this.reflectorSelection.addItem(reflectorName);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Machine Version:", this.machineSelection));
        panel.add(new SubOptionPanel("Reflector:", this.reflectorSelection));
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        EnigmaTask task = new EnigmaTask(text, app);

        // Settings grab
        task.machine = (EnigmaMachine) this.machineSelection.getSelectedItem();
        task.reflectorTest = this.reflectorSelection.getSelectedIndex() - 1;
        task.start = 0;
        task.end = task.machine.reflectorCount;
        if (task.reflectorTest != -1) {
            task.start = task.reflectorTest;
            task.end = task.start + 1;
        }

        app.out().println("Using machine type: %s", task.machine);

        if (method == DecryptionMethod.BRUTE_FORCE) {

            int rotorCombos = MathUtil.factorial(task.machine.getNumberOfRotors(), 3);
            app.out().println("Going throught all combinations of the %d rotors (%d) and indicator settings (%d), totalling %d test subjects.", task.machine.getNumberOfRotors(), rotorCombos, (int) Math.pow(26, 3), rotorCombos * (int) Math.pow(26, 3));
            double constant = 120 / 60000D; // Time taken per letter per rotor setting
            app.out().println("Estimated time %c %ds, This may take a while...", (char) 8776, (int) (constant * rotorCombos * task.cipherText.length * (task.reflectorTest == -1 ? task.machine.getNumberOfReflectors() : 1)));
            Timer timer = new Timer();
            KeyIterator.iterateIntegerArray(task::onList, 3, task.machine.getNumberOfRotors(), false);
            app.out().println("Time taken %fs", timer.getTimeRunning(Time.SECOND));

            task.squeezeFirst.sort();
            app.out().println("Determining ring settings");

            for (int i = 0; i < task.squeezeFirst.size(); i++) {
                EnigmaSection trial = task.squeezeFirst.get(i);
                for (int s2 = 0; s2 < 26; s2++) {
                    for (int s3 = 0; s3 < 26; s3++) {
                        Integer[] indicator = trial.copyIndicator();
                        Integer[] ring = new Integer[] { 0, s2, s3 };

                        indicator[1] = (indicator[1] + s2) % 26;
                        indicator[2] = (indicator[2] + s3) % 26;

                        task.lastSolution = new Solution(Enigma.decodeFast(task.cipherText, task.plainText, trial.machine, Arrays.copyOf(indicator, indicator.length), ring, trial.rotors, trial.reflector), app.getLanguage());

                        if (task.lastSolution.score > task.bestSolution.score) {
                            task.bestSolution = task.lastSolution;
                            task.bestSolution.setKeyString("%s, %s, %s", this.toString(indicator), this.toString(ring), Arrays.toString(trial.rotors));

                            task.bestSolution.bakeSolution();
                            app.out().println("%s", task.bestSolution);
                            app.getKeyPanel().updateSolution(task.bestSolution);
                        }
                    }
                }
            }
        }

        app.out().println(task.getBestSolution());
    }

    public String toString(int[] order) {
        char[] text = new char[3];
        for (int i = 0; i < order.length; i++)
            text[i] = (char) (order[i] + 'A');
        return new String(text);
    }

    public String toString(Integer[] order) {
        char[] text = new char[3];
        for (int i = 0; i < order.length; i++)
            text[i] = (char) (order[i] + 'A');
        return new String(text);
    }

    public class EnigmaTask extends DecryptionTracker {

        private EnigmaMachine machine;
        private int reflectorTest; // -1 if test all, otherwise is the index of the reflector to test
        private int start, end;
        private DynamicResultList<EnigmaSection> squeezeFirst;

        public EnigmaTask(String text, IApplication app) {
            super(text.toCharArray(), app);
            this.squeezeFirst = new DynamicResultList<EnigmaSection>(500);
        }

        public void onList(Integer[] rotor) {
            KeyIterator.iterateIntegerArray(o -> onList2(rotor, o), 3, 26, true);
        }

        public void onList2(Integer[] rotor, Integer[] data) {

            for (int reflector = this.start; reflector < this.end; reflector++) {

                this.plainText = Enigma.decodeFast(this.cipherText, this.plainText, this.machine, Arrays.copyOf(data, data.length), EnigmaLib.DEFAULT_SETTING, rotor, reflector);

                EnigmaSection trialSolution = new EnigmaSection(TextFitness.scoreFitnessQuadgrams(this.plainText, this.getLanguage()), this.machine, data, rotor, reflector);

                if (this.squeezeFirst.addResult(trialSolution))
                    trialSolution.makeCopy();
            }
        }
    }

    public static class EnigmaSection extends ResultNegative {

        public EnigmaMachine machine;
        public Integer[] indicator;
        public Integer[] ring;
        public Integer[] rotors;
        public int reflector;

        public EnigmaSection(double score, EnigmaMachine machine, Integer[] notchKey, Integer[] rotors, int reflector) {
            super(score);
            this.machine = machine;
            this.indicator = notchKey;
            this.rotors = rotors;
            this.reflector = reflector;
        }

        public void makeCopy() {
            this.indicator = Arrays.copyOf(this.indicator, this.indicator.length);
            this.rotors = Arrays.copyOf(this.rotors, this.rotors.length);
            if (this.ring != null)
                this.ring = Arrays.copyOf(this.ring, this.ring.length);
        }

        public Integer[] copyIndicator() {
            return Arrays.copyOf(this.indicator, this.indicator.length);
        }

        public String displaySetting(int[] order) {
            if (order == null)
                return "null";
            char[] text = new char[3];
            for (int i = 0; i < order.length; i++)
                text[i] = (char) (order[i] + 'A');
            return new String(text);
        }

        public String displaySetting(Integer[] order) {
            if (order == null)
                return "null";
            char[] text = new char[3];
            for (int i = 0; i < order.length; i++)
                text[i] = (char) (order[i] + 'A');
            return new String(text);
        }

        public String toKeyString() {
            return String.format("Machine Type: %s, Rotors:%s: Ind:%s, Ring:%s, Reflector:%d", this.machine, Arrays.toString(this.rotors), this.displaySetting(this.indicator), this.displaySetting(this.ring), this.reflector);
        }

        @Override
        public String toString() {
            return String.format("%f, %s", this.score, this.toKeyString());
        }
    }
}
