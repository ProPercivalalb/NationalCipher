package nationalcipher.cipher.tools;

import javalibrary.math.ArrayHelper;
import javalibrary.math.matrics.Matrix;

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
	
	public static interface BifidKey {
		public void onIteration(String keysquare);
	}
	
	public static interface SubstitutionKey {
		public void onIteration(String keyalphabet);
	}
	
	public static interface RailFenceKey {
		public void onIteration(int rows);
	}
	
	public static interface RedefenceKey {
		public void onIteration(int[] order);
	}
	
	public static interface NihilistTranspositionKey {
		public void onIteration(int[] order, int blockLength);
	}
	
	public static interface MyszkowskiKey {
		public void onIteration(String key);
	}
	
	public static interface HomophonicKey {
		public void onIteration(String key);
	}
	
	public static interface VigenereAutoKey {
		public void onIteration(String key);
	}
	
	public static interface PortaAutoKey {
		public void onIteration(String key);
	}
	
	public static interface AMSCOKey {
		public void onIteration(int[] order);
	}
	
	public static interface CadenusKey {
		public void onIteration(String key, int textLength);
	}
	
	public static interface PortaKey {
		public void onIteration(String key);
	}
	
	public static interface VigereneKey {
		public void onIteration(String key);
	}
	
	public static interface BazeriesKey {
		public void onIteration(int no);
	}
	
	public static interface HillKey {
		public void onIteration(Matrix matrix);
	}
	
	public static interface PolluxKey {
		public void onIteration(char[] key);
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
	
	public static void iterateRailFence(RailFenceKey task, int min, int max) {
		for(int i = min; i < max; i++)
			task.onIteration(i);
	}
	
	public static void iterateRedefence(RedefenceKey task, int rows) {
		iterateRedefence(task, ArrayHelper.range(0, rows), 0);
	}
	
	private static void iterateRedefence(RedefenceKey task, int[] arr, int pos) {
	    if(arr.length - pos == 1)
	    	task.onIteration(arr);
	    else
	        for(int i = pos; i < arr.length; i++) {
	            int h = arr[pos];
	            int j = arr[i];
	            arr[pos] = j;
	            arr[i] = h;
	            
	            iterateRedefence(task, arr, pos + 1);
	            arr[pos] = h;
	    	    arr[i] = j;
	        }
	}

	public static void iterateAMSCO(AMSCOKey task, int keyLength) {
		iterateAMSCO(task, ArrayHelper.range(0, keyLength), 0);
	}
	
	private static void iterateAMSCO(AMSCOKey task, int[] arr, int pos) {
		if(arr.length - pos == 1)
			task.onIteration(arr);
		else
		    for(int i = pos; i < arr.length; i++) {
		        int h = arr[pos];
		        int j = arr[i];
		        arr[pos] = j;
		        arr[i] = h;
		            
		        iterateAMSCO(task, arr, pos + 1);
		        arr[pos] = h;
		    	arr[i] = j;
		    }
	}

	public static void iterateCadenus(CadenusKey task, int length, int textLength) {
		iterateCadenus(task, new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','X','Y','Z'}, length, 0, "", textLength);
	}
	
	private static void iterateCadenus(CadenusKey task, char[] characters, int no, int time, String key, int textLength) {
		for(char character : characters) {
			String backup = key;
			if(key.contains("" + character))
				continue;
			
			backup += character;
			
			if(time + 1 >= no) {
				task.onIteration(backup, textLength);
				continue;
			}
			
			iterateCadenus(task, characters, no, time + 1, backup, textLength);
		}
	}
	
	public static void iteratePorta(PortaKey task, int length) {
		iteratePorta(task, length, 0, "");
	}
	
	private static void iteratePorta(PortaKey task, int no, int time, String key) {
		for(char i = 'A'; i <= 'Z'; i += 2) {
			String backup = key;
			backup += i;
			
			if(time + 1 >= no) {
				task.onIteration(backup);
				continue;
			}
			
			iteratePorta(task, no, time + 1, backup);
		}
	}
	
	public static void iterateHill(HillKey task, int size) {
		iterateHill(task, 0, 25, (int)Math.pow(size, 2), size, size, 0, new int[(int)Math.pow(size, 2)]);
	}
	
	private static void iterateHill(HillKey task, int range_low, int range_high, int no, int rows, int columns, int time, int[] array) {
		for(int i = range_low; i <= range_high; i++) {
			array[time] = i;
			
			if(time + 1 >= no) {
				Matrix matrix = new Matrix(array, rows, columns);
				task.onIteration(matrix);
				continue;
			}
			
			iterateHill(task, range_low, range_high, no, rows, columns, time + 1, array);
		}
	}
	
	public static void iterateMyszkowski(MyszkowskiKey task, int keyLength) {
		iterateMyszkowski(task, keyLength, 0, "");
	}
	
	private static void iterateMyszkowski(MyszkowskiKey task, int no, int time, String key) {
		for(char i = 'A'; i <= 'Z'; i++) {
			String backup = key;
			backup += i;
			
			if(time + 1 >= no) {
				task.onIteration(backup);
				continue;
			}
			
			iterateMyszkowski(task, no, time + 1, backup);
		}
	}
	
	public static void iterateHomophonic(HomophonicKey task, int keyLength) {
		iterateHomophonic(task, keyLength, 0, "");
	}
	
	private static void iterateHomophonic(HomophonicKey task, int no, int time, String key) {
		for(char i = 'A'; i <= 'Z'; i++) {
			String backup = key;
			backup += i;
			
			if(time + 1 >= no) {
				task.onIteration(backup);
				continue;
			}
			
			iterateHomophonic(task, no, time + 1, backup);
		}
	}

	public static void iterateVigenereAutoKey(VigenereAutoKey task, int keyLength) {
		iterateVigenereAutoKey(task, keyLength, 0, "");
	}
	
	private static void iterateVigenereAutoKey(VigenereAutoKey task, int no, int time, String key) {
		for(char i = 'A'; i <= 'Z'; i++) {
			String backup = key;
			backup += i;
			
			if(time + 1 >= no) {
				task.onIteration(backup);
				continue;
			}
			
			iterateVigenereAutoKey(task, no, time + 1, backup);
		}
	}
	
	
	public static void iterateNihilistTransposition(NihilistTranspositionKey task, int length, int blockLength) {
		iterateNihilistTransposition(task, ArrayHelper.range(0, length), 0, blockLength);
	}
	
	private static void iterateNihilistTransposition(NihilistTranspositionKey task, int[] arr, int pos, int blockLength) {
	    if(arr.length - pos == 1)
	    	task.onIteration(arr, blockLength);
	    else
	        for(int i = pos; i < arr.length; i++) {
	            int h = arr[pos];
	            int j = arr[i];
	            arr[pos] = j;
	            arr[i] = h;
	            
	            iterateNihilistTransposition(task, arr, pos + 1, blockLength);
	            arr[pos] = h;
	    	    arr[i] = j;
	        }
	}
	
	public static void iteratePortaAutokey(PortaAutoKey task, int length) {
		iteratePortaAutokey(task, length, 0, "");
	}
	
	private static void iteratePortaAutokey(PortaAutoKey task, int no, int time, String key) {
		for(char i = 'A'; i <= 'Z'; i += 2) {
			String backup = key;
			backup += i;
			
			if(time + 1 >= no) {
				task.onIteration(backup);
				continue;
			}
			
			iteratePortaAutokey(task, no, time + 1, backup);
		}
	}

	public static void iteratePollux(PolluxKey task) {
		iteratePollux(task, 0, new char[10]);
	}
	
	private static void iteratePollux(PolluxKey task, int pos, char[] arr) {
		if(arr.length - pos == 0)
			task.onIteration(arr);
		else
			for(char i : new char[] {'.', '-', 'X'}) {
				char h = arr[pos];
	            arr[pos] = i;
	            
	            iteratePollux(task, pos + 1, arr);
	            arr[pos] = h;
			}
	}


}
