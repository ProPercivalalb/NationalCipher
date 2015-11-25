package nationalcipher.cipher;

import java.util.Arrays;

public class ColumnarRow {

	public static String encode(String plainText, String key) {
		int[] order = new int[key.length()];
		
		char[] charArray = key.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[key.indexOf(charArray[i])] = i;
		
		return encode(plainText, order);
	}
	
	public static String encode(String plainText, int[] order) {
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
	
	/**
	public static String decode(String cipherText, String key) {
		int[] order = new int[key.length()];
		
		char[] charArray = key.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[key.indexOf(charArray[i])] = i;
		
		return decode(cipherText, order);
	}
	
	public static String decode(String cipherText, int[] order) {
		String plainText = "";
		int rows = (int)Math.ceil(cipherText.length() / (double)order.length);
		int index = 0;
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < order.length; column++) {
				int trueColumn = order[column];
				if(row * order.length + trueColumn < cipherText.length())
					plainText += cipherText.charAt(row * order.length + trueColumn);
			}
		}
		return plainText;
	}**/
	
	public static String decode(String cipherText, String key) {
		int[] order = new int[key.length()];
		
		char[] charArray = key.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[key.indexOf(charArray[i])] = i;
		
		return new String(decode(cipherText.toCharArray(), order));
	}
	
	public static char[] decode(char[] cipherText, int[] order) {
		int index = 0;
		int columns = order.length;
		
		int[] reversedOrder = new int[columns];
		for(int i = 0; i < columns; i++)
			reversedOrder[order[i]] = i;
		
		int rows = (int)Math.ceil(cipherText.length / (double)columns);
		
		char[] grid = new char[cipherText.length];
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				int trueColumn = reversedOrder[column];
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
