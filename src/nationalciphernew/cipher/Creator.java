package nationalciphernew.cipher;

public class Creator {

	public static interface CaesarShift {
		public void onIteration(int i);
	}
	
	public static interface AffineKey {
		public void onIteration(int a, int b);
	}
	
	public static interface PlayfairKey {
		public void onIteration(String keysquare);
	}
	
	
	public static interface SubstitutionKey {
		public void onIteration(String keyalphabet);
	}
	
	public static void iterateCaesar(CaesarShift task) {
		for(int i = 0; i < 26; i++)
			task.onIteration(i);
	}
	
	public static void iterateAffine(AffineKey task) {
		for(int a : new int[] {1,3,5,7,9,11,15,17,19,21,23,25})
  			for(int b = 0; b < 26; ++b)
  				task.onIteration(a, b);
	}

	public static void iteratePlayfair(PlayfairKey task) {
		
	}
	
	public static void iterateSubstitution(SubstitutionKey task) {
		
	}
}
