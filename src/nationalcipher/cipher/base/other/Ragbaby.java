package nationalcipher.cipher.base.other;

import javalibrary.language.Languages;
import nationalcipher.cipher.decrypt.methods.Solution;

public class Ragbaby {

	public static void main(String[] args) {
		Languages.english.loadNGramData();
		String plainText = "WELCOME THERE ARE MANY".toUpperCase();
		String key = "PE5RSI9TA1NVG7B2C3D4F6H8J0KLMOQUWXYZ";
		String cipherText = encode(plainText, key);
		
		System.out.println(cipherText);
		
		byte[] output = new byte[cipherText.length()];
		System.out.println(new String(decode(cipherText.toCharArray(), output, key)));
		
		String noSpaces = cipherText.replaceAll(" ", "");
		System.out.println(new Solution(plainText.replaceAll(" ", "").getBytes(), Languages.english.getQuadgramData()).toString());
		byte[] outputNoSpaces = new byte[noSpaces.length()];
		attack(noSpaces, outputNoSpaces, 0, 1, key);
	}
	
	public static Solution best = Solution.WORST_SOLUTION;
	
	public static void attack(String text, byte[] output, int pos, int word, String key) {
		if(pos == text.length() - 1) {
			Solution solution = new Solution(output, Languages.english.getQuadgramData());
			if(solution.isResultBetter(best)) {
				best = solution;
				System.out.println(solution.toString());
			}
		}
		
		for(int i = pos + 1; i < Math.min(pos + 8, text.length()) + 1; i++) {
			int number = word;
			for(int j = pos; j < i; j++) {
				char character = text.charAt(j);
				
				output[j] = (byte)key.charAt(((key.indexOf(character) - number++) % key.length() + key.length()) % key.length());
			}
			
			attack(text, output, i, word + 1, key);
		}
	}

	public static String encode(String plainText, String key) {
		char[] cipherText = new char[plainText.length()];
		
		int word = 2;
		int number = 1;
		for(int i = 0; i < plainText.length(); i++) {
			char character = plainText.charAt(i);
			
			if(character == ' ') {
				cipherText[i] = ' ';
				number = word++;
			}
			else
				cipherText[i] = key.charAt((key.indexOf(character) + number++) % key.length());
		}
		
		return new String(cipherText);
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String key) {
		int word = 1;
		int number = word;
		for(int i = 0; i < cipherText.length; i++) {
			char character = cipherText[i];
			
			if(character == ' ') {
				plainText[i] = ' ';
				word++;
				number = word;
			}
			else
				plainText[i] = (byte)key.charAt(((key.indexOf(character) - number++) % key.length() + key.length()) % key.length());
		}
		
		return plainText;
	}
}
