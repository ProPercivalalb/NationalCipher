package nationalcipher.api;

import javax.annotation.Nullable;

import nationalcipher.cipher.base.TriKeyCipher;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class ProgressiveCipher extends TriKeyCipher<String, Integer, Integer>  {

    private VigenereType type;
    
    public ProgressiveCipher(VigenereType typeIn) {
        super(VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, 15).create(), IntegerKeyType.builder().setRange(2, 15).create(), IntegerKeyType.builder().setRange(2, 25).create());
        this.type = typeIn;
    }

    @Override
    public Character[] encode(Character[] plainText, TriKey<String, Integer, Integer> key, IFormat format) {
        String key2 = key.getFirstKey();
        Character[] cipherText = new Character[plainText.length];
        int progression = 0;
        int count = 0;
        for(int index = 0; index < plainText.length; index++) {
            byte charIdVig = this.type.encode((byte)(char)plainText[index], (byte)key2.charAt(index % key2.length()));
            cipherText[index] = (char)this.type.encode(charIdVig, (byte)(progression + 'A'));
            
            if(count + 1 == key.getSecondKey()) {
                count = 0;
                progression = (progression + key.getThirdKey()) % 26;
            }
            else
                count++;
            
        }
        
        return cipherText;
    }

    @Override
    public byte[] decodeEfficently(byte[] cipherText, @Nullable byte[] plainText, TriKey<String, Integer, Integer> key) {
        String key2 = key.getFirstKey();
        int progression = 0;
        int count = 0;
        for(int index = 0; index < cipherText.length; index++) {
            byte charIdProg = this.type.decode((byte)cipherText[index], (byte)(progression + 'A'));
            plainText[index] = this.type.decode((byte)charIdProg, (byte)key2.charAt(index % key2.length()));
            
            if(count + 1 == key.getSecondKey()) {
                count = 0;
                progression = (progression + key.getThirdKey()) % 26;
            }
            else
                count++;
            
        }
        
        return plainText;
    }

}
