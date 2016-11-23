package nationalcipher.cipher.base.substitution;

import nationalcipher.cipher.base.IRandEncrypter;

public class RunningKey implements IRandEncrypter {

	public static void main(String[] args) {
		//THESLIDEFAIRCANBEUSEDWITHVIGENEREVARIANTORBEAUFORT
		//System.out.println(new String(encode("THISCIPHERCANBEUSEDWITHANYOFTHEPERIODICS")));
		//System.out.println(new String(decode("BAPSPGDMXYGPRSMIVMFO".toCharArray(), "THISCIPHERCANBEUSEDW")));
	}
	
	///public static String encode(String plainText) {
	//	if(plainText.length() % 2 == 1) plainText += 'X';
		
	//	return Vigenere.encode(plainText.substring(plainText.length() / 2, plainText.length()), plainText.substring(0, plainText.length() / 2));
	//}
	
	//public static char[] decode(char[] cipherText, String key) {
	//	return ArrayUtil.concat(key.toCharArray(), Vigenere.decode(cipherText, key));
	//}

	@Override
	public String randomlyEncrypt(String plainText) {
		return "";//encode(plainText);
	}
}
