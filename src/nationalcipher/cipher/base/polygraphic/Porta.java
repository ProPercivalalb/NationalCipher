package nationalcipher.cipher.base.polygraphic;

import javalibrary.math.MathUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

/**
 * @author Alex Barter (10AS)
 */
public class Porta implements IRandEncrypter {

	public static String encode(String plainText, String keyword, boolean move) {
		return new String(decode(plainText.toCharArray(), keyword, move));
	}
	
	public static String[] key = new String[] {"NOPQRSTUVWXYZ", "ZNOPQRSTUVWXY", "YZNOPQRSTUVWX", "XYZNOPQRSTUVW", "WXYZNOPQRSTUV", "VWXYZNOPQRSTU", "UVWXYZNOPQRST", "TUVWXYZNOPQRS", "STUVWXYZNOPQR", "RSTUVWXYZNOPQ", "QRSTUVWXYZNOP", "PQRSTUVWXYZNO", "OPQRSTUVWXYZN", "NOPQRSTUVWXYZ", "OPQRSTUVWXYZN", "PQRSTUVWXYZNO", "QRSTUVWXYZNOP", "RSTUVWXYZNOPQ", "STUVWXYZNOPQR", "TUVWXYZNOPQRS", "UVWXYZNOPQRST", "VWXYZNOPQRSTU", "WXYZNOPQRSTUV", "XYZNOPQRSTUVW", "YZNOPQRSTUVWX", "ZNOPQRSTUVWXY"};
	
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
			String row = key[rowNo + (shiftRight ? 0 : 13)];
			
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