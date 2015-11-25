package nationalcipher.cipher;

public class NihilistTransposition {

	public static char[] decode(char[] cipherText, int[] order) {
		
		
		char[] plainText = new char[cipherText.length];
		
		int index = 0;
		int columns = order.length;
		
		int[] reversedOrder = new int[columns];
		for(int i = 0; i < columns; i++)
			reversedOrder[order[i]] = i;

		for(int row = 0; row < order.length; row++) {
			for(int column = 0; column < order.length; column++) {
				int trueColumn = reversedOrder[column];
				int trueRow = reversedOrder[row];
		
				plainText[trueRow * columns + trueColumn] = cipherText[index++];
			}
		}

		return plainText;
	}

}
