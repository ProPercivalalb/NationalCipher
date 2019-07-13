package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class NicodemusCipher extends BiKeyCipher<String, Integer> {

    private VigenereType type;
    
    public NicodemusCipher(VigenereType type) {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(3, 10),
                IntegerKeyType.builder().setRange(1, 10)); //Default is 5
        this.type = type;
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<String, Integer> key, IFormat format) {
        StringBuilder cipherText = new StringBuilder(plainText.length());
        
        int[] order = new int[key.getFirstKey().length()];
        
        int p = 0;
        for(char ch = 'A'; ch <= 'Z'; ++ch) {
            int keyindex = key.getFirstKey().indexOf(ch);
            if(keyindex != -1)
                order[p++] = keyindex;
        }
        
        int start_row = 0;
        int total_row = (int)Math.ceil((double)plainText.length() / key.getFirstKey().length());
        
        while(start_row < total_row) {
            int end_row = Math.min(total_row, start_row + key.getSecondKey());
            
            for(int col = 0; col < key.getFirstKey().length(); col++) {
                for(int row = start_row; row < end_row; row++) {
                    if(row * key.getFirstKey().length() + order[col] >= plainText.length()) continue;
                    
                    cipherText.append(this.type.encode(plainText.charAt(row * key.getFirstKey().length() + order[col]), key.getFirstKey().charAt(order[col] % key.getFirstKey().length())));
                }
            }
            
            start_row = end_row;
        }
        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<String, Integer> key) {
        // Possible settings
        int READ_OFF = key.getSecondKey();

        byte[] order = new byte[key.getFirstKey().length()];
        
        int q = 0;
        for(char ch = 'A'; ch <= 'Z'; ++ch)
            for(byte i = 0; i < order.length; i++)
                if(ch == key.getFirstKey().charAt(i))
                    order[q++] = i;
        
        int blocks = (int)Math.ceil((double)cipherText.length() / (key.getFirstKey().length() * READ_OFF));
        int blockSize = key.getFirstKey().length() * READ_OFF;
        boolean complete = blocks * blockSize == cipherText.length();
        
        int index = 0;
        for(int b = 0; b < blocks; b++) {
            
            for(int r = 0; r < READ_OFF; r++) {
                for(int p = 0; p < key.getFirstKey().length(); p++) {

                    if(complete || blocks - 1 != b) {
                        int row = index % key.getFirstKey().length();
                        plainText[order[p] - row + index++] = this.type.decode(cipherText.charAt(b * blockSize + p * READ_OFF + r), key.getFirstKey().charAt(order[p] % key.getFirstKey().length()));
                    }
                    else {
                        int charactersLeft = cipherText.length() - b * blockSize;
                        int lastRow = charactersLeft % key.getFirstKey().length();
                        int esitmate = (int)Math.floor(charactersLeft / key.getFirstKey().length());
                        index = 0;
                        for(int i = 0; i < key.getFirstKey().length(); i++) {
                            int total = esitmate;
                            
                            if(lastRow > order[i]) 
                                total += 1;
                            
                            for(int j = 0; j < total; j++) {
                                byte place = order[i];
                                plainText[b * blockSize + j * key.getFirstKey().length() + place] = this.type.decode(cipherText.charAt(b * blockSize + index + j), key.getFirstKey().charAt(place % key.getFirstKey().length()));
                            }
                            
                            index += total;
                        }
                    }
                }
            }
        }
        return plainText;
    }
}
