package nationalcipher.cipher.base.substitution;

import javalibrary.math.MathUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class PortaAutokey implements IRandEncrypter {
	
	public static String encode(String plainText, String key, boolean shiftRight) {
		return Porta.encode(plainText, key + plainText, shiftRight);
	}
	
	public static char[] decode(char[] cipherText, String key, boolean shiftRight) {
		String autoKey = key;
		char[] plainText = new char[cipherText.length];
		
		for(int pos = 0; pos < cipherText.length; pos++){
			int rowNo = (int)Math.floor((autoKey.charAt(pos) - 'A') / 2);
			String row = Porta.key[rowNo + (shiftRight ? 0 : 13)];
			
			int inGrid = row.indexOf(cipherText[pos]);
			if(inGrid >= 0)
				plainText[pos] = (char)(inGrid + 'A');
			else
				plainText[pos] = row.charAt(cipherText[pos] - 'A');
			
			autoKey += plainText[pos];
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15), RandomUtil.pickBoolean());
	}
}
