package nationalcipher.cipher;

import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Myszkowski implements IRandEncrypter {

	public static String encode(String plainText, String keyword) {
		String cipherText = "";
		
		int rows = (int)Math.ceil(plainText.length() / (double)keyword.length());
		
		for(char c = 'A'; c <= 'Z'; c++) {
			if(keyword.indexOf(c) == -1)
				continue;
			
			for(int row = 0; row < rows; row++)
				for(int i = 0; i < keyword.length(); i++)
					if(c == keyword.charAt(i)) 
						if(row * keyword.length() + i < plainText.length())
							cipherText += plainText.charAt(row * keyword.length() + i);
		}
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String keyword) {
		char[] plainText = new char[cipherText.length];
		
		int rows = (int)Math.ceil(cipherText.length / (double)keyword.length());
		
		int index = 0;
		for(char c = 'A'; c <= 'Z'; c++) {
			if(keyword.indexOf(c) == -1)
				continue;
			
			for(int row = 0; row < rows; row++)
				for(int i = 0; i < keyword.length(); i++) 
					if(c == keyword.charAt(i))
						if(row * keyword.length() + i < cipherText.length)
							plainText[row * keyword.length() + i] = cipherText[index++];
		}
		
		return plainText;	
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15));
	}
}
