package nationalcipher.cipher.base.substitution;

import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class FourSquare implements IRandEncrypter {

	public static String encode(String plainText, String keysquare1, String keysquare2) {
		if(plainText.length() % 2 == 1)
			plainText += 'X';
		
	    String cipherText = "";
	    
	    String shortAlpha = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
	    
	    for(int i = 0; i < plainText.length(); i += 2){
	        char a = plainText.charAt(i);
	        if(a == 'J') a = 'I';
	        char b = plainText.charAt(i + 1);
	        if(b == 'J') b = 'I';
	        int aIndex = shortAlpha.indexOf(a);
	        int bIndex = shortAlpha.indexOf(b);
	        int aRow = (int)Math.floor(aIndex / 5);
	        int bRow = (int)Math.floor(bIndex / 5);
	        int aCol = aIndex % 5;
	        int bCol = bIndex % 5;
	        
	        cipherText += keysquare1.charAt(5 * aRow + bCol);
	        cipherText += keysquare2.charAt(5 * bRow + aCol);
	    }
	   
	    return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, byte[] result, String keysquare1, String keysquare2) {
	    String shortAlpha = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
	    
	    for(int i = 0; i < cipherText.length; i += 2){
	        char a = cipherText[i];
	        char b = cipherText[i + 1];
	        int aIndex = keysquare1.indexOf(a);
	        int bIndex = keysquare2.indexOf(b);
	        int aRow = aIndex / 5;
	        int bRow = bIndex / 5;
	        int aCol = aIndex % 5;
	        int bCol = bIndex % 5;
	        
	        result[i] = (byte)shortAlpha.charAt(5 * aRow + bCol);
	        result[i + 1] = (byte)shortAlpha.charAt(5 * bRow + aCol);
	    }
	   
	    return result;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey25(), KeyGeneration.createLongKey25());
	}
}
