package nationalcipher.cipher.base.substitution;

import java.util.Arrays;

import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Portax implements IRandEncrypter {

	public static String encode(String plainText, String keyword) {
		while(plainText.length() % 2 != 0)
			plainText += 'X';
		
		return new String(decode(plainText.toCharArray(), keyword));
	}

	public static char[] decode(char[] cipherText, String keyword) {
		int period = keyword.length();
		
		char[] plainText = new char[cipherText.length];
		String[] slidingKey = new String[period];
		Arrays.fill(slidingKey, "");
		
		for(int i = 0; i < keyword.length(); i++) {
			char slidingChar = keyword.charAt(i);
			int slide = (slidingChar - 'A') / 2;
			for(int s = 0; s < 13; s++)
				slidingKey[i] += (char)((13 + s - slide) % 13 + 'A');
		}

		for(int i = 0; i < cipherText.length; i += period * 2) {
			int actingPeriod = Math.min((cipherText.length - i) / 2, period);
			for(int j = 0; j < actingPeriod; j++) {
				
				char a = cipherText[i + j];
				char b = cipherText[i + j + actingPeriod];

				int row = (b - 'A') % 2;
				int column = (b - 'A') / 2;
				char c;
				char d;
				if(a <= 'M') {
					int aIndex = slidingKey[j].indexOf(a);
					if(aIndex == column) {
						c = (char)(column + 'N');
						d = (char)(aIndex * 2 + (row + 1) % 2 + 'A');
					}
					else {
						c = slidingKey[j].charAt(column);
						d = (char)(aIndex * 2 + row + 'A');
					}
				}
				else {
					int aIndex = a - 'N';
					if(aIndex == column) {
						c = slidingKey[j].charAt(column);
						d = (char)(aIndex * 2 + (row + 1) % 2 + 'A');
					}
					else {
						c = (char)(column + 'N');
						d = (char)(aIndex * 2 + row + 'A');
					}
				}
				
				plainText[i + j] = c;
				plainText[i + j + actingPeriod] = d;
			}
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15));
	}
}
