package nationalcipher.cipher.base.anew;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import javalibrary.string.MorseCode;
import javalibrary.util.ArrayUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class FractionatedMorseCipher extends UniKeyCipher<String, FullStringKeyType.Builder> {

    private static List<Character> list = Arrays.asList('.', '-', 'X');

    public FractionatedMorseCipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS));
    }

    // TODO read and write xx between words

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {

        StringBuilder cipherText = new StringBuilder(plainText.length());
        String morseText = "";

        morseText = MorseCode.getMorseEquivalent(plainText);
        while (morseText.length() % 3 != 0)
            morseText += "X";

        for (int i = 0; i < morseText.length(); i += 3) {
            int a = list.indexOf(morseText.charAt(i));
            int b = list.indexOf(morseText.charAt(i + 1));
            int c = list.indexOf(morseText.charAt(i + 2));
            cipherText.append(key.charAt(a * 9 + b * 3 + c));
        }

        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        char[] morseText = new char[cipherText.length() * 3];

        for (int i = 0; i < cipherText.length(); i++) {
            int index = key.indexOf(cipherText.charAt(i));
            morseText[i * 3] = list.get(index / 9);
            morseText[i * 3 + 1] = list.get((int) (index / 3) % 3);
            morseText[i * 3 + 2] = list.get(index % 3);
        }

        int index = 0;
        int lastX = 0;
        for (int i = 0; i < morseText.length; i++) {

            char morseCh = morseText[i];
            boolean isX = morseCh == 'X';
            boolean end = i == morseText.length - 1;

            if (isX || end) { // When char is X or is at the end of the text
                char character = MorseCode.getCharFromMorse(morseText, lastX, i - lastX + (end && !isX ? 1 : 0));
                if (character == ' ')
                    for (int j = lastX; j < i; j++)
                        plainText[index++] = morseText[j];
                else
                    plainText[index++] = character;

                lastX = i + 1;
            }
        }

        return ArrayUtil.copyRange(plainText, 0, index);
    }

}
