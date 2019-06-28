package nationalcipher.api;

import java.util.Map;

import javax.annotation.Nullable;

import javalibrary.math.MathUtil;
import javalibrary.streams.PrimTypeUtil;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.util.CipherUtils;

public class PlayfairCipher extends UniKeyCipher<String> {

    public PlayfairCipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).create());
    }
    
    @Override
    public Character[] padPlainText(Character[] plainText, String key) {
        StringBuilder builder =  new StringBuilder(plainText.length);
        
        int i = 0;
        for(; plainText.length - i >= 2; i += 2) {
            char a = plainText[i];
            char b = plainText[i + 1];
            if(a == 'J') a = 'I';
            if(b == 'J') b = 'I';
            
            builder.append(a);
            
            if(a == b) {
                builder.append(a == 'X' ? 'Q' : 'X');
                i--;
            } else {
                builder.append(b);
            }
        }
        
        if(i < plainText.length) {
            builder.append(plainText[plainText.length - 1]);
        }
        
        if(builder.length() % 2 == 1) {
            int f = builder.charAt(builder.length() - 1);
            builder.append(f == 'X' ? 'Q' : 'X');
        }
        
        return PrimTypeUtil.toCharacterArray(builder.toString());
    }
    
    @Override
    public String padPlainText(String plainText, String key) {
        StringBuilder builder =  new StringBuilder(plainText.length());
        int i = 0;
        for(; plainText.length() - i >= 2; i += 2) {
            char a = plainText.charAt(i);
            char b = plainText.charAt(i + 1);
            if(a == 'J') a = 'I';
            if(b == 'J') b = 'I';
            
            builder.append(a);
            
            if(a == b) {
                builder.append(a == 'X' ? 'Q' : 'X');
                i--;
            } else {
                builder.append(b);
            }
        }
        
        if(i < plainText.length()) {
            builder.append(plainText.charAt(plainText.length() - 1));
        }
        
        if(builder.length() % 2 == 1) {
            int f = builder.charAt(builder.length() - 1);
            builder.append(f == 'X' ? 'Q' : 'X');
        }
        
        return builder.toString();
    }

    @Override
    public Character[] encode(Character[] plainText, String key, IFormat format) {
        Character[] cipherText = new Character[plainText.length];
           
        for(int i = 0; i < plainText.length; i += 2){
            char a = plainText[i];
            char b = plainText[i + 1];
            int i1 = key.indexOf(a);
            int i2 = key.indexOf(b);
            int row1 = (int)Math.floor(i1 / 5);
            int col1 = i1 % 5;
            int row2 = (int)Math.floor(i2 / 5);
            int col2 = i2 % 5;
            
            char c, d;
            
            if(row1 == row2) {
                c = key.charAt(row1 * 5 + MathUtil.mod(col1 + 1, 5));
                d = key.charAt(row2 * 5 + MathUtil.mod(col2 + 1, 5));
            }
            else if(col1 == col2) {
                c = key.charAt(MathUtil.mod(row1 + 1, 5) * 5 + col1);
                d = key.charAt(MathUtil.mod(row2 + 1, 5) * 5 + col2);
            }
            else {
                c = key.charAt(row1 * 5 + col2);
                d = key.charAt(row2 * 5 + col1);
            }
            
            cipherText[i] = c;
            cipherText[i + 1] = d;
        }
        
        return cipherText;
    }
    
    @Override
    public byte[] decodeEfficently(byte[] cipherText, @Nullable byte[] plainText, String key) {
        Map<Character, Integer> keyIndex = CipherUtils.createCharacterIndexMapping(key);
        
        int size = 5;
        for(int i = 0; i < cipherText.length; i += 2) {
            int i1 = keyIndex.get((char)cipherText[i]);
            int i2 = keyIndex.get((char)cipherText[i + 1]);
            int row1 = i1 / size;
            int col1 = i1 % size;
            int row2 = i2 / size;
            int col2 = i2 % size;
            
            if(row1 == row2) {
                plainText[i] = (byte)(char)key.charAt(row1 * size + (col1 + size - 1) % size);
                plainText[i + 1] = (byte)(char)key.charAt(row2 * size + (col2 + size - 1) % size);
            }
            else if(col1 == col2) {
                plainText[i] = (byte)(char)key.charAt(((row1 + size - 1) % size) * size + col1);
                plainText[i + 1] = (byte)(char)key.charAt(((row2 + size - 1) % size) * size + col2);
            }
            else {
                plainText[i] = (byte)(char)key.charAt(row1 * size + col2);
                plainText[i + 1] = (byte)(char)key.charAt(row2 * size + col1);
            }
        }
        
        return plainText;
    }
}
