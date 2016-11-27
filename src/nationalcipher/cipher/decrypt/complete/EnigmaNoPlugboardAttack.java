package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javalibrary.fitness.TextFitness;
import nationalcipher.cipher.base.substitution.Enigma;
import nationalcipher.cipher.decrypt.CipherAttack;
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
			//ONLY 3 COGS USED
			KeyIterator.permutateArray(task, (byte)0, 3, 7, false);
			
			Collections.sort(task.best, this.comparator);
			app.out().println("Determining ring settings");
			
			for(int i = 0; i < task.best.size(); i++) {
				EnigmaSection trialSolution = task.best.get(i);
				for(int s2 = 0; s2 < 26; s2++) {
					for(int s3 = 0; s3 < 26; s3++) {
						int[] indicator = Arrays.copyOf(trialSolution.indicator, trialSolution.indicator.length);
						int[] ring = new int[] {0, s2, s3};

						indicator[1] = (indicator[1] + s2) % 26;
						indicator[2] = (indicator[2] + s3) % 26;
					
						task.lastSolution = new Solution(Enigma.decode(task.cipherText, task.plainText, Arrays.copyOf(indicator, indicator.length), ring, trialSolution.rotors), app.getLanguage());
						
						if(task.lastSolution.score > task.bestSolution.score) {
							task.bestSolution = task.lastSolution;
						
							task.bestSolution.setKeyString("%s, %s, %s", this.toString(indicator), this.toString(ring), Arrays.toString(trialSolution.rotors));
						
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

		private List<EnigmaSection> best = new ArrayList<EnigmaSection>();
		
		public EnigmaTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onList(byte id, int[] data, Object... extra) {
			if(id == 0)
				KeyIterator.permutateArray(this, (byte)1, 3, 26, true, data);
			else if(id == 1) {
				int[] rotor = (int[])extra[0];
				
				this.plainText = Enigma.decode(this.cipherText, this.plainText, Arrays.copyOf(data, data.length), new int[] {0, 0, 0}, rotor);
				
				
				EnigmaSection trialSolution = new EnigmaSection(TextFitness.scoreFitnessQuadgrams(this.plainText, this.getLanguage()), data, rotor);

				if(trialSolution.score < this.UPPER_ESTIMATE) {
					this.best.add(trialSolution);
					trialSolution.makeCopy();
				}
			}
		}
	}
	
	public Comparator<EnigmaSection> comparator = new Comparator<EnigmaSection>() {
		@Override
	    public int compare(EnigmaSection c1, EnigmaSection c2) {
			double diff = c2.score - c1.score;
	        return diff == 0.0D ? 0 : diff > 0 ? 1 : -1; 
	    }
	};
	
	public static class EnigmaSection {
		public double score;
		public int[] indicator;
		public int[] rotors;
		public EnigmaSection(double score, int[] notchKey, int[] rotors) {
			this.score = score;
			this.indicator = notchKey;
			this.rotors = rotors;
		}
		
		public void makeCopy() {
			this.indicator = Arrays.copyOf(this.indicator, this.indicator.length);
			this.rotors = Arrays.copyOf(this.rotors, this.rotors.length);
		}
	}
}
