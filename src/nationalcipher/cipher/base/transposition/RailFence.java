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
	
	public static char[] decode(char[] cipherText, int rows, int startingOffset) {
		char[] plainText = new char[cipherText.length];
		int ghostLength = cipherText.length + startingOffset;
		
		int branchTotal = 2 * (rows - 1);
		int branchs = ghostLength / branchTotal;
		int noUnassigned = ghostLength - (branchs * branchTotal);
		System.out.println(rows + " " + startingOffset);
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
			
			if(startingOffset >= row) {
				occurs -= 1;
				if(row < rows && row + (rows - row) * 2 <= startingOffset)
					occurs -= 1;
			}
			System.out.println("Occurs: " + occurs);

			for(int i = 0; i < occurs; i++) {
				int newIndex = 0;
				
				if(row > 1 && row < rows) {
					int branch2 = i;
					if(startingOffset >= row) {
						System.out.println("ADD");
						branch2 += 1;
					}
	
					int branch = (int)(branch2 / 2);
					newIndex = branch * branchTotal + row - 1 - startingOffset + (row == 3 ? 3 : 0);
					System.out.println("" + newIndex);
					if(branch2 % 2 == 1)
						newIndex += (rows - row) * 2;
					System.out.println(plainText);
					plainText[newIndex] = cipherText[index++];
				}
				else {
					int branch = i;
					if(startingOffset >= row)
						branch += 1;
					newIndex = branch * branchTotal + row - 1 - startingOffset;
					System.out.println(plainText);
					plainText[newIndex] = cipherText[index++];
				}
				
			}
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, RandomUtil.pickRandomInt(2, 15));
	}
}
