package nationalcipher.cipher.base.other;

import java.util.Arrays;

import nationalcipher.cipher.base.transposition.Columnar;

public class ADFGVX {

	public static String encode(String plainText, String keysquare, int[] order, String adfgvx, boolean defaultRead) {
		String cipherText = "";
		
		for(int i = 0; i < plainText.length(); i++) {
			char c = plainText.charAt(i);
			if(c == 'J') c = 'I';
			
			int charIndex = keysquare.indexOf(c);
			int row = (int)Math.floor(charIndex / adfgvx.length());
			int column = charIndex % adfgvx.length();
			
			cipherText += adfgvx.charAt(row);
			cipherText += adfgvx.charAt(column);
		}
		
		return Columnar.encode(cipherText, order, defaultRead);
	}
	
	public static String decode(String cipherText, String keysquare, String key, boolean defaultRead) {
		return decode(cipherText, keysquare, key, "ADFGVX", defaultRead);
	}
	
	public static char[] decode(char[] cipherText, String keysquare, int[] order, boolean defaultRead) {
		return decode(cipherText, keysquare, order, "ADFGVX", defaultRead);
	}
	
	public static String decode(String cipherText, String keysquare, String key, String adfgvx, boolean defaultRead) {
		int[] order = new int[key.length()];
		
		char[] charArray = key.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[key.indexOf(charArray[i])] = i;
		
		return new String(decode(cipherText.toCharArray(), keysquare, order, adfgvx, defaultRead));
	}
	
	public static char[] decode(char[] cipherText, String keysquare, int[] order, String adfgvx, boolean defaultRead) {
		return decodeTransformed(Columnar.decode(cipherText, order, defaultRead), keysquare, adfgvx);
	}
	
	public static char[] decodeTransformed(char[] untransformedText, String keysquare) {
		return decodeTransformed(untransformedText, keysquare, "ADFGVX");
	}

	public static char[] decodeTransformed(char[] untransformedText, String keysquare, String adfgvx) {
		char[] plainText = new char[untransformedText.length / 2];
		
		for(int i = 0; i < untransformedText.length; i += 2) {
			char c1 = untransformedText[i];
			char c2 = untransformedText[i + 1];
			
			int row = adfgvx.indexOf(c1);
			int column = adfgvx.indexOf(c2);
			if(row != -1 && column != -1)
				plainText[i / 2] = keysquare.charAt(row * adfgvx.length() + column);
		}
		
		return plainText;
	}	
}
