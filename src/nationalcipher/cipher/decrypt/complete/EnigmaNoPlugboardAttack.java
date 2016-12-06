package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;

import javalibrary.fitness.TextFitness;
import javalibrary.lib.Timer;
import javalibrary.list.DynamicResultList;
import javalibrary.math.MathUtil;
import javalibrary.math.Units.Time;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.substitution.Enigma;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.complete.EnigmaAttack.EnigmaSection;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ArrayPermutations;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.ui.IApplication;

public class EnigmaNoPlugboardAttack extends CipherAttack {

	public EnigmaNoPlugboardAttack() {
		super("Enigma No Plugboard");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		EnigmaTask task = new EnigmaTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			int rotorCombos = MathUtil.factorial(task.getNumberRotor(), 3);
			app.out().println("Going throught all combinations of the %d rotors (%d) and indicator settings (%d), totalling %d test subjects.", task.getNumberRotor(), rotorCombos, (int)Math.pow(26, 3), rotorCombos * (int)Math.pow(26, 3));
			double constant = 120 / 60000D; //Time taken per letter per rotor setting
			app.out().println("Estimated time %c %ds, This may take a while...", (char)8776, (int)(constant * rotorCombos * task.cipherText.length * 3));
			Timer timer = new Timer();
			KeyIterator.permutateArray(task, (byte)0, 3, task.getNumberRotor(), false);
			app.out().println("Time taken %fs", timer.getTimeRunning(Time.SECOND));
			
			task.squeezeFirst.sort();
			app.out().println("Determining ring settings");
			
			for(int i = 0; i < task.squeezeFirst.size(); i++) {
				EnigmaSection trial = task.squeezeFirst.get(i);
				for(int s2 = 0; s2 < 26; s2++) {
					for(int s3 = 0; s3 < 26; s3++) {
						int[] indicator = trial.copyIndicator();
						int[] ring = new int[] {0, s2, s3};

						indicator[1] = (indicator[1] + s2) % 26;
						indicator[2] = (indicator[2] + s3) % 26;
					
						task.lastSolution = new Solution(task.decryptEnigma(task.cipherText, task.plainText, trial.etw, trial.etwInverse, trial.reflector, Arrays.copyOf(indicator, indicator.length), ring, trial.rotors), app.getLanguage());
						
						if(task.lastSolution.score > task.bestSolution.score) {
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
		for(int i = 0; i < order.length; i++)
			text[i] = (char)(order[i] + 'A');
		return new String(text);
	}
	
	public class EnigmaTask extends InternalDecryption implements ArrayPermutations {

		private DynamicResultList<EnigmaSection> squeezeFirst;
		
		public EnigmaTask(String text, IApplication app) {
			super(text.toCharArray(), app);
			this.squeezeFirst = new DynamicResultList<EnigmaSection>(500);
		}

		@Override
		public void onList(byte id, int[] data, Object... extra) {
			if(id == 0)
				KeyIterator.permutateArray(this, (byte)1, 3, 26, true, data);
			else if(id == 1) {
				//for(char[] reflector : EnigmaLib.ENIGMA_REFLECTORS) {
					int[] rotor = (int[])extra[0];
					
					this.plainText = this.decryptEnigma(this.cipherText, this.plainText, EnigmaLib.ENIGMA_D_ETW, EnigmaLib.ENIGMA_D_ETW_INVERSE, EnigmaLib.ENIGMA_D_UKW, Arrays.copyOf(data, data.length), EnigmaLib.DEFAULT_SETTING, rotor);
					
					EnigmaSection trialSolution = new EnigmaSection(TextFitness.scoreFitnessQuadgrams(this.plainText, this.getLanguage()), EnigmaLib.ENIGMA_D_ETW, EnigmaLib.ENIGMA_D_ETW_INVERSE, EnigmaLib.ENIGMA_D_UKW, data, rotor);
	
					if(this.squeezeFirst.addResult(trialSolution))
						trialSolution.makeCopy();
				//}
			}
		}
		
		public int getNumberRotor() {
			return EnigmaLib.NO_ENIGMA_D_ROTORS;
		}
		
		public byte[] decryptEnigma(char[] cipherText, byte[] plainText, char[] etw, char[] etwInverse, char[] reflector, int[] indicator, int[] ring, int[] rotors) {
			return Enigma.decode(this.cipherText, this.plainText, 
					EnigmaLib.ENIGMA_D_ROTORS, EnigmaLib.ENIGMA_D_ROTORS_INVERSE, EnigmaLib.ENIGMA_D_ROTORS_NOTCHES, 
					etw, etwInverse, reflector, indicator, ring, rotors);
			
			//EnigmaLib.ENIGMA_ROTORS, EnigmaLib.ENIGMA_ROTORS_INVERSE, EnigmaLib.ENIGMA_ROTORS_NOTCHES, EnigmaLib.REFLECTOR_B
		}
	}
}
