package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class CadenusCipher extends UniKeyCipher<String> {

    public CadenusCipher() {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, 5));;
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        
        if(plainText.length() != key.length() * 25) {
            String combinedMulti = "";
            for(int i = 0; i < plainText.length() / (key.length() * 25); i++)
                combinedMulti += encode(plainText.subSequence(i * key.length() * 25, (i + 1) * key.length() * 25), key);
            return combinedMulti;
        }
        else {
            int keyLength = key.length();
    
            int[] order = new int[key.length()];
            
            int p = 0;
            for(char ch = 'A'; ch <= 'Z'; ++ch)
                for(int i = 0; i < order.length; i++)
                    if(ch == key.charAt(i))
                        order[i] = p++;
            
            //Creates grid
            char[] temp_grid = new char[plainText.length()];
    
            for(int j = 0; j < 25; j++) {
                for(int i = 0; i < keyLength; i++) {
                    int newColumn = order[i];
                    int newIndex = (j - charValue(key.charAt(i)) + 25) % 25;
                    temp_grid[newIndex * keyLength + newColumn] = plainText.charAt(j * keyLength + i);
                }
            }
            return new String(temp_grid);
        }

    }
    
    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        int[] order = new int[key.length()];
        
        int p = 0;
        for(char ch = 'A'; ch <= 'Z'; ++ch) {
            for(int i = 0; i < order.length; i++) {
                if(ch == key.charAt(i)) {
                    order[p++] = i;
                }
            }
        }

        return decode(cipherText, key, order);
    }
    
    public static char[] decode(CharSequence cipherText, String key, int[] order) {
        int keyLength = order.length;
        
        //Creates grid
        char[] grid = new char[cipherText.length()];
                
        for(int j = 0; j < 25; j++) {
            for(int i = 0; i < keyLength; i++) {
                int newColumn = order[i];
                int newIndex = (j + charValue(key.charAt(newColumn))) % 25;
                grid[newIndex * keyLength + newColumn] = cipherText.charAt(j * keyLength + i);
            }
        }

        return grid;
    }

    public static int charValue(char character) {
        if(character >= 'W')
            return ('Z' - character + 1) % 25;
        return ('Z' - character) % 25;
    }
}
