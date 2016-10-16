package nationalcipher.cipher.decrypt.complete.methods;

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
}
