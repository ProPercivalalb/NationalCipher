package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.Beaufort;
import javalibrary.cipher.Caesar;
import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.string.StringTransformer;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.stats.StatCalculator;

public class BeaufortDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Beaufort";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.CALCULATED);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		char[] textChar = text.toCharArray();
		
		if(method == DecryptionMethod.CALCULATED) {
			char[] invervedText = new char[textChar.length];
			//Runs through all the characters from the array
			for(int i = 0; i < textChar.length; i++) {

				invervedText[i] = (char)('Z' - textChar[i] + 'A');
			}
			
			int keyLength = StatCalculator.calculateBestKappaIC(text, 2, 50, settings.getLanguage());
			
			progress.addMaxValue(keyLength * 26);
			
			String keyword = "";
	        for(int i = 0; i < keyLength; ++i) {
	        	String temp = StringTransformer.getEveryNthChar(new String(invervedText), i, keyLength);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), settings.getLanguage(), progress);
	            keyword += (char)('Z' - shift);
	        }
	        output.println("Keyword: " + keyword);
			
	        String plainText = new String(Beaufort.decode(textChar, keyword));
	        output.println("Plaintext: " + plainText);
			UINew.BEST_SOULTION = plainText;
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
}
