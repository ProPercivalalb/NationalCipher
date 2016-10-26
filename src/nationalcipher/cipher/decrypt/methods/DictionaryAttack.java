package nationalcipher.cipher.decrypt.methods;

public class DictionaryAttack {

	public static String createLong26Key(String word, String complete) {
		return createLong26Key(word, complete, '?');
	}
	
	public static String createLong26Key(String word, String complete, char excludedCharacter) {
		String key = "";
		
		for(char i : (word + complete).toCharArray()) {
			if(i != excludedCharacter && !key.contains("" + i))
				key += i;
		}
		
		return key;
	}
}
