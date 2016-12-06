package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;

import javalibrary.lib.Timer;
import javalibrary.list.DynamicResultList;
import javalibrary.list.ResultNegative;
import javalibrary.math.MathUtil;
import javalibrary.math.Units.Time;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.substitution.Enigma;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ArrayPermutations;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.ui.IApplication;

public class EnigmaAttack extends CipherAttack {

	public EnigmaAttack() {
		super("Enigma");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		EnigmaTask task = new EnigmaTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			int rotorCombos = MathUtil.factorial(task.getNumberRotor(), 3);
			app.out().println("Going throught all combinations of the %d rotors (%d) and indicator settings (%d), totalling %d test subjects.", task.getNumberRotor(), rotorCombos, (int)Math.pow(26, 3), rotorCombos * (int)Math.pow(26, 3));
			double constant = 120 / 60000D; //Time taken per letter per rotor setting
			app.out().println("Estimated time %c %ds, This may take a while...", (char)8776, (int)(constant * rotorCombos * task.cipherText.length));
			Timer timer = new Timer();
			KeyIterator.permutateArray(task, (byte)0, 3, task.getNumberRotor(), false);
			app.out().println("Time taken %fs", timer.getTimeRunning(Time.SECOND));
			
			task.squeezeFirst.sort();
			app.out().println("Determining ring settings");
			app.out().println("%d Possible indicators and rotor orders, therefore %d possible ring settings", task.squeezeFirst.size(), task.squeezeFirst.size() * 26);

			
			for(int i = 0; i < task.squeezeFirst.size(); i++) {
				EnigmaSection trial = task.squeezeFirst.get(i);
				for(int s3 = 0; s3 < 26; s3++) {
					int[] indicator = trial.copyIndicator();
					int[] ring = new int[] {0, 0, s3};

					indicator[2] = (indicator[2] + s3) % 26;
				
					task.plainText = task.decryptEnigma(task.cipherText, task.plainText, trial.reflector, Arrays.copyOf(indicator, indicator.length), ring, trial.rotors);
					EnigmaSection nextTrialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(task.plainText) * 1000, trial.reflector, indicator, trial.rotors);
					nextTrialSolution.ring = ring;
	
					if(task.squeezeSecond.addResult(nextTrialSolution))
						nextTrialSolution.makeCopy();
			
				}
			}
			
			task.squeezeSecond.sort();
			
			for(int i = 0; i < task.squeezeSecond.size(); i++) {
				EnigmaSection trial = task.squeezeSecond.get(i);

				for(int s2 = 0; s2 < 26; s2++) {
					int[] indicator = trial.copyIndicator();
					int[] ring = new int[] {0, s2, trial.ring[2]};
	
					indicator[1] = (indicator[1] + s2) % 26;
					
					task.plainText = task.decryptEnigma(task.cipherText, task.plainText, trial.reflector, Arrays.copyOf(indicator, indicator.length), ring, trial.rotors);
					EnigmaSection nextTrialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(task.plainText) * 1000, trial.reflector, indicator, trial.rotors);
					nextTrialSolution.ring = ring;
	
					if(task.squeezeThird.addResult(nextTrialSolution))
						nextTrialSolution.makeCopy();
				}
			}
			
			task.squeezeThird.sort();
			
			for(int option = 0; option < task.squeezeThird.size(); option++) {
				EnigmaSection trial = task.squeezeThird.get(option);
			
				int plugboardIndex = 0;
				char[][] plugboard = new char[13][2];
				String possiblePlugBoard = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				while(true) {
		
					task.plainText = task.decryptEnigma(task.cipherText, task.plainText, trial.reflector, trial.copyIndicator(), trial.ring, trial.rotors, plugboard);
					
					Solution bestSolution = new Solution(task.plainText, app.getLanguage().getTrigramData()).bakeSolution();
					byte[] testText = Arrays.copyOf(task.plainText, task.plainText.length);
					boolean foundFinalPlug = true;
					for(int i1 = 0; i1 < possiblePlugBoard.length() - 1; i1++) {
						for(int i2 = i1 + 1; i2 < possiblePlugBoard.length(); i2++) {
							if(i1 == i2) continue;
							plugboard[plugboardIndex][0] = possiblePlugBoard.charAt(i1);
							plugboard[plugboardIndex][1] = possiblePlugBoard.charAt(i2);
							testText = task.decryptEnigma(task.cipherText, testText, trial.reflector, trial.copyIndicator(), trial.ring, trial.rotors, plugboard);
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

		private DynamicResultList<EnigmaSection> squeezeFirst;
		private DynamicResultList<EnigmaSection> squeezeSecond;
		private DynamicResultList<EnigmaSection> squeezeThird;
		
		public EnigmaTask(String text, IApplication app) {
			super(text.toCharArray(), app);
			this.squeezeFirst = new DynamicResultList<EnigmaSection>(256);
			this.squeezeSecond = new DynamicResultList<EnigmaSection>(256);
			this.squeezeThird = new DynamicResultList<EnigmaSection>(64);
		}

		@Override
		public void onList(byte id, int[] data, Object... extra) {
			if(id == 0)
				KeyIterator.permutateArray(this, (byte)1, 3, 26, true, data);
			else if(id == 1) {
				int[] rotor = (int[])extra[0];
				
				this.plainText = this.decryptEnigma(this.cipherText, this.plainText, EnigmaLib.REFLECTOR_B, Arrays.copyOf(data, data.length), EnigmaLib.DEFAULT_SETTING, rotor);
				EnigmaSection trialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(this.plainText) * 1000, EnigmaLib.REFLECTOR_B, data, rotor);
				
				if(this.squeezeFirst.addResult(trialSolution))
					trialSolution.makeCopy();

			}
		}
		
		public char[][] getReflectors() {
			return EnigmaLib.ENIGMA_REFLECTORS;
		}
		
		public int getNumberRotor() {
			return EnigmaLib.NO_ENIGMA_ROTORS;
		}
		
		public byte[] decryptEnigma(char[] cipherText, byte[] plainText, char[] reflector, int[] indicator, int[] ring, int[] rotors) {
			return Enigma.decode(this.cipherText, this.plainText, 
					EnigmaLib.ENIGMA_ROTORS, EnigmaLib.ENIGMA_ROTORS_INVERSE, EnigmaLib.ENIGMA_ROTORS_NOTCHES,
					reflector, indicator, ring, rotors);
		}
		
		public byte[] decryptEnigma(char[] cipherText, byte[] plainText, char[] reflector, int[] indicator, int[] ring, int[] rotors, char[][] plugboard) {
			return Enigma.decode(this.cipherText, this.plainText, 
					EnigmaLib.ENIGMA_ROTORS, EnigmaLib.ENIGMA_ROTORS_INVERSE, EnigmaLib.ENIGMA_ROTORS_NOTCHES, 
					reflector, indicator, ring, rotors, plugboard);
		}
	}
	
	public static class EnigmaSection extends ResultNegative {
		
		public char[] etw;
		public char[] etwInverse;
		public char[] reflector;
		public int[] indicator;
		public int[] ring;
		public int[] rotors;

		public EnigmaSection(double score, char[] etw, char[] etwInverse, char[] reflector, int[] notchKey, int[] rotors) {
			super(score);
			this.etw = etw;
			this.etwInverse = etwInverse;
			this.reflector = reflector;
			this.indicator = notchKey;
			this.rotors = rotors;
		}
		
		public EnigmaSection(double score, char[] reflector, int[] notchKey, int[] rotors) {
			this(score, new char[0], new char[0], reflector, notchKey, rotors);
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
			return String.format("Machine Type: [R:%s], Rotors:%s: Ind:%s, Ring:%s", new String(this.reflector), Arrays.toString(this.rotors), this.displaySetting(this.indicator), this.displaySetting(this.ring));
		}
		
		@Override
		public String toString() {
			return String.format("%f, %s", this.score, this.toKeyString());
		}
	}
}
