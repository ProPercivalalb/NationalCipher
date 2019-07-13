package nationalcipher.cipher.base.anew;

import java.math.BigInteger;

import javax.annotation.Nullable;

import javalibrary.math.MathUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.IntegerGenKeyType;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.util.CipherUtils;

public class AffineCipher extends BiKeyCipher<Integer, Integer> {

    public AffineCipher() {
        super(IntegerGenKeyType.builder().setRange(0, 25).setFilter(MathUtil::hasMultiplicativeInverseMod26),
                IntegerKeyType.builder().setRange(0, 25));
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<Integer, Integer> key, IFormat format) {
        StringBuilder cipherText = new StringBuilder(plainText.length());
        
        String tempAlphabet = "";
        for(int i = 0; i <= 26; ++i) {
            tempAlphabet += (char)('A' + (key.getFirstKey() * i + key.getSecondKey()) % 26);
        }
        
        System.out.println("ALPHABET: " + tempAlphabet);
        for(int i = 0; i < plainText.length(); i++) {
            byte ch = CipherUtils.getAlphaIndex(plainText.charAt(i));
            
            if(ch == -1) {
                //if(format)
                //  cipherText.charAt(i++] = ch;
            } else {
                char newLetter = (char)(tempAlphabet.charAt(plainText.charAt(i) - 'A'));
                cipherText.append(newLetter);
            }
        }
        
        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<Integer, Integer> key) {
        
        int multiplicativeInverse = BigInteger.valueOf((int)key.getFirstKey()).modInverse(BigInteger.valueOf(26)).intValue();
        
        //Runs through all the characters from the array
        for(int i = 0; i < cipherText.length(); i++)
            plainText[i] = (char)(MathUtil.mod(multiplicativeInverse * (cipherText.charAt(i) - 'A' - key.getSecondKey()), 26) + 'A');

        return plainText;
    }
}
