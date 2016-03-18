package nationalcipher.cipher;

import javalibrary.math.MathHelper;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class PortaAutokey implements IRandEncrypter {
	
	public static String encode(String plainText, String key, boolean shiftRight) {
		String autoKey = key;
		char[] cipherText = new char[plainText.length()];
		
		for(int pos = 0; pos < plainText.length(); pos++){
			int rowNo = (autoKey.charAt(pos) - 'A') / 2;
			String row = "";
			
			for(int j = 0; j < 13; j++)
				row += (char)(MathHelper.mod(j + (shiftRight ? -1 : 1) * rowNo, 13) + 'N');
			
			int inGrid = row.indexOf(plainText.charAt(pos));
			if(inGrid >= 0)
				cipherText[pos] = (char)(inGrid + 'A');
			else
				cipherText[pos] = row.charAt(plainText.charAt(pos) - 'A');
			
			autoKey += plainText.charAt(pos);
		}
		
		return new String(cipherText);
	}
	
	public static char[] decode(char[] cipherText, String key, boolean shiftRight) {
		String autoKey = key;
		char[] plainText = new char[cipherText.length];
		
		for(int pos = 0; pos < cipherText.length; pos++){
			int rowNo = (autoKey.charAt(pos) - 'A') / 2;
			String row = "";
			
			for(int j = 0; j < 13; j++)
				row += (char)(MathHelper.mod(j + (shiftRight ? -1 : 1) * rowNo, 13) + 'N');
			
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
