package nationalcipher.cipher;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.math.matrics.Matrix;
import nationalcipher.cipher.manage.IRandEncrypter;

/**
 * @author Alex Barter (10AS)
 */
public class Hill {

	public static char[] decode(char[] cipherText, Matrix keyMatrix) throws MatrixNotSquareException, MatrixNoInverse { 
	    Matrix inverseMatrix;
		inverseMatrix = keyMatrix.inverseMod(26);
		
		int size = inverseMatrix.size();
	    char[] plainText = new char[cipherText.length]; 
	    for(int i = 0; i < cipherText.length; i += size){
	    	
	    	int[][] let = new int[size][1];
	    	for(int j = 0; j < size; j++)
	    		let[j][0] = ((int)cipherText[i + j] - 'A');
	    	
	    	Matrix cipherMatrix = new Matrix(let);
	    	Matrix plainMatrix = inverseMatrix.multiply(cipherMatrix).modular(26);
	    	
	    	for(int j = 0; j < size; j++)
	    		plainText[i + j] = (char)(plainMatrix.matrix[j][0] + 'A');
	    		
	    }
	    
	    return plainText;
	}
}
