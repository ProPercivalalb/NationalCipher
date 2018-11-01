package nationalcipher.cipher.decrypt.methods;

import javalibrary.math.matrics.Matrix;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.transposition.Grille.GrilleKey;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ArrayPermutations;
import nationalcipher.cipher.tools.KeyGeneration;

public class KeyIterator {

	public static interface IntegerKey {
		public void onIteration(int no);
	}
	
	public static void iterateIntegerKey(IntegerKey capturer, int min, int range, int step) {
		for(int i = min; i < min + range; i += step)
			capturer.onIteration(i);
	}
	
	public static interface AffineKey {
		public void onIteration(int a, int b);
	}
	
	public static void iterateAffineKey(AffineKey capturer) {
		for(int a : new int[] {1,3,5,7,9,11,15,17,19,21,23,25})
  			for(int b = 0; b < 26; b++)
  				capturer.onIteration(a, b);
	}
	
	public static interface Long27Key {
		public void onIteration(String key);
	}
	
	public static interface Long26Key {
		public void onIteration(String key);
	}
	
	public static void iterateLong26Key(Long26Key capturer) {
		
	}
	
	public static interface Long25Key {
		public void onIteration(String key);
	}
	
	public static void iterateLong25Key(Long26Key capturer) {
		
	}
	
	public static interface ShortCustomKey {
		public void onIteration(String key);
	}
	
	public static void iterateShort26Key(ShortCustomKey capturer, int length, boolean repeats) {
		iterateShortCustomKey(capturer, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", length, repeats);
	}
	
	public static void iterateShortCustomKey(ShortCustomKey capturer, String chars, int length, boolean repeats) {
		iterateShortKey(capturer, chars.toCharArray(), length, 0, "", repeats);
	}

	private static void iterateShortKey(ShortCustomKey capturer, char[] characters, int no, int time, String key, boolean repeats) {
		for(char character : characters) {
			String backup = key;
			if(!repeats && key.contains("" + character))
				continue;
			
			backup += character;
			
			if(time + 1 >= no) {
				capturer.onIteration(backup);
				continue;
			}
			
			iterateShortKey(capturer, characters, no, time + 1, backup, repeats);
		}
	}
	
	public static interface IntegerOrderedKey {
		public void onIteration(int[] order);
	}
	
	public static void iterateIntegerOrderedKey(IntegerOrderedKey task, int length) {
		iterateIntegerOrderedKey(task, ArrayUtil.createRange(length));
	}
	
	public static void iterateIntegerOrderedKey(IntegerOrderedKey task, int[] arr) {
		iterateIntegerOrderedKey(task, arr, 0);
	}
	
	private static void iterateIntegerOrderedKey(IntegerOrderedKey task, int[] arr, int pos) {
	    if(arr.length - pos == 1)
	    	task.onIteration(arr);
	    else
	        for(int i = pos; i < arr.length; i++) {
	            int h = arr[pos];
	            int j = arr[i];
	            arr[pos] = j;
	            arr[i] = h;
	            
	            iterateIntegerOrderedKey(task, arr, pos + 1);
	            arr[pos] = h;
	    	    arr[i] = j;
	        }
	}
	

	public static interface DoubleIntegerOrderedKey {
		public void onIteration(int[] order1, int[] order2);
	}
	
	public static void iterateDoubleIntegerOrderedKey(DoubleIntegerOrderedKey task, int length1, int length2) {
		iterateDoubleIntegerOrderedKey(task, ArrayUtil.createRange(length1), length2, 0);
	}
	
	private static void iterateDoubleIntegerOrderedKey(DoubleIntegerOrderedKey task, int[] arr, int length2, int pos) {
	    if(arr.length - pos == 1)
	    	iterateIntegerOrderedKeySecond(task, ArrayUtil.createRange(length2), arr, 0);
	    else
	        for(int i = pos; i < arr.length; i++) {
	            int h = arr[pos];
	            int j = arr[i];
	            arr[pos] = j;
	            arr[i] = h;
	            
	            iterateDoubleIntegerOrderedKey(task, arr, length2, pos + 1);
	            arr[pos] = h;
	    	    arr[i] = j;
	        }
	}
	

	private static void iterateIntegerOrderedKeySecond(DoubleIntegerOrderedKey task, int[] arr, int[] firstArray, int pos) {
	    if(arr.length - pos == 1)
	    	task.onIteration(firstArray, arr);
	    else
	        for(int i = pos; i < arr.length; i++) {
	            int h = arr[pos];
	            int j = arr[i];
	            arr[pos] = j;
	            arr[i] = h;
	            
	            iterateIntegerOrderedKeySecond(task, arr, firstArray, pos + 1);
	            arr[pos] = h;
	    	    arr[i] = j;
	        }
	}
	       
	
	public static interface PermutateString {
		public void onPermutate(String key);
	}
	
	public static void permutateString(PermutateString task, String str) {
		permutateString(task, str, ArrayUtil.createRange(str.length()), 0);
	}
	
	private static void permutateString(PermutateString task, String str, int[] arr, int pos) {
	    if(arr.length - pos == 1) {
	    	char[] newStr = new char[str.length()];
	    	for(int i = 0; i < arr.length; i++)
	    		newStr[i] = str.charAt(arr[i]);
	    	task.onPermutate(new String(newStr));
	    }
	    else
	        for(int i = pos; i < arr.length; i++) {
	            int h = arr[pos];
	            int j = arr[i];
	            arr[pos] = j;
	            arr[i] = h;
	            
	            permutateString(task, str, arr, pos + 1);
	            arr[pos] = h;
	    	    arr[i] = j;
	        }
	}
	
	public static interface SquareMatrixKey {
		public void onIteration(Matrix matrix);
	}
	
	public static void iteratorSquareMatrixKey(SquareMatrixKey task, int size) {
		iteratorSquareMatrixKey(task, 0, 25, (int)Math.pow(size, 2), size, size, 0, new int[(int)Math.pow(size, 2)]);
	}
	
	private static void iteratorSquareMatrixKey(SquareMatrixKey task, int range_low, int range_high, int no, int rows, int columns, int time, int[] array) {
		for(int i = range_low; i <= range_high; i++) {
			array[time] = i;
			
			if(time + 1 >= no) {
				Matrix matrix = new Matrix(array, rows, columns);
				task.onIteration(matrix);
				continue;
			}
			
			iteratorSquareMatrixKey(task, range_low, range_high, no, rows, columns, time + 1, array);
		}
	}
	
	public static interface CharacterKey {
		public void onIteration(char[] key);
	} 
	
	public static void iteratePollux(CharacterKey task) {
		iteratePollux(task, 0, new char[10]);
	}
	
	private static void iteratePollux(CharacterKey task, int pos, char[] arr) {
		if(arr.length - pos == 0)
			task.onIteration(arr);
		else
			for(char i : KeyGeneration.ALL_POLLUX_CHARS) {
				char h = arr[pos];
	            arr[pos] = i;
	            
	            iteratePollux(task, pos + 1, arr);
	            arr[pos] = h;
			}
	}
	
	public static interface GrilleKey {
		public void onIteration(int[] key);
	}
	
	public static void iterateGrille(GrilleKey task, int size) {
		double halfSize = size / 2D;
		int rows = (int)Math.ceil(halfSize);
		int cols = (int)Math.floor(halfSize);
		int keySize = rows * cols;
		
		int[] key = new int[keySize];
		int count = 0;
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
				key[count++] = r * size + c;
		
		KeyIterator.permutateArray(new ArrayPermutations() {

			@Override
			public void onList(byte id, int[] data, Object... extra) {
				GrilleKey keyGenerator = (GrilleKey)extra[0];
				int[] starting = (int[])extra[1];
				int[] next = new int[starting.length];
				
				for(int i = 0; i < key.length; i++) {
					int quadrant = data[i];
					int value = starting[i];
					for(int rot = 0; rot < quadrant; rot++) {
						int row = value / size;
						int col = value % size;
						value = col * size + (size - row - 1);
					}
					next[i] = value;
				}
				keyGenerator.onIteration(next);
			}
			
		}, keySize, 4, true, task, key);
	}
	
	public static interface ArrayPermutations {
		
		/**
		 * @param id An arbitrary incase the same class needs to implement this method twice you can specify which part should run
		 * @param data The int array, this is changed each iteration so make a copy if you need to refer to it again
		 */
		public void onList(byte id, int[] data, Object... extra);
	}
	
	/**
	 * 
	 * @param task
	 * @param size
	 * @param range The possible range of values from 0 to range (not including range)
	 * @param duplicates
	 */
	public static void permutateArray(ArrayPermutations task, int size, int range, boolean duplicates, Object... extra) {
		permutateArray(task, (byte)-1, size, range, duplicates, 0, new int[size], extra);
	}
	
	public static void permutateArray(ArrayPermutations task, byte id, int size, int range, boolean duplicates, Object... extra) {
		permutateArray(task, id, size, range, duplicates, 0, new int[size], extra);
	}
	
	private static void permutateArray(ArrayPermutations task, byte id, int size, int range, boolean duplicates, int count, int[] pattern, Object... extra) {
		int nextCount = count + 1;
		
		hasDuplicate:
		for(int i = 0; i < range; i++) {
			int previous = pattern[count];
			
			//If can't have duplicates
			if(!duplicates)
				for(int j = 0; j < count; j++) 
					if(pattern[j] == i)
						continue hasDuplicate;
			
			pattern[count] = i;
			
			if(nextCount == size) 
				task.onList(id, pattern, extra);
			else
				permutateArray(task, id, size, range, duplicates, nextCount, pattern, extra);
			
			pattern[count] = previous;
		}
			
	}
}
