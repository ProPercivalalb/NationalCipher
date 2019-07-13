package nationalcipher.cipher.util;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class CipherUtils {
    
	//TODO Use AlphabetMap
	public static Map<Character, Integer> createCharacterIndexMapping(CharSequence key) {
		Map<Character, Integer> keyIndex = new HashMap<Character, Integer>(); 
		
		for(int i = 0; i < key.length(); i++) {
		    keyIndex.put(key.charAt(i), i);
		}
		
		return keyIndex;
	}
	
	public static byte getAlphaIndex(char alphaChar) {
	    if('A' <= alphaChar && alphaChar <= 'Z') {
            return (byte)(alphaChar - 'A');
        }  else if('a' <= alphaChar && alphaChar <= 'z') {
            return (byte)(alphaChar - 'a');
        } else {
            return -1;
        }
	}
	
	public static byte[] charSeqToByteArray(CharSequence input) {
        byte[] output = new byte[input.length()];
        for(int i = 0; i < input.length(); i++)
            output[i] = (byte)input.charAt(i);
        return output;
    }
	
	public static String byteArrayToCharSeq(byte[] input) {
        return new String(input, Charset.forName("UTF-8"));
    }
}
