package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.math.matrics.Matrix;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.ExtendedMatrixKeyType;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.base.keys.SquareMatrixKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharArrayWrapper;

public class HillSubstitutionCipher extends BiKeyCipher<Matrix, String, SquareMatrixKeyType.Builder, FullStringKeyType.Builder> {

    private KeywordCipher keywordCipher;
    
    public HillSubstitutionCipher() {
        super(SquareMatrixKeyType.builder(),
                FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS));
        this.keywordCipher = new KeywordCipher();
    }
    
    @Override
    public CharSequence padPlainText(CharSequence plainText, BiKey<Matrix, String> key) {
        int blockSize = key.getFirstKey().squareSize();

        if (plainText.length() % blockSize != 0) {
            StringBuilder builder = new StringBuilder(plainText.length() + blockSize - (plainText.length() % blockSize));
            builder.append(plainText);
            while (builder.length() % blockSize != 0) {
                builder.append('X');
            }

            return builder;
        } else {
            return plainText;
        }
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<Matrix, String> key, IFormat format) { 
        char[] cipherText = new char[plainText.length()];
        int size = key.getFirstKey().squareSize();
        plainText = this.keywordCipher.encode(plainText, key.getSecondKey(), format);
        for(int i = 0; i < plainText.length(); i += size) {
            
            int[] let = new int[size];
            for(int j = 0; j < size; j++) {
                let[j] = ((char)plainText.charAt(i + j) - 'A');
            }
            
            Matrix plainMatrix = new Matrix(let, size, 1);
            Matrix cipherMatrix = key.getFirstKey().multiply(plainMatrix).modular(26);
            
            for(int j = 0; j < size; j++) {
                cipherText[i + j] = (char)(cipherMatrix.data[j] + 'A');
            }
                
        }
        
        return new CharArrayWrapper(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<Matrix, String> key) {
        return decodeUsingInverse(cipherText, plainText, key.getFirstKey().inverseMod(26), key.getSecondKey());
    }
    
    public char[] decodeUsingInverse(CharSequence cipherText, char[] plainText, Matrix inverseKey, String secondKey)  { 
        int size = inverseKey.squareSize();
        for(int i = 0; i < cipherText.length(); i += size) {
            
            int[] let = new int[size];
            for(int j = 0; j < size; j++)
                let[j] = ((int)cipherText.charAt(i + j) - 'A');
            
            Matrix cipherMatrix = new Matrix(let, size, 1);
            Matrix plainMatrix = inverseKey.multiply(cipherMatrix).modular(26);
            
            for(int j = 0; j < size; j++) {
                plainText[i + j] = (char)(plainMatrix.data[j] + 'A');
            }
        }
        
        plainText = this.keywordCipher.decodeEfficently(new CharArrayWrapper(plainText), plainText, secondKey);
        
        return plainText;
    }
}
