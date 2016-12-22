package nationalcipher.cipher.base.transposition;

import java.util.Arrays;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class AMSCO implements IRandEncrypter {
	
	public static String encode(String plainText, boolean first, String key) {
		int[] order = new int[key.length()];
		char[] charArray = key.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[key.indexOf(charArray[i])] = i;
		
		return encode(plainText, first, order);
	}
	
	public static String encode(String plainText, boolean first, int[] order) {
		
		String[] columns = new String[order.length];
		Arrays.fill(columns, "");
		int index = 0;
		int row = 0;
		while(index < plainText.length()) {
			for(int i = 0; i < order.length; i++) {
				if(index >= plainText.length()) break;
				if((i + row) % 2 == (first ? 0 : 1)) {
					columns[order[i]] += plainText.substring(index, Math.min(index + 2, plainText.length()));
					index += 2;
				}
				else {
					columns[order[i]] += plainText.substring(index, Math.min(index + 1, plainText.length()));
					index += 1;
				}
			}
			row++;
		}
		String read = "";
		for(int i = 0; i < order.length; i++)
			read += columns[i];
		
		return read;
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, int[] order, boolean first) {
		int period = order.length;
		
		
		int[] reversedOrder = new int[order.length];
		for(int i = 0; i < order.length; i++)
			reversedOrder[order[i]] = i;

		int noChar1st = (int)((period + 1) / 2) * 2 + (int)(period / 2);
		int noChar2nd = (int)((period + 1) / 2) + (int)(period / 2) * 2;
		
		int rows = 0;
		int charactersLastRow = 0;
		
		boolean choose = first;
		int chars = 0;
		
		do {
			charactersLastRow = chars;
			chars += choose ? noChar1st : noChar2nd;
			rows += 1;
			choose = !choose;
		}
		while(chars < cipherText.length);
		
		charactersLastRow = plainText.length - charactersLastRow;
		
		int noCharColumn1st = (int)((rows - 1) / 2) + (int)(rows / 2) * 2;
		int noCharColumn2nd = (int)((rows - 1) / 2) * 2 + (int)(rows / 2);

		int index = 0;
		
		char[][] grid = new char[period][];
		
		for(int column = 0; column < period; column++) {
			int realColumn = reversedOrder[column];
			boolean isDoubleLetter = ((realColumn + rows) % 2 == (first ? 0 : 1)); //Double letter first

			int length = isDoubleLetter ? noCharColumn1st : noCharColumn2nd;
			int noCharN1st = (int)((realColumn + 1) / 2) * 2 + (int)(realColumn / 2);
			int noCharN2nd = (int)((realColumn + 1) / 2) + (int)(realColumn / 2) * 2;

			int left = charactersLastRow - (isDoubleLetter ? noCharN1st : noCharN2nd);
			if(left > 0)
				length += isDoubleLetter ? 1 : Math.min(2, left);

			grid[realColumn] = ArrayUtil.copyRange(cipherText, index, index + length);
			index += length;
		}
		
	
		int[] indexTracker = new int[period];
		
		int textIndex = 0;
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < period; column++) {
				int number = (column + row) % 2 == (first ? 0 : 1) ? 2 : 1;
				
				for(int i = 0; i < number; i++) {
					if(indexTracker[column] + i >= grid[column].length) break;
					plainText[textIndex] = (byte)grid[column][indexTracker[column] + i];
					textIndex++;
				}
				
				indexTracker[column] += number;
			}
		}
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, RandomUtil.pickBoolean(), KeyGeneration.createOrder(RandomUtil.pickRandomInt(3, 7)));
	}
}
