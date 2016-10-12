package nationalcipher.cipher.base.transposition;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;

/**
 * @author Alex Barter (10AS)
 */
public class RailFence implements IRandEncrypter {

	public static String encode(String plainText, int rows) {
		
		StringBuilder[] cipherText = new StringBuilder[rows];
		for(int i = 0; i < rows; ++i)
			cipherText[i] = new StringBuilder();
		
		int branchTotal = rows * 2 - 2;
		
		for(int i = 0; i < plainText.length(); ++i) {
			char character = plainText.charAt(i);
			int index_in_ite = i % branchTotal;
			if(index_in_ite < rows)
				cipherText[index_in_ite].append(character);
			else
				cipherText[rows - (index_in_ite - rows) - 2].append(character);
		}
		
		String last = "";
		
		for(StringBuilder text : cipherText)
			last += text.toString();
		return last;
	}
	
	public static char[] decode(char[] cipherText, int rows) {
		char[] plainText = new char[cipherText.length];
		
		int branchTotal = 2 * (rows - 1);
		int branchs = cipherText.length / branchTotal;
		int noUnassigned = cipherText.length - (branchs * branchTotal);
		
		int index = 0;
		for(int row = 1; row <= rows; row++) {
			if(index >= cipherText.length) break;
			
			int occurs = branchs; //Times a letter occurs in a row
			
			if(row > 1 && row < rows) occurs *= 2;

			if(noUnassigned >= row) {
				occurs += 1;
				
				if(row < rows && row + (rows - row) * 2 <= noUnassigned)
					occurs += 1;
			}
			
			for(int i = 0; i < occurs; i++) {
				int newIndex = 0;
				
				if(row > 1 && row < rows) {
					int branch = (int)(i / 2);
					newIndex = branch * branchTotal + row - 1;
					
					if(i % 2 == 1)
						newIndex += (rows - row) * 2;	
				}
				else
					newIndex = i * branchTotal + row - 1;
				
				plainText[newIndex] = cipherText[index];
				index++;
			}
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, RandomUtil.pickRandomInt(2, 15));
	}
}
