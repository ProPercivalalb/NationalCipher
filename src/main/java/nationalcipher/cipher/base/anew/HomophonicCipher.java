package nationalcipher.cipher.base.anew;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import javalibrary.math.MathUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class HomophonicCipher extends UniKeyCipher<String> {

    public HomophonicCipher() {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setRange(4, 4));
    }
    
    @Override
    public CharSequence padPlainText(CharSequence plainText, String key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for(int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : c);
        }
        
        return builder;
    }
    
    public static void main(String[] args) {
        HomophonicCipher cipher = new HomophonicCipher();
        cipher.encode("WORDDIVISIONMAYBEKEPT", "GOLF", null);
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        StringBuilder cipherText = new StringBuilder();
        
        List<char[]> rows = new ArrayList<char[]>();

        for(int i = 0; i < 4; i++) {
            int charIndex = key.charAt(i) - 'A';
            
            if(key.charAt(i) >= 'J') charIndex--;
            
            for(int no = 0; no < 25; no++) {
                int num = (i * 25 + (no + 25 - charIndex) % 25 + 1) % 100;
                int digit = num % 10;
                int tens = (num - digit) / 10;
                rows.add(new char[] {(char)(tens + '0'), (char)(digit + '0')});
            }
        }
        
        for(int i = 0; i < plainText.length(); i++) {
            int charIndex = plainText.charAt(i) - 'A';
            if(charIndex >= 9) charIndex--;
            
            cipherText.append(rows.get(RandomUtil.pickRandomInt(4) * 25 + charIndex));
        }
        
        return cipherText;
    }
    
    @Override
    public char[] decodeEfficently(CharSequence cipherText, String key) {
        return decodeEfficently(cipherText, new char[cipherText.length() / 2], key);
    }
    
    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        
        int[] rows = new int[100];
        String shortAlpha = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        
        for(int i = 0; i < 4; i++) {
            int c = key.charAt(i) - 'A';
            
            if(key.charAt(i) >= 'J') c--;
            
            for(int no = 0; no < 25; no++) {

                rows[(i * 25 + MathUtil.mod(no - c, 25) + 1) % 100] = no;
            }
        }
        
        for(int i = 0; i < plainText.length; i++) {
            int col = rows[10 * (cipherText.charAt(i * 2) - '0') + (cipherText.charAt(i * 2 + 1) - '0')];
            plainText[i] = shortAlpha.charAt(col);
        }
        
        return plainText;
    }

    @Override
    public boolean deterministic() {
        return false;
    }
}
