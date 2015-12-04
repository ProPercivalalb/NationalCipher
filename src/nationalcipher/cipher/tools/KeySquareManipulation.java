package nationalcipher.cipher.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javalibrary.util.RandomUtil;

/**
 * @author Alex Barter (10AS)
 */
public class KeySquareManipulation {

	private static Random rand = new Random();
	
	public static String exchange2letters(String keySquare){
		char[] chars = keySquare.toCharArray();
	    int i = rand.nextInt(100) % keySquare.length();
	    int j = rand.nextInt(100) % keySquare.length();
	    char temp = chars[i];
	    chars[i] = chars[j];
	    chars[j] = temp;
	    return new String(chars);
	}
	
	public static int[] modifyOrder(int[] order) {
	    int modification = RandomUtil.pickRandomInt(50);
	    
	    switch(modification) {
	      	case 0: return reverseOrder(order);
	        case 1: return cutOrder(order);      
	        default: 
	        	return exchangeOrder(order);
	    }
	}
	
	public static List<Integer> modifyOrder(List<Integer> list) {
		int i = rand.nextInt(list.size());
		int j = rand.nextInt(list.size());
		int temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
		return list;
	}
	
	public static int[] reverseOrder(int[] order) {
		int length = order.length;
		
		for(int i = 0; i < length / 2; i++) {
		    int temp = order[i];
		    order[i] = order[length - i - 1];
		    order[length - i - 1] = temp;
		}
		
		return order;
	}
	
	public static int[] cutOrder(int[] order) {
		int length = order.length;
		int cuttingPos = RandomUtil.pickRandomInt(1, length - 1);
		
		int[] newOrder = new int[length];
		System.arraycopy(order, 0, newOrder, length - cuttingPos, cuttingPos);
		System.arraycopy(order, cuttingPos, newOrder, 0, length - cuttingPos);
		return newOrder;
	}
	
	public static int[] exchangeOrder(int[] order){
	    int i = rand.nextInt(order.length);
	    int j = rand.nextInt(order.length);
	    int temp = order[i];
	    order[i] = order[j];
	    order[j] = temp;
	    return order;
	}
	
	public static char[] exchangeOrder(char[] order){
	    int i = rand.nextInt(100) % order.length;
	    int j = rand.nextInt(100) % order.length;
	    char temp = order[i];
	    order[i] = order[j];
	    order[j] = temp;
	    return order;
	}
	private static char[] list = new char[] {'.', '-', 'X'}; 
	public static char[] swapMorseIndex(char[] order) {
	    char i = list[rand.nextInt(3)];
	    order[rand.nextInt(order.length)] = i;
	    return order;
	}
	
	private static char[] list2 = new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'}; 
	public static String swapCharIndex(String order) {
	    char i = RandomUtil.pickRandomChar(list2);
	    char[] c = order.toCharArray();
	    c[rand.nextInt(order.length())] = i;
	    return new String(c);
	}
	
	public static String exchange2lettersWithKnows(String keySquare, String knows){
		char[] chars = keySquare.toCharArray();
	    int i = 0;
	    int j = 0;
	    while(knows.charAt((i = rand.nextInt(100) % keySquare.length())) != '*');
	    while(knows.charAt((j = rand.nextInt(100) % keySquare.length())) != '*');
	    char temp = chars[i];
	    chars[i] = chars[j];
	    chars[j] = temp;
	    return new String(chars);
	}
	
	public static String swap2rows(String keySquare){
		char[] chars = keySquare.toCharArray();
		int i = rand.nextInt(100) % 5;
	    int j = rand.nextInt(100) % 5;
	    char temp;
	    int k;
	    for(k = 0; k < 5; k++){
	        temp = chars[i * 5 + k];
	        chars[i * 5 + k] = chars[j * 5 + k];
	        chars[j * 5 + k] = temp;
	    }
	    return new String(chars);
	}

	public static String swap2cols(String keySquare){
		char[] chars = keySquare.toCharArray();
		int i = rand.nextInt(100) % 5;
	    int j = rand.nextInt(100) % 5;
	    char temp;
	    int k;
	    for(k = 0; k < 5;k++){
	        temp = chars[k * 5 + i];
	        chars[k * 5 + i] = chars[k * 5 + j];
	        chars[k * 5 + j] = temp;
	    }
	    return new String(chars);
	}
	
	public static String modifyKey(String keySquare){
		char[] chars = keySquare.toCharArray();
		int k, j;
	    int i = rand.nextInt(100) % 50;
	    switch(i){
	        case 0: return swap2rows(keySquare);
	        case 1: return swap2cols(keySquare);      
	        //Reverse the keySquare
	        case 2: for(k = 0; k < 25; k++) 
	        			chars[k] = keySquare.charAt(24-k);
	        		return new String(chars);
	        case 3: for(k = 0; k < 5; k++) for(j = 0; j < 5; j++) chars[k*5 + j] = keySquare.charAt((4-k)*5+j); // swap rows up-down
	        		return new String(chars);
	        case 4: for(k = 0; k < 5; k++) for(j = 0; j < 5; j++) chars[j*5 + k] = keySquare.charAt((4-j)*5+k); // swap cols left-right
	        		return new String(chars);
	        default: return exchange2letters(keySquare);
	    }
	}
	
	public static String generateRandKeySquare() {
		String keySquare = "";
		
		while(keySquare.length() != 25) {
			char ch = (char)((int)Math.floor(26 * Math.random()) + 'A');
			if(ch != 'J' && !keySquare.contains("" + ch))
				keySquare += ch;
		}
		
		return keySquare;
	}
	
	public static String generateRandKey() {
		char[] characters = new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		String keySquare = "";
		
		while(keySquare.length() != 26) {
			char ch = (char)((int)Math.floor(26 * Math.random()) + 'A');
			if(!keySquare.contains("" + ch))
				keySquare += ch;
		}
		
		return keySquare;
	}
	
	public static String generateRandTrifidKey(char nullChar) {
		List<Character> characters = new ArrayList<Character>(Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z', nullChar));
		String key = "";
		while(!characters.isEmpty()) {
			char rC = RandomUtil.pickRandomElement(characters);
			key += rC;
			characters.remove((Character)rC);
		}

		return key;
	}
}
