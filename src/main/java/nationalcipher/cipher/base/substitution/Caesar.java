package nationalcipher.cipher.base.substitution;

import javalibrary.math.MathUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;

public class Caesar implements IRandEncrypter {

	public static String encode(String plainText, int shift) {
		char[] charArray = plainText.toCharArray();
		
		String cipherText = "";
		
		//Runs through all the characters from the array
		for(char ch : charArray) {
			
			if(!Character.isAlphabetic(ch))
				cipherText += ch;
			else {
				char newLetter = (char)(MathUtil.wrap(shift + ch - 'A', 0, 26) + 'A');
				cipherText += newLetter;
			}
		}
		
		return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, int shift) {
		byte[] plainText = new byte[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = (byte)((26 + cipherText[i] - shift - 'A') % 26 + 'A');
		
		return plainText;
	}
	
	public static byte[] decode(byte[] cipherText, int len, int shift) {
		//byte[] plainText = new byte[cipherText.length];
		
		for(int i = 0; i < len; i++)
			cipherText[i] = (byte)((26 + cipherText[i] - shift - 65) % 26 + 65);
		
		return cipherText;
	}
	
	public static int[] decode(int[] cipherText, int shift) {
		int[] plainText = new int[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = ((26 + cipherText[i] - shift - 'A') % 26 + 'A');
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, RandomUtil.pickRandomInt(1, 25));
	}
	
	@Override
	public int getDifficulty() {
		return 1;
	}
}
