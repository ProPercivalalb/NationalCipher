package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class VigenereCipher extends UniKeyCipher<String> {

    private VigenereType type;
    
    public VigenereCipher(VigenereType type) {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, 15));
        this.type = type;
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        char[] cipherText = new char[plainText.length()];
        int period = key.length();
        
        for(int index = 0; index < plainText.length(); index++)
            cipherText[index] = this.type.encode(plainText.charAt(index), key.charAt(index % period));
        
        return new String(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        int period = key.length();
        
        for(int index = 0; index < cipherText.length(); index++)
            plainText[index] = this.type.decode(cipherText.charAt(index), key.charAt(index % period));
        
        return plainText;
    }
}
