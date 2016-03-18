package nationalcipher.cipher;

import javalibrary.math.MathHelper;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

/**
 * @author Alex Barter (10AS)
 */
public class Porta implements IRandEncrypter {

	public static String encode(String plainText, String keyword, boolean move) {
		return new String(decode(plainText.toCharArray(), keyword, move));
	}
	
	/**
	 * @param shiftRight 'true' for rotates right, 'false' for rotates left
	 * 	   true           false
	 * NOPQRSTUVWXYZ  NOPQRSTUVWXYZ
	 * ZNOPQRSTUVWXY  OPQRSTUVWXYZN
	 * YZNOPQRSTUVWX  PQRSTUVWXYZNO
	 */
	public static char[] decode(char[] cipherText, String keyword, boolean shiftRight) {
		char[] plainText = new char[cipherText.length];
		
		for(int pos = 0; pos < cipherText.length; pos++){
			int rowNo = (int)Math.floor((keyword.charAt(pos % keyword.length()) - 'A') / 2);
			String row = "";
			
			for(int j = 0; j < 13; j++)
				row += (char)(MathHelper.mod(j + (shiftRight ? -1 : 1) * rowNo, 13) + 'N');
			
			int inGrid = row.indexOf(cipherText[pos]);
			if(inGrid >= 0)
				plainText[pos] = (char)(inGrid + 'A');
			else
				plainText[pos] = row.charAt(cipherText[pos] - 'A');
		}
	 
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15), RandomUtil.pickBoolean());
	}
}
