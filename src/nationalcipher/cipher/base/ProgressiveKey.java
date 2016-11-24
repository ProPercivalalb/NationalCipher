package nationalcipher.cipher.base;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.tools.KeyGeneration;

public class ProgressiveKey implements IRandEncrypter {

	public static String encode(String plainText, String key, int period, int progressiveKey, VigenereType type) {
		byte[] cipherText = new byte[plainText.length()];
		int progression = 0;
		int count = 0;
		for(int index = 0; index < plainText.length(); index++) {
			byte charIdVig = type.encode((byte)plainText.charAt(index), (byte)key.charAt(index % key.length()));
			cipherText[index] = type.encode(charIdVig, (byte)(progression + 'A'));
			
			if(count + 1 == period) {
				count = 0;
				progression = (progression + progressiveKey) % 26;
			}
			else
				count++;
			
		}
		
		return new String(cipherText);
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String key, int period, int progressiveKey, VigenereType type) {
		int progression = 0;
		int count = 0;
		for(int index = 0; index < cipherText.length; index++) {
			byte charIdProg = type.decode((byte)cipherText[index], (byte)(progression + 'A'));
			plainText[index] = type.decode((byte)charIdProg, (byte)key.charAt(index % key.length()));
			
			if(count + 1 == period) {
				count = 0;
				progression = (progression + progressiveKey) % 26;
			}
			else
				count++;
			
		}
		
		return plainText;
	}
	
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(6), 6, RandomUtil.pickRandomInt(1, 25), RandomUtil.pickRandomElement(VigenereType.NORMAL_LIST));
	}
}
