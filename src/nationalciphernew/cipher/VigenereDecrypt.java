package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.Caesar;
import javalibrary.cipher.Vigenere;
import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.string.StringTransformer;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator.VigereneKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.KeySearch;
import nationalciphernew.cipher.manage.Solution;
import nationalciphernew.cipher.stats.StatCalculator;

public class VigenereDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Vigenere";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.CALCULATED, DecryptionMethod.KEY_MANIPULATION);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		char[] textChar = text.toCharArray();
		VigenereTask task = new VigenereTask(textChar, settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.CALCULATED) {
			int keyLength = StatCalculator.calculateBestKappaIC(text, 2, 50, settings.getLanguage());
			
			progress.addMaxValue(keyLength * 26);
			
			String keyword = "";
	        for(int i = 0; i < keyLength; ++i) {
	        	String temp = StringTransformer.getEveryNthChar(text, i, keyLength);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), settings.getLanguage(), progress);
	            keyword += (char)('A' + shift);
	        }
	        output.println("Keyword: " + keyword);
			
	        String plainText = new String(Vigenere.decode(textChar, keyword));
	        output.println("Plaintext: " + plainText);
			UINew.BEST_SOULTION = plainText;
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {

			progress.setIndeterminate(true);
			task.run(2, 50);
			
			output.println(task.getBestSolution());
			
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}

	public int findBestCaesarShift(char[] text, ILanguage language, ProgressValue progressBar) {
		int best = 0;
	    double smallestSum = Double.MAX_VALUE;
	    for(int shift = 0; shift < 26; ++shift) {
	    	char[] encodedText = Caesar.decode(text, shift);
	        double currentSum = ChiSquared.calculate(encodedText, language);
	    
	        if(currentSum < smallestSum) {
	        	best = shift;
	            smallestSum = currentSum;
	        }
	            
	        progressBar.increase();
	    }
	    return best;
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public class VigenereTask extends KeySearch implements VigereneKey {

		public VigenereTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}
			
		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Vigenere.decode(this.text, key), this.settings.getLanguage()).setKeyString(key);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
			
		}
		
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(Vigenere.decode(this.text, key), this.settings.getLanguage()).setKeyString(key);
		}

		@Override
		public void solutionFound() {
			this.output.println("%s", this.bestSolution);
			this.keyPanel.updateSolution(this.bestSolution);
			this.sortSolutions();
			UINew.topSolutions.updateDialog(this.solutions);
		}

		@Override
		public void onIteration() {
			this.keyPanel.iterations.setText("" + this.iteration++);
		}
	}
}
