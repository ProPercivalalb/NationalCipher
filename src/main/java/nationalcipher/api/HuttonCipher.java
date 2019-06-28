package nationalcipher.api;

import javax.annotation.Nullable;

import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class HuttonCipher extends BiKeyCipher<String, String> {

    public HuttonCipher() {
        super(VariableStringKeyType.builder().setAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXY").setRange(2, 7).create(), VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, 7).create());
    }

    @Override
    public Character[] encode(Character[] plainText, BiKey<String, String> key, IFormat format) {
        
        char[] firstKey = key.getFirstKey().toCharArray();
        char[] secondKey = new char[26];
        int d = 0;
        for(d = 0; d < key.getSecondKey().length(); d++) {
            secondKey[d] = key.getSecondKey().charAt(d);
        }
        
        for(char alpha = 'A'; alpha <= 'Z'; alpha++) {
            if(!ArrayUtil.contains(secondKey, 0, d, alpha))
                secondKey[d++] = alpha;
        }
        
        Character[] cipherText = new Character[plainText.length];
        
        for(int i = 0; i < plainText.length; i++) {
            int keyIndex = ArrayUtil.indexOf(secondKey, plainText[i]);
            int newKeyIndex = (keyIndex + (firstKey[i % firstKey.length] - 'A' + 1)) % secondKey.length;
            cipherText[i] = secondKey[newKeyIndex];
            secondKey[keyIndex] = cipherText[i];
            secondKey[newKeyIndex] = plainText[i];
        }
        
        return cipherText;
    }
    
    @Override
    public byte[] decodeEfficently(byte[] cipherText, @Nullable byte[] plainText, BiKey<String, String> key) {
        
        char[] firstKey = key.getFirstKey().toCharArray();
        byte[] secondKey = new byte[26];
        int d = 0;
        for(d = 0; d < key.getSecondKey().length(); d++) {
            secondKey[d] = (byte)key.getSecondKey().charAt(d);
        }
        
        for(byte alpha = 'A'; alpha <= 'Z'; alpha++) {
            if(!ArrayUtil.contains(secondKey, 0, d, alpha))
                secondKey[d++] = alpha;
        }
        
        for(int i = 0; i < cipherText.length; i++) {
            int keyIndex = ArrayUtil.indexOf(secondKey, cipherText[i]);
            int newKeyIndex = (keyIndex - (firstKey[i % firstKey.length] - 'A' + 1) + secondKey.length) % secondKey.length;
            plainText[i] = (byte)secondKey[newKeyIndex];
            secondKey[keyIndex] = plainText[i];
            secondKey[newKeyIndex] = cipherText[i];
        }
        
        return plainText;
    }
}
