package nationalcipher.cipher.decrypt.anew;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Stream;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javalibrary.fitness.TextFitness;
import javalibrary.lib.Timer;
import javalibrary.list.DynamicResultList;
import javalibrary.math.MathUtil;
import javalibrary.math.Units.Time;
import nationalcipher.cipher.base.anew.EnigmaCipher;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.anew.EnigmaAttack.EnigmaSection;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class EnigmaUhrAttack extends CipherAttack<QuadKey<Integer[], Integer[], Integer[], Integer>, EnigmaCipher> {

    private JComboBox<EnigmaMachine> machineSelection;
    private JComboBox<String> reflectorSelection;
    private JTextField plugboardDefinition;
    
    public EnigmaUhrAttack() {
        super(new EnigmaCipher(EnigmaLib.ENIGMA_M3), "Enigma - Plugboard Uhr");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
        this.machineSelection = new JComboBox<EnigmaMachine>();
        this.reflectorSelection = new JComboBox<String>();
        this.plugboardDefinition = new JTextField();

        Stream.of(EnigmaLib.MACHINES).filter(m -> m.isSolvable() && m.canPlugboard() && !m.hasThinRotor() && m.canUhr()).forEach(m -> this.machineSelection.addItem(m));

        this.machineSelection.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                EnigmaMachine currentMachine = (EnigmaMachine) EnigmaUhrAttack.this.machineSelection.getSelectedItem();

                EnigmaUhrAttack.this.reflectorSelection.removeAllItems();
                if (currentMachine.getReflectorCount() > 1)
                    EnigmaUhrAttack.this.reflectorSelection.addItem("-Check all-");
                for (String reflectorName : currentMachine.reflectorNames)
                    EnigmaUhrAttack.this.reflectorSelection.addItem(reflectorName);
            }
        });

        EnigmaMachine currentMachine = (EnigmaMachine) EnigmaUhrAttack.this.machineSelection.getSelectedItem();
        if (currentMachine.getReflectorCount() > 1)
            this.reflectorSelection.addItem("-Check all-");
        for (String reflectorName : currentMachine.reflectorNames)
            this.reflectorSelection.addItem(reflectorName);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Machine Version:", this.machineSelection));
        panel.add(new SubOptionPanel("Reflector:", this.reflectorSelection));
        panel.add(new SubOptionPanel("Plugboard:", this.plugboardDefinition));
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        EnigmaTask task = new EnigmaTask(text, app);

        // Settings grab
        task.machine = (EnigmaMachine) this.machineSelection.getSelectedItem();
        this.getCipher().setMachine(task.machine);
        task.reflectorTest = this.reflectorSelection.getSelectedIndex() - 1;
        task.start = 0;
        task.end = task.machine.getReflectorCount();
        if (task.reflectorTest != -1) {
            task.start = task.reflectorTest;
            task.end = task.start + 1;
        }

        String plugboardInput = this.plugboardDefinition.getText();
        char[][] plugboardDefinition = new char[10][3];
        boolean definitionProvided = false;
        int count = 0;
        for (String split : plugboardInput.split("[, ]"))
            if (split.length() == 3) {
                plugboardDefinition[count++] = split.toCharArray();
                definitionProvided = true;
            }

        if (definitionProvided)
            task.machine = task.machine.createWithUhr(4, plugboardDefinition);

        app.out().println("Using machine type: %s", task.machine);

        if (method == DecryptionMethod.BRUTE_FORCE) {

            int rotorCombos = MathUtil.factorial(task.machine.getRotorCount(), 3);
            app.out().println("Going throught all combinations of the %d rotors (%d) and indicator settings (%d), totalling %d test subjects.", task.machine.getRotorCount(), rotorCombos, (int) Math.pow(26, 3), rotorCombos * (int) Math.pow(26, 3));
            double constant = 120 / 60000D; // Time taken per letter per rotor setting
            app.out().println("Estimated time %c %ds, This may take a while...", (char) 8776, (int) (constant * rotorCombos * task.getCipherText().length() * (task.reflectorTest == -1 ? task.machine.getReflectorCount() : 1)));
            Timer timer = new Timer();
            KeyIterator.iterateIntegerArray(task::onList, 3, task.machine.getRotorCount(), false);
            app.out().println("Time taken %fs", timer.getTimeRunning(Time.SECOND));

            task.squeezeFirst.sort();
            app.out().println("Determining ring settings");
            app.out().println("%d possible indicators and rotor orders, therefore %d possible ring settings", task.squeezeFirst.size(), task.squeezeFirst.size() * 26 * 26);

            for (EnigmaSection trial : task.squeezeFirst) {
                for (int s2 = 0; s2 < 26; s2++) {
                    for (int s3 = 0; s3 < 26; s3++) {
                        Integer[] indicator = trial.copyIndicator();
                        Integer[] ring = new Integer[] { 0, s2, s3 };

                        indicator[1] = (indicator[1] + s2) % 26;
                        indicator[2] = (indicator[2] + s3) % 26;
                        
                        char[] plainText = this.getCipher().decodeEfficently(task.getCipherText(), task.getPlainTextHolder(false), QuadKey.of(indicator, ring, trial.rotors, trial.reflector));
                        
                        EnigmaSection nextTrialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(plainText) * 1000, trial.machine, indicator, trial.rotors, trial.reflector);
                        nextTrialSolution.ring = ring;

                        if (task.squeezeSecond.add(nextTrialSolution)) {
                            nextTrialSolution.bake();
                        }
                    }
                }
            }

            task.squeezeSecond.sort();
            app.out().println("Determining plugboard");

            for (EnigmaSection trial : task.squeezeSecond) {
                app.out().println("%s", trial);
            }
        }

        return task;
    }

    public class EnigmaTask extends DecryptionTracker {

        private EnigmaMachine machine;
        private int reflectorTest; // -1 if test all, otherwise is the index of the reflector to test
        private int start, end;
        private DynamicResultList<EnigmaSection> squeezeFirst;
        private DynamicResultList<EnigmaSection> squeezeSecond;

        public EnigmaTask(CharSequence text, IApplication app) {
            super(text, app);
            this.squeezeFirst = new DynamicResultList<EnigmaSection>(256);
            this.squeezeSecond = new DynamicResultList<EnigmaSection>(64);
        }

        public boolean onList(Integer[] rotor) {
            return KeyIterator.iterateIntegerArray(o -> onList2(rotor, o), 3, 26, true);
        }

        public boolean onList2(Integer[] rotor, Integer[] indicator) {
            for (int reflector = this.start; reflector < this.end; reflector++) {

                char[] plainText = EnigmaUhrAttack.this.getCipher().decodeEfficently(this.getCipherText(), this.getPlainTextHolder(false), QuadKey.of(indicator, EnigmaLib.DEFAULT_SETTING, rotor, reflector));

                EnigmaSection trialSolution = new EnigmaSection(TextFitness.scoreFitnessQuadgrams(plainText, this.getLanguage()), this.machine, indicator, rotor, reflector);
                
                if (this.squeezeFirst.add(trialSolution)) {
                    trialSolution.bake();
                }
                
                this.increaseIteration();
            }
            return true;
        }
    }
}
