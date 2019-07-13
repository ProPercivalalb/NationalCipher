package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.TriKeyCipher;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharacterArrayWrapper;

public class DigrafidCipher extends TriKeyCipher<String, String, Integer> {

    public DigrafidCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_27_CHARS).setDim(3, 9),
                SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_27_CHARS).setDim(9, 3),
                IntegerKeyType.builder().setRange(2, 15)); //period 0
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, TriKey<String, String, Integer> key) {
        if(plainText.length() % 2 == 1) {
            StringBuilder builder = new StringBuilder(plainText.length() + 1);
            builder.append(plainText);
            builder.append('X');
            return builder;
        } else {
            return plainText;
        }
    }
    
    @Override
    public CharSequence encode(CharSequence plainText, TriKey<String, String, Integer> key, IFormat format) {
        int fractionation = key.getThirdKey();
        if(fractionation == 0) fractionation = plainText.length() / 2; //I believe this will work
        
        int period = fractionation * 2;
        
        int[] numberText = new int[plainText.length() * 3 / 2];
        int blocks = (int)Math.ceil(plainText.length() / (double)period);
        
        int index = 0;
        
        Character[] cipherText = new Character[plainText.length()];
        
        for(int b = 0; b < blocks; b++) {
            int min = Math.min(fractionation, (plainText.length() - b * period) / 2);
            
            for(int f = 0; f < min; f++) {
                int cTIndex = b * period + f * 2;
                int index1 = key.getFirstKey().indexOf(plainText.charAt(cTIndex));
                int index2 = key.getSecondKey().indexOf(plainText.charAt(cTIndex + 1));
                
                numberText[b * fractionation * 3 + f] = index1 % 9;
                numberText[b * fractionation * 3 + min + f] = (index1 / 9) * 3 + (index2 % 3);
                numberText[b * fractionation * 3 + min * 2 + f] = index2 / 3;
            }
            
            for(int f = 0; f < min; f++) {
                int n1 = numberText[b * fractionation * 3 + f * 3];
                int n2 = numberText[b * fractionation * 3 + f * 3 + 1];
                int n3 = numberText[b * fractionation * 3 + f * 3 + 2];
                cipherText[index++] = key.getFirstKey().charAt(n1 + (int)(n2 / 3) * 9);
                cipherText[index++] = key.getSecondKey().charAt(n3 * 3 + n2 % 3);
            }
        }

        return new CharacterArrayWrapper(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, TriKey<String, String, Integer> key) {
        int fractionation = key.getThirdKey();
        byte[] numberText = new byte[cipherText.length() * 3 / 2]; //TODO Use resuseable one
        if(fractionation == 0) fractionation = cipherText.length() / 2; //I believe this will work
        
        int period = fractionation * 2;
        
        int blocks = (int)Math.ceil(cipherText.length() / (double)period);
        
        int indexNo = 0;
        int index = 0;
        
        for(int b = 0; b < blocks; b++) {
            int min = Math.min(fractionation, (cipherText.length() - b * period) / 2);
            
            for(int f = 0; f < min; f++) {
                int cTIndex = b * period + f * 2;
                int index1 = key.getFirstKey().indexOf(cipherText.charAt(cTIndex));
                int index2 = key.getSecondKey().indexOf(cipherText.charAt(cTIndex + 1));

                numberText[indexNo++] = (byte)(index1 % 9);
                numberText[indexNo++] = (byte)((index1 / 9) * 3 + (index2 % 3));
                numberText[indexNo++] = (byte)(index2 / 3);
            }
            
            for(int f = 0; f < min; f++) {
                int n1 = numberText[b * fractionation * 3 + f];
                int n2 = numberText[b * fractionation * 3 + min + f];
                int n3 = numberText[b * fractionation * 3 + min * 2 + f];
                plainText[index++] = key.getFirstKey().charAt(n1 + (int)(n2 / 3) * 9);
                plainText[index++] = key.getSecondKey().charAt(n3 * 3 + n2 % 3);
            }
        }

        return plainText;
    }
}
