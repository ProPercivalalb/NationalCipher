package nationalcipher.cipher.base.substitution;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.tools.KeyGeneration;

public class Nicodemus implements IRandEncrypter {

	public static String encode(String plainText, String key, int blockHeight, VigenereType type) {
		
		String cipherText = "";
		
		int[] order = new int[key.length()];
		
		int p = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch) {
			int keyindex = key.indexOf(ch);
			if(keyindex != -1)
				order[p++] = keyindex;
		}
		
		int start_row = 0;
		int total_row = (int)Math.ceil(plainText.length() / (double)key.length());
		
		while(start_row < total_row) {
			int end_row = start_row + blockHeight >= total_row ? total_row : start_row + blockHeight;
			
			for(int i = 0; i < key.length(); i++) 
				for(int j = start_row; j < end_row; j++)
					if(j * key.length() + order[i] < plainText.length())
						cipherText += (char)type.encode((byte)plainText.charAt(j * key.length() + order[i]), (byte)key.charAt(i % key.length()));

			start_row = end_row;
		}
		return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String key, VigenereType type) {
		//Possible settings
		int READ_OFF = 5;

		byte[] order = new byte[key.length()];
		
		int q = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch)
			for(byte i = 0; i < order.length; i++)
				if(ch == key.charAt(i))
					order[q++] = i;
		
		int blocks = (int)Math.ceil((double)cipherText.length / (key.length() * READ_OFF));
		int blockSize = key.length() * READ_OFF;
		boolean complete = blocks * blockSize == cipherText.length;
		
		int index = 0;
		for(int b = 0; b < blocks; b++) {
			
			for(int r = 0; r < READ_OFF; r++) {
				for(int p = 0; p < key.length(); p++) {

					if(complete || blocks - 1 != b) {
						int row = index % key.length();
						plainText[order[p] - row + index++] = type.decode((byte)cipherText[b * blockSize + p * READ_OFF + r], (byte)key.charAt(order[p] % key.length()));
					}
					else {
						int charactersLeft = cipherText.length - b * blockSize;
						int lastRow = charactersLeft % key.length();
						int esitmate = (int)Math.floor(charactersLeft / key.length());
						index = 0;
						for(int i = 0; i < key.length(); i++) {
							int total = esitmate;
							
							if(lastRow > order[i]) 
								total += 1;
							
							for(int j = 0; j < total; j++) {
								byte place = order[i];
								plainText[b * blockSize + j * key.length() + place] = type.decode((byte)cipherText[b * blockSize + index + j], (byte)key.charAt(place % key.length()));
							}
							
							index += total;
						}
					}
				}
			}
		}
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(3, 10), RandomUtil.pickRandomInt(1, 10), RandomUtil.pickRandomElement(VigenereType.NORMAL_LIST));
	}
}
