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
		if(method == DecryptionMethod.BRUTE_FORCE) {
			SubstitutionTask task = new SubstitutionTask(text.toCharArray(), language, output, progress);
			
			Creator.iterateSubstitution(task);
			
			output.println(task.plainText);
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.simulatedAnnealing.get(0) / settings.simulatedAnnealing.get(1)) * settings.simulatedAnnealing.get(2).intValue());
			
			char[] textChar = text.toCharArray();
			
			double bestFitnessFinal = Double.NEGATIVE_INFINITY;
			
			int iteration = 0;
			String maxValue = progress.maxValue.toString();
			
			while(true) {
				String bestKey = KeySquareManipulation.generateRandKey();
				String bestText = Keyword.decode(textChar, bestKey);
				double maxscore = TextFitness.scoreFitnessQuadgrams(bestText, language);
				
				String bestEverKey = bestKey;
				String bestEverText = bestText;
				double bestscore = maxscore;
				
				for(double TEMP = settings.simulatedAnnealing.get(0); TEMP >= 0; TEMP -= settings.simulatedAnnealing.get(1)) {
					for(int count = 0; count < settings.simulatedAnnealing.get(2); count++){ 
							
						String lastKey = KeySquareManipulation.exchange2letters(bestKey);
				
						String lastText = Keyword.decode(textChar, lastKey);
						double score = TextFitness.scoreFitnessQuadgrams(lastText, language);
						double dF = score - maxscore;
						
					    if(dF >= 0) {
					        maxscore = score;
					        bestKey = lastKey;
					    }
					    else if(TEMP > 0) { 
					    	double prob = Math.exp(dF / TEMP);
					        if(prob > RandomUtil.pickDouble()) {
					        	maxscore = score;
						        bestKey = lastKey;
					        }
						}
					    
					    if(maxscore > bestscore) {
					        bestEverKey = lastKey;
					        bestEverText = lastText;
					        bestscore = maxscore;
					        keyPanel.fitness.setText("" + bestscore);
					        keyPanel.key.setText(lastKey);
							UINew.BEST_SOULTION = bestEverText;
						}
					    
					    keyPanel.iterations.setText("" + iteration++ + "/" + maxValue);
					    progress.increase();
					}
				}
				
				if(bestscore > bestFitnessFinal) {
					bestFitnessFinal = bestscore;
					output.println("BEST EVERRRRR!!!  Best Fitness: %f, Key: %s, Plaintext: %s", bestscore, bestEverKey, bestEverText);
				}
			    progress.setValue(0);
			}
		}
		else if(method == DecryptionMethod.CALCULATED) {
			
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			SubstitutionTask task = new SubstitutionTask(text.toCharArray(), language, output, progress);
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
	

	public static class SubstitutionTask implements SubstitutionKey {

		public char[] text;
		public ILanguage language;
		public Output output;
		public ProgressValue progress;
		
		public SubstitutionTask(char[] text, ILanguage language, Output output, ProgressValue progress) {
			this.text = text;
			this.language = language;
			this.output = output;
			this.progress = progress;
		}
		
		public String plainText = "", lastText = "";
		public double bestScore = Double.NEGATIVE_INFINITY, currentScore = 0;
			
		@Override
		public void onIteration(String keyalphabet) {
			this.currentScore = 0;
			char[] plainText = new char[text.length];
			
			for(int i = 0; i < text.length; i++) {
				plainText[i] = (char)(keyalphabet.indexOf(text[i]) + 'A');
				if(i >= 4 - 1) {
					this.currentScore += TextFitness.scoreWord(new String(plainText, i - 3, 4), language.getQuadgramData());
					if(this.currentScore <= this.bestScore) break;
				}
			}
			
			
			if(this.currentScore >= this.bestScore) {
				this.output.println("Fitness: %f, Key: %s, Plaintext: %s", this.currentScore, keyalphabet, new String(plainText));	
				this.bestScore = this.currentScore;
				this.plainText = this.lastText;
				UINew.BEST_SOULTION = this.plainText;
			}
			
			progress.increase();
		}
	}
}
