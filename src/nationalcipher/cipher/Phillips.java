package nationalcipher.cipher;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Phillips implements IRandEncrypter {

	public static int[][] rows = new int[][] {{0,1,2,3,4}, {1,0,2,3,4}, {1,2,0,3,4}, {1,2,3,0,4}, {1,2,3,4,0}, {2,1,3,4,0}, {2,3,1,4,0}, {2,3,4,1,0}};
	public static int[][] rowsIndex = new int[][] {{0,1,2,3,4}, {1,0,2,3,4}, {2, 0, 1, 3, 4}, {3, 0, 1, 2, 4}, {4, 0, 1, 2, 3}, {4, 1, 0, 2, 3}, {4, 2, 0, 1, 3}, {4, 3, 0, 1, 2}};
	
	public static String encode(String plainText, String keysquare, boolean orderRows, boolean orderColumns) {
		char[] cipherText = new char[plainText.length()];
		
		for(int i = 0; i < plainText.length(); i++) {
			int squareIndex = ((int)(i / 5) % rows.length);
			
			int[] order = rows[squareIndex];
			int[] orderIndex = rowsIndex[squareIndex];
			
			char ch = plainText.charAt(i);
			if(ch == 'J') ch = 'I';
			
			int index = keysquare.indexOf(ch);
			
			int row = index / 5;
			int column = index % 5;
			
			int newRow;
			int newColumn;
			
			if(orderRows)
				newRow = order[(orderIndex[row] + 1) % 5];
			else
				newRow = (row + 1) % 5;
			
			if(orderColumns)
				newColumn = order[(orderIndex[column] + 1) % 5];
			else
				newColumn = (column + 1) % 5;
			
			cipherText[i] = keysquare.charAt(newRow * 5 + newColumn);
		}
		
		return new String(cipherText);
	}
	
	public static char[] decode(char[] cipherText, String keysquare, boolean orderRows, boolean orderColumns) {
		char[] plainText = new char[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++) {
			int squareIndex = ((int)(i / 5) % rows.length);
			
			int[] order = rows[squareIndex];
			int[] orderIndex = rowsIndex[squareIndex];
			
			int index = keysquare.indexOf(cipherText[i]);
			
			int row = index / 5;
			int column = index % 5;
			
			int newRow;
			int newColumn;
			
			if(orderRows)
				newRow = order[(orderIndex[row] + 4) % 5];
			else
				newRow = (row + 4) % 5;
			
			if(orderColumns)
				newColumn = order[(orderIndex[column] + 4) % 5];
			else
				newColumn = (column + 4) % 5;
	
			plainText[i] = keysquare.charAt(newRow * 5 + newColumn);
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		boolean first = RandomUtil.pickBoolean();
		boolean second = RandomUtil.pickBoolean();
		if(!first)
			second = true;
		return encode(plainText, KeyGeneration.createKeySquare5x5(), first, second);
	}
}