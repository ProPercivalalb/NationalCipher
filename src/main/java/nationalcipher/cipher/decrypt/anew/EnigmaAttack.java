package nationalcipher.cipher.decrypt.anew;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
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
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.anew.EnigmaCipher;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.ui.IApplication;

public class EnigmaAttack extends CipherAttack<QuadKey<Integer[], Integer[], Integer[], Integer>, EnigmaCipher> {

    private JComboBox<EnigmaMachine> machineSelection;
    private JComboBox<String> reflectorSelection;

    public EnigmaAttack() {
        super(new EnigmaCipher(EnigmaLib.ENIGMA_M3), "Enigma - Plain");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
        this.machineSelection = new JComboBox<EnigmaMachine>();
        this.reflectorSelection = new JComboBox<String>();

        Stream.of(EnigmaLib.MACHINES)
            .filter(m -> m.isSolvable() && !m.hasThinRotor())
            .forEach(m -> this.machineSelection.addItem(m));

        this.machineSelection.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                EnigmaMachine currentMachine = (EnigmaMachine) EnigmaAttack.this.machineSelection.getSelectedItem();

                EnigmaAttack.this.reflectorSelection.removeAllItems();
                if (currentMachine.reflector.length > 1)
                    EnigmaAttack.this.reflectorSelection.addItem("-Check all-");
                for (String reflectorName : currentMachine.reflectorNames)
                    EnigmaAttack.this.reflectorSelection.addItem(reflectorName);
            }
        });

        EnigmaMachine currentMachine = (EnigmaMachine) this.machineSelection.getSelectedItem();
        if (currentMachine.reflector.length > 1)
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
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        EnigmaTracker tracker = new EnigmaTracker(text, app);

        // Settings grab
        tracker.machine = (EnigmaMachine) this.machineSelection.getSelectedItem();
        this.getCipher().setMachine(tracker.machine);
        tracker.reflectorTest = this.reflectorSelection.getSelectedIndex() - 1;
        tracker.start = 0;
        tracker.end = tracker.machine.reflector.length;
        if (tracker.reflectorTest != -1) {
            tracker.start = tracker.reflectorTest;
            tracker.end = tracker.start + 1;
        }

        this.output(tracker, "Using machine type: %s", tracker.machine);

        if (method == DecryptionMethod.BRUTE_FORCE) {
            //tracker.getProgress().addMaxValue(MathUtil.choose(tracker.machine.getRotorCount(), 3));
            BigInteger numRotors = MathUtil.factorialLength(BigInteger.valueOf(tracker.machine.getRotorCount()), BigInteger.valueOf(3));
            tracker.getProgress().addMaxValue(numRotors.multiply(BigInteger.valueOf(26).pow(3)).multiply(BigInteger.valueOf(tracker.end - tracker.start)));
            
            //this.output(tracker, "Going throught all combinations of the %d rotors (%d) and indicator settings (%d), totalling %d test subjects.", tracker.machine.getNumberOfRotors(), rotorCombos, (int) Math.pow(26, 3), rotorCombos * (int) Math.pow(26, 3));
            //double constant = 120 / 30000D; // Time taken per letter per rotor setting
            //this.output(tracker, "Estimated time %c %ds, This may take a while...", (char) 8776, (int) (constant * rotorCombos * tracker.getCipherText().length() * (tracker.reflectorTest == -1 ? tracker.machine.getNumberOfReflectors() : 1)));
            Timer timer = new Timer();
            KeyIterator.iterateIntegerArray(tracker::iterateIndicator, 3, tracker.machine.getRotorCount(), false);
            this.output(tracker, "Time taken %fs", timer.getTimeRunning(Time.SECOND));

            tracker.squeezeFirst.sort();
            this.output(tracker, "Determining ring settings");
            tracker.getProgress().addMaxValue(BigInteger.valueOf(tracker.squeezeFirst.size()).multiply(BigInteger.valueOf(26).multiply(BigInteger.valueOf(26))));
            for (EnigmaSection trial : tracker.squeezeFirst) {
                for (int s2 = 0; s2 < 26; s2++) {
                    for (int s3 = 0; s3 < 26; s3++) {
                        Integer[] indicator = trial.copyIndicator();
                        Integer[] ring = new Integer[] { 0, s2, s3 };

                        indicator[1] = (indicator[1] + s2) % 26;
                        indicator[2] = (indicator[2] + s3) % 26;

                        this.decryptAndUpdate(tracker, QuadKey.of(indicator, ring, trial.rotors, trial.reflector));
                    }
                }
            }
        }

        tracker.finish();
        return tracker;
    }

    public class EnigmaTracker extends DecryptionTracker {

        private EnigmaMachine machine;
        private int reflectorTest; // -1 if test all, otherwise is the index of the reflector to test
        private int start, end;
        private DynamicResultList<EnigmaSection> squeezeFirst;

        public EnigmaTracker(CharSequence text, IApplication app) {
            super(text, app);
            this.squeezeFirst = new DynamicResultList<EnigmaSection>(500);
        }

        public boolean iterateIndicator(Integer[] rotor) {
            return KeyIterator.iterateIntegerArray(indicator -> iterateReflector(rotor, indicator), 3, 26, true);
        }

        public boolean iterateReflector(Integer[] rotor, Integer[] indicator) {
            for (int reflector = this.start; reflector < this.end; reflector++) {

                char[] plainText = EnigmaAttack.this.getCipher().decodeEfficently(this.getCipherText(), this.getPlainTextHolder(false), QuadKey.of(indicator, EnigmaLib.DEFAULT_SETTING, rotor, reflector));

                EnigmaSection trialSolution = new EnigmaSection(TextFitness.scoreFitnessQuadgrams(plainText, this.getLanguage()), this.machine, indicator, rotor, reflector);

                if (this.squeezeFirst.add(trialSolution)) {
                    trialSolution.bake();
                }
                this.increaseIteration();
            }
            
            return true;
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

        public void bake() {
            this.indicator = ArrayUtil.copy(this.indicator);
            this.rotors = ArrayUtil.copy(this.rotors);
            if (this.ring != null) {
                this.ring = ArrayUtil.copy(this.ring);
            }
        }

        public Integer[] copyIndicator() {
            return ArrayUtil.copy(this.indicator);
        }
        
        public String toKeyString() {
            return String.format("Machine Type: %s, Rotors:%s: Ind:%s, Ring:%s, Reflector:%d", this.machine, Arrays.toString(this.rotors), CipherUtils.displayAsLetters(this.indicator), CipherUtils.displayAsLetters(this.ring), this.reflector);
        }

        @Override
        public String toString() {
            return String.format("%f, %s", this.score, this.toKeyString());
        }
    }
}
