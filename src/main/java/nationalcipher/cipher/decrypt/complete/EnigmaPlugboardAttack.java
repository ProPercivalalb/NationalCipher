package nationalcipher.cipher.decrypt.complete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javalibrary.lib.Timer;
import javalibrary.list.DynamicResultList;
import javalibrary.math.MathUtil;
import javalibrary.math.Units.Time;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.base.enigma.EnigmaUtil;
import nationalcipher.cipher.base.substitution.Enigma;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.complete.EnigmaPlainAttack.EnigmaSection;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class EnigmaPlugboardAttack extends CipherAttack {

	private JComboBox<EnigmaMachine> machineSelection;
	private JComboBox<String> reflectorSelection;
	private JTextField plugboardDefinition;
	
	public EnigmaPlugboardAttack() {
		super("Enigma - Plugboard");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
		this.machineSelection = new JComboBox<EnigmaMachine>();
		this.reflectorSelection = new JComboBox<String>();
		this.plugboardDefinition = new JTextField();

		Stream.of(EnigmaLib.MACHINES).filter(m -> m.isSolvable() && m.canPlugboard() && !m.hasThinRotor()).forEach(m -> this.machineSelection.addItem(m));
		
		this.machineSelection.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				EnigmaMachine currentMachine = (EnigmaMachine)EnigmaPlugboardAttack.this.machineSelection.getSelectedItem();
				
				EnigmaPlugboardAttack.this.reflectorSelection.removeAllItems();
				if(currentMachine.reflectorCount > 1)
					EnigmaPlugboardAttack.this.reflectorSelection.addItem("-Check all-");
				for(String reflectorName : currentMachine.reflectorNames)
					EnigmaPlugboardAttack.this.reflectorSelection.addItem(reflectorName);
			}
		});
		
		EnigmaMachine currentMachine = (EnigmaMachine)EnigmaPlugboardAttack.this.machineSelection.getSelectedItem();
		if(currentMachine.reflectorCount > 1)
			this.reflectorSelection.addItem("-Check all-");
		for(String reflectorName : currentMachine.reflectorNames)
			this.reflectorSelection.addItem(reflectorName);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Machine Version:", this.machineSelection));
		panel.add(new SubOptionPanel("Reflector:", this.reflectorSelection));
		panel.add(new SubOptionPanel("Plugboard:", this.plugboardDefinition));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		EnigmaTask task = new EnigmaTask(text, app);
		
		//Settings grab
		task.machine = (EnigmaMachine)this.machineSelection.getSelectedItem();
		task.reflectorTest = this.reflectorSelection.getSelectedIndex() - 1;
		task.start = 0;
		task.end = task.machine.reflectorCount;
		if(task.reflectorTest != -1) {
			task.start = task.reflectorTest;
			task.end = task.start + 1;
		}
		
		
		String plugboardInput = this.plugboardDefinition.getText();
		int[][] plugboardDefinition = new int[13][2];
		boolean definitionProvided = false;
		int count = 0;
		for(String split : plugboardInput.split("[, ]"))
			if(split.length() == 2) {
				plugboardDefinition[count][0] = split.charAt(0) - 'A';
				plugboardDefinition[count++][1] = split.charAt(1) - 'A';
				definitionProvided = true;
			}
		
		if(definitionProvided)
			task.machine = task.machine.createWithPlugboard(plugboardDefinition);
		
		app.out().println("Using machine type: %s", task.machine);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			int rotorCombos = MathUtil.factorial(task.machine.getNumberOfRotors(), 3);
			app.out().println("Going throught all combinations of the %d rotors (%d) and indicator settings (%d), totalling %d test subjects.", task.machine.getNumberOfRotors(), rotorCombos, (int)Math.pow(26, 3), rotorCombos * (int)Math.pow(26, 3));
			double constant = 120 / 60000D; //Time taken per letter per rotor setting
			app.out().println("Estimated time %c %ds, This may take a while...", (char)8776, (int)(constant * rotorCombos * task.cipherText.length * (task.reflectorTest == -1 ? task.machine.getNumberOfReflectors() : 1)));
			Timer timer = new Timer();
			KeyIterator.iterateIntegerArray(task::onList, 3, task.machine.getNumberOfRotors(), false);
			app.out().println("Time taken %fs", timer.getTimeRunning(Time.SECOND));
			
			task.squeezeFirst.sort();
			app.out().println("Determining ring settings");
			app.out().println("%d possible indicators and rotor orders, therefore %d possible ring settings", task.squeezeFirst.size(), task.squeezeFirst.size() * 26 * 26);

			
			for(int i = 0; i < task.squeezeFirst.size(); i++) {
				EnigmaSection trial = task.squeezeFirst.get(i);
				for(int s2 = 0; s2 < 26; s2++) {
					for(int s3 = 0; s3 < 26; s3++) {
						Integer[] indicator = trial.copyIndicator();
						Integer[] ring = new Integer[] {0, s2, s3};

						indicator[1] = (indicator[1] + s2) % 26;
						indicator[2] = (indicator[2] + s3) % 26;
				
						task.plainText = Enigma.decode(task.cipherText, task.plainText, trial.machine, Arrays.copyOf(indicator, indicator.length), ring, trial.rotors, trial.reflector);
						EnigmaSection nextTrialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(task.plainText) * 1000, trial.machine, indicator, trial.rotors, trial.reflector);
						nextTrialSolution.ring = ring;
	
						if(task.squeezeSecond.addResult(nextTrialSolution))
							nextTrialSolution.makeCopy();
					}
				}
			}
			
			task.squeezeSecond.sort();
			app.out().println("Determining plugboard");
			
			for(int option = 0; option < task.squeezeSecond.size(); option++) {
				EnigmaSection trial = task.squeezeSecond.get(option);
				
				Integer[] plugboard = new Integer[26];
				Integer[] possiblePlugBoard = new Integer[26];
				for(int i = 0; i < 26; i++) {
					plugboard[i] = i;
					possiblePlugBoard[i] = i;
				}
				Solution bestSolution = null;
				for(int t = 0; t < 13; t++) {
					task.plainText = Enigma.decode(task.cipherText, task.plainText, trial.machine, trial.copyIndicator(), trial.ring, trial.rotors, -1, trial.reflector, plugboard);
					
					bestSolution = new Solution(task.plainText, StatCalculator.calculateMonoIC(task.plainText) * 1000).bakeSolution();
					int bestPlug1 = 0;
					int bestPlug2 = 1;
					byte[] testText = Arrays.copyOf(task.plainText, task.plainText.length);
					boolean foundFinalPlug = true;
					for(int i1 = 0; i1 < possiblePlugBoard.length - 1; i1++) {
						for(int i2 = i1 + 1; i2 < possiblePlugBoard.length; i2++) {
							int plug1 = possiblePlugBoard[i1];
							int plug2 = possiblePlugBoard[i2];
							
							plugboard[plug2] = plug1;
							plugboard[plug1] = plug2;
							testText = Enigma.decode(task.cipherText, testText, trial.machine, trial.copyIndicator(), trial.ring, trial.rotors, -1, trial.reflector, plugboard);
							Solution lastSolution = new Solution(testText, StatCalculator.calculateMonoIC(testText) * 1000);
	
							if(lastSolution.isResultBetter(bestSolution)) {
								bestSolution = lastSolution;
								bestPlug1 = plug1;
								bestPlug2 = plug2;
								foundFinalPlug = false;
							}

							plugboard[plug2] = plug2;
							plugboard[plug1] = plug1;
						}
					}
					
					if(foundFinalPlug) break;
				
					plugboard[bestPlug2] = bestPlug1;
					plugboard[bestPlug1] = bestPlug2;
					
					Integer[] possiblePlugBoardNext = new Integer[possiblePlugBoard.length - 2];
					int currentIndex = 0;
					for(int i = 0; i < possiblePlugBoard.length; i++)
						if(possiblePlugBoard[i] != bestPlug1 && possiblePlugBoard[i] != bestPlug2)
							possiblePlugBoardNext[currentIndex++] = possiblePlugBoard[i];
					
					possiblePlugBoard = possiblePlugBoardNext;
				}
				
				/**
				int plugboardIndex = 0;
				int[][] plugboard = new int[13][2];
				int[] possiblePlugBoard = new int[26];
				for(int i = 0; i < 26; i++) possiblePlugBoard[i] = i;
				while(true) {
		
					task.plainText = Enigma.decode(task.cipherText, task.plainText, trial.machine, trial.copyIndicator(), trial.ring, trial.rotors, trial.reflector, plugboard);
					
					Solution bestSolution = new Solution(task.plainText, app.getLanguage().getTrigramData()).bakeSolution();
					int bestPlug1 = 0;
					int bestPlug2 = 1;
					byte[] testText = Arrays.copyOf(task.plainText, task.plainText.length);
					boolean foundFinalPlug = true;
					for(int i1 = 0; i1 < possiblePlugBoard.length - 1; i1++) {
						for(int i2 = i1 + 1; i2 < possiblePlugBoard.length; i2++) {
							if(i1 == i2) {
								app.out().println("Same letter: ");
								continue;
							}
							plugboard[plugboardIndex][0] = possiblePlugBoard[i1];
							plugboard[plugboardIndex][1] = possiblePlugBoard[i2];
							testText = Enigma.decode(task.cipherText, testText, trial.machine, trial.copyIndicator(), trial.ring, trial.rotors, trial.reflector, plugboard);
							Solution lastSolution = new Solution(testText, app.getLanguage().getTrigramData());
	
							if(lastSolution.isResultBetter(bestSolution)) {
								bestSolution = lastSolution;
								bestSolution.setKeyString("%c%c", plugboard[plugboardIndex][0], plugboard[plugboardIndex][1]);
								bestPlug1 = plugboard[plugboardIndex][0];
								bestPlug2 = plugboard[plugboardIndex][1];
								bestSolution.bakeSolution();
								foundFinalPlug = false;
							}
						}
					}
					
					plugboard[plugboardIndex][0] = bestPlug1;
					plugboard[plugboardIndex][1] = bestPlug2;
					
					if(foundFinalPlug) {
						plugboardIndex++;
						char[] plugs = new char[Math.max(plugboardIndex * 3 - 1, 0)];
						Arrays.fill(plugs, ' ');
						for(int p = 0; p < plugboardIndex; p++) {
							plugs[p * 3] = (char)(plugboard[p][0] + 'A');
							plugs[p * 3 + 1] = (char)(plugboard[p][1] + 'A');
						}
						bestSolution.setKeyString("%s, Plugs:[%s]", trial.toKeyString(), new String(plugs));
						app.out().println("%s", bestSolution);
						if(bestSolution.isResultBetter(task.bestSolution))
							task.bestSolution = bestSolution;
						break;
					}
					
					
					int[] possiblePlugBoardNext = new int[possiblePlugBoard.length - 2];
					int currentIndex = 0;
					for(int i = 0; i < possiblePlugBoard.length; i++)
						if(!ArrayUtil.contains(plugboard[plugboardIndex], possiblePlugBoard[i]))
							possiblePlugBoardNext[currentIndex++] = possiblePlugBoard[i];
					
					possiblePlugBoard = possiblePlugBoardNext;
					
					plugboardIndex++;
				}**/
				
				
				

				bestSolution.setKeyString("%s, Plugs:[%s]", trial.toKeyString(), EnigmaUtil.convertMappingToReadablePlugboard(plugboard));
				/**
				Solution bestSolution2 = Solution.WORST_SOLUTION;
				for(int s2 = 0; s2 < 26; s2++) {
					for(int s3 = 0; s3 < 26; s3++) {
						int[] indicator = trial.copyIndicator();
						int[] ring = new int[] {trial.ring[0], trial.ring[1], trial.ring[2]};
						
						indicator[1] = (indicator[1] + s2) % 26;
						indicator[2] = (indicator[2] + s3) % 26;
				
						ring[1] = (ring[1] + s2) % 26;
						ring[2] = (ring[2] + s3) % 26;
						
						task.plainText = Enigma.decode(task.cipherText, task.plainText, trial.machine, indicator, ring, trial.rotors, -1, trial.reflector, plugboard);
						Solution lastSolution = new Solution(task.plainText, app.getLanguage());
	
						if(lastSolution.isResultBetter(bestSolution2)) {
							bestSolution2 = lastSolution;
							bestSolution2.setKeyString("%s, Plugs:[%s]", trial.toKeyString(), str);
							bestSolution2.bakeSolution();
						}
					}
				}
				app.out().println("%s", bestSolution2);
				if(bestSolution2.isResultBetter(task.bestSolution))
					task.bestSolution = bestSolution2;**/
				

				app.out().println("%s", bestSolution);
				if(bestSolution.isResultBetter(task.bestSolution))
					task.bestSolution = bestSolution;
				
			}
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			KeyIterator.iterateIntegerArray(task::onList3, 3, 3, false);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class EnigmaTask extends InternalDecryption {

		private EnigmaMachine machine;
		private int reflectorTest; //-1 if test all, otherwise is the index of the reflector to test
		private int start, end;
		private DynamicResultList<EnigmaSection> squeezeFirst;
		private DynamicResultList<EnigmaSection> squeezeSecond;
		
		public EnigmaTask(String text, IApplication app) {
			super(text.toCharArray(), app);
			this.squeezeFirst = new DynamicResultList<EnigmaSection>(256 * 2);
			this.squeezeSecond = new DynamicResultList<EnigmaSection>(64 * 2);
		}

		public void onList(Integer[] rotor) {
			KeyIterator.iterateIntegerArray(o -> onList2(rotor, o), 3, 26, true);
		}
		
		public void onList2(Integer[] rotor, Integer[] data) {
			for(int reflector = this.start; reflector < this.end; reflector++) {
				
				this.plainText = Enigma.decode(this.cipherText, this.plainText, this.machine, Arrays.copyOf(data, data.length), EnigmaLib.DEFAULT_SETTING, rotor, reflector);
				EnigmaSection trialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(this.plainText) * 1000, this.machine, data, rotor, reflector);
				
				if(this.squeezeFirst.addResult(trialSolution))
					trialSolution.makeCopy();
			}
		}
		
		public void onList3(Integer[] rotor) {
			KeyIterator.iterateIntegerArray(o -> onList2(rotor, o),  3, 26, true);
		}
		
		public void onList4(Integer[] rotor, Integer[] data) {
			for(int reflector = this.start; reflector < this.end; reflector++) {
				Integer[] plugboard = new Integer[26];
				Integer[] possiblePlugBoard = new Integer[26];
				for(int i = 0; i < 26; i++) {
					plugboard[i] = i;
					possiblePlugBoard[i] = i;
				}
				
				while(true) {
					this.plainText = Enigma.decode(this.cipherText, this.plainText, this.machine, Arrays.copyOf(data, data.length), EnigmaLib.DEFAULT_SETTING, rotor, -1, reflector, plugboard);
					
					Solution bestSolution = new Solution(this.plainText, StatCalculator.calculateMonoIC(this.plainText) * 1000).bakeSolution();
					int bestPlug1 = 0;
					int bestPlug2 = 1;
					byte[] testText = Arrays.copyOf(this.plainText, this.plainText.length);
					boolean foundFinalPlug = true;
					for(int i1 = 0; i1 < possiblePlugBoard.length - 1; i1++) {
						for(int i2 = i1 + 1; i2 < possiblePlugBoard.length; i2++) {
							int plug1 = possiblePlugBoard[i1];
							int plug2 = possiblePlugBoard[i2];
							
							plugboard[plug2] = plug1;
							plugboard[plug1] = plug2;
							testText = Enigma.decode(this.cipherText, testText, this.machine, Arrays.copyOf(data, data.length), EnigmaLib.DEFAULT_SETTING, rotor, -1, reflector, plugboard);
							Solution lastSolution = new Solution(testText, StatCalculator.calculateMonoIC(testText) * 1000);
	
							if(lastSolution.isResultBetter(bestSolution)) {
								bestSolution = lastSolution;
								bestPlug1 = plug1;
								bestPlug2 = plug2;
								foundFinalPlug = false;
							}

							plugboard[plug2] = plug2;
							plugboard[plug1] = plug1;
						}
					}
					
					if(foundFinalPlug) {
						EnigmaSection trialSolution = new EnigmaSection(bestSolution.score, this.machine.createWithPresetPlugboard(plugboard), data, rotor, reflector);
						if(this.squeezeFirst.addResult(trialSolution)) {
							this.app.out().println("%s", trialSolution);
							trialSolution.makeCopy();
						}
						break;
					}
				
					plugboard[bestPlug2] = bestPlug1;
					plugboard[bestPlug1] = bestPlug2;
					
					Integer[] possiblePlugBoardNext = new Integer[possiblePlugBoard.length - 2];
					int currentIndex = 0;
					for(int i = 0; i < possiblePlugBoard.length; i++)
						if(possiblePlugBoard[i] != bestPlug1 && possiblePlugBoard[i] != bestPlug2)
							possiblePlugBoardNext[currentIndex++] = possiblePlugBoard[i];
					
					possiblePlugBoard = possiblePlugBoardNext;
				}
			}
		}
	}
}
