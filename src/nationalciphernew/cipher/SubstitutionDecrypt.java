package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javalibrary.Output;
import javalibrary.cipher.Keyword;
import javalibrary.cipher.Playfair;
import javalibrary.cipher.wip.KeySquareManipulation;
import javalibrary.dict.Dictionary;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.math.MathHelper;
import javalibrary.swing.ProgressValue;
import javalibrary.util.RandomUtil;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.Creator.PlayfairKey;
import nationalciphernew.cipher.Creator.SubstitutionKey;
import nationalciphernew.cipher.PlayfairDecrypt.PlayfairTask;

public class SubstitutionDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Substitution";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.CALCULATED, DecryptionMethod.DICTIONARY);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, ILanguage language, Output output, KeyPanel keyPanel, ProgressValue progress) {
		SubstitutionTask task = new SubstitutionTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
	
			Creator.iterateSubstitution(task);
			
			output.println(new String(task.bestSolution.text));
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run(text, settings);
		}
		else if(method == DecryptionMethod.CALCULATED) {
			
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			progress.addMaxValue(Dictionary.words.size());
			for(String word : Dictionary.words) {
				String change = "";
				for(char i : word.toCharArray()) {
					if(!change.contains("" + i))
						change += i;
				}
				String regex = new String[]{"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "NOPQRSTUVWXYZABCDEFGHIJKLM", "ZYXWVUTSRQPONMLKJIHGFEDCBA"}[settings.keywordCreation];
				
				for(char i : regex.toCharArray()) {
					if(!change.contains("" + i))
						change += i;
				}
				
				//output.println(word + " " + change);
				task.onIteration(change);
			}
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	

	public static class SubstitutionTask extends SimulatedAnnealing implements SubstitutionKey {

		public String bestKey = "", bestMaximaKey = "", lastKey = "";
		
		public SubstitutionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(String keyalpha) {
			this.lastSolution = new Solution(Playfair.decode(this.text, keyalpha), this.settings.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, keyalpha, new String(this.bestSolution.text));	
				UINew.BEST_SOULTION = new String(this.bestSolution.text);
			}
			
			progress.increase();
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKey();
			return new Solution(Keyword.decode(this.text, this.bestMaximaKey), this.settings.getLanguage());
		}

		@Override
		public Solution modifyKey() {
			this.lastKey = KeySquareManipulation.exchange2letters(this.bestMaximaKey);
			return new Solution(Keyword.decode(this.text, this.lastKey), this.settings.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestKey);
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.iterations.setText("" + this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("Best Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, this.bestKey, new String(this.bestSolution.text));
			UINew.BEST_SOULTION = new String(this.bestSolution.text);
			this.progress.setValue(0);
			return false;
		}
	}
}
