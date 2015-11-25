package nationalcipher.cipher;

import java.math.BigInteger;

import javalibrary.math.MathHelper;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;

public class Affine implements IRandEncrypter {

	public static String encode(String plainText, int a, int b) {
		
		String cipherText = "";
		
		String tempAlphabet = "";
		for(int i = 'A'; i <= 'Z'; ++i)
			tempAlphabet += (char)('A' + MathHelper.wrap(a * (i - 'A') + b, 0, 26));
		
		for(char ch : plainText.toCharArray()) {
				
			if(!Character.isLetter(ch))
				cipherText += ch;
			else
				cipherText += (char)(tempAlphabet.indexOf(ch) + 'A');
		}
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, int a, int b) {
		
		char[] plainText = new char[cipherText.length];
		
		int multiplicativeInverse = BigInteger.valueOf((int)a).modInverse(BigInteger.valueOf(26)).intValue();
		
		//Runs through all the characters from the array
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = (char) (MathHelper.mod(multiplicativeInverse * (cipherText[i] - 'A' - b), 26) + 'A');

		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, RandomUtil.pickRandomElement(1,3,5,7,9,11,15,17,19,21,23,25), RandomUtil.pickRandomInt(26));
	}
}
