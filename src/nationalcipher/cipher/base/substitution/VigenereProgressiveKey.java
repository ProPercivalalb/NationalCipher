package nationalcipher.cipher.base.substitution;

import javalibrary.math.MathUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class VigenereProgressiveKey implements IRandEncrypter {

	public static void main(String[] args) {
		System.out.println(new String(encode("TESTINGTHISCIPHERNOW", "CAT", 4, 5)));
		System.out.println(new String(decode("VELVNLNYKUCFZEPVLAKQ".toCharArray(), "CAT", 4, 5)));
	}
	
	public static String encode(String plainText, String keyword, int period, int progressiveKey) {
		System.out.println(keyword + "Period: " + period + " prog i: " + progressiveKey);
		String cipherText = "";

		for(int index = 0; index < plainText.length(); index++) {
			int progression = (int)Math.floor(index / period) * progressiveKey;
			cipherText += (char)(MathUtil.mod(plainText.charAt(index) + progression + keyword.charAt(index % keyword.length()), 26) + 'A');
		}
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String key, int period, int progKey) {
		char[] plainText = new char[cipherText.length];

		for(int index = 0; index < cipherText.length; index++) {
			int progression = (int)Math.floor(index / period) * progKey;
			plainText[index] = (char)(MathUtil.mod(cipherText[index] - progression - key.charAt(index % key.length()), 26) + 'A');
		}
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15), RandomUtil.pickRandomInt(1, 20), RandomUtil.pickRandomInt(1, 25));
	}
}
