package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class MyszkowskiCipher extends UniKeyCipher<String, VariableStringKeyType.Builder> {

    public MyszkowskiCipher() {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, Integer.MAX_VALUE).setRepeats());
    }
    
    @Override
    public VariableStringKeyType.Builder limitDomainForFirstKey(VariableStringKeyType.Builder firstKey) {
        return firstKey.setRange(2, 15);
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        StringBuilder cipherText = new StringBuilder(plainText.length());

        int rows = (int) Math.ceil(plainText.length() / (double) key.length());

        for (char c = 'A'; c <= 'Z'; c++) {
            if (key.indexOf(c) == -1)
                continue;

            for (int row = 0; row < rows; row++) {
                for (int i = 0; i < key.length(); i++) {
                    if (c == key.charAt(i)) {
                        if (row * key.length() + i < plainText.length()) {
                            cipherText.append(plainText.charAt(row * key.length() + i));
                        }
                    }
                }
            }
        }

        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {

        int rows = (int) Math.ceil(cipherText.length() / (double) key.length());

        int index = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            if (key.indexOf(c) == -1)
                continue;

            for (int row = 0; row < rows; row++) {
                for (int i = 0; i < key.length(); i++) {
                    if (c == key.charAt(i)) {
                        if (row * key.length() + i < cipherText.length()) {
                            plainText[row * key.length() + i] = cipherText.charAt(index++);
                        }
                    }
                }
            }
        }

        return plainText;
    }

}
