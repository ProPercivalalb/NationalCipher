package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.string.MorseCode;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.PolluxKeyType;
import nationalcipher.util.CharacterArrayWrapper;

public class PolluxCipher extends UniKeyCipher<Character[]> {

    public PolluxCipher() {
        super(PolluxKeyType.builder());
    }
    
    private static char[] DIGIT_STR = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    @Override
    public CharSequence encode(CharSequence plainText, Character[] key, IFormat format) {

        String morseText = MorseCode.getMorseEquivalent(plainText);
        
        morseText = MorseCode.getMorseEquivalent(plainText);
        
        Character[] cipherText = new Character[morseText.length()];
        
        
        for(int i = 0; i < morseText.length(); i++) {
            char a = morseText.charAt(i);
            int no = 0; //Between 0-9 inclusive
            while(key[(no = RandomUtil.pickRandomInt(key.length))] != a) {}
            cipherText[i] = DIGIT_STR[no];
        }
        
        return new CharacterArrayWrapper(cipherText);
    }
    
    @Override
    public char[] decodeEfficently(CharSequence cipherText, Character[] key) {
        return decodeEfficently(cipherText, null, key);
    }
    
    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, Character[] key) {
        char[] morseText = new char[cipherText.length()];
        
        for(int i = 0; i < cipherText.length(); i++) {
            morseText[i] = key[cipherText.charAt(i) - '0'];
        }
        
        StringBuilder builder = new StringBuilder(cipherText.length() / 4);
        
        int last = 0;
        for(int i = 0; i < morseText.length; i++) {
            char a = morseText[i];
            boolean end = i == morseText.length - 1;
            if(a == 'X' || end) {
                Character morseChar = MorseCode.getCharFromMorse(morseText, last, i - last + (end ? 1 : 0));
                if(morseChar != null) {
                    builder.append((char)morseChar); // Cast to char is more efficient
                } else {
                    builder.append(morseText, last, i - last + (end ? 1 : 0));
                }
                last = i + 1;
            }
        }
        
        return builder.toString().toCharArray(); //TODO Unchecked
    }
    
    @Override
    public boolean deterministic() {
        return false;
    }
}
