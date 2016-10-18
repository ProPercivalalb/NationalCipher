package nationalcipher.cipher.decrypt.complete.methods;

public class DictionaryAttack {

	public static String createLong26Key(String word, String complete, char excludedCharacter) {
		String key = "";
		
		for(char i : (word + complete).toCharArray()) {
			if(i != excludedCharacter && !key.contains("" + i))
				key += i;
		}
		
		return key;
	}
}
