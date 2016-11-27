package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
			//ONLY 5 COGS USED
			//KeyIterator.permutateArray(task, (byte)0, 3, 3, false);
			task.onList((byte)0, new int[] {0, 1, 2});
			
			Collections.sort(task.best, this.comparator);
			app.out().println("Determining ring settings");
			app.out().println("%d Possible indicators and rotor orders, therefore %d possible ring settings", task.best.size(), task.best.size() * 26);
			
			task.best = task.best.subList(0, Math.min(200, task.best.size()));
			app.out().println("%d Possible indicators and rotor orders, therefore %d possible ring settings", task.best.size(), task.best.size() * 26);
			
			
			for(int i = 0; i < task.best.size(); i++) {
				EnigmaSection trialSolution = task.best.get(i);
				//app.out().println("%f, Ind:%s, %s", trialSolution.score, toString(trialSolution.indicator), Arrays.toString(trialSolution.rotors));
				for(int s3 = 0; s3 < 26; s3++) {
					int[] indicator = Arrays.copyOf(trialSolution.indicator, trialSolution.indicator.length);
					int[] ring = new int[] {0, 0, s3};

					indicator[2] = (indicator[2] + s3) % 26;
				
					task.plainText = Enigma.decode(task.cipherText, task.plainText, Arrays.copyOf(indicator, indicator.length), ring, trialSolution.rotors);
					EnigmaSection nextTrialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(task.plainText) * 1000, indicator, trialSolution.rotors);
					nextTrialSolution.ring = ring;
					//app.out().println("    %f, Ind:%s, Ring:%s, %s", nextTrialSolution.score, toString(nextTrialSolution.indicator), toString(nextTrialSolution.ring), Arrays.toString(nextTrialSolution.rotors));
					if(nextTrialSolution.score > 30D) {
						task.bestNext.add(nextTrialSolution);
						//app.out().println("%f, Ind:%s, Ring:%s, %s", nextTrialSolution.score, toString(nextTrialSolution.indicator), toString(nextTrialSolution.ring), Arrays.toString(nextTrialSolution.rotors));
						nextTrialSolution.makeCopy();
					}
				}
			}
			
			Collections.sort(task.bestNext, this.comparator);
			
			for(int i = 0; i < task.bestNext.size(); i++) {
				EnigmaSection trialSolution = task.bestNext.get(i);

				for(int s2 = 0; s2 < 26; s2++) {
					int[] indicator = Arrays.copyOf(trialSolution.indicator, trialSolution.indicator.length);
					int[] ring = new int[] {0, s2, trialSolution.ring[2]};
	
					indicator[1] = (indicator[1] + s2) % 26;
					
					task.plainText = Enigma.decode(task.cipherText, task.plainText, Arrays.copyOf(indicator, indicator.length), ring, trialSolution.rotors);
					EnigmaSection nextTrialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(task.plainText) * 1000, indicator, trialSolution.rotors);
					nextTrialSolution.ring = ring;
					
					if(nextTrialSolution.score > 30D) {
						task.bestNext2.add(nextTrialSolution);
						nextTrialSolution.makeCopy();
	
					}
				}
			}
			
			Collections.sort(task.bestNext2, this.comparator);
			
			for(int i = 0; i < task.bestNext2.size(); i++) {
				EnigmaSection trial = task.bestNext2.get(i);
				int plugboardIndex = 0;
				char[][] plugboard = new char[13][2];
				while(true) {
					int[] indicator = Arrays.copyOf(trial.indicator, trial.indicator.length);
					task.plainText = Enigma.decode(task.cipherText, task.plainText, indicator, trial.ring, trial.rotors);
					Solution bestSolution = new Solution(task.plainText, app.getLanguage().getTrigramData()).bakeSolution();
					for(char c1 = 'A'; c1 <= 'Z'; c1++) {
						for(char c2 = 'A'; c2 <= 'Z'; c2++) {
							if(c1 == c2) continue;
							indicator = Arrays.copyOf(trial.indicator, trial.indicator.length);
							plugboard[plugboardIndex][0] = c1;
							plugboard[plugboardIndex][1] = c2;

							for(int j = 0; j < task.plainText.length; j++) {
								if(task.plainText[j] == c1)
									task.plainText[j] = (byte)c2;
								else if(task.plainText[j] == c2)
									task.plainText[j] = (byte)c1;
							}
							Solution lastSolution = new Solution(task.plainText, app.getLanguage().getTrigramData());
	
							if(lastSolution.score > bestSolution.score) {
								bestSolution = lastSolution;
								bestSolution.setKeyString("%c %c", c1, c2);
								bestSolution.bakeSolution();
								app.out().println(" %s", bestSolution);
							}
							
							for(int j = 0; j < task.plainText.length; j++) {
								if(task.plainText[j] == c1)
									task.plainText[j] = (byte)c2;
								else if(task.plainText[j] == c2)
									task.plainText[j] = (byte)c1;
							}
						}	
					}
					
					app.out().println("First round");
					break;
				}
				if(i == 3)
				break;
				//	app.out().println("%f, Ind:%s, Ring:%s, %s, %s", trialSolution.score, toString(trialSolution.indicator), toString(trialSolution.ring), Arrays.toString(trialSolution.rotors), new String(task.plainText));
				
			
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
		private List<EnigmaSection> bestNext = new ArrayList<EnigmaSection>();
		private List<EnigmaSection> bestNext2 = new ArrayList<EnigmaSection>();
		
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
				EnigmaSection trialSolution = new EnigmaSection(StatCalculator.calculateMonoIC(this.plainText) * 1000, data, rotor);
				
				if(trialSolution.score > 40D) {
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
		public int[] ring;
		public int[] rotors;
		public EnigmaSection(double score, int[] notchKey, int[] rotors) {
			this.score = score;
			this.indicator = notchKey;
			this.rotors = rotors;
		}
		
		public void makeCopy() {
			this.indicator = Arrays.copyOf(this.indicator, this.indicator.length);
			this.rotors = Arrays.copyOf(this.rotors, this.rotors.length);
			if(this.ring != null)
				this.ring = Arrays.copyOf(this.ring, this.ring.length);
		}
	}
}
