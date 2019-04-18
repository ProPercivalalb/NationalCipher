package nationalcipher.cipher.decrypt.methods;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javalibrary.math.matrics.Matrix;
import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.transposition.Grille.GrilleKey;
import nationalcipher.cipher.tools.KeyGeneration;

public class KeyIterator {
	
	public static void iterateIntegerKey(IntConsumer consumer, int min, int range, int step) {
		for(int i = min; i < min + range; i += step)
			consumer.accept(i);
	}
	
	public static void iterateAffineKey(BiConsumer<Integer, Integer> consumer) {
		for(int a : new int[] {1,3,5,7,9,11,15,17,19,21,23,25})
  			for(int b = 0; b < 26; b++)
  				consumer.accept(a, b);
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
		iterateShortCustomKey(capturer, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", length, repeats);
	}
	
	public static void iterateShortCustomKey(ShortCustomKey capturer, String chars, int length, boolean repeats) {
		iterateShortKey(capturer, chars.toCharArray(), length, 0, "", repeats);
	}

	private static void iterateShortKeyFast(ShortCustomKey capturer, char[] characters, int no, int time, String key) {
		//for(int )
		
		for(char character : characters) {
			String backup = key;
			if(key.contains("" + character))
				continue;
			
			backup += character;
			
			if(time + 1 >= no) {
				capturer.onIteration(backup);
				continue;
			}
			
			iterateShortKeyFast(capturer, characters, no, time + 1, backup);
		}
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
	
	public static interface CadenusKey {
		public void onIteration(String key, int[] order);
	}
	
	public static void iterateCadenusKey(CadenusKey capturer, int length) {
		iterateShortCadenusKey(capturer, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", length);
	}
	
	public static void iterateShortCadenusKey(CadenusKey capturer, String chars, int length) {
		iterateCadenusKey(capturer, chars.toCharArray(), length, 0, "");
	}

	private static void iterateCadenusKey(CadenusKey capturer, char[] characters, int no, int time, String key) {
		for(char character : characters) {
			String backup = key;
			if(key.contains("" + character))
				continue;
			
			backup += character;
			
			if(time + 1 >= no) {
				capturer.onIteration(backup, new int[0]);
				continue;
			}
			
			iterateCadenusKey(capturer, characters, no, time + 1, backup);
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
	
	public static interface HuttonKey {
		public void onIteration(String key1, String key2);
	}
	
	public static void iterateHutton(HuttonKey task, int length1, int length2) {
		iterateHuttonKey1(task, "ABCDEFGHIJKLMNOPQRSTUVWXY".toCharArray(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(), length1, length2, 0, "");
	}
	
	private static void iterateHuttonKey1(HuttonKey capturer, char[] characters, char[] characters2, int no, int no2, int time, String key) {
		for(char character : characters) {
			String backup = key;
			
			backup += character;
			
			if(time + 1 >= no) {
				iterateHuttonKey2(capturer, backup, characters2, no2, 0, "");
				continue;
			}
			
			iterateHuttonKey1(capturer, characters, characters2, no, no2, time + 1, backup);
		}
	}
	
	
	private static void iterateHuttonKey2(HuttonKey capturer, String key1, char[] characters2, int no, int time, String key) {
		for(char character : characters2) {
			String backup = key;
			if(key.contains("" + character))
				continue;
			
			backup += character;
			
			if(time + 1 >= no) {
				capturer.onIteration(key1, backup);
				continue;
			}
			
			iterateHuttonKey2(capturer, key1, characters2, no, time + 1, backup);
		}
	}
	
	public static <T> void permutateString(Consumer<String> consumer, String str) {
		permutateObject(d -> consumer.accept(PrimTypeUtil.toString(d)), PrimTypeUtil.toCharacterArray(str));
	}
	       
	public static <T> void permutateObject(Consumer<T[]> consumer, T[] items) {
		permutateObject(consumer, items, 0);
	}
	
	private static <T> void permutateObject(Consumer<T[]> consumer, T[] arr, int pos) {
	    if(arr.length - pos == 1)
	    	consumer.accept(arr);
	    else {
	        for(int i = pos; i < arr.length; i++) {
	            T h = arr[pos];
	            T j = arr[i];
	            arr[pos] = j;
	            arr[i] = h;
	            
	            permutateObject(consumer, arr, pos + 1);
	            arr[pos] = h;
	    	    arr[i] = j;
	        }
	    }
	}
	
	public static <T> void iterateObject(Consumer<T[]> consumer, Class<T> clazz, int length, T[] items) {
		iterateObject(consumer, (T[])Array.newInstance(clazz, length), items);
	}
	
	public static <T> void iterateObject(Consumer<T[]> consumer, T[] holder, T[] items) {
		iterateObject(consumer, holder, 0, items);
	}
	
	private static <T> void iterateObject(Consumer<T[]> consumer, T[] holder, int pos, T[] items) {
		if(holder.length - pos == 0)
			consumer.accept(holder);
		else {
			for(T i : items) {
				holder[pos] = i;
	            iterateObject(consumer, holder, pos + 1, items);
			}
		}
	}
	
	public static void iteratorSquareMatrixKey(Consumer<Matrix> consumer, int size) {
		iteratorSquareMatrixKey(consumer, 0, 25, (int)Math.pow(size, 2), size, size, 0, new int[(int)Math.pow(size, 2)]);
	}
	
	private static void iteratorSquareMatrixKey(Consumer<Matrix> consumer, int range_low, int range_high, int no, int rows, int columns, int time, int[] array) {
		for(int i = range_low; i <= range_high; i++) {
			array[time] = i;
			
			if(time + 1 >= no) {
				Matrix matrix = new Matrix(array, rows, columns);
				consumer.accept(matrix);
				continue;
			}
			
			iteratorSquareMatrixKey(consumer, range_low, range_high, no, rows, columns, time + 1, array);
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
		
		KeyIterator.permutateArray(new IntArrayPermutations() {

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
	

	public static interface IntArrayPermutations {
		
		/**
		 * @param id An arbitrary incase the same class needs to implement this method twice you can specify which part should run
		 * @param data The int array, this is changed each iteration so make a copy if you need to refer to it again
		 */
		public void onList(byte id, int[] data, Object... extra);
	}
	
	public static interface GenericArrayPermutations<T> extends IntArrayPermutations {
		
		public abstract void onList(byte id, T[] data, Object... extra);
		
		@Override
		public default void onList(byte id, int[] data, Object... extra) {
			
			for(int i = 0; i < data.length; i++) {
				//this.temp[i] = this.elements[i];
			}
			
			//onList(id, this.temp, extra);
		}
		
		
	}
	
	/**
	 * 
	 * @param task
	 * @param size
	 * @param range The possible range of values from 0 to range (not including range)
	 * @param duplicates
	 */
	public static void permutateArray(IntArrayPermutations task, int size, int range, boolean duplicates, Object... extra) {
		permutateArray(task, (byte)-1, size, range, duplicates, 0, new int[size], extra);
	}
	
	public static void permutateArray(IntArrayPermutations task, byte id, int arrayLength, int range, boolean duplicates, Object... extra) {
		permutateArray(task, id, arrayLength, range, duplicates, 0, new int[arrayLength], extra);
	}
	
	private static void permutateArray(IntArrayPermutations task, byte id, int arrayLength, int range, boolean duplicates, int count, int[] pattern, Object... extra) {
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
			
			if(nextCount == arrayLength) 
				task.onList(id, pattern, extra);
			else
				permutateArray(task, id, arrayLength, range, duplicates, nextCount, pattern, extra);
			
			pattern[count] = previous;
		}	
	}
}
