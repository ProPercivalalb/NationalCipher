package nationalcipher.cipher.tools;

import javalibrary.math.matrics.Matrix;
import javalibrary.util.ArrayUtil;
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
	
	public static String changeCharacters(String key, String characters, boolean canHaveRepeats) {
		return new String(changeCharacters(key.toCharArray(), characters.toCharArray(), canHaveRepeats));
	}
	
	public static char[] changeCharacters(char[] keySquare, char[] characters, boolean canHaveRepeats) {
	    int i1 = RandomUtil.pickRandomInt(keySquare.length);
	    do {
		    int i2 = RandomUtil.pickRandomInt(characters.length);
		    char tempChar = characters[i2];
	    	if(canHaveRepeats || !ArrayUtil.contains(keySquare, tempChar)) {
			    keySquare[i1] = tempChar;
			    return keySquare;
	    	}
	    } 
	    while(true);
	}
	
	public static String changeLength(String key, int min, int max, String characters, boolean canHaveRepeats) {
		return new String(changeLength(key.toCharArray(), min, max, characters.toCharArray(), canHaveRepeats));
	}
	
	public static char[] changeLength(char[] keySquare, int min, int max, char[] characters, boolean canHaveRepeats) {
	    int newLength = RandomUtil.pickRandomInt(min, max);
    	char[] newKey = new char[newLength];
    	
	    if(newLength < keySquare.length) {
	    	System.arraycopy(keySquare, 0, newKey, 0, newLength);
	    	return newKey;
	    }
	    else if(newLength == keySquare.length) {
	    	return keySquare;
	    }
	    else {
	    	System.arraycopy(keySquare, 0, newKey, 0, keySquare.length);
	    	int pos = keySquare.length;
	    	do {
	    	    int i2 = RandomUtil.pickRandomInt(characters.length);
	    	    char tempChar = characters[i2];
		    	if(canHaveRepeats || !ArrayUtil.contains(keySquare, tempChar)) {
		    		newKey[pos++] = tempChar;
				    return keySquare;
		    	}
		    } 
		    while(true);
	    }
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
		double d = RandomUtil.pickDouble();
		if(d < 0.1) {
			if(d < 0.02) return swapTwoRows(keySquare, columns, rows);
			else if(d < 0.04) return swapTwoColumns(keySquare, columns, rows);
			else if(d < 0.06)  return reverseKey(keySquare);
			else if(d < 0.08)  return flipRows(keySquare, columns, rows);
			else return flipColumns(keySquare, columns, rows);
		}
		else 
			return swapTwoCharacters(keySquare);
	}

	public static int[] swapOrder(int[] order) {
	    int i1 = RandomUtil.pickRandomInt(order.length);
	    int i2 = RandomUtil.pickRandomInt(order.length);
	    int temp = order[i1];
	    order[i1] = order[i2];
	    order[i2] = temp;
	    return order;
	}
	
	public static int[] reverseOrder(int[] order) {
		for(int i = 0; i < order.length / 2; i++) {
			int temp = order[i];
			order[i] = order[order.length - i - 1];
			order[order.length - i - 1] = temp;
		}
		return order;
	}
	
	public static int[] cutSwapOrder(int[] order) {
		int[] newOrder = new int[order.length];
		int cut = RandomUtil.pickRandomInt(1, order.length - 2);
		
		int index = 0;
		for(int i = cut; i < order.length; i++)
			newOrder[index++] = order[i];
		for(int i = 0; i < cut; i++)
			newOrder[index++] = order[i];
		
		return newOrder;
	}
	
	public static int[] rotateOrder(int[] order) {
		int[] newOrder = new int[order.length];

		int rotate = RandomUtil.pickRandomInt(1, order.length - 1);
		for(int i = 0; i < order.length; i++)
			newOrder[(i + rotate) % order.length] = order[i];
			
		return newOrder;
	}
	
	public static int[] modifyOrder(int[] order) {
	    switch(RandomUtil.pickRandomInt(30)) {
	        case 0: return reverseOrder(order);
	        case 1: return cutSwapOrder(order);
	        case 2: return rotateOrder(order);
	        default: return swapOrder(order);
	    }
	}

	public static Matrix modifyMatrix(Matrix matrix, int row, int size) {
		int col = RandomUtil.pickRandomInt(size);
		int value = RandomUtil.pickRandomInt(26);
		Matrix copy = matrix.copy();
		copy.data[row * size + col] = value;
		return copy;
	}
	
	public static Character[] swapMorseIndex(Character[] order) {
	    order[RandomUtil.pickRandomInt(order.length)] = RandomUtil.pickRandomElement(KeyGeneration.ALL_POLLUX_CHARS);
	    return order;
	}
	
	public static String modifyHuttonKey1(String key1) {
		double d = RandomUtil.pickDouble();
		if(d < 0.1) {
			if(d < 0.00) return changeLength(key1, 2, 8, "ABCDEFGHIJKLMNOPQRSTUVWXY", true);
			else return swapTwoCharacters(key1);
		}
		else 
			return changeCharacters(key1, "ABCDEFGHIJKLMNOPQRSTUVWXY", true);
	}
	
	public static String modifyHuttonKey2(String key1) {
		double d = RandomUtil.pickDouble();
		if(d < 0.1) {
			if(d < 0.00) return changeLength(key1, 2, 8, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", false);
			else return swapTwoCharacters(key1);
		}
		else 
			return changeCharacters(key1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", false);
	}
}
