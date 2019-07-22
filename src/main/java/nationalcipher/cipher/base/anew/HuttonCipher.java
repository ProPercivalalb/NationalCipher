package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.util.ArrayUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharacterArrayWrapper;

public class HuttonCipher extends BiKeyCipher<String, String, VariableStringKeyType.Builder, VariableStringKeyType.Builder> {

    public HuttonCipher() {
        super(VariableStringKeyType.builder().setAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXY").setRange(1, Integer.MAX_VALUE),
                VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(1, Integer.MAX_VALUE));
    }
    
    @Override
    public VariableStringKeyType.Builder limitDomainForFirstKey(VariableStringKeyType.Builder firstKey) {
        return firstKey.setRange(1, 7);
    }

    @Override
    public VariableStringKeyType.Builder limitDomainForSecondKey(VariableStringKeyType.Builder secondKey) {
        return secondKey.setRange(1, 7);
    }
    
    @Override
    public CharSequence encode(CharSequence plainText, BiKey<String, String> key, IFormat format) {
        char[] secondKey = new char[26];
        int d = 0;
        for (d = 0; d < key.getSecondKey().length(); d++) {
            secondKey[d] = key.getSecondKey().charAt(d);
        }

        for (char alpha = 'A'; alpha <= 'Z'; alpha++) {
            if (!ArrayUtil.contains(secondKey, 0, d, alpha))
                secondKey[d++] = alpha;
        }

        Character[] cipherText = new Character[plainText.length()];

        for (int i = 0; i < plainText.length(); i++) {
            int keyIndex = ArrayUtil.indexOf(secondKey, plainText.charAt(i));
            int newKeyIndex = (keyIndex + (key.getFirstKey().charAt(i % key.getFirstKey().length()) - 'A' + 1)) % secondKey.length;
            cipherText[i] = secondKey[newKeyIndex];
            secondKey[keyIndex] = cipherText[i];
            secondKey[newKeyIndex] = plainText.charAt(i);
        }

        return new CharacterArrayWrapper(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<String, String> key) {
        char[] secondKey = new char[26];
        int d = 0;
        for (d = 0; d < key.getSecondKey().length(); d++) {
            secondKey[d] = key.getSecondKey().charAt(d);
        }

        for (char alpha = 'A'; alpha <= 'Z'; alpha++) {
            if (!ArrayUtil.contains(secondKey, 0, d, alpha))
                secondKey[d++] = alpha;
        }

        for (int i = 0; i < cipherText.length(); i++) {
            int keyIndex = ArrayUtil.indexOf(secondKey, cipherText.charAt(i));
            int newKeyIndex = (keyIndex - (key.getFirstKey().charAt(i % key.getFirstKey().length()) - 'A' + 1) + secondKey.length) % secondKey.length;
            plainText[i] = secondKey[newKeyIndex];
            secondKey[keyIndex] = (char) plainText[i];
            secondKey[newKeyIndex] = cipherText.charAt(i);
        }

        return plainText;
    }
}
