package nationalcipher.cipher.base;

import java.util.Arrays;

public class Quagmire {

	public static String encode(String plainText, String topKey, String gridKey, String indicatorKey, char indicatorBelow) {
		String cipherText = "";
		
		String[] keyAlpha = new String[indicatorKey.length()];
		Arrays.fill(keyAlpha, "");
		
		for(int i = 0; i < indicatorKey.length(); i++)
			for(int k = 0; k < 26; k++) 
				keyAlpha[i] += gridKey.charAt((26 - topKey.indexOf(indicatorBelow) + gridKey.indexOf(indicatorKey.charAt(i)) + k) % 26);
		
		for(int i = 0; i < keyAlpha.length; i++)
			System.out.println(keyAlpha[i]);
		System.out.println("");
		
		for(int i = 0; i < plainText.length(); i++) {
			cipherText += keyAlpha[i % indicatorKey.length()].charAt(topKey.indexOf(plainText.charAt(i)));
		}
			
		return cipherText;
	}
	
	
	//Assuming the topKey is the ABCDEFGHIJKLMNPQRSTUVWXYZ
	public static byte[] decode(char[] cipherText, String gridKey, String indicatorKey, char indicatorBelow) {
		byte[] plainText = new byte[cipherText.length];
		int indicatorIndex = indicatorBelow - 'A';
		
		int[][] keyAlpha = new int[indicatorKey.length()][26];
		
		for(int i = 0; i < indicatorKey.length(); i++)
			for(int k = 0; k < 26; k++) 
				keyAlpha[i][gridKey.charAt((26 - indicatorIndex + gridKey.indexOf(indicatorKey.charAt(i)) + k) % 26) - 'A'] = k;
		
		for(int i = 0; i < cipherText.length; i++) {
			plainText[i] = (byte)(keyAlpha[i % indicatorKey.length()][cipherText[i] - 'A'] + 'A');
		}
		
		return plainText;
	}
	
	public static byte[] decode(char[] cipherText, String topKey, String gridKey, String indicatorKey, char indicatorBelow) {
		byte[] plainText = new byte[cipherText.length];
		int indicatorIndex = topKey.indexOf(indicatorBelow);
		
		int[][] keyAlpha = new int[indicatorKey.length()][26];
		
		for(int i = 0; i < indicatorKey.length(); i++)
			for(int k = 0; k < 26; k++) 
				keyAlpha[i][gridKey.charAt((26 - indicatorIndex + gridKey.indexOf(indicatorKey.charAt(i)) + k) % 26) - 'A'] += k;
		
		for(int i = 0; i < cipherText.length; i++) {
			plainText[i] = (byte)topKey.charAt(keyAlpha[i % indicatorKey.length()][cipherText[i] - 'A']);
		}
		
		return plainText;
	}
}
