package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.dict.Dictionary;
import javalibrary.math.MathUtil;
import javalibrary.swing.ProgressValue;
import javalibrary.util.ArrayUtil;
import nationalcipher.Settings;
import nationalcipher.cipher.Cadenus;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.CadenusKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.cipher.tools.InternalDecryption;

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
			List<Integer> factors = MathUtil.getFactors(text.length() / 25);			
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
			
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			progress.addMaxValue(Dictionary.words.size());
			List<Integer> factors = MathUtil.getFactors(text.length() / 25);			
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
			char[] plainText = new char[0];
			for(int i = 0; i < this.text.length / textLength; i++)
				plainText = ArrayUtil.concat(plainText, Cadenus.decode(ArrayUtil.copyOfRange(this.text, i * textLength, textLength), key));

			this.lastSolution = new Solution(plainText, this.settings.getLanguage()).setKeyString(key);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}
