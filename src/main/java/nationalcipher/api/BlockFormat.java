package nationalcipher.api;

import java.util.Arrays;

public class BlockFormat implements IFormat {

	@Override
	public Character[] apply(Character[] text) {
	
		Character[] condensed = Arrays.copyOf(text, text.length);
		
		int offset = 0;
		for(int i = 0; i < condensed.length; i++) {
			if(!Character.isAlphabetic(condensed[i]))
				offset++;
			else
				condensed[i - offset] = condensed[i];
		}
		
		return Arrays.copyOf(condensed, condensed.length - offset);
	}
}
