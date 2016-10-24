package nationalcipher.cipher.decrypt.complete.methods;

import javalibrary.math.matrics.Matrix;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.tools.Creator.HillKey;

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
		iterateShortCustomKey(capturer, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", length, repeats);
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
		iterateIntegerOrderedKey(task, ArrayUtil.range(0, length), 0);
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
}
