package nationalcipher.cipher.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javalibrary.math.ArrayHelper;
import javalibrary.util.RandomUtil;

public class KeyGeneration {

	private static char[] all27Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#".toCharArray();
	private static char[] all26Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static char[] all25Chars = "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray();
	
	private static int pickRandomLength(int minLength, int maxLength) {
		return RandomUtil.pickRandomInt(maxLength - minLength + 1) + minLength;
	}
	
	public static String createKey(int minLength, int maxLength) {
		int length = pickRandomLength(minLength, maxLength);
		
		String key = "";
		
		for(int i = 0; i < length; i++)
			key += RandomUtil.pickRandomChar(all26Chars);
		
		return key;
	}
	

	public static int[] createOrder(int length) {
		int[] array = ArrayHelper.range(0, length);

		List<Integer> key2 = new ArrayList<Integer>();
		for(int ch : array) {
			key2.add(ch);
		}
		
		Collections.shuffle(key2);
		
		int[] key3 = new int[key2.size()];
		
		int index = 0;
		for(int ch : key2)
			key3[index++] = ch;
		return key3;
	}
	
	public static String createKeySquare5x5() {
		String keySquare = "";
		
		while(keySquare.length() != 25) {
			char ch = (char)((int)Math.floor(26 * Math.random()) + 'A');
			if(ch != 'J' && !keySquare.contains("" + ch))
				keySquare += ch;
		}
		
		return keySquare;
	}

	public static String createFullKey() {
		List<Character> characters = new ArrayList<Character>();
		for(char character : all26Chars)
			characters.add(character);
		
		String key = "";
		while(!characters.isEmpty()) {
			char rC = RandomUtil.pickRandomElement(characters);
			key += rC;
			characters.remove((Character)rC);
		}

		return key;
	}
	
	public static String createFullKeyWithHash() {
		List<Character> characters = new ArrayList<Character>();
		for(char character : all27Chars)
			characters.add(character);
		
		String key = "";
		while(!characters.isEmpty()) {
			char rC = RandomUtil.pickRandomElement(characters);
			key += rC;
			characters.remove((Character)rC);
		}

		return key;
	}

	public static char[] createPolluxKey() {
		List<Character> characters = new ArrayList<Character>(Arrays.asList('X', '.', '-'));
		String key = "";
		while(!characters.isEmpty()) {
			char rC = RandomUtil.pickRandomElement(characters);
			key += rC;
			characters.remove((Character)rC);
		}
		char[] r = new char[] {'X', '.', '-'};
		for(int i = 0; i < 7; i++) {
			key += RandomUtil.pickRandomChar(r);
		}
		List<Character> key2 = new ArrayList<Character>();
		for(char ch : key.toCharArray()) {
			key2.add(ch);
		}
		
		Collections.shuffle(key2);
		
		char[] key3 = new char[key2.size()];
		
		int index = 0;
		for(char ch : key2)
			key3[index++] = ch;
		return key3;
	}

}
