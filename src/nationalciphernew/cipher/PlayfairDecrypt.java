package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javalibrary.Output;
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

public class PlayfairDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Playfair";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.CALCULATED, DecryptionMethod.DICTIONARY);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, ILanguage language, Output output, KeyPanel keyPanel, ProgressValue progress) {
		PlayfairTask task = new PlayfairTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			Creator.iteratePlayfair(task);
			
			output.println(task.bestText);
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.simulatedAnnealing.get(0) / settings.simulatedAnnealing.get(1)) * settings.simulatedAnnealing.get(2).intValue());
			
			task.run(text, settings);
			
			/**
			char[] textChar = text.toCharArray();
			
			double bestFitnessFinal = Double.NEGATIVE_INFINITY;
			
			int iteration = 0;
			String maxValue = progress.maxValue.toString();
			
			while(true) {
				String bestKey = KeySquareManipulation.generateRandKeySquare();
				String bestText = Playfair.decode(textChar, bestKey);
				double maxscore = TextFitness.scoreFitnessQuadgrams(bestText, language);
				
				String bestEverKey = bestKey;
				String bestEverText = bestText;
				double bestscore = maxscore;
				
				for(double TEMP = settings.simulatedAnnealing.get(0); TEMP >= 0; TEMP -= settings.simulatedAnnealing.get(1)) {
					for(int count = 0; count < settings.simulatedAnnealing.get(2); count++){ 
						iteration += 1;
						String lastKey = KeySquareManipulation.modifyKey(bestKey);
				
						String lastText = Playfair.decode(textChar, lastKey);
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
						}
					    
					    keyPanel.iterations.setText("" + iteration++ + "/" + maxValue);
					    progress.increase();
					}
				}
				
				if(bestscore > bestFitnessFinal) {
					bestFitnessFinal = bestscore;
					output.println("BEST EVERRRRR!!!  Best Fitness: %f, Key: %s, Plaintext: %s", bestscore, bestEverKey, bestEverText);
					UINew.BEST_SOULTION = bestEverText;
				}
			    progress.setValue(0);
			}**/
		}
		else if(method == DecryptionMethod.CALCULATED) {
			
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			progress.addMaxValue(Dictionary.words.size());
			for(String word : Dictionary.words) {
				String change = "";
				for(char i : word.toCharArray()) {
					if(i != 'J' && !change.contains("" + i))
						change += i;
				}
				String regex = new String[]{"ABCDEFGHIKLMNOPQRSTUVWXYZ", "NOPQRSTUVWXYZABCDEFGHIKLM", "ZYXWVUTSRQPONMLKIHGFEDCBA"}[settings.keywordCreation];
				
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
	

	public static class PlayfairTask extends SimulatedAnnealing implements PlayfairKey {

		public char[] text;
		public Settings settings;
		public KeyPanel keyPanel;	
		public Output output;
		public ProgressValue progress;
		
		public PlayfairTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			this.text = text;
			this.settings = settings;
			this.keyPanel = keyPanel;
			this.output = output;
			this.progress = progress;
		}
		
		public char[] bestText = "", lastText = "";
		public String bestKey = "", lastKey = "";
		public double bestScore = Double.NEGATIVE_INFINITY, currentScore = 0;
			
		@Override
		public void onIteration(String keysquare) {
			this.lastText = Playfair.decode(text, keysquare);
			
			this.currentScore = TextFitness.scoreFitnessQuadgrams(this.lastText, this.settings.language);
			
			if(this.currentScore >= this.bestScore) {
				this.output.println("Fitness: %f, KeySquare: %s, Plaintext: %s", this.currentScore, keysquare, new String(bestText));	
				this.bestScore = this.currentScore;
				this.bestText = this.lastText;
				UINew.BEST_SOULTION = new String(this.bestText);
			}
			
			progress.increase();
		}

		@Override
		public Solution generateKey() {
			this.bestKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(Playfair.decode(this.text, this.bestKey), this.settings.language);
		}

		@Override
		public Solution modifyKey() {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestKey);
			return new Solution(Playfair.decode(this.text, this.lastKey), this.settings.language);
		}

		@Override
		public void storeKey(Solution solution, boolean searchingMaximas) {
			this.bestKey = this.lastKey;
			if(!searchingMaximas) {
				//keyPanel.fitness.setText("" + bestscore);
			    keyPanel.key.setText(this.lastKey);
			}
				
		}

		@Override
		public void solutionFound() {
			output.println("BEST EVERRRRR!!!  Best Fitness: %f, Key: %s, Plaintext: %s", bestscore, bestEverKey, bestEverText);
			//UINew.BEST_SOULTION = bestEverText;
		}
	}
}
