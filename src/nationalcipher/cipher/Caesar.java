package nationalcipher.cipher;

import javalibrary.math.MathHelper;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;

public class Caesar implements IRandEncrypter {

	public static String encode(String plainText, int shift) {
		char[] charArray = plainText.toCharArray();
		
		String cipherText = "";
		
		//Runs through all the characters from the array
		for(char ch : charArray) {
			
			if(!Character.isAlphabetic(ch))
				cipherText += ch;
			else {
				char newLetter = (char)(MathHelper.wrap(shift + ch - 'A', 0, 26) + 'A');
				cipherText += newLetter;
			}
		}
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, int shift) {
		char[] plainText = new char[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = (char)((26 + cipherText[i] - shift - 'A') % 26 + 'A');
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, RandomUtil.pickRandomInt(26));
	}
}
