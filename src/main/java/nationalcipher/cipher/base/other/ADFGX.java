package nationalcipher.cipher.base.other;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class ADFGX implements IRandEncrypter {

	public static String encode(String plainText, String keysquare, Integer[] order, boolean defaultRead) {
		//TODO I-J same
		return ADFGVX.encode(plainText, keysquare, order, "ADFGX", defaultRead);
	}
	
	//defaultRead is false normally
	public static char[] decode(char[] cipherText, String keysquare, Integer[] order, boolean defaultRead) {
		return ADFGVX.decode(cipherText, keysquare, order, "ADFGX", defaultRead);
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		Integer[] o = KeyGeneration.createOrder(RandomUtil.pickRandomInt(2, 7));
		//System.out.println(Arrays.toString(o));
		return encode(plainText, KeyGeneration.createLongKey25(), o, false);
	}
	
	@Override
	public int getDifficulty() {
		return 10;
	}
}
