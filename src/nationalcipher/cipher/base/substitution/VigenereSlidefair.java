package nationalcipher.cipher.base.substitution;

import java.util.Arrays;

import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class VigenereSlidefair implements IRandEncrypter {
	
	public static String encode(String plainText, String key) {
		String cipherText = "";
		
		String[] keyAlpha = new String[key.length()];
		Arrays.fill(keyAlpha, "");
		
		for(int i = 0; i < key.length(); i++)
			for(int k = 0; k < 26; k++) 
				keyAlpha[i] += (char)((key.charAt(i) - 'A' + k) % 26 + 'A');
		
		
		for(int i = 0; i < plainText.length() / 2; i++) {
			char a = plainText.charAt(i * 2);
			char b = plainText.charAt(i * 2 + 1);
		
			String alpha = keyAlpha[i % key.length()];
			
			int index = alpha.indexOf(b);
			if(a - 'A' == index) {
				cipherText += (char)((index + 1) % 26 + 'A');
				cipherText += alpha.charAt((index + 1) % 26);
			}
			else {
				cipherText += (char)(index + 'A');
				cipherText += alpha.charAt(a - 'A');
			}
		}
		
		return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, String key) {
		byte[] plainText = new byte[cipherText.length];
		
		String[] keyAlpha = new String[key.length()];
		Arrays.fill(keyAlpha, "");
		
		for(int i = 0; i < key.length(); i++)
			for(int k = 0; k < 26; k++) 
				keyAlpha[i] += (char)((key.charAt(i) - 'A' + k) % 26 + 'A');
		
		for(int i = 0; i < cipherText.length / 2; i++) {
			char a = cipherText[i * 2];
			char b = cipherText[i * 2 + 1];
			
			String alpha = keyAlpha[i % key.length()];

			int index = alpha.indexOf(b);
			if(a - 'A' == index) {
				plainText[i * 2] = (byte)((index + 25) % 26 + 'A');
				plainText[i * 2 + 1] = (byte)alpha.charAt((index + 25) % 26);
			}
			else {
				plainText[i * 2] = (byte)(index + 'A');
				plainText[i * 2 + 1] = (byte)alpha.charAt(a - 'A');
			}
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15));
	}
}
