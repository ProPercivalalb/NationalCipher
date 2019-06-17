package nationalcipher.cipher.base.transposition;

import java.util.Arrays;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Grille implements IRandEncrypter {

	public static void main(String[] args) {
		int size = 8;
		Integer[] possibleKey = KeyGeneration.createGrilleKey(size);
		
		/**
		String input = "1 8 10 12";
		String[] split = input.split("[, ]");
		int[] key = new int[split.length];
		int i = 0;
		for(String no : split)
			key[i++] = Integer.valueOf(no) - 1;
		possibleKey = new int[split.length];
		int count = 0;
		for(i = 0; i < size * size; i++)
			if(ArrayUtil.contains(key, i)) 
				possibleKey[count++] = i;**/
		
		KeyIterator.iterateGrille(o -> System.out.println(Arrays.toString(o)), size);
		
		String cipherText = encode("theturninggrille".toUpperCase(), size, possibleKey);
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
	public static String encode(String plainText, int size, Integer[] key) {
		while(plainText.length() % (key.length * 4) != 0) plainText += 'X';
		
		if(plainText.length() != key.length * 4) {
			String combined = "";
			for(int i = 0; i < plainText.length(); i += key.length * 4) {
				combined += encode(plainText.substring(i, i + key.length * 4), size, key);
			}
			
			return combined;
		}
		else {
			int middleIndex = (size * size - 1) / 2;
			int[] fullKey = createFullKey(key, size);
			
			char[] cipherText = new char[plainText.length()];
			for(int i = 0; i < plainText.length(); i++) {
				int index = fullKey[i];
				if(index > middleIndex && size % 2 == 1)
					index--;
				cipherText[index] = plainText.charAt(i);
			}
				
			return new String(cipherText);
		}
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, int size, Integer[] key) {
		int middleIndex = (size * size - 1) / 2;
		int[] fullKey = createFullKey(key, size);
	

		for(int i = 0; i < size * size; i++) {
			if(i == middleIndex && size % 2 == 1) continue;
			int index = i;
			if(index > middleIndex && size % 2 == 1)
				index--;
			plainText[ArrayUtil.indexOf(fullKey, i)] = (byte)cipherText[index];
		}
		
		return plainText;
	}
	
	/**
	 * Creates the full length key and orders each quadrant
	 * @return
	 */
	public static int[] createFullKey(Integer[] key, int size) {

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
		//Orders each quadrant
		for(int rot = 0; rot < 4; rot++) {
			int count = 0;
			for(int i = 0; i < size * size; i++)
				if(ArrayUtil.contains(normal, rot * key.length, (rot + 1) * key.length, i)) 
					ordered[rot * key.length + count++] = i;
		}
		
		return ordered;
	}


	@Override
	public String randomlyEncrypt(String plainText) {
		int size = RandomUtil.pickRandomInt(2, 8);
		return encode(plainText, size, KeyGeneration.createGrilleKey(size));
	}
	
	@Override
	public int getDifficulty() {
		return 5;
	}
}
