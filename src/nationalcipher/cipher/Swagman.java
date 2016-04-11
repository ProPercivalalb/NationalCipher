package nationalcipher.cipher;

import java.util.Arrays;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Swagman implements IRandEncrypter {
	
	public static void main(String[] args) throws InterruptedException {
		while(true) {
			int size = RandomUtil.pickRandomInt(2, 6);
			int[] key = KeyGeneration.createSwagmanKey(size);
			
			String orignal = "DONTBEAFRAIDTOTAKEABIGLEAPIFONEISINDICATEDYOUCANNOTCROSSARIVERORACHASMINTWOSMALLJUMPS";
			while(orignal.length() % size != 0)
				orignal += 'X';
			String encoded = encode(orignal, size, key);
			System.out.println("Size: " +size + " " + encoded);
			String decoded = new String(decode(encoded.toCharArray(), size, key));
			//System.out.println(decoded);
			if(!decoded.equals(orignal))
				System.out.println("FAILED");
			//Thread.sleep(200);
		}
	}
	
	public static String encode(String plainText, int size, int[] key) {
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
	
	public static char[] decode(char[] cipherText, int size, int[] key) {
		int[] inKey = new int[key.length];
		for(int c = 0; c < size; c++) for(int r = 0; r < size; r++) inKey[key[r * size + c] * size + c] = r;

		
		char[] tempText = new char[cipherText.length];
		char[] plainText = new char[cipherText.length];
		
		int squareMag = (int)Math.pow(size, 2);
		int noSquares = (int)Math.ceil(cipherText.length / (double)squareMag);
		
		int[] colInSquare = new int[noSquares];
		for(int i = 0; i < noSquares; i++) colInSquare[i] = size;
		if(cipherText.length % squareMag != 0) colInSquare[noSquares - 1] = (cipherText.length % squareMag) / size;
		
		
		for(int s = 0; s < noSquares; s++)
			for(int r = 0; r < size; r++)
				for(int c = 0; c < colInSquare[s]; c++)
					tempText[s * squareMag + c + colInSquare[s] * inKey[r * size + c % size]] = cipherText[s * squareMag + c * size + r];
		
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
		return encode(plainText, size, KeyGeneration.createSwagmanKey(size));
	}
}
