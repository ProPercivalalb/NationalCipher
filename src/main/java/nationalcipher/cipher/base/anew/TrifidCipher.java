package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.base.keys.IntegerGenKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharacterArrayWrapper;

public class TrifidCipher extends BiKeyCipher<String, Integer, FullStringKeyType.Builder, IntegerGenKeyType.Builder> {

    public TrifidCipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_27_CHARS),
                IntegerGenKeyType.builder().setRange(0, 5000).addFilter(i -> i != 1));
    }
    
    @Override
    public IntegerGenKeyType.Builder limitDomainForSecondKey(IntegerGenKeyType.Builder secondKey) {
        return secondKey.setRange(0, 15);
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<String, Integer> key, IFormat format) {
        int period = key.getSecondKey();
        if (period == 0)
            period = plainText.length();
        int[] numberText = new int[plainText.length() * 3];
        for (int i = 0; i < plainText.length(); i++) {

            char a = plainText.charAt(i);

            int index = key.getFirstKey().indexOf(a);
            int tableNo = index / 9 + 1;
            int rowNo = (int) (index / 3) % 3 + 1;
            int colNo = index % 3 + 1;
            int blockBase = (int) (i / period) * (period * 3) + i % period;
            int min = Math.min(period, plainText.length() - (int) (i / period) * period);

            numberText[blockBase] = tableNo;
            numberText[blockBase + min] = rowNo;
            numberText[blockBase + min * 2] = colNo;
        }

        Character[] cipherText = new Character[plainText.length()];
        int index = 0;

        for (int i = 0; i < numberText.length; i += 3) {

            int a = numberText[i] - 1;
            int b = numberText[i + 1] - 1;
            int c = numberText[i + 2] - 1;
            cipherText[index++] = key.getFirstKey().charAt(a * 9 + b * 3 + c);
        }

        return new CharacterArrayWrapper(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<String, Integer> key) {
        int period = key.getSecondKey();
        byte[] numberText = new byte[cipherText.length() * 3]; // TODO Use resuseable one
        if (period == 0)
            period = cipherText.length();

        int blocks = (int) Math.ceil(cipherText.length() / (double) period);

        int indexNo = 0;
        int index = 0;

        for (int b = 0; b < blocks; b++) {
            int chPass = b * period;
            int noPass = chPass * 3;
            int min = Math.min(period, cipherText.length() - chPass);

            for (int f = 0; f < min; f++) {
                int index1 = key.getFirstKey().indexOf(cipherText.charAt(chPass + f));

                numberText[indexNo++] = (byte) (index1 / 9);
                numberText[indexNo++] = (byte) ((int) (index1 / 3) % 3);
                numberText[indexNo++] = (byte) (index1 % 3);
            }

            for (int f = 0; f < min; f++)
                plainText[index++] = key.getFirstKey().charAt(numberText[noPass + f] * 9 + numberText[noPass + min + f] * 3 + numberText[noPass + min * 2 + f]);
        }

        return plainText;
    }
}
