package nationalcipher.cipher.base.substitution;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class TriSquare implements IRandEncrypter {
	
	public static String encode(String plainText, String keysquare1, String keysquare2, String keysquare3) {
		String cipherText = "";
		for(int i = 0; i < plainText.length() / 2; i++) {
			char a = plainText.charAt(i * 2);
			char b = plainText.charAt(i * 2 + 1);
			
			int column = keysquare1.indexOf(a) % 5;
			int rowSort = keysquare1.indexOf(a) / 5;
			int row = keysquare2.indexOf(b) / 5;
			int columnSort = keysquare2.indexOf(b) % 5;
			cipherText += keysquare1.charAt(5 * RandomUtil.pickRandomInt(5) + column);
			cipherText += keysquare3.charAt(5 * rowSort + columnSort);
			cipherText += keysquare2.charAt(5 * row + RandomUtil.pickRandomInt(5));
		}
		
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String keysquare1, String keysquare2, String keysquare3) {
		char[] plainText = new char[cipherText.length / 3 * 2];
		
		for(int i = 0; i < cipherText.length / 3; i++) {
			char a = cipherText[i * 3];
			char b = cipherText[i * 3 + 1];
			char c = cipherText[i * 3 + 2];
			
			int column = keysquare1.indexOf(a) % 5;
			
			int row = keysquare2.indexOf(c) / 5;
			
			int index = keysquare3.indexOf(b);
			int columnSort = index % 5;
			int rowSort = index / 5;
			
			plainText[i * 2] = keysquare1.charAt(rowSort * 5 + column);
			plainText[i * 2 + 1] = keysquare2.charAt(row * 5 + columnSort);
		}
		
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey25(), KeyGeneration.createLongKey25(), KeyGeneration.createLongKey25());
	}
}
