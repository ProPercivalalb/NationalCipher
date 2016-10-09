package nationalcipher.cipher;

import java.util.stream.IntStream;

import javalibrary.lib.Timer;
import javalibrary.math.MathUtil;
import javalibrary.math.Units.Time;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;

public class Caesar implements IRandEncrypter {

	public static String encode(String plainText, int shift) {
		char[] charArray = plainText.toCharArray();
		
		String cipherText = "";
		
		//Runs through all the characters from the array
		for(char ch : charArray) {
			
			if(!Character.isAlphabetic(ch))
				cipherText += ch;
			else {
				char newLetter = (char)(MathUtil.wrap(shift + ch - 'A', 0, 26) + 'A');
				cipherText += newLetter;
			}
		}
		
		return cipherText;
	}
	
	public static void main(String[] args) {
		String s = "TESTINGTHEEFFECTIVENESSOFBYTEANDINTANDCHARARRAYS";
		int len = s.length();
		int no = 100000;
	
			char[] charArray = s.toCharArray();

		double total = 0.0D;
		Timer t = new Timer();
		for(int j = 0; j < 100; j++) {
			for(int i = 0; i < no; i++)
			ConjugatedBifid.decode(charArray, "ABCDEFGHIKLMNOPQRSTUVWXYZ", "ABCDEFGHIKLMNOPQRSTUVWXYZ", 2);
			total += t.getTimeRunning(Time.MILLISECOND);
			//t.displayTime();
			t.restart();
		}
		
		System.out.println("Average: " + (total / 100.0D));
		
		

		//for(int i = 0; i < no; i++)
		//	decode(charArray, 6);
		
		
		//t.displayTime();
		//t.restart();
	}
	
	public static char[] decode(char[] cipherText, int shift) {
		char[] plainText = new char[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = (char)((26 + cipherText[i] - shift - 'A') % 26 + 'A');
		
		return plainText;
	}
	
	public static byte[] decode(byte[] cipherText, int len, int shift) {
		//byte[] plainText = new byte[cipherText.length];
		
		for(int i = 0; i < len; i++)
			cipherText[i] = (byte)((26 + cipherText[i] - shift - 65) % 26 + 65);
		
		return cipherText;
	}
	
	public static int[] decode(int[] cipherText, int shift) {
		int[] plainText = new int[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = ((26 + cipherText[i] - shift - 'A') % 26 + 'A');
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, RandomUtil.pickRandomInt(26));
	}
}
