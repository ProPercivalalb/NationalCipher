package nationalcipher.cipher.base.anew;

import java.util.Arrays;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class SlidefairCipher extends UniKeyCipher<String> {
    
    private VigenereType type;
    
    public SlidefairCipher(VigenereType type) {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, 15));
        this.type = type;
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        StringBuilder cipherText = new StringBuilder(plainText.length());
        
        String[] keyAlpha = new String[key.length()];
        Arrays.fill(keyAlpha, "");
        
        for(int i = 0; i < key.length(); i++)
            for(int k = 0; k < 26; k++) 
                keyAlpha[i] += (char)this.type.encode((char)(k + 'A'), key.charAt(i));
        
        
        for(int i = 0; i < plainText.length() / 2; i++) {
            char a = plainText.charAt(i * 2);
            char b = plainText.charAt(i * 2 + 1);
        
            String alpha = keyAlpha[i % key.length()];
            
            int index = alpha.indexOf(b);
            if(a - 'A' == index) {
                cipherText.append((char)((index + 1) % 26 + 'A'));
                cipherText.append(alpha.charAt((index + 1) % 26));
            }
            else {
                cipherText.append((char)(index + 'A'));
                cipherText.append(alpha.charAt(a - 'A'));
            }
        }
        
        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        char[][] keyAlpha = new char[key.length()][26];
        byte[][] keyAlphaIndex = new byte[key.length()][26];
        
        for(int i = 0; i < key.length(); i++)
            for(byte k = 0; k < 26; k++) {
                char a = type.encode((char)(k + 'A'), key.charAt(i));
                keyAlpha[i][k] = a;
                keyAlphaIndex[i][a - 'A'] = k;
            }
        
        for(int i = 0; i < cipherText.length() / 2; i++) {
            char a = cipherText.charAt(i * 2);
            char b = cipherText.charAt(i * 2 + 1);
            
            char[] alpha = keyAlpha[i % key.length()];

            byte index = keyAlphaIndex[i % key.length()][b - 'A'];
            if(a - 'A' == index) {
                plainText[i * 2] = (char)((index + 25) % 26 + 'A');
                plainText[i * 2 + 1] = alpha[(index + 25) % 26];
            }
            else {
                plainText[i * 2] = (char)(index + 'A');
                plainText[i * 2 + 1] = alpha[a - 'A'];
            }
        }
        
        return plainText;
    }
}
