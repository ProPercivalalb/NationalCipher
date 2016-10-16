package nationalcipher.cipher.decrypt.complete.methods;

public class DictionaryAttack {

	public static String createLong26Key(String word, String complete) {
		String key = "";
		
		for(char i : (word + complete).toCharArray()) {
			if(!key.contains("" + i))
				key += i;
		}
		
		return key;
	}
}
