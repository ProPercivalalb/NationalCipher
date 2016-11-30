package nationalcipher.cipher.base.other;

import javalibrary.math.MathUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;


/**
 * @author Alex Barter (10AS)
 */
public class Playfair implements IRandEncrypter {

	public static String encode(String plainText, String keysquare) {

		plainText = plainText.replaceAll("J", "I");
		do {
			boolean valid = true;

			for(int i = 0; i < plainText.length() && valid; i += 2) {

				char a = plainText.charAt(i);
				if(!(i + 1 < plainText.length())) {
					plainText += 'X';
					break;
				}
					
				char b = plainText.charAt(i + 1);
				if(a == b) {
					char nullChar = 'X';
					if(a == 'X')
						nullChar = 'Q';
					plainText = plainText.substring(0, i + 1) + nullChar + plainText.substring(i + 1, plainText.length());
					valid = false;
					break;
				}
			}
			
			if(valid)
				break;
		}
		while(true);
		
		
		if(plainText.length() % 2 == 1)
			plainText += 'X';
		
	    String cipherText = "";
	    
	    for(int i = 0; i < plainText.length(); i += 2){
	        char a = plainText.charAt(i);
	        char b = plainText.charAt(i + 1);
	        int i1 = keysquare.indexOf(a);
	        int i2 = keysquare.indexOf(b);
	        int row1 = (int)Math.floor(i1 / 5);
	        int col1 = i1 % 5;
	        int row2 = (int)Math.floor(i2 / 5);
	        int col2 = i2 % 5;
	        
	        char c, d;
	        
	        if(row1 == row2) {
	        	c = keysquare.charAt(row1 * 5 + MathUtil.mod(col1 + 1, 5));
	        	d = keysquare.charAt(row2 * 5 + MathUtil.mod(col2 + 1, 5));
	        }
	        else if(col1 == col2) {
	        	c = keysquare.charAt(MathUtil.mod(row1 + 1, 5) * 5 + col1);
	        	d = keysquare.charAt(MathUtil.mod(row2 + 1, 5) * 5 + col2);
	        }
	        else {
	            c = keysquare.charAt(row1 * 5 + col2);
	            d = keysquare.charAt(row2 * 5 + col1);
	        }
	        
	        cipherText += "" + c + "" + d;
	    }
	    return cipherText;
	}
	
	//Double letter 1 down to right
	public static byte[] decode(char[] cipherText, byte[] plainText, String keysquare) {
	    for(int i = 0; i < cipherText.length; i += 2) {
	        int i1 = keysquare.indexOf(cipherText[i]);
	        int i2 = keysquare.indexOf(cipherText[i + 1]);
	        int row1 = i1 / 5;
	        int col1 = i1 % 5;
	        int row2 = i2 / 5;
	        int col2 = i2 % 5;
	        
	        if(row1 == row2) {
	        	plainText[i] = (byte)keysquare.charAt(row1 * 5 + (col1 + 5 - 1) % 5);
	        	plainText[i + 1] = (byte)keysquare.charAt(row2 * 5 + (col2 + 5 - 1) % 5);
	        }
	        else if(col1 == col2) {
	        	plainText[i] = (byte)keysquare.charAt(((row1 + 5 - 1) % 5) * 5 + col1);
	        	plainText[i + 1] = (byte)keysquare.charAt(((row2 + 5 - 1) % 5) * 5 + col2);
	        }
	        else {
	        	plainText[i] = (byte)keysquare.charAt(row1 * 5 + col2);
	            plainText[i + 1] = (byte)keysquare.charAt(row2 * 5 + col1);
	        }
	    }
	    
	    return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey25());
	}
	
}
