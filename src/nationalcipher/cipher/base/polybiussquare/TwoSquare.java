package nationalcipher.cipher.base.polybiussquare;

import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class TwoSquare implements IRandEncrypter {

	public static String encode(String plainText, String keysquare1, String keysquare2) {
		if(plainText.length() % 2 == 1)
			plainText += 'X';
		
	    String cipherText = "";
	    
	    for(int i = 0; i < plainText.length(); i += 2){
	        char a = plainText.charAt(i);
	        char b = plainText.charAt(i + 1);
	        int aIndex = keysquare1.indexOf(a);
	        int bIndex = keysquare2.indexOf(b);
	        int aRow = (int)Math.floor(aIndex / 5);
	        int bRow = (int)Math.floor(bIndex / 5);
	        int aCol = aIndex % 5;
	        int bCol = bIndex % 5;
	        
	        if(aRow == bRow)
	        	cipherText += "" + b + "" + a;
	        else {
		        cipherText += keysquare2.charAt(5 * aRow + bCol);
	 	        cipherText += keysquare1.charAt(5 * bRow + aCol);
	        }
	    }
	   
	    return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String keysquare1, String keysquare2) {
	    char[] plainText = new char[cipherText.length];
	    
	    for(int i = 0; i < cipherText.length; i += 2){
	        char a = cipherText[i];
	        char b = cipherText[i + 1];
	        int aIndex = keysquare2.indexOf(a);
	        int bIndex = keysquare1.indexOf(b);
	        int aRow = (int)Math.floor(aIndex / 5);
	        int bRow = (int)Math.floor(bIndex / 5);
	        int aCol = aIndex % 5;
	        int bCol = bIndex % 5;
	        
	        if(aRow == bRow) {
	        	plainText[i] = b;
	        	plainText[i + 1] = a;
	        }
	        else {
	        	plainText[i] = keysquare1.charAt(5 * aRow + bCol);
	        	plainText[i + 1] = keysquare2.charAt(5 * bRow + aCol);

	        }
	    }
	   
	    return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey25(), KeyGeneration.createLongKey25());
	}
}
