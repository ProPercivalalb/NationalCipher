package nationalcipher.cipher.base.transposition;

import java.util.Arrays;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

/**
 * @author Alex Barter (10AS)
 */
public class Redefence implements IRandEncrypter {

	public static byte[] decode(char[] cipherText, int[] order, int startingRail) {
		int rows = order.length;
		
		byte[] plainText = new byte[cipherText.length];
		int ghostLength = cipherText.length + startingRail;
		
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
			
			if(startingRail >= row) {
				occurs -= 1;
				if(row < rows && row + (rows - row) * 2 <= startingRail)
					occurs -= 1;
			}

			for(int i = 0; i < occurs; i++) {
				int newIndex = 0;
				
				if(row > 1 && row < rows) {
					int branch2 = i;
					if(startingRail >= row) {
						branch2 += 1;
						if(row < rows && row + (rows - row) * 2 <= startingRail)
							branch2 += 1;
					}
	
					int branch = (int)(branch2 / 2);
					newIndex = branch * branchTotal + row - 1 - startingRail;
					if(branch2 % 2 == 1)
						newIndex += (rows - row) * 2;
					plainText[newIndex] = (byte)cipherText[index++];
				}
				else {
					int branch = i;
					if(startingRail >= row)
						branch += 1;
					newIndex = branch * branchTotal + row - 1 - startingRail;
					plainText[newIndex] = (byte)cipherText[index++];
				}
				
			}
		}
		return plainText;
	}
	
	public static String encode(String plainText, int[] order, int startingRail) {
		System.out.println(Arrays.toString(order) + " " + startingRail);
		int rows = order.length;
		
		StringBuilder[] cipherText = new StringBuilder[rows];
		for(int i = 0; i < rows; ++i)
			cipherText[i] = new StringBuilder();
		
		int branchTotal = rows * 2 - 2;
		
		for(int i = 0; i < plainText.length(); ++i) {
			char character = plainText.charAt(i);
			int index_in_ite = (i + startingRail) % branchTotal;
			if(index_in_ite < rows)
				cipherText[index_in_ite].append(character);
			else
				cipherText[rows - (index_in_ite - rows) - 2].append(character);
		}
		
		String last = "";
		
		for(int o : order)
			last += cipherText[o].toString();
		return last;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		int[] order = KeyGeneration.createOrder(2, 7);
		return encode(plainText, order, RandomUtil.pickRandomInt((order.length - 1) * 2));
	}
}
