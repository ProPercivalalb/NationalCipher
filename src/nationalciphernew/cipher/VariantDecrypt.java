package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;

import javalibrary.Output;
import javalibrary.cipher.Caesar;
import javalibrary.cipher.Variant;
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

public class VariantDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Variant";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.CALCULATED);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		char[] textChar = text.toCharArray();
		
		if(method == DecryptionMethod.CALCULATED) {
			int keyLength = StatCalculator.calculateBestKappaIC(text, 2, 50, settings.getLanguage());
			
			progress.addMaxValue(keyLength * 26);
			
			String keyword = "";
	        for(int i = 0; i < keyLength; ++i) {
	        	String temp = StringTransformer.getEveryNthChar(text, i, keyLength);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), settings.getLanguage(), progress);
	            keyword += (char)('A' - (shift - 26) % 26);
	        }
	        output.println("Keyword: " + keyword);
			
	        String plainText = new String(Variant.decode(textChar, keyword));
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
	public void createSettingsUI(JDialog dialog) {
		
	}
}
