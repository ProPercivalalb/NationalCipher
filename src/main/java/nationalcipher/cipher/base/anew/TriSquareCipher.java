package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.TriKeyCipher;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.tools.KeyGeneration;

public class TriSquareCipher extends TriKeyCipher<String, String, String, SquareStringKeyType.Builder, SquareStringKeyType.Builder, SquareStringKeyType.Builder> {

    public TriSquareCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5), SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5), SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5));
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, TriKey<String, String, String> key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : c);
        }

        if (builder.length() % 2 == 1) {
            builder.append('X');
        }

        return builder;
    }

    @Override
    public CharSequence encode(CharSequence plainText, TriKey<String, String, String> key, IFormat format) {
        StringBuilder cipherText = new StringBuilder(plainText.length() * 3 / 2);
        for (int i = 0; i < plainText.length() / 2; i++) {
            char a = plainText.charAt(i * 2);
            char b = plainText.charAt(i * 2 + 1);
            int column1 = key.getFirstKey().indexOf(a) % 5;
            int row1 = key.getFirstKey().indexOf(a) / 5;
            int row2 = key.getSecondKey().indexOf(b) / 5;
            int column2 = key.getSecondKey().indexOf(b) % 5;
            cipherText.append(key.getFirstKey().charAt(5 * RandomUtil.pickRandomInt(5) + column1));
            cipherText.append(key.getThirdKey().charAt(5 * row1 + column2));
            cipherText.append(key.getSecondKey().charAt(5 * row2 + RandomUtil.pickRandomInt(5)));
        }

        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, TriKey<String, String, String> key) {
        return decodeEfficently(cipherText, new char[cipherText.length() / 3 * 2], key);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, TriKey<String, String, String> key) {
        for (int i = 0; i < cipherText.length() / 3; i++) {
            char a = cipherText.charAt(i * 3);
            char b = cipherText.charAt(i * 3 + 1);
            char c = cipherText.charAt(i * 3 + 2);

            int column = key.getFirstKey().indexOf(a) % 5;

            int row = key.getSecondKey().indexOf(c) / 5;

            int index = key.getThirdKey().indexOf(b);
            int columnSort = index % 5;
            int rowSort = index / 5;

            plainText[i * 2] = key.getFirstKey().charAt(rowSort * 5 + column);
            plainText[i * 2 + 1] = key.getSecondKey().charAt(row * 5 + columnSort);
        }

        return plainText;
    }

    @Override
    public boolean deterministic() {
        return false;
    }
}
