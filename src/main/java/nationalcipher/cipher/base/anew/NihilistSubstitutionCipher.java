package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class NihilistSubstitutionCipher extends BiKeyCipher<String, String, FullStringKeyType.Builder, VariableStringKeyType.Builder> {

    public NihilistSubstitutionCipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS), VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setRange(2, Integer.MAX_VALUE));
    }

    @Override
    public VariableStringKeyType.Builder limitDomainForSecondKey(VariableStringKeyType.Builder secondKey) {
        return secondKey.setRange(2, 15);
    }
    
    @Override
    public CharSequence padPlainText(CharSequence plainText, BiKey<String, String> key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : c);
        }

        return builder;
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<String, String> key, IFormat format) {
        String cipherText = "";
        for (int i = 0; i < plainText.length(); i++) {
            if (i < key.getSecondKey().length())
                System.out.print(" " + getNumberValue(key.getSecondKey().charAt(i % key.getSecondKey().length()), key.getFirstKey()));
            int no = getNumberValue(plainText.charAt(i), key.getFirstKey()) + getNumberValue(key.getSecondKey().charAt(i % key.getSecondKey().length()), key.getFirstKey());
            if (no >= 100)
                no -= 100;
            String strNo = "" + no;
            if (strNo.length() < 2)
                strNo = "0" + strNo;
            cipherText += strNo;
        }
        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, BiKey<String, String> key) {
        return decodeEfficently(cipherText, new char[cipherText.length() / 2], key);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<String, String> key) {

        for (int i = 0; i < cipherText.length() / 2; i++) {
            int no = (cipherText.charAt(i * 2) - '0') * 10 + (cipherText.charAt(i * 2 + 1) - '0');
            if (no <= 10)
                no += 100;

            no -= getNumberValue(key.getSecondKey().charAt(i % key.getSecondKey().length()), key.getFirstKey());

            int column = no % 10;

            int row = (no - column) / 10 - 1;

            // TODO if(row * 5 + column - 1 >= key.getFirstKey().length() || row * 5 +
            // column - 1 < 0) return StringTransformer.repeat("Z",
            // plainText.length).toCharArray();
            plainText[i] = key.getFirstKey().charAt(row * 5 + column - 1);
        }

        return plainText;
    }

    private static int getNumberValue(char character, String keysquare) {
        int index = keysquare.indexOf(character);
        int row = (int) (index / 5) + 1;
        int column = index % 5 + 1;
        return row * 10 + column;
    }
}
