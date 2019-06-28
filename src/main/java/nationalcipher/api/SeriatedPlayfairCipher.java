package nationalcipher.api;

import java.util.Map;

import javax.annotation.Nullable;

import javalibrary.math.MathUtil;
import javalibrary.streams.PrimTypeUtil;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.util.CipherUtils;

public class SeriatedPlayfairCipher extends BiKeyCipher<String, Integer> {

    public SeriatedPlayfairCipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).create(), IntegerKeyType.builder().setRange(2, 15).create());
        //TODO Add period 0
    }
    
    @Override
    public Character[] padPlainText(Character[] plainText, BiKey<String, Integer> key) {
        //TODO Improve
        int period = key.getSecondKey();
        
        StringBuilder builder =  new StringBuilder(plainText.length);
        int i = 0;
        for(; (plainText.length - i) >= period * 2; i += period * 2) {
            for(int j = 0; j < period; j++) {
                builder.append(plainText[i + j]);
            }
            int offset = 0;
            for(int j = 0; j < period; j++) {
                char a = plainText[i + j + offset];
                    
                if(i + j + period >= plainText.length) {
                   builder.append(a == 'X' ? 'Q' : 'X');
                } else {
                    char b = plainText[i + j + period];
                    if(a == 'J') a = 'I';
                    if(b == 'J') b = 'I';
                       
                    if(a == b) {
                       builder.append(a == 'X' ? 'Q' : 'X');
                       i--;
                       offset++;
                   } else {
                       builder.append(b);
                   }
               }
            }
        }
        
        System.out.println("" + i);
        System.out.println(PrimTypeUtil.toString(plainText).substring(i, plainText.length));
        
        // Deal with smaller final block separately
        
        return PrimTypeUtil.toCharacterArray(builder.toString());
    }
    
    @Override
    public String padPlainText(String plainText, BiKey<String, Integer> key) {
        //TODO Improve
        int period = key.getSecondKey();
        while(true) {
            boolean valid = true;

            label:
            for(int i = 0; i < plainText.length() && valid; i += period * 2) {
                int min = Math.min(period, (plainText.length() - i + 1) / 2);

                for(int j = 0; j < Math.min(min, (plainText.length() - i) / 2); j++) {
                    char a = plainText.charAt(i + j);
                    char b = plainText.charAt(i + j + min);
                    if(a == b) {
                        char nullChar = 'X';
                        if(a == 'X')
                            nullChar = 'Q';
                        plainText = plainText.substring(0, i + min + j) + nullChar + plainText.substring(i + min + j, plainText.length());
                        valid = false;
                        break label;
                    }
                }
            }
            
            if(valid)
                break;
        }
        
        if(plainText.length() % 2 != 0)
            plainText += 'X';
        
        return plainText;
    }

    @Override
    public Character[] encode(Character[] plainText, BiKey<String, Integer> key, IFormat format) {
        int period = key.getSecondKey();
        if(period == 0) period = plainText.length / 2;
        
        Character[] cipherText = new Character[plainText.length];
        
        for(int i = 0; i < plainText.length; i += period * 2) {
            int min = Math.min(period, (int)Math.ceil((double)(plainText.length - i) / 2));

            for(int j = 0; j < min; j++) {
                char a = plainText[i + j];
                char b = plainText[i + j + min];
                
                 
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
        
        return cipherText;
    }
    
    @Override
    public byte[] decodeEfficently(byte[] cipherText, @Nullable byte[] plainText, BiKey<String, Integer> key) {
        int period = key.getSecondKey();
        if(period == 0) period = cipherText.length / 2;

        Map<Character, Integer> keyIndex = CipherUtils.createCharacterIndexMapping(key.getFirstKey());
    
        for(int i = 0; i < cipherText.length; i += period * 2) {
            int min = Math.min(period, (int)Math.ceil((double)(plainText.length - i) / 2));

            for(int j = 0; j < min; j++) {
                int i1 = keyIndex.get((char)cipherText[i + j]);
                int i2 = keyIndex.get((char)cipherText[i + j + min]);
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
                    
                plainText[i + j] = (byte)c;
                plainText[i + j + min] = (byte)d;
            }
        }
        
        return plainText;
    }
}
