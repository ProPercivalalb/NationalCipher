package nationalcipher.cipher.base.anew;

import java.util.Map;

import javax.annotation.Nullable;

import javalibrary.math.MathUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.util.CharacterArrayWrapper;

public class SeriatedPlayfairCipher extends BiKeyCipher<String, Integer> {

    public SeriatedPlayfairCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5), 
                IntegerKeyType.builder().setRange(2, 15));
        //TODO Add period 0
    }
    
    @Override
    public CharSequence padPlainText(CharSequence plainText, BiKey<String, Integer> key) {
        StringBuilder builder = new StringBuilder(plainText);
        
        //TODO Improve
        int period = key.getSecondKey();
        while(true) {
            boolean valid = true;

            label:
            for(int i = 0; i < builder.length() && valid; i += period * 2) {
                int min = Math.min(period, (builder.length() - i + 1) / 2);

                for(int j = 0; j < Math.min(min, (builder.length() - i) / 2); j++) {
                    char a = builder.charAt(i + j);
                    char b = builder.charAt(i + j + min);
                    if(a == b) {
                        char nullChar = 'X';
                        if(a == 'X')
                            nullChar = 'Q';
                        builder.insert(i + min + j, nullChar);
                        
                        valid = false;
                        break label;
                    }
                }
            }
            
            if(valid)
                break;
        }
        
        if(builder.length() % 2 != 0)
            builder.append('X');
        
        return builder;
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<String, Integer> key, IFormat format) {
        int period = key.getSecondKey();
        if(period == 0) period = plainText.length() / 2;
        
        Character[] cipherText = new Character[plainText.length()];
        
        for(int i = 0; i < plainText.length(); i += period * 2) {
            int min = Math.min(period, (int)Math.ceil((double)(plainText.length() - i) / 2));

            for(int j = 0; j < min; j++) {
                char a = plainText.charAt(i + j);
                char b = plainText.charAt(i + j + min);
                
                 
                int i1 = key.getFirstKey().indexOf(a);
                int i2 = key.getFirstKey().indexOf(b);
                int row1 = (int)Math.floor((double)i1 / 5);
                int col1 = i1 % 5;
                int row2 = (int)Math.floor((double)i2 / 5);
                int col2 = i2 % 5;
                    
                char c, d;
                    
                if(row1 == row2) {
                    c = key.getFirstKey().charAt(row1 * 5 + MathUtil.mod(col1 + 1, 5));
                    d = key.getFirstKey().charAt(row2 * 5 + MathUtil.mod(col2 + 1, 5));
                }
                else if(col1 == col2) {
                    c = key.getFirstKey().charAt(MathUtil.mod(row1 + 1, 5) * 5 + col1);
                    d = key.getFirstKey().charAt(MathUtil.mod(row2 + 1, 5) * 5 + col2);
                }
                else {
                    c = key.getFirstKey().charAt(row1 * 5 + col2);
                    d = key.getFirstKey().charAt(row2 * 5 + col1);
                }
                
                if(c == d) {
                    System.out.println("ERROR " + a + "" + b  + " " + c + "" + d);
                }
                    
                cipherText[i + j] = c;
                cipherText[i + j + min] = d;
            }
        }
        
        return new CharacterArrayWrapper(cipherText);
    }
    
    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<String, Integer> key) {
        int period = key.getSecondKey();
        if(period == 0) period = cipherText.length() / 2;

        Map<Character, Integer> keyIndex = CipherUtils.createCharacterIndexMapping(key.getFirstKey());
    
        for(int i = 0; i < cipherText.length(); i += period * 2) {
            int min = Math.min(period, (int)Math.ceil((double)(plainText.length - i) / 2));

            for(int j = 0; j < min; j++) {
                int i1 = keyIndex.get(cipherText.charAt(i + j));
                int i2 = keyIndex.get(cipherText.charAt(i + j + min));
                int row1 = i1 / 5;
                int col1 = i1 % 5;
                int row2 = i2 / 5;
                int col2 = i2 % 5;
                    
                char c, d;
                    
                if(row1 == row2) {
                    c = key.getFirstKey().charAt(row1 * 5 + MathUtil.mod(col1 - 1, 5));
                    d = key.getFirstKey().charAt(row2 * 5 + MathUtil.mod(col2 - 1, 5));
                }
                else if(col1 == col2) {
                    c = key.getFirstKey().charAt(MathUtil.mod(row1 - 1, 5) * 5 + col1);
                    d = key.getFirstKey().charAt(MathUtil.mod(row2 - 1, 5) * 5 + col2);
                }
                else {
                    c = key.getFirstKey().charAt(row1 * 5 + col2);
                    d = key.getFirstKey().charAt(row2 * 5 + col1);
                }
                    
                plainText[i + j] = c;
                plainText[i + j + min] = d;
            }
        }
        
        return plainText;
    }
}
