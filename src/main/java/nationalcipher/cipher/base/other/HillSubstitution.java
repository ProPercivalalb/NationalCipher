package nationalcipher.cipher.base.other;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.math.matrics.Matrix;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class HillSubstitution implements IRandEncrypter {
	
	public static String encode(String plainText, String key, Matrix keyMatrix) {
		while(plainText.length() % keyMatrix.squareSize() != 0)
			plainText += 'X';
		return new String(encode(plainText.toCharArray(), key, keyMatrix));
	}
	
	public static char[] encode(char[] plainText, String key, Matrix keyMatrix) throws MatrixNotSquareException, MatrixNoInverse { 
	    keyMatrix.inverseMod(26); //Check keyMatrix has an inverse so can be decrypted
	    plainText = Keyword.encode(new String(plainText), key).toCharArray();

		int size = keyMatrix.squareSize();
		
	    char[] cipherText = new char[plainText.length]; 
	    
	    for(int i = 0; i < plainText.length; i += size){
	    	
	    	int[] let = new int[size];
	    	for(int j = 0; j < size; j++)
	    		let[j] = ((int)plainText[i + j] - 'A');
	    	
	    	Matrix plainMatrix = new Matrix(let, size, 1);
	    	Matrix cipherMatrix = keyMatrix.multiply(plainMatrix).modular(26);
	    	
	    	for(int j = 0; j < size; j++)
	    		cipherText[i + j] = (char)(cipherMatrix.data[j] + 'A');
	    		
	    }
	    
	    return cipherText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		do {
			try {
				return encode(plainText, KeyGeneration.createLongKey26(), KeyGeneration.createMatrix(RandomUtil.pickRandomInt(2, 3), 26));
			}
			catch(MatrixNoInverse noInverse) {}
		}
		while(true);
		
	}
	
	@Override
	public int getDifficulty() {
		return 8;
	}
}
