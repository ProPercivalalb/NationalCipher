package nationalcipher.cipher.base.other;

import java.util.Map;

import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.CipherUtils;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class ConjugatedBifid implements IRandEncrypter {

	public static String encode(String plainText, String create, String recreate, int period) {
		if(period == 0)
			period = plainText.length();
		
		int[] digits = new int[plainText.length() * 2];
		for(int i = 0; i < plainText.length(); i++) {
			char c = plainText.charAt(i);
			if(c == 'J') c = 'I';
			int charIndex = create.indexOf(c);
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
		    cipherText += recreate.charAt(row * 5 + column);
		}
		
	    return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String keysquare1, String keysquare2, int period) {
		return decode(cipherText, plainText, PrimTypeUtil.toCharacterArray(keysquare1), PrimTypeUtil.toCharacterArray(keysquare2), period);
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, Character[] keysquare1, Character[] keysquare2, int period) {
		if(period == 0) period = cipherText.length;
		
		Map<Character, Integer> keyIndex2 = CipherUtils.createCharacterIndexMapping(keysquare2);
		
		int[] numberText = new int[cipherText.length * 2];
		for(int i = 0; i < cipherText.length; i++) {
			
			char a = cipherText[i];
			int index = keyIndex2.get(a);
			int row = index / 5;
			int column = index % 5;
			
			int lowestCol = (int)(i / period) * period;
			int actualPeriod = Math.min(period, cipherText.length - lowestCol);
			int blockSize = 2 * actualPeriod;
			int relCol = i - lowestCol;
			int blockBase = lowestCol * 2;
	
			int posMultipler = relCol * 4;
			
			if(relCol * 2 < actualPeriod)
				numberText[blockBase + posMultipler] = row;
			else
				numberText[blockBase + posMultipler - blockSize + 1] = row;
	
			if(relCol * 2 + 1 < actualPeriod)
				numberText[blockBase + posMultipler + 2] = column;
			else
				numberText[blockBase + posMultipler - blockSize + 3] = column;
		}
		
		int index = 0;
		
		for(int i = 0; i < numberText.length; i += 2) {
			int a = numberText[i];
			int b = numberText[i + 1];
			plainText[index++] = (byte)(char)keysquare1[a * 5 + b];
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey25(), KeyGeneration.createLongKey25(), RandomUtil.pickRandomInt(2, 15));
	}
}
