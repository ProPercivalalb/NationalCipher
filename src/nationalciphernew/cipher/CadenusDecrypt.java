package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.Cadenus;
import javalibrary.dict.Dictionary;
import javalibrary.math.MathHelper;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.Creator.CadenusKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.InternalDecryption;
import nationalciphernew.cipher.manage.Solution;

public class CadenusDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Cadenus";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.DICTIONARY);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		CadenusTask task = new CadenusTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			List<Integer> factors = MathHelper.getFactors(text.length() / 25);			
			output.println("" + factors);
			progress.addMaxValue(1000);
			
			
			if(!factors.isEmpty()) {
				
				for(int factor : factors) {
					Creator.iterateCadenus(task, factor, 25 * factor);
				}
	
				
				output.println(task.getBestSolution());
			}
			else
				output.println("NOT SQUARE FACTORS");
			
			
			
			/**
			int minLength = 4;
			int maxLength = 4;
			
			BigInteger TWENTY_SIX = BigInteger.valueOf(25);
			
			for(int length = minLength; length <= maxLength; ++length) {
				BigInteger total = TWENTY_SIX;
				for(int i = 1; i < length; i++)
					total = total.multiply(BigInteger.valueOf(25 - i));
				
				progress.addMaxValue(total);
			}
			
			
			for(int length = minLength; length <= maxLength; ++length)
				//Creator.iterateCadenus(task, length);
			
			task.sortSolutions();
			UINew.topSolutions.updateDialog(task.solutions);
			 **/
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			progress.addMaxValue(Dictionary.words.size());
			List<Integer> factors = MathHelper.getFactors(text.length() / 25);			
			output.println("" + factors);
			progress.addMaxValue(1000);
			
			
			if(!factors.isEmpty()) {
				
				for(int factor : factors) {
					output.println("Factor: %d", factor);
					for(String word : Dictionary.words) {
					
						if(word.length() == factor)
							task.onIteration(word, 25 * factor);
						else if(word.length() > factor) {
							
							String change = "";
							for(char i : word.toCharArray()) {
								if(i != 'J' && !change.contains("" + i))
									change += i;
							}
							
							if(change.length() == factor)
								task.onIteration(change, 25 * factor);
						}
					}
				}
	
				
				output.println(task.getBestSolution());
			}
			else
				output.println("NOT SQUARE FACTORS");
		
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}

	public static class CadenusTask extends InternalDecryption implements CadenusKey {

		public CadenusTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(String key, int textLength) {
			String str = new String(this.text);
			String plainText = "";
			for(int i = 0; i < this.text.length / textLength; i++) {
				plainText += new String(Cadenus.decode(str.substring(i * textLength, (i + 1) * textLength).toCharArray(), key));
			}
			
			
			this.lastSolution = new Solution(plainText.toCharArray(), this.settings.getLanguage()).setKeyString(key);
			this.solutions.add(this.lastSolution);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, key, new String(this.bestSolution.text));	
				this.keyPanel.fitness.setText("" + this.bestSolution.score);
				this.keyPanel.key.setText(key);
				UINew.BEST_SOULTION = new String(this.bestSolution.text);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			progress.increase();
		}
	}
}
