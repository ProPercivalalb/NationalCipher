package nationalcipher.cipher.tools;

import java.util.Arrays;
import java.util.List;

import javalibrary.math.matrics.Matrix;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import javalibrary.util.RandomUtil;

public class KeyGeneration {

	private final static char[] allPolluxChars = "X.-".toCharArray();
	
	public final static char[] ALL_36_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
	public final static char[] ALL_27_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#".toCharArray();
	public final static char[] ALL_26_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	public final static char[] ALL_25_CHARS = "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray();
	
	public static String createRepeatingShortKey26(int length) {
		char[] key = new char[length];
		
		for(int i = 0; i < length; i++)
			key[i] = RandomUtil.pickRandomChar(ALL_26_CHARS);
		
		return new String(key);
	}
	
	public static String createShortKey26(int length) {
		List<Character> characters = ListUtil.toList(ALL_26_CHARS);
		
		char[] key = new char[length];
		
		for(int i = 0; i < length; i++) {
			key[i] = RandomUtil.pickRandomElement(characters);
			characters.remove((Character)key[i]);
		}
			
		return new String(key);
	}
	
	public static String createLongKey25() {
		return createLongKeyUniversal(ALL_25_CHARS);
	}
	
	public static String createLongKey26() {
		return createLongKeyUniversal(ALL_26_CHARS);
	}
	
	public static String createLongKey27() {
		return createLongKeyUniversal(ALL_27_CHARS);
	}
	
	public static String createLongKey36() {
		return createLongKeyUniversal(ALL_36_CHARS);
	}
	
	public static String createLongKeyUniversal(char[] charList) {
		List<Character> characters = ListUtil.toList(charList);
		
		char[] key = new char[characters.size()];
		for(int i = 0; i < key.length; i++) {
			char rC = RandomUtil.pickRandomElement(characters);
			key[i] = rC;
			characters.remove((Character)rC);
		}

		return new String(key);
	}
	
	public static int[] createOrder(int length) {
		int[] order = ArrayUtil.range(0, length);

		ArrayUtil.shuffle(order);
		
		return order;
	}
	
	public static Matrix createMatrix(int size, int range) {
		return createMatrix(size, size, range);
	}
	
	public static Matrix createMatrix(int rows, int columns, int range) {
		Matrix matrix = new Matrix(rows, columns);
		for(int i = 0; i < matrix.data.length; i++)
			matrix.data[i] = RandomUtil.pickRandomInt(range);
		
		return matrix;
	}
	
	public static List<Integer> createOrderList(int length) {
		return ListUtil.toList(createOrder(length));
	}
	
	
	//Specific key generators
	
	public static char[] createPolluxKey() {
		List<Character> characters = ListUtil.toList(allPolluxChars);
		
		char[] key = new char[10];
		int i = 0;
		
		for(; i < characters.size(); i++) {
			char rC = RandomUtil.pickRandomElement(characters);
			key[i] = rC;
			characters.remove((Character)rC);
		}
		
		for(; i < key.length; i++)
			key[i] = RandomUtil.pickRandomChar(allPolluxChars);
		
		ArrayUtil.shuffle(key);
		
		return key;
	}

	
	//Very inefficient requires much more work to be more efficient
	public static int[] createSwagmanKey(int size) {
		while(true) {
			try {
				int[] key = new int[size * size];
				Arrays.fill(key, -1);
			
				while(!isFilled(key, size)) {
					creation:
					for(int row = 0; row < size; row++) {
						for(int col = 0; col < size; col++) {
							boolean change = autoFill(key, size);
							if(change) break creation;
							if(key[row * size + col] != -1) continue;
							
							List<Integer> options = getOptionsForCell(key, size, row, col);
				
							Integer option = RandomUtil.pickRandomElement(options);
						
							key[row * size + col] = option;
							options.remove(option);
						}
					}
				}
				return key;
			}
			catch(Exception e) {} //If the key is invalid will attept again till it gets it gets a valid key
		}
	}
	
	private static boolean isFilled(int[] key, int size) {
		for(int row = 0; row < size; row++)
			for(int col = 0; col < size; col++)
				if(key[row * size + col] == -1)
					return false;
		
		return true;
	}
	
	private static List<Integer> getOptionsForCell(int[] key, int size, int index) {
		return getOptionsForCell(key, size, index / size, index % size);
	}
	
	private static List<Integer> getOptionsForCell(int[] key, int size, int row, int column) {
		List<Integer> validOptions = ListUtil.range(0, size - 1);

		for(int nR = 0; nR < size; nR++)
			validOptions.remove((Integer)key[nR * size + column]);
		
		for(int nC = 0; nC < size; nC++)
			validOptions.remove((Integer)key[row * size + nC]);
		
		return validOptions;
	}
	
	private static boolean autoFill(int[] key, int size) {
		boolean finished;
		boolean change = false;
		
		do {
			finished = true;
			search:
			for(int row = 0; row < size; row++) {
				for(int col = 0; col < size; col++) {
					if(key[row * size + col] != -1) continue;
					
					List<Integer> options = getOptionsForCell(key, size, row, col);
					
					if(options.size() == 1) {
						key[row * size + col] = options.get(0);
						finished = false;
						change = true;
						break search;
					}
				}
			}
		}
		while(!finished);
		
		return change;
	}
	
	// Random key length generator
	
	
	public static String createShortKey26(int minLength, int maxLength) {
		int length = RandomUtil.pickRandomInt(minLength, maxLength);
		return createShortKey26(length);
	}
	

	public static String createRepeatingShortKey26(int minLength, int maxLength) {
		int length = RandomUtil.pickRandomInt(minLength, maxLength);
		return createRepeatingShortKey26(length);
	}
	
	public static int[] createOrder(int minLength, int maxLength) {
		int length = RandomUtil.pickRandomInt(minLength, maxLength);
		return createOrder(length);
	}
	
	
	
	
	

}
