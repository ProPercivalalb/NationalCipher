package nationalcipher.cipher.base.other;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Trifid implements IRandEncrypter {
	
	public static void main(String[] args) {
		System.out.println(encode("THEENEMYWILLFALLTONIGHT", "MERKATBCDFGHIJLNOPQSUVWXYZ#", 5));
	}
	
	public static String encode(String plainText, String keysquares, int period) {
		if(period == 0) period = plainText.length();
		int[] numberText = new int[plainText.length() * 3];
		for(int i = 0; i < plainText.length(); i++) {
			
			char a = plainText.charAt(i);
			
			int index = keysquares.indexOf(a);
			int tableNo = index / 9 + 1;
			int rowNo = (int)(index / 3) % 3 + 1;
			int colNo = index % 3 + 1;
			int blockBase = (int)(i / period) * (period * 3) + i % period;
			int min = Math.min(period, plainText.length() - (int)(i / period) * period);
			
			numberText[blockBase] = tableNo;
			numberText[blockBase + min] = rowNo;
			numberText[blockBase + min * 2] = colNo;
		}

		char[] cipherText = new char[plainText.length()];
		int index = 0;
		
		for(int i = 0; i < numberText.length; i += 3) {

			int a = numberText[i] - 1;
			int b = numberText[i + 1] - 1;
			int c = numberText[i + 2] - 1;
			cipherText[index++] = keysquares.charAt(a * 9 + b * 3 + c);
		}

		
		return new String(cipherText);
	}
	
	public static String decode(String cipherText, String keysquares, int period) {
		return new String(decode(cipherText.toCharArray(), new byte[cipherText.length()], new byte[cipherText.length() * 3], keysquares, period));
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, byte[] numberText, String keysquares, int period) {
		if(period == 0) period = cipherText.length;
		
		int blocks = (int)Math.ceil(cipherText.length / (double)period);
		
		int indexNo = 0;
		int index = 0;
		
		for(int b = 0; b < blocks; b++) {
			int chPass = b * period;
			int noPass = chPass * 3;
			int min = Math.min(period, cipherText.length - chPass);
			
			for(int f = 0; f < min; f++) {
				int index1 = keysquares.indexOf(cipherText[chPass + f]);
				
				numberText[indexNo++] = (byte)(index1 / 9);
				numberText[indexNo++] = (byte)((int)(index1 / 3) % 3);
				numberText[indexNo++] = (byte) (index1 % 3);
			}
			
			for(int f = 0; f < min; f++)
				plainText[index++] = (byte)keysquares.charAt(numberText[noPass + f] * 9 
													+ numberText[noPass + min + f] * 3 
													+ numberText[noPass + min * 2 + f]);
		}
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey27(), RandomUtil.pickRandomElement(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
	}
}
