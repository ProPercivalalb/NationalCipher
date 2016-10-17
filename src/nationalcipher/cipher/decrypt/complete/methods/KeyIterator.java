package nationalcipher.cipher.decrypt.complete.methods;

import javalibrary.util.ArrayUtil;

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
}
