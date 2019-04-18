package nationalcipher.cipher.base.other;

import java.util.Arrays;

import javalibrary.util.ArrayUtil;

public class Hutton {

	public static void main(String[] args) {
		String toSolve = "WQQOZAYKTCUJACPCSZZJGRMFJRAALRVMYJACGYOZUDXYUPNIKVIVBMZKFHBCVOKDCGBCXJJAVVQYUQTWMRYJECPFWTFLQDNTSKJKCKEQMYGLKWLCCUCGLFWDLKOATUNQGDGGYLUPZRWBSUTMTOUIISWYXHYWEJJIWCXZGWAKXOFFRQSGUFDVBBJHLRNEIEJDEBFXNJWRNSRZRPBDXVWNPMMIHEFAXGCZVJYPRXRKZHJIXJOWKCWHUWQDTQMZVTSCUUABXBIFUEDEBNXGBHHHUXCDBJUZNVRCAASWFFESDZYORKHUWUNVBKXVUJMDMXMYCCMAZTOMPMINSORYYODDGOOYYYXNBJWJVFGYKXKYEMRCLXLZZRZUNIBKJTOCSNEAGBVTXJHQGXDLWQBTEJTGKBKOD";
		String plainText = "THEENCRYPTIONPROCEDUREISASFOLLOWSWRITEDOWNTHEPLAINTEXTWITHOUTSPACESBELOWITWRITEAKEYWORDREPEATEDLYTILLTHENUMBEROFLETTERSMATCHESTHENUMBERINTHEPLAINTEXTWRITEDOWNANOTHERKEYWORDANDFOLLOWITWITHTHEREMAININGLETTERSOFTHEALPHABETINORDERNOTETHEFIRSTKEYWORDMUSTNOTCONTAINTHELETTERZDUPLICAT";
		String keyword1 = "WE";
		String keyword2 = "PE";
		
		String cipherText = encode(plainText, keyword1, keyword2);
		System.out.println(cipherText);
		
		plainText = new String(decode(cipherText.toCharArray(), keyword1, keyword2));
		System.out.println(plainText);
	}
	
	public static String encode(String plainText, String keyword1, String keyword2) {
		
		char[] firstKey = keyword1.toCharArray();
		char[] secondKey = new char[26];
		int d = 0;
		for(d = 0; d < keyword2.length(); d++) {
			secondKey[d] = keyword2.charAt(d);
		}
		
		for(char alpha = 'A'; alpha <= 'Z'; alpha++) {
			if(!ArrayUtil.contains(secondKey, 0, d, alpha))
				secondKey[d++] = alpha;
		}
		
		System.out.println(Arrays.toString(secondKey));
		
		char[] cipherText = new char[plainText.length()];
		
		for(int i = 0; i < plainText.length(); i++) {
			int keyIndex = ArrayUtil.indexOf(secondKey, plainText.charAt(i));
			int newKeyIndex = (keyIndex + (firstKey[i % firstKey.length] - 'A' + 1)) % secondKey.length;
			cipherText[i] = secondKey[newKeyIndex];
			secondKey[keyIndex] = cipherText[i];
			secondKey[newKeyIndex] = plainText.charAt(i);
		}
		
		return new String(cipherText);
	}
	
	public static byte[] decode(char[] cipherText, String keyword1, String keyword2) {
		
		char[] firstKey = keyword1.toCharArray();
		char[] secondKey = new char[26];
		int d = 0;
		for(d = 0; d < keyword2.length(); d++) {
			secondKey[d] = keyword2.charAt(d);
		}
		
		for(char alpha = 'A'; alpha <= 'Z'; alpha++) {
			if(!ArrayUtil.contains(secondKey, 0, d, alpha))
				secondKey[d++] = alpha;
		}
		
		byte[] plainText = new byte[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++) {
			int keyIndex = ArrayUtil.indexOf(secondKey, cipherText[i]);
			int newKeyIndex = (keyIndex - (firstKey[i % firstKey.length] - 'A' + 1) + secondKey.length) % secondKey.length;
			plainText[i] = (byte)secondKey[newKeyIndex];
			secondKey[keyIndex] = (char)plainText[i];
			secondKey[newKeyIndex] = cipherText[i];
		}
		
		return plainText;
	}
}
