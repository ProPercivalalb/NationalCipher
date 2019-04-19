package nationalcipher.cipher.base;

import java.util.HashMap;
import java.util.Map;

import javalibrary.streams.PrimTypeUtil;

public class CipherUtils {

	public static Map<Character, Integer> createCharacterIndexMapping(String key) {
		return createCharacterIndexMapping(PrimTypeUtil.toCharacterArray(key));
	}
	
	public static Map<Character, Integer> createCharacterIndexMapping(Character[] key) {
		Map<Character, Integer> keyIndex = new HashMap<Character, Integer>(); 
		
		for(int i = 0; i < key.length; i++) keyIndex.put(key[i], i);
		
		return keyIndex;
	}
}
