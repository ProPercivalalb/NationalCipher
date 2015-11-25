package nationalcipher.cipher;

import java.util.Arrays;

import javalibrary.string.StringTransformer;

/**
 * @author Alex Barter (10AS)
 */
public class Columnar {

	public static String encode(String plainText, String key) {
		int[] order = new int[key.length()];
		
		char[] charArray = key.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[key.indexOf(charArray[i])] = i;
		
		return encode(plainText, order);
	}
	
	public static String encode(String plainText, int[] order) {
		int[] reversedOrder = new int[order.length];
		for(int i = 0; i < order.length; i++)
			reversedOrder[order[i]] = i;
		
		String cipherText = "";
		for(int column = 0; column < order.length; column++) 
			cipherText += StringTransformer.getEveryNthChar(plainText, reversedOrder[column], order.length);
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String key) {
		int[] order = new int[key.length()];
		
		char[] charArray = key.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[key.indexOf(charArray[i])] = i;
		
		return decode(cipherText, order);
	}
	
	public static char[] decode(char[] cipherText, int[] order) {
		int index = 0;
		int columns = order.length;
		
		int rows = (int)Math.ceil(cipherText.length / (double)columns);
		
		char[] grid = new char[cipherText.length];

		for(int column = 0; column < columns; column++) {
			for(int row = 0; row < rows; row++) {
				
				int trueColumn = order[column];
				if(row * columns + trueColumn >= cipherText.length)
					continue;
					
				if(index >= cipherText.length)
					break;

				char character = cipherText[index];

				grid[row * columns + trueColumn] = character;
					
				index += 1;
			}
		}

		return grid;
	}
}
