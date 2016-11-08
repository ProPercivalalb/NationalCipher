package nationalcipher.cipher.base.other;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Digrafid implements IRandEncrypter {

	public static String encode(String plainText, String keysquare1, String keysquare2, int fractionation) {
		if(plainText.length() % 2 == 1) plainText += 'X';
		if(fractionation == 0) fractionation = plainText.length() / 2; //I believe this will work
		
		int period = fractionation * 2;
		
		int[] numberText = new int[plainText.length() * 3 / 2];
		int blocks = (int)Math.ceil(plainText.length() / (double)period);
		
		int index = 0;
		
		char[] cipherText = new char[plainText.length()];
		
		for(int b = 0; b < blocks; b++) {
			int min = Math.min(fractionation, (plainText.length() - b * period) / 2);
			
			for(int f = 0; f < min; f++) {
				int cTIndex = b * period + f * 2;
				int index1 = keysquare1.indexOf(plainText.charAt(cTIndex));
				int index2 = keysquare2.indexOf(plainText.charAt(cTIndex + 1));
				
				numberText[b * fractionation * 3 + f] = index1 % 9;
				numberText[b * fractionation * 3 + min + f] = (index1 / 9) * 3 + (index2 % 3);
				numberText[b * fractionation * 3 + min * 2 + f] = index2 / 3;
			}
			
			for(int f = 0; f < min; f++) {
				int n1 = numberText[b * fractionation * 3 + f * 3];
				int n2 = numberText[b * fractionation * 3 + f * 3 + 1];
				int n3 = numberText[b * fractionation * 3 + f * 3 + 2];
				cipherText[index++] = keysquare1.charAt(n1 + (int)(n2 / 3) * 9);
				cipherText[index++] = keysquare2.charAt(n3 * 3 + n2 % 3);
			}
		}

		return new String(cipherText);
	}
	
	public static String decode(String cipherText, String keysquare1, String keysquare2, int fractionation) {
		return new String(decode(cipherText.toCharArray(), new byte[cipherText.length()], new byte[cipherText.length() * 3 / 2], keysquare1, keysquare2, fractionation));
	}
	
	//Most memory efficient version used for automatic decrypt
	public static byte[] decode(char[] cipherText, byte[] plainText, byte[] numberText, String keysquare1, String keysquare2, int fractionation) {
		if(fractionation == 0) fractionation = cipherText.length / 2; //I believe this will work
		
		int period = fractionation * 2;
		
		int blocks = (int)Math.ceil(cipherText.length / (double)period);
		
		int indexNo = 0;
		int index = 0;
		
		for(int b = 0; b < blocks; b++) {
			int min = Math.min(fractionation, (cipherText.length - b * period) / 2);
			
			for(int f = 0; f < min; f++) {
				int cTIndex = b * period + f * 2;
				int index1 = keysquare1.indexOf(cipherText[cTIndex]);
				int index2 = keysquare2.indexOf(cipherText[cTIndex + 1]);

				numberText[indexNo++] = (byte)(index1 % 9);
				numberText[indexNo++] = (byte)((index1 / 9) * 3 + (index2 % 3));
				numberText[indexNo++] = (byte)(index2 / 3);
			}
			
			for(int f = 0; f < min; f++) {
				int n1 = numberText[b * fractionation * 3 + f];
				int n2 = numberText[b * fractionation * 3 + min + f];
				int n3 = numberText[b * fractionation * 3 + min * 2 + f];
				plainText[index++] = (byte)keysquare1.charAt(n1 + (int)(n2 / 3) * 9);
				plainText[index++] = (byte)keysquare2.charAt(n3 * 3 + n2 % 3);
			}
		}

		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey27(), KeyGeneration.createLongKey27(), RandomUtil.pickRandomElement(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
	}
}
