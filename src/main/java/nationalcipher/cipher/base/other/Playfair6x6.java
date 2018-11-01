package nationalcipher.cipher.base.other;

public class Playfair6x6 {

	public static byte[] decode(char[] cipherText, byte[] plainText, String key) {
		int size = 6;
	    for(int i = 0; i < cipherText.length; i += 2) {
	        int i1 = key.indexOf(cipherText[i]);
	        int i2 = key.indexOf(cipherText[i + 1]);
	        int row1 = i1 / size;
	        int col1 = i1 % size;
	        int row2 = i2 / size;
	        int col2 = i2 % size;
	        
	        if(row1 == row2) {
	        	plainText[i] = (byte)key.charAt(row1 * size + (col1 + size - 1) % size);
	        	plainText[i + 1] = (byte)key.charAt(row2 * size + (col2 + size - 1) % size);
	        }
	        else if(col1 == col2) {
	        	plainText[i] = (byte)key.charAt(((row1 + size - 1) % size) * size + col1);
	        	plainText[i + 1] = (byte)key.charAt(((row2 + size - 1) % size) * size + col2);
	        }
	        else {
	        	plainText[i] = (byte)key.charAt(row1 * size + col2);
	            plainText[i + 1] = (byte)key.charAt(row2 * size + col1);
	        }
	    }
	    
	    return plainText;
	}
}
