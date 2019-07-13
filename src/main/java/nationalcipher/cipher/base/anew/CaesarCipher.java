package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.util.CipherUtils;

public class CaesarCipher extends UniKeyCipher<Integer> {

    public CaesarCipher() {
        super(IntegerKeyType.builder().setRange(1, 25));
    }

    @Override
    public CharSequence encode(CharSequence plainText, Integer key, IFormat format) {
        StringBuilder cipherText = new StringBuilder(plainText.length());
        int i;
        for (i = 0; i < plainText.length(); i++) {
            byte ch = CipherUtils.getAlphaIndex(plainText.charAt(i));
            if (ch == -1) {
                // if(format)
                // cipherText.charAt(i++] = ch;
            } else {
                char newLetter = (char) (((ch + key) % 26) + 'A');
                cipherText.append(newLetter);
            }
        }

        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, Integer key) {
        for (int i = 0; i < cipherText.length(); i++) {
            byte ch = CipherUtils.getAlphaIndex(cipherText.charAt(i));
            if (ch == -1)
                plainText[i] = cipherText.charAt(i);
            else {
                plainText[i] = (char) (((ch + 26 - key) % 26) + 'A');
            }
        }

        return plainText;
    }

}
