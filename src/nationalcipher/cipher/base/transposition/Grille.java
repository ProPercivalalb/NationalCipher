package nationalcipher.cipher.base.transposition;

import java.util.Arrays;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;

public class Grille {

	public static void main(String[] args) {
		int size = 6;
		int[] possibleKey = generatePossibleGrilleKey(size);
		
		System.out.println(Arrays.toString(possibleKey));
		
		/**
		String input = "5 6 11 12 16 18 24 27 28 ";
		String[] split = input.split("[, ]");
		int[] key = new int[split.length];
		int i = 0;
		for(String no : split)
			key[i++] = Integer.valueOf(no) - 1;
		int[] keyOrdered = new int[split.length];
		int count = 0;
		for(i = 0; i < size * size; i++)
			if(ArrayUtil.contains(key, i)) 
				keyOrdered[count++] = i;**/
		String cipherText = encode("ISBRANCHEDINMYUPSTRICTLYREMEMBERSONG", size, possibleKey);
		System.out.println(cipherText);
		byte[] plainText = decode(cipherText.toCharArray(), new byte[cipherText.length()], size, possibleKey);
		System.out.println(new String(plainText));
	}
	
	/**
	 * 
	 * @param plainText
	 * @param size
	 * @param key Must be given in numerical order
	 * @return
	 */
	public static String encode(String plainText, int size, int[] key) {
		double halfSize = size / 2D;
		if((int)(Math.ceil(halfSize) * Math.floor(halfSize)) != key.length) return "ERROR";

		int[] normal = new int[key.length * 4];
		for(int i = 0; i < key.length; i++)
			normal[i] = key[i];

		for(int rot = 1; rot < 4; rot++) {
			for(int i = 0; i < key.length; i++) {
				int value = normal[(rot - 1) * key.length + i];
				int row = value / size;
				int col = value % size;
				normal[rot * key.length + i] = col * size + (size - row - 1);
			}
		}
		
		int[] ordered = new int[normal.length];
		//Orders each quater
		for(int rot = 0; rot < 4; rot++) {
			int count = 0;
			for(int i = 0; i < plainText.length(); i++)
				if(ArrayUtil.contains(normal, rot * key.length, (rot + 1) * key.length, i)) 
					ordered[rot * key.length + count++] = i;
		}
		
		System.out.println(Arrays.toString(ordered));
		
		char[] cipherText = new char[plainText.length()];
		
		if(size % 2 == 0) {
			for(int i = 0; i < plainText.length(); i++)
				cipherText[ordered[i]] = plainText.charAt(i);
		}
		else {
			int middle = (int)Math.floor(plainText.length() / 2D);
			for(int i = 0; i < plainText.length(); i++) {
				int index = ordered[i];
	
				if(index > middle) { 
					index -= 1;
				}
		
				cipherText[index] = plainText.charAt(i);
			
			}
		}
		
		return new String(cipherText);
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, int size, int[] key) {
		double halfSize = size / 2D;
		if((int)(Math.ceil(halfSize) * Math.floor(halfSize)) != key.length) return new byte[0];
		
		int[] normal = new int[key.length * 4];
		for(int i = 0; i < key.length; i++)
			normal[i] = key[i];

		for(int rot = 1; rot < 4; rot++) {
			for(int i = 0; i < key.length; i++) {
				int value = normal[(rot - 1) * key.length + i];
				int row = value / size;
				int col = value % size;
				normal[rot * key.length + i] = col * size + (size - row - 1);
			}
		}
		
		int[] ordered = new int[normal.length];
		//Orders each quater
		for(int rot = 0; rot < 4; rot++) {
			int count = 0;
			for(int i = 0; i < normal.length; i++)
				if(ArrayUtil.contains(normal, rot * key.length, (rot + 1) * key.length, i)) 
					ordered[rot * key.length + count++] = i;
		}
	

		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = (byte)cipherText[ordered[i]];
		
		return plainText;
	}
	
	public static int[] generatePossibleGrilleKey(int size) {
		double halfSize = size / 2D;
		
		int[] key = new int[(int)(Math.ceil(halfSize) * Math.floor(halfSize))];
		int count = 0;
		for(int j = 0; j < Math.ceil(size / 2D); j++) {
			for(int i = 0; i < size / 2; i++) {
				key[count++] = j * size + i;
			}
		}
		System.out.println(Arrays.toString(key));
		
		for(int i = 0; i < key.length; i++) {
			int quadrant = RandomUtil.pickRandomInt(4);
			int value = key[i];
			for(int rot = 0; rot < quadrant; rot++) {
				int row = value / size;
				int col = value % size;
				value = col * size + (size - row - 1);
			}
			key[i] = value;
		}

		return key;
	}
}
