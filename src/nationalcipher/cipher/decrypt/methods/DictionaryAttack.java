package nationalcipher.cipher.decrypt.methods;

import java.util.Arrays;

import javalibrary.dict.Dictionary;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.transposition.RouteCipherType;

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
	
	public static char[] createLongKey(char[] word, char[] fullAlphabet, RouteCipherType route, int width, int height) {
		int[] routeData = route.getPattern(width, height, width * height);
		char[] untransformed = createLongKey(word, fullAlphabet);
		char[] transformed = new char[untransformed.length];
		for(int index = 0; index < untransformed.length; index++)
			transformed[routeData[index]] = untransformed[index];
		
		return transformed;
	}
	
	public static char[] createLongKey(char[] word, char[] fullAlphabet) {
		char[] complete = new char[fullAlphabet.length];
		int index = 0;
			
		for(char cInW : word)
			if(!ArrayUtil.contains(complete, index, cInW) && ArrayUtil.contains(fullAlphabet, cInW))
				complete[index++] = cInW;
			
		for(char cInA : fullAlphabet)
			if(!ArrayUtil.contains(complete, index, cInA))
				complete[index++] = cInA;
		
		return complete;
	}
}
