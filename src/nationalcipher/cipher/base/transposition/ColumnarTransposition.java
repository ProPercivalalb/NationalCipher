package nationalcipher.cipher.base.transposition;

import java.util.Arrays;

import javalibrary.string.StringTransformer;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

/**
 * @author Alex Barter (10AS)
 */
public class ColumnarTransposition implements IRandEncrypter {
	
	public static String encode(String plainText, int[] order, boolean defaultRead) {
		System.out.print(Arrays.toString(order));
		if(defaultRead) {
			String cipherText = "";
			for(int column = 0; column < order.length; column++) 
				cipherText += StringTransformer.getEveryNthChar(plainText, order[column], order.length);
			return cipherText;
		}
		else {
			char[] cipherText = new char[plainText.length()];
			int rows = (int)Math.ceil(plainText.length() / (double)order.length);
			
			int index = 0;
			for(int row = 0; row < rows; row++) {
				for(int column = 0; column < order.length; column++) {
					int fakeColumn = order[column];
	
				
					if(row * order.length + fakeColumn < plainText.length())
						cipherText[row * order.length + fakeColumn] = plainText.charAt(index++);
				}
			}
			return new String(cipherText);
		}
	}
	
	/**
	 * 
	 * @param cipherText
	 * @param order
	 * @param defaultRead True means it will read the cipherText down columns, False means the cipherText was read across rows
	 * @return
	 */
	public static byte[] decode(char[] cipherText, byte[] plainText, int[] order, boolean defaultRead) {
		int[] orderIndex = ArrayUtil.toIndexedArray(order);
		int period = order.length;
		int rows = (int)Math.ceil(cipherText.length / (double)period);
		
		
		int index = 0;
		if(defaultRead)
			for(int col = 0; col < period; col++) {
				int trueColumn = orderIndex[col];
				for(int row = 0; row < rows; row++) {
					if(row * period + trueColumn >= cipherText.length)
						continue;
						
					if(index >= cipherText.length)
						break;
	
					plainText[row * period + trueColumn] = (byte)cipherText[index++];
				}
			}
		else
			for(int row = 0; row < rows; row++) {
				for(int col = 0; col < period; col++) { //Swapped is all that needs to happen
					int trueColumn = orderIndex[col];
					if(row * period + trueColumn >= cipherText.length)
						continue;
						
					if(index >= cipherText.length)
						break;

					plainText[row * period + trueColumn] = (byte)cipherText[index++];
				}
			}

		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		//return encode(plainText, KeyGeneration.createOrder(1, 8), RandomUtil.pickBoolean());
		int[] order = KeyGeneration.createOrder(15);
		System.out.println(Arrays.toString(order));
		return encode(plainText, order, true);
	}
}
