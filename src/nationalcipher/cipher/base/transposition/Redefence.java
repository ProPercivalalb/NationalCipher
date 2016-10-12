package nationalcipher.cipher.base.transposition;

import nationalcipher.cipher.base.IRandEncrypter;

/**
 * @author Alex Barter (10AS)
 */
public class Redefence implements IRandEncrypter {

	public static String decode(String cipherText, String key) {
		
		int[] order = new int[key.length()];
		
		int p = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch) {
			int keyindex = key.indexOf(ch);
			if(keyindex != -1)
				order[p++] = keyindex + 1;
		}
		
		return decode(cipherText, order);
	}
	
	public static char[] decode(char[] cipherText, int[] order) {
		int rows = order.length;
		
		char[] plainText = new char[cipherText.length];
		
		int branchTotal = 2 * (rows - 1);
		int branchs = cipherText.length / branchTotal;
		int noUnassigned = cipherText.length - (branchs * branchTotal);
		
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
	
	public static String decode(String cipherText, int[] order) {
		int rows = order.length;
		
		char[] plainText = new char[cipherText.length()];
		int index = 0;
		int no_per_ite = rows * 2 - 2;
		double total = (double)cipherText.length() / no_per_ite;
		int total_ite = (int)Math.floor(total);
		int total_left = (int)((total - total_ite) * no_per_ite);
		
		for(int k = 0; k < rows; k++) {
			int c_row = order[k];
			
			if(index >= cipherText.length())
				break;
			
			int times = total_ite;
			if(c_row != 1 && c_row != rows)
				times *= 2;

			if(total_left >= c_row)
				times += 1;
			
			if(c_row != rows && rows - c_row <= total_left - rows)
				times += 1;
			
			for(int i = 0; i < times; i++) {
				int newIndex = 0;
				
				if(c_row == 1 || c_row == rows)
					newIndex = i * no_per_ite + c_row - 1;
				else {
					int x = (int)Math.floor(i / 2);
					newIndex = x * no_per_ite + c_row - 1;
					
					if(i % 2 == 1)
						newIndex += (rows - c_row) * 2;			
				}
				
				plainText[newIndex] = cipherText.charAt(index);
				index++;
			}
		}
		return new String(plainText);
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return null;
	}
}
