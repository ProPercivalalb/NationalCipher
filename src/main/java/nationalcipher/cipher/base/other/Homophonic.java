package nationalcipher.cipher.base.other;

import java.util.ArrayList;
import java.util.List;

import javalibrary.math.MathUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Homophonic implements IRandEncrypter {
	
	public static String encode(String plainText, String key) {
		String cipherText = "";
		
		List<String> rows = new ArrayList<String>();
		
		for(int i = 0; i < 4; i++) {
			int charIndex = key.charAt(i) - 'A';
			
			if(charIndex >= 9) charIndex--;
			
			for(int no = 0; no < 25; no++) {
				String n = "" + (i * 25 + MathUtil.mod(no - charIndex, 25) + 1) % 100;
				if(n.length() < 2) n = "0" + n;
				rows.add(n);
			}
		}
		
		for(int i = 0; i < plainText.length(); i++) {
			int charIndex = plainText.charAt(i) - 'A';
			if(charIndex >= 9) charIndex--;
			
			cipherText += rows.get(RandomUtil.pickRandomInt(4) * 25 + charIndex);
		}
		
		return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, String key) {
		byte[] plainText = new byte[cipherText.length / 2];
		
		int[] rows = new int[100];
		String shortAlpha = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
		
		for(int i = 0; i < 4; i++) {
			int c = key.charAt(i) - 'A';
			
			if(c >= 8) c--;
			
			for(int no = 0; no < 25; no++) {

				rows[(i * 25 + MathUtil.mod(no - c, 25) + 1) % 100] = no;
			}
		}
		
		for(int i = 0; i < plainText.length; i++) {
			int col = rows[10 * (cipherText[i * 2] - '0') + (cipherText[i * 2 + 1] - '0')];
			plainText[i] = (byte)shortAlpha.charAt(col);
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(4));
	}
	
	@Override
	public int getDifficulty() {
		return 5;
	}
}
