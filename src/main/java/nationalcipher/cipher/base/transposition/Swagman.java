package nationalcipher.cipher.base.transposition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Swagman implements IRandEncrypter {
	
    public static boolean validate(int[] key, int size) {
        for(int row = 0; row < size; row++) {
            Set<Integer> set = new HashSet<>();
            for(int col = 0; col < size; col++) {
                set.add(key[row * size + col]);
            } 
            if(set.size() != size || set.contains(-1)) 
                return false;
        }
        
        for(int col = 0; col < size; col++) {
            Set<Integer> set = new HashSet<>();
            for(int row = 0; row < size; row++) {
                set.add(key[row * size + col]);
            }
            if(set.size() != size || set.contains(-1)) 
                return false;
        }
        
        return true;
    }
    
	public static void main(String[] args) throws InterruptedException {
	    
	   for(int i = 0; i < 10000; i++) {
	        int[] key = KeyGeneration.createSwagmanKey(3);
            System.out.println("Key: " + validate(key, 3) + " " + i + Arrays.toString(key));

	        //for(int k = 0; k < 5; k++) {
	        ///    String s = "";
	        //    for(int j = 0; j < 5; j++) {
	        //        s += key[k * 5 + j] + ", ";
	        //    }
	        //    System.out.println(s);
	        //}
	        //System.out.println("Key: " + Arrays.toString(key));
	    }
	    
	    /**
	    
		while(true) {
			int size = RandomUtil.pickRandomInt(2, 7);
			int[] key = KeyGeneration.createSwagmanKey(size);
			System.out.println("Key: " + Arrays.toString(key));
			String orignal = "DONTBEAFRAIDTOTAKEABIGLEAPIFONEISINDICATEDYOUCANNOTCROSSARIVERORACHASMINTWOSMALLJUMPS";
			while(orignal.length() % size != 0)
				orignal += 'X';
			String encoded = encode(orignal, key, size);
			System.out.println("Size: " +size + " " + encoded);
			String decoded = new String(decode(encoded.toCharArray(), key, size));
			//System.out.println(decoded);
			if(!decoded.equals(orignal))
				System.out.println("FAILED");
			//Thread.sleep(200);
		}**/
	}
	
	public static String encode(String plainText, int[] key, int size) {
	    System.out.println(key);
		while(plainText.length() % size != 0) plainText += 'X';
		
		char[] tempText = new char[plainText.length()];
		char[] cipherText = new char[plainText.length()];
		
		int squareMag = (int)Math.pow(size, 2);
		int rowLength = plainText.length() / size;
		int noSquares = (int)Math.ceil(plainText.length() / (double)squareMag);
		
		int[] colInSquare = new int[noSquares];
		for(int i = 0; i < noSquares; i++) colInSquare[i] = size;
		if(plainText.length() % squareMag != 0) colInSquare[noSquares - 1] = (plainText.length() % squareMag) / size;

		for(int s = 0; s < noSquares; s++)
			for(int r = 0; r < size; r++)
				for(int c = 0; c < colInSquare[s]; c++)
					tempText[s * squareMag + c + colInSquare[s] * key[r * size + c % size]] = plainText.charAt(s * size + r * rowLength + c);
	

		for(int s = 0; s < noSquares; s++)
			for(int r = 0; r < size; r++)
				for(int c = 0; c < colInSquare[s]; c++)
					cipherText[s * squareMag + c * size + r] = tempText[s * squareMag + c + colInSquare[s] * r];

		
		return new String(cipherText);
	}
	
	public static byte[] decode(char[] cipherText, int[] key, int size) {
		int[] inKey = new int[key.length];
		for(int c = 0; c < size; c++) for(int r = 0; r < size; r++) inKey[key[r * size + c] * size + c] = r;

		
		byte[] tempText = new byte[cipherText.length];
		byte[] plainText = new byte[cipherText.length];
		
		int squareMag = (int)Math.pow(size, 2);
		int noSquares = (int)Math.ceil(cipherText.length / (double)squareMag);
		
		int[] colInSquare = new int[noSquares];
		for(int i = 0; i < noSquares; i++) colInSquare[i] = size;
		if(cipherText.length % squareMag != 0) colInSquare[noSquares - 1] = (cipherText.length % squareMag) / size;
		
		
		for(int s = 0; s < noSquares; s++)
			for(int r = 0; r < size; r++)
				for(int c = 0; c < colInSquare[s]; c++)
					tempText[s * squareMag + c + colInSquare[s] * inKey[r * size + c % size]] = (byte)cipherText[s * squareMag + c * size + r];
		
		int i = 0;

		for(int r = 0; r < size; r++)
			for(int s = 0; s < noSquares; s++)
				for(int c = 0; c < colInSquare[s]; c++)
					plainText[i++] = tempText[s * squareMag + c + r * colInSquare[s]];
		
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		int size = RandomUtil.pickRandomInt(2, 5);
		return encode(plainText, KeyGeneration.createSwagmanKey(size), size);
	}
}
