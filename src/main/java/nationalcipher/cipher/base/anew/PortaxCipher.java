package nationalcipher.cipher.base.anew;

import java.util.Arrays;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class PortaxCipher extends UniKeyCipher<String, VariableStringKeyType.Builder> {

    public PortaxCipher() {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, Integer.MAX_VALUE));
    }
    
    @Override
    public VariableStringKeyType.Builder limitDomainForFirstKey(VariableStringKeyType.Builder firstKey) {
        return firstKey.setRange(2, 15);
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, String key) {
        if (plainText.length() % 2 == 1) {
            StringBuilder builder = new StringBuilder(plainText.length() + 1);
            builder.append(plainText);
            builder.append('X');
            return builder;
        } else {
            return plainText;
        }
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        return decode(plainText, key);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        int period = key.length();

        String[] slidingKey = new String[period];
        Arrays.fill(slidingKey, "");

        for (int i = 0; i < key.length(); i++) {
            char slidingChar = key.charAt(i);
            int slide = (slidingChar - 'A') / 2;
            for (int s = 0; s < 13; s++)
                slidingKey[i] += (char) ((13 + s - slide) % 13 + 'A');
        }

        for (int i = 0; i < cipherText.length(); i += period * 2) {
            int actingPeriod = Math.min((cipherText.length() - i) / 2, period);
            for (int j = 0; j < actingPeriod; j++) {

                char a = cipherText.charAt(i + j);
                char b = cipherText.charAt(i + j + actingPeriod);

                int row = (b - 'A') % 2;
                int column = (b - 'A') / 2;
                char c;
                char d;
                if (a <= 'M') {
                    int aIndex = slidingKey[j].indexOf(a);
                    if (aIndex == column) {
                        c = (char) (column + 'N');
                        d = (char) (aIndex * 2 + (row + 1) % 2 + 'A');
                    } else {
                        c = slidingKey[j].charAt(column);
                        d = (char) (aIndex * 2 + row + 'A');
                    }
                } else {
                    int aIndex = a - 'N';
                    if (aIndex == column) {
                        c = slidingKey[j].charAt(column);
                        d = (char) (aIndex * 2 + (row + 1) % 2 + 'A');
                    } else {
                        c = (char) (column + 'N');
                        d = (char) (aIndex * 2 + row + 'A');
                    }
                }

                plainText[i + j] = c;
                plainText[i + j + actingPeriod] = d;
            }
        }

        return plainText;
    }

}
