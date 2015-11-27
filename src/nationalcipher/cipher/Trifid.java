package nationalcipher.cipher;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Trifid implements IRandEncrypter {
	
	public static String encode(String plainText, String keysquares, int period) {
		System.out.println("trifid: " + period);
		int[] numberText = new int[plainText.length() * 3];
		for(int i = 0; i < plainText.length(); i++) {
			
			char a = plainText.charAt(i);
			
			int index = keysquares.indexOf(a);
			int tableNo = index / 9 + 1;
			int rowNo = (int)(index / 3) % 3 + 1;
			int colNo = index % 3 + 1;
			int blockBase = (int)(i / period) * (period * 3) + i % period;
			int min = Math.min(period, plainText.length() - (int)(i / period) * period);
			
			numberText[blockBase] = tableNo;
			numberText[blockBase + min] = rowNo;
			numberText[blockBase + min * 2] = colNo;
		}

		char[] cipherText = new char[plainText.length()];
		int index = 0;
		
		for(int i = 0; i < numberText.length; i += 3) {

			int a = numberText[i] - 1;
			int b = numberText[i + 1] - 1;
			int c = numberText[i + 2] - 1;
			cipherText[index++] = keysquares.charAt(a * 9 + b * 3 + c);
		}

		
		return new String(cipherText);
	}
	
	public static char[] decode(char[] cipherText, String keysquares, int period) {
		char[] plainText = new char[cipherText.length];
		
		String numberText = "";
		for(int i = 0; i < cipherText.length; i++) {
			char a = cipherText[i];
			
			int index = keysquares.indexOf(a);
			int tableNo = index / 9 + 1;
			int rowNo = (int)(index / 3) % 3 + 1;
			int colNo = index % 3 + 1;
			numberText += tableNo + "" + rowNo + "" + colNo;
		}
		
		int index = 0;
		
		for(int i = 0; i < numberText.length(); i += period * 3) {
			int min = Math.min(period, (numberText.length() - i) / 3);

			for(int j = 0; j < min; j++) {
				int a = numberText.charAt(i + j) - '0' - 1;
				int b = numberText.charAt(i + j + min) - '0' - 1;
				int c = numberText.charAt(i + j + min * 2) - '0' - 1;
				plainText[index++] = keysquares.charAt(a * 9 + b * 3 + c);
			}
		}

		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createFullKeyWithHash(), RandomUtil.pickRandomInt(3, 15));
	}
}
