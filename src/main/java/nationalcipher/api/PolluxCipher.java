package nationalcipher.api;

import javalibrary.string.MorseCode;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.PolluxKeyType;

public class PolluxCipher extends UniKeyCipher<Character[]> {

    public PolluxCipher() {
        super(PolluxKeyType.builder().create());
    }
    
    private static char[] DIGIT_STR = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    @Override
    public Character[] encode(Character[] plainText, Character[] key, IFormat format) {

        String morseText = MorseCode.getMorseEquivalent(plainText);
        
        morseText = MorseCode.getMorseEquivalent(plainText);
        
        Character[] cipherText = new Character[morseText.length()];
        
        
        for(int i = 0; i < morseText.length(); i++) {
            char a = morseText.charAt(i);
            int no = 0; //Between 0-9 inclusive
            while(key[(no = RandomUtil.pickRandomInt(key.length))] != a) {}
            cipherText[i] = DIGIT_STR[no];
        }
        
        return cipherText;
    }
    
    @Override
    public byte[] decodeEfficently(byte[] cipherText, Character[] key) {
        char[] morseText = new char[cipherText.length];
        
        for(int i = 0; i < cipherText.length; i++) {
            int a = cipherText[i] - '0';
        
            morseText[i] = key[a];
        }
        
        String plainText = "";
        
        int last = 0;
        for(int i = 0; i < morseText.length; i++) {
            char a = morseText[i];
            boolean end = i == morseText.length - 1;
            if(a == 'X' || end) {
                String code = new String(morseText, last, i - last + (end ? 1 : 0));
                
                last = i + 1;
                try {
                    plainText += MorseCode.getCharFromMorse(code);
                }
                catch(NullPointerException e) {
                    plainText += code;
                }
            }
        }
        
        return plainText.getBytes(); //TODO Unchecked
    }
}
