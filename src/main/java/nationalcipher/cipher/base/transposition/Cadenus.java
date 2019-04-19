package nationalcipher.cipher.base.transposition;

import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Cadenus implements IRandEncrypter {
	
	public static String encode(String plainText, String key) {
		while(plainText.length() % (key.length() * 25) != 0)
			plainText += 'X';
		
		if(plainText.length() != key.length() * 25) {
			String combinedMulti = "";
			for(int i = 0; i < plainText.length() / (key.length() * 25); i++)
				combinedMulti += encode(plainText.substring(i * key.length() * 25, (i + 1) * key.length() * 25), key);
			return combinedMulti;
		}
		else {
			int keyLength = key.length();
	
			int[] order = new int[key.length()];
			
			int p = 0;
			for(char ch = 'A'; ch <= 'Z'; ++ch)
				for(int i = 0; i < order.length; i++)
					if(ch == key.charAt(i))
						order[i] = p++;
			
			//Creates grid
			char[] temp_grid = new char[plainText.length()];
	
			for(int j = 0; j < 25; j++) {
				for(int i = 0; i < keyLength; i++) {
					int newColumn = order[i];
					int newIndex = (j - charValue(key.charAt(i)) + 25) % 25;
					temp_grid[newIndex * keyLength + newColumn] = plainText.charAt(j * keyLength + i);
				}
			}
			return new String(temp_grid);
		}

	}
	
	public static byte[] decode(char[] cipherText, String key) {
	
		int[] order = new int[key.length()];
		
		int p = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch) {
			for(int i = 0; i < order.length; i++) {
				if(ch == key.charAt(i)) {
					order[p++] = i;
				}
			}
		}

		return decode(cipherText, key, order);
	}
	
	public static byte[] decode(char[] cipherText, String key, int[] order) {
		int keyLength = order.length;
		
		//Creates grid
		byte[] grid = new byte[cipherText.length];
				
		for(int j = 0; j < 25; j++) {
			for(int i = 0; i < keyLength; i++) {
				int newColumn = order[i];
				int newIndex = (j + charValue(key.charAt(newColumn))) % 25;
				grid[newIndex * keyLength + newColumn] = (byte)cipherText[j * keyLength + i];
			}
		}

		return grid;
	}
	
	public static int charValue(char character) {
		if(character >= 'W')
			return ('Z' - character + 1) % 25;
		return ('Z' - character) % 25;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 5));
	}
}
