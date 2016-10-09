package nationalcipher.cipher;

import javalibrary.math.MathUtil;

public class ProgressiveKey {

	protected static String encodeBase(String encodedText, int period, int progressiveKey) {
		String cipherText = "";

		for(int index = 0; index < encodedText.length(); index++) {
			int progression = (int)(index / period) * progressiveKey;
			cipherText += (char)(MathUtil.mod((encodedText.charAt(index) - 'A' + progression), 26) + 'A');
		}
		
		return cipherText;
	}
	
	public static char[] decodeBase(char[] cipherText, int period, int progressiveKey) {
		char[] decodedText = new char[cipherText.length];

		int progression = 0;
		int count = 0;
		for(int index = 0; index < cipherText.length; index++) {
			decodedText[index] = (char)((26 + cipherText[index] - 'A' - progression) % 26 + 'A');
			if(count++ == period) {
				count = 0;
				progression = (progression + progressiveKey) % 26;
			}
		}
		
		return decodedText;
	}
}
