package nationalcipher.cipher.util;

import java.util.HashMap;
import java.util.Map;

import javalibrary.streams.PrimTypeUtil;

public class CipherUtils {

	public static Map<Character, Integer> createCharacterIndexMapping(String key) {
		return createCharacterIndexMapping(PrimTypeUtil.toCharacterArray(key));
	}
	
	//TODO Use AlphabetMap
	public static Map<Character, Integer> createCharacterIndexMapping(Character[] key) {
		Map<Character, Integer> keyIndex = new HashMap<Character, Integer>(); 
		
		for(int i = 0; i < key.length; i++) keyIndex.put(key[i], i);
		
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
}
