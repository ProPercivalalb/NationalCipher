package nationalcipher.cipher;

import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Keyword implements IRandEncrypter {

	public static String encode(String plainText, String keyword) {
		char[] charArray = plainText.toCharArray();
		
		String cipherText = "";
		
		for(char ch : charArray)
			if(ch >= 'A' && ch <= 'Z')
				cipherText += keyword.charAt(ch - 'A');
		
		return cipherText;
	}

	public static char[] decode(char[] cipherText, String keyword) {
		
		char[] plainText = new char[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = (char)(keyword.indexOf(cipherText[i]) + 'A');

		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey26());
	}
}
