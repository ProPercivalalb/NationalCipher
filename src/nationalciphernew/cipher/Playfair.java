package nationalciphernew.cipher;

import javalibrary.math.ArrayHelper;
import javalibrary.math.MathHelper;


/**
 * @author Alex Barter (10AS)
 */
public class Playfair {

	private static int KEY_SQUARE_SIZE = 5;
	
	public static String encode(char[] plainText, char[] keysquare) {
	    String cipherText = "";
	    
	    for(int i = 0; i < plainText.length(); i += 2){
	        char a = plainText[i);
	        char b = plainText[i + 1);
	        int i1 = keysquare.indexOf(a);
	        int i2 = keysquare.indexOf(b);
	        int row1 = (int)Math.floor(i1 / 5);
	        int col1 = i1 % KEY_SQUARE_SIZE;
	        int row2 = (int)Math.floor(i2 / 5);
	        int col2 = i2 % KEY_SQUARE_SIZE;
	        
	        char c, d;
	        
	        if(row1 == row2) {
	        	c = keysquare[row1 * 5 + MathHelper.mod(col1 + 1, 5));
	        	d = keysquare[row2 * 5 + MathHelper.mod(col2 + 1, 5));
	        }
	        else if(col1 == col2) {
	        	c = keysquare[MathHelper.mod(row1 + 1, 5) * 5 + col1);
	        	d = keysquare[MathHelper.mod(row2 + 1, 5) * 5 + col2);
	        }
	        else {
	            c = keysquare[row1 * 5 + col2);
	            d = keysquare[row2 * 5 + col1);
	        }
	        
	        cipherText += "" + c + "" + d;
	    }
	    return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String keysquare) {
		char[] plainText = new char[cipherText.length];
	    
	    for(int i = 0; i < cipherText.length; i += 2){
	        char a = cipherText[i];
	        char b = cipherText[i + 1];
	        int i1 = keysquare.indexOf(a);
	        int i2 = keysquare.indexOf(b);
	        int row1 = (int)Math.floor(i1 / KEY_SQUARE_SIZE);
	        int col1 = i1 % 5;
	        int row2 = (int)Math.floor(i2 / KEY_SQUARE_SIZE);
	        int col2 = i2 % 5;
	        
	        char c, d;
	        
	        if(row1 == row2) {
	        	plainText[i] = keysquare.charAt(row1 * 5 + MathHelper.mod(col1 - 1, 5));
	        	plainText[i + 1] = keysquare.charAt(row2 * 5 + MathHelper.mod(col2 - 1, 5));
	        }
	        else if(col1 == col2) {
	        	plainText[i] = keysquare.charAt((KEY_SQUARE_SIZE - 1 + row1) % KEY_SQUARE_SIZE * KEY_SQUARE_SIZE + col1);
	        	plainText[i + 1] = keysquare.charAt(MathHelper.mod(row2 - 1, 5) * 5 + col2);
	        }
	        else {
	        	plainText[i] = keysquare.charAt(row1 * 5 + col2);
	        	plainText[i + 1] = keysquare.charAt(row2 * 5 + col1);
	        }
	    }
	    return plainText;
	}
	
}
