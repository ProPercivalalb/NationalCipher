package nationalcipher.cipher;

import java.util.Arrays;

import javalibrary.math.MathUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Nicodemus implements IRandEncrypter {

	public static String encode(String plainText, String keyword) {
		String cipherText = "";
		
		int[] order = new int[keyword.length()];
		
		int p = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch) {
			int keyindex = keyword.indexOf(ch);
			if(keyindex != -1)
				order[p++] = keyindex;
		}
		
		int start_row = 0;
		int total_row = (int)Math.ceil(plainText.length() / (double)keyword.length());
		
		while(start_row < total_row) {
			int end_row = start_row + 5 >= total_row ? total_row : start_row + 5;
			
			for(int i = 0; i < keyword.length(); i++) 
				for(int j = start_row; j < end_row; j++)
					if(j * keyword.length() + order[i] < plainText.length())
						cipherText += (char)(MathUtil.mod(plainText.charAt(j * keyword.length() + order[i]) + keyword.charAt(order[i]), 26) + 'A');

			start_row = end_row;
		}
		return cipherText;
	}
	
	public static String decode(String cipherText, String keyword) {
		int[] order = new int[keyword.length()];
		
		char[] charArray = keyword.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[keyword.indexOf(charArray[i])] = i;

		char[] text = new char[cipherText.length()];
		
		int blockSize = keyword.length() * 5;
		int totalBlocks = (int)Math.floor(cipherText.length() / blockSize);
		int charactersDone = totalBlocks * blockSize;
		
		for(int i = 0; i < totalBlocks; i++) {
			for(int j = 0; j < blockSize; j++) {
				int place = order[(int)Math.floor(j / 5)];
				char c = cipherText.charAt(i * blockSize + j);
				text[i * blockSize + (j % 5) * keyword.length() + place] = (char)(MathUtil.mod(c - keyword.charAt(place), 26) + 'A');
			}
		}
		
		int charactersLeft = cipherText.length() - charactersDone;
		int lastRow = charactersLeft % keyword.length();
		int esitmate = (int)Math.floor(charactersLeft / keyword.length());
		
		int index = 0;
		for(int i = 0; i < keyword.length(); i++) {
			int total = esitmate;
			
			if(lastRow > order[i]) 
				total += 1;
			
			for(int j = 0; j < total; j++) {
				int place = order[i];
				char c = cipherText.charAt(charactersDone + index + j);
				text[charactersDone + j * keyword.length() + place] = (char)(MathUtil.mod(c - keyword.charAt(place), 26) + 'A');
			}
			
			index += total;
		}
		
		return new String(text);
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15));
	}
}
