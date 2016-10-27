package nationalcipher.cipher.base;

public class Nicodemus {

	public static char[] decodeBase(char[] cipherText, String key) {
		//Possible settings
		int READ_OFF = 5;
		
		
		char[] plainText = new char[cipherText.length];
		
		int[] order = new int[key.length()];
		
		int q = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch)
			for(int i = 0; i < order.length; i++)
				if(ch == key.charAt(i))
					order[q++] = i;
		
		int blocks = (int)Math.ceil((double)cipherText.length / (key.length() * READ_OFF));
		int blockSize = key.length() * READ_OFF;
		boolean complete = blocks * blockSize == cipherText.length;
		
		int index = 0;
		for(int b = 0; b < blocks; b++) {
			
			for(int r = 0; r < READ_OFF; r++) {
				for(int p = 0; p < key.length(); p++) {

					if(complete || blocks - 1 != b) {
						int row = index % key.length();
						plainText[order[p] - row + index++] = cipherText[b * blockSize + p * READ_OFF + r];

					}
					else {
						int charactersLeft = cipherText.length - b * blockSize;
						int lastRow = charactersLeft % key.length();
						int esitmate = (int)Math.floor(charactersLeft / key.length());
						index = 0;
						for(int i = 0; i < key.length(); i++) {
							int total = esitmate;
							
							if(lastRow > order[i]) 
								total += 1;
							
							for(int j = 0; j < total; j++) {
								int place = order[i];
								plainText[b * blockSize + j * key.length() + place] = cipherText[b * blockSize + index + j];
							}
							
							index += total;
						}
					}
					
			
				
				}
			}
		}
		return plainText;
	}
}
