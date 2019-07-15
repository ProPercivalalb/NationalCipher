package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharacterArrayWrapper;

public class KeywordCipher extends UniKeyCipher<String, FullStringKeyType.Builder> {

    public KeywordCipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS));
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {

        Character[] cipherText = new Character[plainText.length()];

        for (int i = 0; i < plainText.length(); i++) {
            char ch = plainText.charAt(i);
            if (ch >= 'A' && ch <= 'Z')
                cipherText[i] = key.charAt(ch - 'A');
        }

        return new CharacterArrayWrapper(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        for (int i = 0; i < cipherText.length(); i++)
            plainText[i] = (char) (key.indexOf(cipherText.charAt(i)) + 'A');

        return plainText;
    }
}
