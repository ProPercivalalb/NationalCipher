package nationalciphernew.cipher;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javalibrary.Output;
import javalibrary.cipher.Affine;
import javalibrary.cipher.Caesar;
import javalibrary.cipher.Vigenere;
import javalibrary.fitness.ChiSquared;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.math.MathHelper;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.Creator.AffineKey;
import nationalciphernew.cipher.stats.StatCalculator;

public class VigenereDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Vigenere";
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
	            keyword += (char)('A' + shift);
	        }
	        output.println("Keyword: " + keyword);
			
	        String plainText = new String(Vigenere.decode(textChar, keyword));
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
	            
	        progressBar.addValue(1);
	    }
	    return best;
	}
}
