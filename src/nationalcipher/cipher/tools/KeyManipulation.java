package nationalcipher.cipher.tools;

import javalibrary.util.RandomUtil;

public class KeyManipulation {

	public static String swapTwoCharacters(String keySquare) {
		return new String(swapTwoCharacters(keySquare.toCharArray()));
	}
	
	public static char[] swapTwoCharacters(char[] keySquare) {
	    int i1 = RandomUtil.pickRandomInt(keySquare.length);
	    int i2 = RandomUtil.pickRandomInt(keySquare.length);
	    char temp = keySquare[i1];
	    keySquare[i1] = keySquare[i2];
	    keySquare[i2] = temp;
	    return keySquare;
	}
	
	public static String swapTwoRows(String keySquare, int columns, int rows) {
		return new String(swapTwoRows(keySquare.toCharArray(), columns, rows));
	}
	
	public static char[] swapTwoRows(char[] keySquare, int columns, int rows) {
		int r1 = RandomUtil.pickRandomInt(rows);
	    int r2 = RandomUtil.pickRandomInt(rows);
	    char temp;
	    for(int c = 0; c < columns; c++){
	        temp = keySquare[r1 * columns + c];
	        keySquare[r1 * columns + c] = keySquare[r2 * columns + c];
	        keySquare[r2 * columns + c] = temp;
	    }
	    return keySquare;
	}

	public static String swapTwoColumns(String keySquare, int columns, int rows) {
		return new String(swapTwoColumns(keySquare.toCharArray(), columns, rows));
	}
	
	public static char[] swapTwoColumns(char[] keySquare, int columns, int rows) {
		int c1 = RandomUtil.pickRandomInt(columns);
	    int c2 = RandomUtil.pickRandomInt(columns);
	    char temp;
	    for(int r = 0; r < rows; r++){
	        temp = keySquare[r * columns + c1];
	        keySquare[r * columns + c1] = keySquare[r * columns + c2];
	        keySquare[r * columns + c2] = temp;
	    }
	    return keySquare;
	}
	
	public static String flipColumns(String keySquare, int columns, int rows) {
		return new String(flipColumns(keySquare.toCharArray(), columns, rows));
	}
	
	/**
	 * Looking at columns flips them.
	 * ABC          CBA   
	 * DEF  becomes FED
	 */
	public static char[] flipColumns(char[] keySquare, int columns, int rows) {
		for(int c = 0; c < columns / 2; c++) {
			for(int r = 0; r < rows; r++)  {
				char temp = keySquare[r * columns + c];
				keySquare[r * columns + c] = keySquare[r * columns + columns - c - 1];
				keySquare[r * columns + columns - c - 1] = temp;
			}
		}
		return keySquare;
	}
	
	public static String flipRows(String keySquare, int columns, int rows) {
		return new String(flipRows(keySquare.toCharArray(), columns, rows));
	}
	
	public static char[] flipRows(char[] keySquare, int columns, int rows) {
		for(int r = 0; r < rows / 2; r++)  {
			for(int c = 0; c < columns; c++) {
				char temp = keySquare[r * columns + c];
				keySquare[r * columns + c] = keySquare[(rows - r - 1) * columns + c];
				keySquare[(rows - r - 1) * columns +  c] = temp;
			}
		}
		return keySquare;
	}
	
	
	public static String reverseKey(String keySquare) {
		return new String(reverseKey(keySquare.toCharArray()));
	}
	
	public static char[] reverseKey(char[] keySquare) {
		for(int i = 0; i < keySquare.length / 2; i++) {
			char temp = keySquare[i];
			keySquare[i] = keySquare[keySquare.length - i - 1];
			keySquare[keySquare.length - i - 1] = temp;
		}
		return keySquare;
	}
	
	public static String modifyKey(String keySquare, int columns, int rows) {
	    switch(RandomUtil.pickRandomInt(50)) {
	        case 0: return swapTwoRows(keySquare, columns, rows);
	        case 1: return swapTwoColumns(keySquare, columns, rows);
	        case 2: return reverseKey(keySquare);
	        case 3: return flipRows(keySquare, columns, rows);
	        case 4: return flipColumns(keySquare, columns, rows);
	        default: return swapTwoCharacters(keySquare);
	    }
	}
}