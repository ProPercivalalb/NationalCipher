package nationalcipher.wip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import javalibrary.dict.Dictionary;
import javalibrary.math.MathUtil;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.decrypt.methods.KeyIterator;

public class CyberSec {

	public static void main(String[] args) throws Exception {
		Dictionary.onLoad();
		List<String> md5 = new ArrayList<>();
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Dictionary.class.getResourceAsStream("/nationalcipher/tabula_staff_passwords")));
		while(true) {
			String line = bufferedReader.readLine();
			if(line == null) break;
			if(line.isEmpty() || line.startsWith("#")) continue;
			md5.add(line);
		}
		
		for(int length = 1; length <= 2; ++length) {
			KeyIterator.iterateShort26Key(new ShortCustomKey() {

				@Override
				public void onIteration(String key) {
					try {
						MessageDigest md = MessageDigest.getInstance("MD5") ;
						md.update(key.getBytes("UTF-8"));
					    byte[] digest = md.digest();
					    String myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
		
					    if(md5.contains(myHash)) {
					    	System.out.println(myHash + " " + key);
					    }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}, length, true);
		}
		System.out.println(new String(Keyword.decode("GTDOSKMNUOOMAXGMHNTEHWWYTRLPIJBJJKGLBYCVVLPITWXQBOCGGBQWLVLNVLAL".toCharArray(), "TQGEMYAUZXJLWNCOFDSKPHVIBR")));
		for(int i = 0; i < 26; i++) {
			//System.out.println(new String(decode("GTDOSKMN, UOOM, AXGMHNT EH W WYTR LPIJBJJK. GLBY CVVL, PITW XQBOCGGBQ WLVLNVLAL".toCharArray(), i)));
		}
		
		for(int a : new int[] {1,3,5,7,9,11,15,17,19,21,23,25}) {
  			for(int b = 0; b < 26; b++) {
  				//System.out.println(new String(decodeAff("GTDOSKMN, UOOM, AXGMHNT EH W WYTR LPIJBJJK. GLBY CVVL, PITW XQBOCGGBQ WLVLNVLAL".toCharArray(), a, b)));
  			}
		}
		
    	System.out.println("" + md5.contains("387aacf36df6ff2a8c9d0a7bd56d66bb"));
	}
	
	public static byte[] decode(char[] cipherText, int shift) {
		byte[] plainText = new byte[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++) {
			if(cipherText[i] >= 'A' && cipherText[i] <= 'Z') 
				plainText[i] = (byte)((26 + cipherText[i] - shift - 'A') % 26 + 'A');
			else
				plainText[i] = (byte) cipherText[i];
		}
		
		return plainText;
	}
	
public static byte[] decodeAff(char[] cipherText, int a, int b) {
		
		byte[] plainText = new byte[cipherText.length];
		
		int multiplicativeInverse = BigInteger.valueOf((int)a).modInverse(BigInteger.valueOf(26)).intValue();
		
		//Runs through all the characters from the array
		for(int i = 0; i < cipherText.length; i++)
			if(cipherText[i] >= 'A' && cipherText[i] <= 'Z') 
				plainText[i] = (byte)(MathUtil.mod(multiplicativeInverse * (cipherText[i] - 'A' - b), 26) + 'A');
			else
				plainText[i] = (byte) cipherText[i];

		return plainText;
	}

}
