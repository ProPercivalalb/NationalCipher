package nationalcipher.cipher;

import java.util.Arrays;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class ADFGX implements IRandEncrypter {

	public static String encode(String plainText, String keysquare, int[] order) {
		//TODO I-J same
		return ADFGVX.encode(plainText, keysquare, order, "ADFGX");
	}
	
	public static char[] decode(char[] cipherText, String keysquare, int[] order) {
		return ADFGVX.decode(cipherText, keysquare, order, "ADFGX");
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		int[] o =KeyGeneration.createOrder(RandomUtil.pickRandomInt(2, 7));
		//System.out.println(Arrays.toString(o));
		return encode(plainText, KeyGeneration.createLongKey25(), o);
	}
}
