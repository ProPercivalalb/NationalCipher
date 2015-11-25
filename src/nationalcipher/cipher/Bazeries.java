package nationalcipher.cipher;

import javalibrary.string.NumberString;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;

public class Bazeries implements IRandEncrypter {

	public static String encode(String plainText, int numberKey) {
		String alphabetSquare = "AFLQVBGMRWCHNSXDIOTYEKPUZ";
		
		String numberSquare = "";
		for(char j : NumberString.convert(numberKey).toCharArray())
			if(numberSquare.indexOf(j) == -1)
				numberSquare += j;
		
		for(char j : "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray())
			if(numberSquare.indexOf(j) == -1)
				numberSquare += j;
		
		
		String s = "" + numberKey;
		String cipherText = "";
		
		int textPos = 0;
		int count = 0;
		int split = s.charAt(0) - '0';
		while(true) {
			for(int j = textPos + split - 1; j >= textPos; --j) {
				if(j < plainText.length()) {
					char c = plainText.charAt(j);
					if(c == 'J') c = 'I';
					cipherText += numberSquare.charAt(alphabetSquare.indexOf(c));
				}
			}
			if(textPos + split >= plainText.length())
				break;
			
			textPos += split;
			count += 1;
			split = s.charAt(count % s.length()) - '0';
		}
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, int numberKey) {
		String alphabetSquare = "AFLQVBGMRWCHNSXDIOTYEKPUZ";
		
		String numberSquare = "";
		for(char j : NumberString.convert(numberKey).toCharArray())
			if(numberSquare.indexOf(j) == -1)
				numberSquare += j;
		
		for(char j : "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray())
			if(numberSquare.indexOf(j) == -1)
				numberSquare += j;
		
		String s = "" + numberKey;
		String plainText = "";
		
		int textPos = 0;
		int count = 0;
		int split = s.charAt(0) - '0';
		while(true) {
			
			for(int j = textPos + split - 1; j >= textPos; --j) {
				if(j < cipherText.length) {
					char c = cipherText[j];
					if(c == 'J') c = 'I';
					plainText += alphabetSquare.charAt(numberSquare.indexOf(c));
				}
			}
			if(textPos + split >= cipherText.length)
				break;
			
			textPos += split;
			count += 1;
			split = s.charAt(count % s.length()) - '0';
		}
		return plainText.toCharArray();
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, RandomUtil.pickRandomInt(1000000));
	}
}
