package nationalcipher.cipher.base.transposition;

import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.IRandEncrypter;

/**
 * @author Alex Barter (10AS)
 */
public class Redefence implements IRandEncrypter {

	public static byte[] decode(char[] cipherText, int[] order, int startingOffset) {
		int rows = order.length;
		
		byte[] plainText = new byte[cipherText.length];
		int ghostLength = cipherText.length + startingOffset;
		
		int branchTotal = 2 * (rows - 1);
		int branchs = ghostLength / branchTotal;
		int noUnassigned = ghostLength - (branchs * branchTotal);
		
		int index = 0;
		for(int k = 0; k < rows; k++) {
			if(index >= cipherText.length) break;
			int row = order[k] + 1;
			
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

			for(int i = 0; i < occurs; i++) {
				int newIndex = 0;
				
				if(row > 1 && row < rows) {
					int branch2 = i;
					if(startingOffset >= row) {
						branch2 += 1;
						if(row < rows && row + (rows - row) * 2 <= startingOffset)
							branch2 += 1;
					}
	
					int branch = (int)(branch2 / 2);
					newIndex = branch * branchTotal + row - 1 - startingOffset;
					if(branch2 % 2 == 1)
						newIndex += (rows - row) * 2;
					plainText[newIndex] = (byte)cipherText[index++];
				}
				else {
					int branch = i;
					if(startingOffset >= row)
						branch += 1;
					newIndex = branch * branchTotal + row - 1 - startingOffset;
					plainText[newIndex] = (byte)cipherText[index++];
				}
				
			}
		}
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return null;
	}
}
