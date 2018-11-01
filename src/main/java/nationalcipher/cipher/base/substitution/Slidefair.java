package nationalcipher.cipher.base.substitution;

import java.util.Arrays;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Slidefair implements IRandEncrypter {

	public static String encode(String plainText, String key, VigenereType type) {
		String cipherText = "";
		
		String[] keyAlpha = new String[key.length()];
		Arrays.fill(keyAlpha, "");
		
		for(int i = 0; i < key.length(); i++)
			for(int k = 0; k < 26; k++) 
				keyAlpha[i] += (char)type.encode((byte)(k + 'A'), (byte)key.charAt(i));
		
		
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
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String key, VigenereType type) {
		byte[][] keyAlpha = new byte[key.length()][26];
		byte[][] keyAlphaIndex = new byte[key.length()][26];
		
		for(int i = 0; i < key.length(); i++)
			for(int k = 0; k < 26; k++) {
				byte a = type.encode((byte)(k + 'A'), (byte)key.charAt(i));
				keyAlpha[i][k] = a;
				keyAlphaIndex[i][a - 'A'] = (byte)k;
			}
		
		for(int i = 0; i < cipherText.length / 2; i++) {
			char a = cipherText[i * 2];
			char b = cipherText[i * 2 + 1];
			
			byte[] alpha = keyAlpha[i % key.length()];

			byte index = keyAlphaIndex[i % key.length()][b - 'A'];
			if(a - 'A' == index) {
				plainText[i * 2] = (byte)((index + 25) % 26 + 'A');
				plainText[i * 2 + 1] = alpha[(index + 25) % 26];
			}
			else {
				plainText[i * 2] = (byte)(index + 'A');
				plainText[i * 2 + 1] = alpha[a - 'A'];
			}
		}
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15), RandomUtil.pickRandomElement(VigenereType.SLIDEFAIR_LIST));
	}
}