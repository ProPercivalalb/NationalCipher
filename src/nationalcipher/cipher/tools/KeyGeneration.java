package nationalcipher.cipher.tools;

import java.util.List;

import javalibrary.math.ArrayUtil;
import javalibrary.util.ListUtil;
import javalibrary.util.RandomUtil;

public class KeyGeneration {

	private static char[] allPolluxChars = "X.-".toCharArray();
	
	private static char[] all27Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#".toCharArray();
	private static char[] all26Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static char[] all25Chars = "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray();
	
	public static String createShortKey26(int length) {
		char[] key = new char[length];
		
		for(int i = 0; i < length; i++)
			key[i] = RandomUtil.pickRandomChar(all26Chars);
		
		return new String(key);
	}
	
	public static String createLongKey25() {
		return createLongKeyUniversal(all25Chars);
	}
	
	public static String createLongKey26() {
		return createLongKeyUniversal(all26Chars);
	}
	
	public static String createLongKey27() {
		return createLongKeyUniversal(all27Chars);
	}
	
	private static String createLongKeyUniversal(char[] charList) {
		List<Character> characters = ListUtil.toList(charList);
		
		char[] key = new char[characters.size()];
		for(int i = 0; i < key.length; i++) {
			char rC = RandomUtil.pickRandomElement(characters);
			key[i] = rC;
			characters.remove((Character)rC);
		}

		return new String(key);
	}
	
	public static int[] createOrder(int length) {
		int[] order = ArrayUtil.range(0, length);

		ArrayUtil.shuffle(order);
		
		return order;
	}
	
	public static List<Integer> createOrderList(int length) {
		return ListUtil.toList(createOrder(length));
	}
	
	
	public static char[] createPolluxKey() {
		List<Character> characters = ListUtil.toList(allPolluxChars);
		
		char[] key = new char[10];
		int i = 0;
		
		for(; i < characters.size(); i++) {
			char rC = RandomUtil.pickRandomElement(characters);
			key[i] = rC;
			characters.remove((Character)rC);
		}
		
		for(; i < key.length; i++)
			key[i] = RandomUtil.pickRandomChar(allPolluxChars);
		
		ArrayUtil.shuffle(key);
		
		return key;
	}

	
	
	
	// Random key length generator
	
	
	public static String createShortKey26(int minLength, int maxLength) {
		int length = RandomUtil.pickRandomInt(minLength, maxLength);
		
		return createShortKey26(length);
	}
	
	
	
	
	
	
	

}
