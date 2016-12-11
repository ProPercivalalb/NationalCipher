package nationalcipher.cipher.decrypt.complete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javalibrary.lib.Timer;
import javalibrary.list.DynamicResultList;
import javalibrary.list.ResultNegative;
import javalibrary.math.MathUtil;
import javalibrary.math.Units.Time;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.substitution.Enigma;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ArrayPermutations;
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
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
		this.machineSelection = new JComboBox<EnigmaMachine>();
		this.reflectorSelection = new JComboBox<String>();
		this.plugboardDefinition = new JTextField();
		for(EnigmaMachine machine : EnigmaLib.MACHINES)
			if(machine.isSolvable() && machine.canPlugboard() && !machine.hasThinRotor())
				this.machineSelection.addItem(machine);
		
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
		char[][] plugboardDefinition = new char[13][2];
		boolean definitionProvided = false;
		int count = 0;
		for(String split : plugboardInput.split("[, ]"))
			if(split.length() == 2) {
				plugboardDefinition[count++] = split.toCharArray();
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
			KeyIterator.permutateArray(task, (byte)0, 3, task.machine.getNumberOfRotors(), false);
			app.out().println("Time taken %fs", timer.getTimeRunning(Time.SECOND));
			
			task.squeezeFirst.sort();
			app.out().println("Determining ring settings");
			app.out().println("%d possible indicators and rotor orders, therefore %d possible ring settings", task.squeezeFirst.size(), task.squeezeFirst.size() * 26 * 26);

			
			for(int i = 0; i < task.squeezeFirst.size(); i++) {
				EnigmaSection trial = task.squeezeFirst.get(i);
				for(int s2 = 0; s2 < 26; s2++) {
					for(int s3 = 0; s3 < 26; s3++) {
						int[] indicator = trial.copyIndicator();
						int[] ring = new int[] {0, s2, s3};

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
			
				int plugboardIndex = 0;
				char[][] plugboard = new char[13][2];
				String possiblePlugBoard = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				while(true) {
		
					task.plainText = Enigma.decode(task.cipherText, task.plainText, trial.machine, trial.copyIndicator(), trial.ring, trial.rotors, trial.reflector, plugboard);
					
					Solution bestSolution = new Solution(task.plainText, app.getLanguage().getTrigramData()).bakeSolution();
					byte[] testText = Arrays.copyOf(task.plainText, task.plainText.length);
					boolean foundFinalPlug = true;
					for(int i1 = 0; i1 < possiblePlugBoard.length() - 1; i1++) {
						for(int i2 = i1 + 1; i2 < possiblePlugBoard.length(); i2++) {
							if(i1 == i2) {
								app.out().println("Same letter: ");
								continue;
							}
							plugboard[plugboardIndex][0] = possiblePlugBoard.charAt(i1);
							plugboard[plugboardIndex][1] = possiblePlugBoard.charAt(i2);
							testText = Enigma.decode(task.cipherText, testText, trial.machine, trial.copyIndicator(), trial.ring, trial.rotors, trial.reflector, plugboard);
							Solution lastSolution = new Solution(testText, app.getLanguage().getTrigramData());
	
							if(lastSolution.isResultBetter(bestSolution)) {
								bestSolution = lastSolution;
								bestSolution.setKeyString("%c%c", plugboard[plugboardIndex][0], plugboard[plugboardIndex][1]);
								bestSolution.bakeSolution();
								foundFinalPlug = false;
							}
						}
					}
					
					if(foundFinalPlug) {
						char[] plugs = new char[Math.max(plugboardIndex * 3 - 1, 0)];
						Arrays.fill(plugs, ' ');
						for(int p = 0; p < plugboardIndex; p++) {
							plugs[p * 3] = plugboard[p][0];
							plugs[p * 3 + 1] = plugboard[p][1];
						}
						bestSolution.setKeyString("%s, Plugs:[%s]", trial.toKeyString(), new String(plugs));
						app.out().println("%s", bestSolution);
						if(bestSolution.isResultBetter(task.bestSolution))
							task.bestSolution = bestSolution;
						break;
					}
					
					possiblePlugBoard = possiblePlugBoard.replaceAll(String.format("[%s]", bestSolution.keyString), "");
					plugboard[plugboardIndex][0] = bestSolution.keyString.charAt(0);
					plugboard[plugboardIndex][1] = bestSolution.keyString.charAt(1);
					plugboardIndex++;
				}
			}
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class EnigmaTask extends InternalDecryption implements ArrayPermutations {

		private EnigmaMachine machine;
		private int reflectorTest; //-1 if test all, otherwise is the index of the reflector to test
		private int start, end;
		private DynamicResultList<EnigmaSection> squeezeFirst;
		private DynamicResultList<EnigmaSection> squeezeSecond;
		
		public EnigmaTask(String text, IApplication app) {
			super(text.toCharArray(), app);
			this.squeezeFirst = new DynamicResultList<EnigmaSection>(256);
			this.squeezeSecond = new DynamicResultList<EnigmaSection>(64);
		}

		@Override
		public void onList(byte id, int[] data, Object... extra) {
			if(id == 0)
				KeyIterator.permutateArray(this, (byte)1, 3, 26, true, data);
			else if(id == 1) {
				int[] rotor = (int[])extra[0];

				for(int reflector = this.start; reflector < this.end; reflector++) {
					
					this.plainText = Enigma.decode(this.cipherText, this.plainText, this.machine, Arrays.copyOf(data, data.length), EnigmaLib.DEFAULT_SETTING, rotor, reflector);
					EnigmaSection trialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(this.plainText) * 1000, this.machine, data, rotor, reflector);
					
					if(this.squeezeFirst.addResult(trialSolution))
						trialSolution.makeCopy();
				}
			}
		}
	}
	
	public static class EnigmaSection extends ResultNegative {
		
		public EnigmaMachine machine;
		public int[] indicator;
		public int[] ring;
		public int[] rotors;
		public int reflector;

		public EnigmaSection(double score, EnigmaMachine machine, int[] notchKey, int[] rotors, int reflector) {
			super(score);
			this.machine = machine;
			this.indicator = notchKey;
			this.rotors = rotors;
			this.reflector = reflector;
		}

		public void makeCopy() {
			this.indicator = Arrays.copyOf(this.indicator, this.indicator.length);
			this.rotors = Arrays.copyOf(this.rotors, this.rotors.length);
			if(this.ring != null) this.ring = Arrays.copyOf(this.ring, this.ring.length);
		}
		
		public int[] copyIndicator() {
			return Arrays.copyOf(this.indicator, this.indicator.length);
		}
		
		public String displaySetting(int[] order) {
			if(order == null) return "null";
			char[] text = new char[3];
			for(int i = 0; i < order.length; i++)
				text[i] = (char)(order[i] + 'A');
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
