package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.string.NumberString;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.IntegerKeyType;

public class BazeriesCipher extends UniKeyCipher<Integer> {

    public BazeriesCipher() {
        super(IntegerKeyType.builder().setRange(0, 1000000));
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, Integer key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : c);
        }

        return builder;
    }

    @Override
    public CharSequence encode(CharSequence plainText, Integer key, IFormat format) {
        String alphabetSquare = "AFLQVBGMRWCHNSXDIOTYEKPUZ";

        String numberSquare = "";
        for (char j : NumberString.convert(key).toCharArray())
            if (numberSquare.indexOf(j) == -1)
                numberSquare += j;

        for (char j : "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray())
            if (numberSquare.indexOf(j) == -1)
                numberSquare += j;

        String s = "" + key;
        String cipherText = "";

        int textPos = 0;
        int count = 0;
        int split = s.charAt(0) - '0';
        while (true) {
            for (int j = textPos + split - 1; j >= textPos; --j) {
                if (j < plainText.length()) {
                    char c = plainText.charAt(j);
                    cipherText += numberSquare.charAt(alphabetSquare.indexOf(c));
                }
            }
            if (textPos + split >= plainText.length())
                break;

            textPos += split;
            count += 1;
            split = s.charAt(count % s.length()) - '0';
        }
        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] unused, Integer key) {

        String alphabetSquare = "AFLQVBGMRWCHNSXDIOTYEKPUZ";

        String numberSquare = "";
        for (char j : NumberString.convert(key).toCharArray())
            if (numberSquare.indexOf(j) == -1)
                numberSquare += j;

        for (char j : "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray())
            if (numberSquare.indexOf(j) == -1)
                numberSquare += j;

        String s = "" + key;
        StringBuilder plainText = new StringBuilder();

        int textPos = 0;
        int count = 0;
        int split = s.charAt(0) - '0';
        while (true) {
            System.out.println("DECODE " + key);
            for (int j = textPos + split - 1; j >= textPos; --j) {
                if (j < cipherText.length()) {
                    char c = cipherText.charAt(j);
                    plainText.append(alphabetSquare.charAt(numberSquare.indexOf(c)));
                }
            }
            if (textPos + split >= cipherText.length())
                break;

            textPos += split;
            count += 1;
            split = s.charAt(count % s.length()) - '0';
        }
        return plainText.toString().toCharArray(); // TODO Unchecked
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, Integer key) {
        return decodeEfficently(cipherText, null, key);
    }
}
