package nationalcipher.cipher.base.other;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.math.matrics.Matrix;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class HillExtended implements IRandEncrypter {
	

	public static String encode(String plainText, Matrix keyMatrix, Matrix extendedMatrix) {
		while(plainText.length() % keyMatrix.squareSize() != 0)
			plainText += 'X';
		return new String(encode(plainText.toCharArray(), keyMatrix, extendedMatrix));
	}

	public static char[] encode(char[] plainText, Matrix keyMatrix, Matrix extendedMatrix) throws MatrixNotSquareException, MatrixNoInverse { 
	    keyMatrix.inverseMod(26); //Check keyMatrix has an inverse so can be decrypted

		int size = keyMatrix.squareSize();
		
	    char[] cipherText = new char[plainText.length]; 
	    
	    for(int i = 0; i < plainText.length; i += size){
	    	
	    	int[] let = new int[size];
	    	for(int j = 0; j < size; j++)
	    		let[j] = ((int)plainText[i + j] - 'A');
	    	
	    	Matrix plainMatrix = new Matrix(let, size, 1);
	    	Matrix cipherMatrix = keyMatrix.multiply(plainMatrix).add(extendedMatrix).modular(26);
	    	
	    	for(int j = 0; j < size; j++)
	    		cipherText[i + j] = (char)(cipherMatrix.data[j] + 'A');
	    		
	    }
	    
	    return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, Matrix keyMatrix, Matrix extendedMatrix) throws MatrixNotSquareException, MatrixNoInverse { 
	    Matrix inverseMatrix = keyMatrix.inverseMod(26);

		int size = inverseMatrix.squareSize();
	    for(int i = 0; i < cipherText.length; i += size) {
	    	
	    	int[] let = new int[size];
	    	for(int j = 0; j < size; j++)
	    		let[j] = ((int)cipherText[i + j] - 'A');
	    	
	    	Matrix cipherMatrix = new Matrix(let, size, 1);
	    	Matrix plainMatrix = inverseMatrix.multiply(cipherMatrix).subtract(extendedMatrix).modular(26);
	    	
	    	for(int j = 0; j < size; j++)
	    		plainText[i + j] = (byte)(plainMatrix.data[j] + 'A');
	    		
	    }
	    
	    return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		do {
			try {
				int size = RandomUtil.pickRandomInt(2, 4);
				return encode(plainText, KeyGeneration.createMatrix(size, 26), KeyGeneration.createMatrix(size, 1, 26));
			}
			catch(MatrixNoInverse noInverse) {}
		}
		while(true);
		
	}
}
