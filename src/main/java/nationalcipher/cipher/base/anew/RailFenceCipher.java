package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.IntegerKeyType;

public class RailFenceCipher extends BiKeyCipher<Integer, Integer, IntegerKeyType.Builder, IntegerKeyType.Builder> {

    // TODO Add read off diagonals mode

    public RailFenceCipher() {
        super(IntegerKeyType.builder().setRange(2, Integer.MAX_VALUE / 2 - 2), IntegerKeyType.builder().setMin(0).setVariableMax(obj -> ((BiKey<Integer, Integer>) obj).getFirstKey() * 2 - 2));
    }
    
    @Override
    public IntegerKeyType.Builder limitDomainForFirstKey(IntegerKeyType.Builder firstKey) {
        return firstKey.setRange(2, 50);
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<Integer, Integer> key, IFormat format) {
        return RedefenceCipher.encodeGeneral(plainText, key.getFirstKey(), key.getSecondKey(), i -> i);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<Integer, Integer> key) {
        return RedefenceCipher.decodeGeneral(cipherText, plainText, key.getFirstKey(), key.getSecondKey(), i -> i);
    }
}
