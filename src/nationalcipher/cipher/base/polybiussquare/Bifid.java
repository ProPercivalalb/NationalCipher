package nationalcipher.cipher.base.polybiussquare;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

/**
 * @author Alex Barter (10AS)
 */
public class Bifid implements IRandEncrypter {

	public static String encode(String plainText, String keysquare, int period) {
		if(period == 0) period = plainText.length();
		
		int[] digits = new int[plainText.length() * 2];
		for(int i = 0; i < plainText.length(); i++) {
			char c = plainText.charAt(i);
			if(c == 'J') c = 'I';
			int charIndex = keysquare.indexOf(c);
		    int charRow = (int)Math.floor(charIndex / 5);
		    int charCol = charIndex % 5;
		    
		    int blockNo = (int)Math.floor(i / period);
		    int blockSize = Math.min(period, plainText.length() - blockNo * period);
		    int blockCol = (i - blockNo * period) % blockSize;
		    
		    digits[blockNo * period * 2 + blockCol] = charRow;
		    digits[blockNo * period * 2 + blockSize + blockCol] = charCol;
		}
		
		String cipherText = "";
		
		for(int i = 0; i < digits.length; i += 2) {
		    int row = digits[i];
		    int column = digits[i + 1];
		    cipherText += keysquare.charAt(row * 5 + column);
		}
		
	    return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String keysquare, int period) {
		return ConjugatedBifid.decode(cipherText, keysquare, keysquare, period);
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey25(), RandomUtil.pickRandomElement(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
	}
}
