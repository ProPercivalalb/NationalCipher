package nationalcipher.cipher.base.other;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.math.matrics.Matrix;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

/**
 * @author Alex Barter (10AS)
 */
public class Hill implements IRandEncrypter {

	public static void main(String[] args) {
		Matrix key = new Matrix(new int[] {3, 3, 3, 2}, 2, 2);
		
		String cipherText = encode("HARRYICRACKEDTHATLASTMESSAGEFORMYSELFANDNOTICEDSOMETHINGREALLYODDTHETEXTSAIDITWASENCRYPTEDUSINGACOLUMNTRANSPOSITIONWITHKEYWORDSEABIRDBUTITWASENCIPHEREDUSINGARAILFENCECIPHERICANONLYASSUMETHATTHETEXTWERETRIEVEDWASANARCHIVEOFTHEORIGINALMESSAGEREENCRYPTEDFORSAFETYWHOEVERTHEFLAGDAYASSOCIATESARETHEYHAVEAPRETTYSOPHISTICATEDOPERATIONIFTHEYAREFILINGMESSAGESLIKETHISMORELIKEONEOFTHEMAJORTERRORISTGROUPSTHANTHEUSUALHACKERCOLLECTIVETHETECHGUYSTOOKALOOKATTHEAERIALFROMTHEBOATANDTHEYTELLMETHATITISADRAGWIREUSUALLYUSEDTOCOMMUNICATEWITHASUBMARINEWHENSUBMERGEDITCARRIEDANACOUSTICTRANSDUCERARRAYASWELLASASHORTWAVETRANSMITTERANDLISTENINGGEARONETHINGTHATPUZZLESMENOWISWHYWEWEREALLOWEDTOFINDTHESHIPFLOATINGATALLSURELYTHEYMUSTHAVEPLANNEDTOSINKHERUSINGTHESCUTTLINGEQUIPMENTOTHERWISEWHATWASITFORTHEYSEEMTOOSMARTTOLEAVEITFLOATINGFORUSTOFINDANYTHOUGHTSMARKPSJUSTBEFOREISENTTHISTHECIPHERCLERKCAMEINWITHADECRYPTOFTHEATTACHEDCOLUMNARTRANSPOSITIONKEYWORDHASLENGTHSIXTHINKITANSWERSSOMEOFOURQUESTIONSABOUTTHENAUTILUSSYSTEM", key);
		System.out.println(KeyGeneration.createLongKey26());
		System.out.println("CipherText: " + cipherText);
		
		
		System.out.println("PlainText: " + new String(decode(cipherText.toCharArray(), new byte[cipherText.length()], key)));
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, Matrix keyMatrix) throws MatrixNotSquareException, MatrixNoInverse { 
	    Matrix inverseMatrix = keyMatrix.inverseMod(26);

		int size = inverseMatrix.squareSize();
	    for(int i = 0; i < cipherText.length; i += size) {
	    	
	    	int[] let = new int[size];
	    	for(int j = 0; j < size; j++)
	    		let[j] = ((int)cipherText[i + j] - 'A');
	    	
	    	Matrix cipherMatrix = new Matrix(let, size, 1);
	    	Matrix plainMatrix = inverseMatrix.multiply(cipherMatrix).modular(26);
	    	
	    	for(int j = 0; j < size; j++)
	    		plainText[i + j] = (byte)(plainMatrix.data[j] + 'A');
	    		
	    }
	    
	    return plainText;
	}
	
	
	public static String encode(String plainText, Matrix keyMatrix) {
		while(plainText.length() % keyMatrix.squareSize() != 0)
			plainText += 'X';
		return new String(encode(plainText.toCharArray(), keyMatrix));
	}

	
	public static char[] encode(char[] plainText, Matrix keyMatrix) throws MatrixNotSquareException, MatrixNoInverse { 
	    keyMatrix.inverseMod(26); //Check keyMatrix has an inverse so can be decrypted

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
				return encode(plainText, KeyGeneration.createMatrix(RandomUtil.pickRandomInt(2, 2), 26));
			}
			catch(MatrixNoInverse noInverse) {}
		}
		while(true);
		
	}
}
