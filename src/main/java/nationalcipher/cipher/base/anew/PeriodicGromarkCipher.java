package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.math.MathUtil;
import javalibrary.string.StringTransformer;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class PeriodicGromarkCipher extends UniKeyCipher<String, VariableStringKeyType.Builder> {

    public PeriodicGromarkCipher() {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, Integer.MAX_VALUE));
    }
    
    @Override
    public VariableStringKeyType.Builder limitDomainForFirstKey(VariableStringKeyType.Builder firstKey) {
        return firstKey.setRange(2, 8);
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {

        /**
         * char[] transBlock = new char[26]; int k = 0; for(; k < key.length(); k++) {
         * transBlock[k] = key.charAt(k); }
         * 
         * for(char ch = 'A'; ch <= 'Z'; ++ch) { if(!key.contains(String.valueOf(ch))) {
         * transBlock[k++] = ch; } }
         * 
         * System.out.println(Arrays.toString(transBlock));
         **/

        int[] inOrd = new int[key.length()];
        int[] noOrd = new int[key.length()];

        int p = 0;
        for (char ch = 'A'; ch <= 'Z'; ++ch) {
            int keyindex = key.indexOf(ch);
            if (keyindex != -1) {
                inOrd[p++] = keyindex;
                noOrd[keyindex] = p;
            } else
                key += ch;
        }

        String transposedKey = "";
        int[] numericKey = new int[plainText.length()];

        for (int i = 0; i < inOrd.length; i++) {
            transposedKey += StringTransformer.getEveryNthChar(key, inOrd[i], inOrd.length);
            numericKey[i] = noOrd[i];
        }

        for (int i = 0; i < numericKey.length - noOrd.length; i++)
            numericKey[i + noOrd.length] = (numericKey[i] + numericKey[i + 1]) % 10;

        StringBuilder cipherText = new StringBuilder(plainText.length());

        for (int i = 0; i < plainText.length(); i++) {
            int keyIndex = (int) (Math.floor((double) i / inOrd.length) % inOrd.length);
            cipherText.append(transposedKey.charAt((transposedKey.indexOf(key.charAt(keyIndex)) + (plainText.charAt(i) - 'A') + numericKey[i]) % 26));
        }

        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        int[] inOrd = new int[key.length()];
        int[] noOrd = new int[key.length()];

        char[] keyFull = new char[26];
        int index = 0;
        for (; index < key.length(); index++)
            keyFull[index] = key.charAt(index);

        int p = 0;
        for (char ch = 'A'; ch <= 'Z'; ++ch) {
            int keyindex = key.indexOf(ch);
            if (keyindex != -1) {
                inOrd[p++] = keyindex;
                noOrd[keyindex] = p;
            } else
                keyFull[index++] = ch;
        }

        int[] transposedKeyIndexOf = new int[26];
        int[] numericKey = new int[cipherText.length()];
        int rows = (int) Math.ceil(26D / key.length());
        index = 0;

        for (int i = 0; i < inOrd.length; i++) {
            for (int r = 0; r < rows; r++) {
                if (r * inOrd.length + inOrd[i] >= 26)
                    break;
                transposedKeyIndexOf[keyFull[r * inOrd.length + inOrd[i]] - 'A'] = index++;
            }

            numericKey[i] = noOrd[i];
        }

        for (int i = 0; i < numericKey.length - noOrd.length; i++)
            numericKey[i + noOrd.length] = (numericKey[i] + numericKey[i + 1]) % 10;

        for (int i = 0; i < cipherText.length(); i++) {
            int keyIndex = (int) (Math.floor(i / inOrd.length) % inOrd.length);
            plainText[i] = (char) (MathUtil.mod(transposedKeyIndexOf[cipherText.charAt(i) - 'A'] - transposedKeyIndexOf[key.charAt(keyIndex) - 'A'] - numericKey[i], 26) + 'A');
        }

        return plainText;
    }

}
