package nationalcipher.cipher.decrypt.anew;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.stream.Stream;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javalibrary.lib.Timer;
import javalibrary.list.DynamicResultList;
import javalibrary.math.MathUtil;
import javalibrary.math.Units.Time;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.anew.EnigmaPlugboardCipher;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.base.keys.QuinKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.anew.EnigmaAttack.EnigmaSection;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class EnigmaPlugboardAttack extends CipherAttack<QuinKey<Integer[], Integer[], Integer[], Integer, Integer[]>, EnigmaPlugboardCipher> {

    private JComboBox<EnigmaMachine> machineSelection;
    private JComboBox<String> reflectorSelection;
    private JTextField plugboardDefinition;

    public EnigmaPlugboardAttack() {
        super(new EnigmaPlugboardCipher(EnigmaLib.ENIGMA_M3), "Enigma - Plugboard");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.PERIODIC_KEY);
        this.machineSelection = new JComboBox<EnigmaMachine>();
        this.reflectorSelection = new JComboBox<String>();
        this.plugboardDefinition = new JTextField();

        Stream.of(EnigmaLib.MACHINES).filter(m -> m.isSolvable() && m.canPlugboard() && !m.hasThinRotor()).forEach(m -> this.machineSelection.addItem(m));

        this.machineSelection.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                EnigmaMachine currentMachine = (EnigmaMachine) EnigmaPlugboardAttack.this.machineSelection.getSelectedItem();

                EnigmaPlugboardAttack.this.reflectorSelection.removeAllItems();
                if (currentMachine.reflector.length > 1)
                    EnigmaPlugboardAttack.this.reflectorSelection.addItem("-Check all-");
                for (String reflectorName : currentMachine.reflectorNames)
                    EnigmaPlugboardAttack.this.reflectorSelection.addItem(reflectorName);
            }
        });

        EnigmaMachine currentMachine = (EnigmaMachine) EnigmaPlugboardAttack.this.machineSelection.getSelectedItem();
        if (currentMachine.reflector.length > 1)
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
        EnigmaTask tracker = new EnigmaTask(text, app);

        // Settings grab
        tracker.machine = (EnigmaMachine) this.machineSelection.getSelectedItem();
        this.getCipher().setMachine(tracker.machine);
        tracker.reflectorTest = this.reflectorSelection.getSelectedIndex() - 1;
        tracker.start = 0;
        tracker.end = tracker.machine.getReflectorCount();
        if (tracker.reflectorTest != -1) {
            tracker.start = tracker.reflectorTest;
            tracker.end = tracker.start + 1;
        }

        String plugboardInput = this.plugboardDefinition.getText();
        int[][] plugboardDefinition = new int[13][2];
        boolean definitionProvided = false;
        int count = 0;
        for (String split : plugboardInput.split("[, ]")) {
            if (split.length() == 2) {
                plugboardDefinition[count][0] = split.charAt(0) - 'A';
                plugboardDefinition[count++][1] = split.charAt(1) - 'A';
                definitionProvided = true;
            }
        }

        if (definitionProvided) {
            tracker.machine = tracker.machine.createWithPlugboard(plugboardDefinition);
        }

        this.output(tracker, "Using machine type: %s", tracker.machine);
        stop:
        if (method == DecryptionMethod.BRUTE_FORCE) {

            BigInteger numRotors = MathUtil.factorialLength(BigInteger.valueOf(tracker.machine.getRotorCount()), BigInteger.valueOf(3));
            tracker.getProgress().addMaxValue(numRotors.multiply(BigInteger.valueOf(26).pow(3)).multiply(BigInteger.valueOf(tracker.end - tracker.start)));
            Timer timer = new Timer();
            KeyIterator.iterateIntegerArray(tracker::onList, 3, tracker.machine.getRotorCount(), false);
            this.output(tracker, "Time taken %fs", timer.getTimeRunning(Time.SECOND));

            tracker.squeezeFirst.sort();
            this.output(tracker, "Determining ring settings");
            this.output(tracker, "%d possible indicators and rotor orders, therefore %d possible ring settings", tracker.squeezeFirst.size(), tracker.squeezeFirst.size() * 26 * 26);
            
            for (EnigmaSection trial : tracker.squeezeFirst) {
                for (int s2 = 0; s2 < 26; s2++) {
                    for (int s3 = 0; s3 < 26; s3++) {
                        if (tracker.shouldStop()) {
                            break stop;
                        }
                        Integer[] indicator = trial.copyIndicator();
                        Integer[] ring = new Integer[] { 0, s2, s3 };

                        indicator[1] = (indicator[1] + s2) % 26;
                        indicator[2] = (indicator[2] + s3) % 26;

                        char[] plainText = this.getCipher().decodeEfficently(tracker.getCipherText(), tracker.getPlainTextHolder(false), QuinKey.of(indicator, ring, trial.rotors, trial.reflector, null));
                        EnigmaSection nextTrialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(plainText) * 1000, trial.machine, indicator, trial.rotors, trial.reflector);
                        nextTrialSolution.ring = ring;

                        if (tracker.squeezeSecond.add(nextTrialSolution)) {
                            nextTrialSolution.bake();
                        }
                        
                        tracker.increaseIteration();
                    }
                }
            }

            tracker.squeezeSecond.sort();
            this.output(tracker, "Determining plugboard");

            for (EnigmaSection trial : tracker.squeezeSecond) {
                Integer[] plugboard = ArrayUtil.createRangeInteger(0, 26);
                Integer[] possiblePlugBoard = ArrayUtil.createRangeInteger(0, 26);

                Solution bestSolution = Solution.WORST_SOLUTION;
                this.output(tracker, "============================");
                QuinKey<Integer[], Integer[], Integer[], Integer, Integer[]> key = QuinKey.of(trial.indicator, trial.ring, trial.rotors, trial.reflector, plugboard);
                for (int t = 0; t < 13; t++) {
                    char[] plainText = this.getCipher().decodeEfficently(tracker.getCipherText(), tracker.getPlainTextHolder(false), key);

                    Solution bestLocalSolution = new Solution(plainText, StatCalculator.calculateMonoIC(plainText) * 1000).setKeyString(this.getCipher().prettifyKey(key)).bake();
                    this.output(tracker, bestLocalSolution.toString());
                    int bestPlug1 = -1;
                    int bestPlug2 = -1;
                    char[] testText = ArrayUtil.copy(plainText);
                    
                    // Tries every plug combination and 
                    for (int i1 = 0; i1 < possiblePlugBoard.length - 1; i1++) {
                        for (int i2 = i1 + 1; i2 < possiblePlugBoard.length; i2++) {
                            if (tracker.shouldStop()) {
                                break stop;
                            }
                            
                            int plug1 = possiblePlugBoard[i1];
                            int plug2 = possiblePlugBoard[i2];

                            plugboard[plug2] = plug1;
                            plugboard[plug1] = plug2;
                            testText = EnigmaPlugboardAttack.this.getCipher().decodeEfficently(tracker.getCipherText(), testText, key);
                            Solution lastSolution = new Solution(testText, StatCalculator.calculateMonoIC(testText) * 1000); // How should the text be scored?

                            if (lastSolution.isBetterThan(bestLocalSolution)) {
                                bestLocalSolution = lastSolution.setKeyString(this.getCipher().prettifyKey(key)).bake();
                                bestPlug1 = plug1;
                                bestPlug2 = plug2;
                            }

                            plugboard[plug2] = plug2;
                            plugboard[plug1] = plug1;
                        }
                    }
                    
                    // No plug was found
                    if (bestPlug1 < 0) {
                        break;
                    } else {
                        plugboard[bestPlug2] = bestPlug1;
                        plugboard[bestPlug1] = bestPlug2;
    
                        Integer[] possiblePlugBoardNext = new Integer[possiblePlugBoard.length - 2];
                        int currentIndex = 0;
                        for (int i = 0; i < possiblePlugBoard.length; i++) {
                            if (possiblePlugBoard[i] != bestPlug1 && possiblePlugBoard[i] != bestPlug2) {
                                possiblePlugBoardNext[currentIndex++] = possiblePlugBoard[i];
                            }
                        }
    
                        possiblePlugBoard = possiblePlugBoardNext;
                        
                        if (bestLocalSolution.isBetterThan(bestSolution)) {
                            bestSolution = bestLocalSolution;
                            this.output(tracker, bestSolution.toString());
                        }
                    }
                }
                

                if (this.isBetterThanBest(tracker, bestSolution)) {
                    this.updateBestSolution(tracker, bestSolution, key);
                }

            }
        } else if (method == DecryptionMethod.PERIODIC_KEY) {
            KeyIterator.iterateIntegerArray(tracker::onList3, 3, 3, false);
        }

        return tracker;
    }

    public class EnigmaTask extends DecryptionTracker {

        private EnigmaMachine machine;
        private int reflectorTest; // -1 if test all, otherwise is the index of the reflector to test
        private int start, end;
        private DynamicResultList<EnigmaSection> squeezeFirst;
        private DynamicResultList<EnigmaSection> squeezeSecond;

        public EnigmaTask(CharSequence text, IApplication app) {
            super(text, app);
            this.squeezeFirst = new DynamicResultList<EnigmaSection>(256 * 2);
            this.squeezeSecond = new DynamicResultList<EnigmaSection>(64 * 2);
        }

        public boolean onList(Integer[] rotor) {
            return KeyIterator.iterateIntegerArray(o -> onList2(rotor, o), 3, 26, true);
        }

        public boolean onList2(Integer[] rotor, Integer[] indicator) {
            for (int reflector = this.start; reflector < this.end; reflector++) {

                char[] plainText = EnigmaPlugboardAttack.this.getCipher().decodeEfficently(this.getCipherText(), this.getPlainTextHolder(false), QuinKey.of(indicator, EnigmaLib.DEFAULT_SETTING, rotor, reflector, null));
                EnigmaSection trialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(plainText) * 1000, this.machine, indicator, rotor, reflector);

                if (this.squeezeFirst.add(trialSolution)) {
                    trialSolution.bake();
                }
                
                this.increaseIteration();
            }
            return true;
        }

        public boolean onList3(Integer[] rotor) {
            return KeyIterator.iterateIntegerArray(o -> onList2(rotor, o), 3, 26, true);
        }

        public boolean onList4(Integer[] rotor, Integer[] indicator) {
            for (int reflector = this.start; reflector < this.end; reflector++) {
                Integer[] plugboard = new Integer[26];
                Integer[] possiblePlugBoard = new Integer[26];
                for (int i = 0; i < 26; i++) {
                    plugboard[i] = i;
                    possiblePlugBoard[i] = i;
                }

                while (true) {
                    char[] plainText = EnigmaPlugboardAttack.this.getCipher().decodeEfficently(this.getCipherText(), this.getPlainTextHolder(false), QuinKey.of(indicator, EnigmaLib.DEFAULT_SETTING, rotor, reflector, plugboard));
                    Solution bestSolution = new Solution(plainText, StatCalculator.calculateMonoIC(plainText) * 1000).bake();
                    int bestPlug1 = 0;
                    int bestPlug2 = 1;
                    char[] testText = ArrayUtil.copy(plainText);
                    boolean foundFinalPlug = true;
                    for (int i1 = 0; i1 < possiblePlugBoard.length - 1; i1++) {
                        for (int i2 = i1 + 1; i2 < possiblePlugBoard.length; i2++) {
                            int plug1 = possiblePlugBoard[i1];
                            int plug2 = possiblePlugBoard[i2];

                            plugboard[plug2] = plug1;
                            plugboard[plug1] = plug2;
                            testText = EnigmaPlugboardAttack.this.getCipher().decodeEfficently(this.getCipherText(), this.getPlainTextHolder(false), QuinKey.of(indicator, EnigmaLib.DEFAULT_SETTING, rotor, reflector, plugboard));
                            Solution lastSolution = new Solution(testText, StatCalculator.calculateMonoIC(testText) * 1000);

                            if (lastSolution.isBetterThan(bestSolution)) {
                                bestSolution = lastSolution;
                                bestPlug1 = plug1;
                                bestPlug2 = plug2;
                                foundFinalPlug = false;
                            }

                            plugboard[plug2] = plug2;
                            plugboard[plug1] = plug1;
                        }
                    }

                    if (foundFinalPlug) {
                        EnigmaSection trialSolution = new EnigmaSection(bestSolution.score, this.machine.createWithPresetPlugboard(plugboard), indicator, rotor, reflector);
                        if (this.squeezeFirst.add(trialSolution)) {
                            this.out().println("%s", trialSolution);
                            trialSolution.bake();
                        }
                        break;
                    }

                    plugboard[bestPlug2] = bestPlug1;
                    plugboard[bestPlug1] = bestPlug2;

                    Integer[] possiblePlugBoardNext = new Integer[possiblePlugBoard.length - 2];
                    int currentIndex = 0;
                    for (int i = 0; i < possiblePlugBoard.length; i++)
                        if (possiblePlugBoard[i] != bestPlug1 && possiblePlugBoard[i] != bestPlug2)
                            possiblePlugBoardNext[currentIndex++] = possiblePlugBoard[i];

                    possiblePlugBoard = possiblePlugBoardNext;
                }
            }
            return true;
        }
    }
}
