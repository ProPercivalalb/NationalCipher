package nationalcipher.cipher.base.substitution;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class VigenereFamily implements IRandEncrypter {

	public static String encode(String plainText, String key, VigenereType type) {
		byte[] cipherText = new byte[plainText.length()];
		int period = key.length();
		
		for(int index = 0; index < plainText.length(); index++)
			cipherText[index] = type.encode((byte)plainText.charAt(index), (byte)key.charAt(index % period));
		
		return new String(cipherText);
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String key, VigenereType type) {
		int period = key.length();
		
		for(int index = 0; index < cipherText.length; index++)
			plainText[index] = type.decode((byte)cipherText[index], (byte)key.charAt(index % period));
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15), RandomUtil.pickRandomElement(VigenereType.NORMAL_LIST));
	}
	
	@Override
	public int getDifficulty() {
		return 2;
	}
}
