package nationalcipher.cipher.base;

import java.util.HashMap;
import java.util.Map;

public class CipherUtils {

	public static Map<Character, Integer> createCharacterIndexMapping(String key) {
		return createCharacterIndexMapping(key.toCharArray());
	}
	
	public static Map<Character, Integer> createCharacterIndexMapping(char[] key) {
		Map<Character, Integer> keyIndex = new HashMap<Character, Integer>(); 
		
		for(int i = 0; i < key.length; i++) keyIndex.put(key[i], i);
		
		return keyIndex;
	}
}
