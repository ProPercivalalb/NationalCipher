package nationalcipher.cipher.base.transposition;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class NihilistTransposition implements IRandEncrypter {

	public static String encode(String plainText, int[] order, boolean readDefault) {
		int blockSize = order.length * order.length;
		
		while(plainText.length() % (blockSize) != 0)
			plainText += 'X';
		
		if(plainText.length() != blockSize) {
			String combinedMulti = "";
			for(int i = 0; i < plainText.length() / (blockSize); i++)
				combinedMulti += encode(plainText.substring(i * blockSize, (i + 1) * blockSize), order, readDefault);
			return combinedMulti;
		}
		else {
			int index = 0;
			int columns = order.length;
			char[] cipherText = new char[blockSize];
			if(readDefault)
				for(int row = 0; row < order.length; row++)
					for(int column = 0; column < order.length; column++) 
						cipherText[index++] = plainText.charAt(order[row] * columns + order[column]);
			else
				for(int column = 0; column < order.length; column++) 
					for(int row = 0; row < order.length; row++)
						cipherText[index++] = plainText.charAt(order[row] * columns + order[column]);
			return new String(cipherText);
		}

	}
	
	public static String decode(String cipherText, int[] order, boolean readDefault) {
		return new String(decode(cipherText.toCharArray(), new byte[cipherText.length()], order, readDefault));
	}
	
	//readDefault true : read across rows, false : is read down columns
	public static byte[] decode(char[] cipherText, byte[] plainText, int[] order, boolean readDefault) {
		
		int index = 0;
		int columns = order.length;
		
		int[] reversedOrder = new int[columns];
		for(int i = 0; i < columns; i++)
			reversedOrder[order[i]] = i;

		
		if(readDefault)
			for(int row = 0; row < order.length; row++)
				for(int column = 0; column < order.length; column++) 
					plainText[index++] = (byte)cipherText[reversedOrder[row] * columns + reversedOrder[column]];
		else
			for(int column = 0; column < order.length; column++) 
				for(int row = 0; row < order.length; row++)
					plainText[index++] = (byte)cipherText[reversedOrder[row] * columns + reversedOrder[column]];


		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createOrder(2, 7), RandomUtil.pickBoolean());
	}
}
