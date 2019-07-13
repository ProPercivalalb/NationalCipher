package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.TriKeyCipher;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharacterArrayWrapper;

public class ProgressiveCipher extends TriKeyCipher<String, Integer, Integer>  {

    private VigenereType type;
    
    public ProgressiveCipher(VigenereType typeIn) {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, 15), IntegerKeyType.builder().setRange(2, 15), IntegerKeyType.builder().setRange(2, 25));
        this.type = typeIn;
    }

    @Override
    public CharSequence encode(CharSequence plainText, TriKey<String, Integer, Integer> key, IFormat format) {
        String key2 = key.getFirstKey();
        Character[] cipherText = new Character[plainText.length()];
        int progression = 0;
        int count = 0;
        for(int index = 0; index < plainText.length(); index++) {
            char charIdVig = this.type.encode(plainText.charAt(index), (char)key2.charAt(index % key2.length()));
            cipherText[index] = this.type.encode(charIdVig, (char)(progression + 'A'));
            
            if(count + 1 == key.getSecondKey()) {
                count = 0;
                progression = (progression + key.getThirdKey()) % 26;
            }
            else
                count++;
            
        }
        
        return new CharacterArrayWrapper(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, TriKey<String, Integer, Integer> key) {
        String key2 = key.getFirstKey();
        int progression = 0;
        int count = 0;
        for(int index = 0; index < cipherText.length(); index++) {
            char charIdProg = this.type.decode(cipherText.charAt(index), (char)(progression + 'A'));
            plainText[index] = this.type.decode(charIdProg, key2.charAt(index % key2.length()));
            
            if(count + 1 == key.getSecondKey()) {
                count = 0;
                progression = (progression + key.getThirdKey()) % 26;
            }
            else
                count++;
            
        }
        
        return plainText;
    }

    public static char[] decodeBase(char[] cipherText, char[] plainText, int period, int progressiveKey, VigenereType type) {
		int progression = 0;
		int count = 0;
		for(int index = 0; index < cipherText.length; index++) {
			char charIdProg = type.decode(cipherText[index], (char)(progression + 'A'));
			plainText[index] = type.decode(cipherText[index], (char)(progression + 'A'));
			
			if(count + 1 == period) {
				count = 0;
				progression = (progression + progressiveKey) % 26;
			}
			else
				count++;
			
		}
		
		return plainText;
	}
}
