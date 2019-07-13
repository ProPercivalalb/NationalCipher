package nationalcipher.cipher.base.anew;

import java.util.Arrays;
import java.util.List;

import javalibrary.string.MorseCode;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;

public class MorbitCipher extends UniKeyCipher<Integer[]> {

    public MorbitCipher() {
        super(OrderedIntegerKeyType.builder().setRange(9, 9));
    }

    @Override
    public CharSequence encode(CharSequence plainText, Integer[] key, IFormat format) {

        String cipherText = "";
        String morseText = "";

        morseText = MorseCode.getMorseEquivalent(plainText);
        if (morseText.length() % 2 != 0)
            morseText += "X";

        List<Character> list = Arrays.asList('.', '-', 'X');
        for (int i = 0; i < morseText.length(); i += 2) {
            int a = list.indexOf(morseText.charAt(i));
            int b = list.indexOf(morseText.charAt(i + 1));
            cipherText += key[a * 3 + b] + 1;
        }

        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, Integer[] key) {
        int[] reversedOrder = new int[key.length];
        for (int i = 0; i < key.length; i++)
            reversedOrder[key[i]] = i;

        StringBuilder plainText = new StringBuilder();
        char[] morseText = new char[cipherText.length() * 2];
        char[] list = new char[] { '.', '-', 'X' };

        for (int i = 0; i < cipherText.length(); i++) {
            int a = cipherText.charAt(i) - '0' - 1;

            int index = reversedOrder[a];
            int first = index / 3;
            int second = index % 3;
            morseText[i * 2] = list[first];
            morseText[i * 2 + 1] = list[second];
        }

        int last = 0;
        for (int i = 0; i < morseText.length; i++) {
            char a = morseText[i];
            boolean isX = a == 'X';
            boolean end = i == morseText.length - 1;
            if (isX || end) {
                String code = new String(morseText, last, i - last + (end && !isX ? 1 : 0));

                last = i + 1;
                Character morseChar = MorseCode.getCharFromMorse(code);
                if (morseChar != null) {
                    plainText.append((char) morseChar); // Cast to char is more efficient
                } else {
                    plainText.append(morseText, last, i - last + (end && !isX ? 1 : 0));
                }
            }

        }

        return plainText.toString().toCharArray();
    }

}
