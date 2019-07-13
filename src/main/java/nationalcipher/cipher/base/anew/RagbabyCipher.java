package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharacterArrayWrapper;

public class RagbabyCipher extends UniKeyCipher<String> {

    public RagbabyCipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_24_CHARS));
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, String key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : (c == 'X' ? 'W' : c));
        }

        return builder;
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        Character[] cipherText = new Character[plainText.length()];

        int word = 2;
        int number = 1;
        for (int i = 0; i < plainText.length(); i++) {
            char character = plainText.charAt(i);

            if (character == ' ') {
                cipherText[i] = ' ';
                number = word++;
            } else
                cipherText[i] = key.charAt((key.indexOf(character) + number++) % key.length());
        }

        return new CharacterArrayWrapper(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        int word = 1;
        int number = word;
        for (int i = 0; i < cipherText.length(); i++) {
            char character = cipherText.charAt(i);

            if (character == ' ') {
                plainText[i] = ' ';
                word++;
                number = word;
            } else
                plainText[i] = key.charAt(((key.indexOf(character) - number++) % key.length() + key.length()) % key.length());
        }

        return plainText;
    }
}
