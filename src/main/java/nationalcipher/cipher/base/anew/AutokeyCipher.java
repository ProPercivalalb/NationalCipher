package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharacterArrayWrapper;

public class AutokeyCipher extends UniKeyCipher<String> {

    private VigenereType type;

    public AutokeyCipher(VigenereType type) {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, 15));
        this.type = type;
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        Character[] cipherText = new Character[plainText.length()];
        int period = key.length();

        for (int index = 0; index < plainText.length(); index++) {
            char charIdKey;

            if (index < period)
                charIdKey = key.charAt(index);
            else
                charIdKey = plainText.charAt(index - period);

            cipherText[index] = this.type.encode(plainText.charAt(index), charIdKey);
        }

        return new CharacterArrayWrapper(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        int period = key.length();

        for (int index = 0; index < cipherText.length(); index++) {
            char charIdKey;

            // Determines key to use; if at start use normal key, else use decrypted
            // plaintext
            if (index < period)
                charIdKey = key.charAt(index);
            else
                charIdKey = plainText[index - period];

            plainText[index] = this.type.decode(cipherText.charAt(index), charIdKey);
        }

        return plainText;
    }
}
