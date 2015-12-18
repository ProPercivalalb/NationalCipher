package nationalcipher;

import java.io.PrintWriter;
import java.util.Arrays;

import nationalcipher.cipher.Solitaire;
import nationalcipher.cipher.tools.Creator.VigenereAutoKey;

public class SolitaireKeyCreator {
	public static PrintWriter[] writer;
	public static void main(String[] args) throws Exception {
		writer = new PrintWriter[26];
		for(int i = 0; i < 26; i++)
			writer[i] = new PrintWriter("solitaire_key_mapping" + (char)(i + 'A') +".txt", "UTF-8");
		iterateVigenereAutoKey(6);
		for(int i = 0; i < 26; i++)
			writer[i].close();

	}

	
	public static void iterateVigenereAutoKey(int keyLength) {
		iterateVigenereAutoKey(keyLength, 0, "");
	}
	
	private static void iterateVigenereAutoKey(int no, int time, String key) {
		for(char i = 'A'; i <= 'Z'; i++) {
			String backup = key;
			backup += i;
			//System.out.println(backup);
			writer[backup.charAt(0) - 'A'].println(backup + " - " + Arrays.toString(Solitaire.createCardOrder(backup)));
			
			if(time + 1 >= no) {
				continue;
			}
			
			
			iterateVigenereAutoKey(no, time + 1, backup);
		}
	}
}
