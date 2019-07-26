package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.EnumKeyType;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;
import nationalcipher.util.CharArrayWrapper;

public class NihilistTranspositionCipher extends BiKeyCipher<Integer[], ReadMode, OrderedIntegerKeyType.Builder, EnumKeyType.Builder<ReadMode>> {

    public NihilistTranspositionCipher() {
        super(OrderedIntegerKeyType.builder().setRange(2, Integer.MAX_VALUE),
                EnumKeyType.builder(ReadMode.class).setUniverse(ReadMode.values()));
    }

    @Override
    public IKeyBuilder<Integer[]> limitDomainForFirstKey(OrderedIntegerKeyType.Builder firstKey) {
        return firstKey.setRange(2, 7);
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, BiKey<Integer[], ReadMode> key) {
        int blockSize = key.getFirstKey().length * key.getFirstKey().length;

        if (plainText.length() % blockSize != 0) {
            StringBuilder builder = new StringBuilder(plainText.length() + blockSize - (plainText.length() % blockSize));
            builder.append(plainText);
            while (builder.length() % blockSize != 0) {
                builder.append('X');
            }

            return builder;
        } else {
            return plainText;
        }
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<Integer[], ReadMode> key, IFormat format) {
        int blockSize = key.getFirstKey().length * key.getFirstKey().length;

        if(plainText.length() != blockSize) {
            StringBuilder builder = new StringBuilder(plainText.length());
            for(int i = 0; i < plainText.length(); i += blockSize) {
                builder.append(encodeSection(plainText, i, key));
            }

            return builder;
        }
        else {
            return encodeSection(plainText, 0, key);
        }
    }
    
    public static CharSequence encodeSection(CharSequence plainText, int start, BiKey<Integer[], ReadMode> key) {
        char[] cipherText = new char[key.getFirstKey().length * key.getFirstKey().length];
        int index = 0;
        switch (key.getSecondKey()) {
        case ACROSS:
            for (int row = 0; row < key.getFirstKey().length; row++) {
                for (int column = 0; column < key.getFirstKey().length; column++) {
                    cipherText[index++] = plainText.charAt(start + key.getFirstKey()[row] * key.getFirstKey().length + key.getFirstKey()[column]);
                }
            }
            break;
        case DOWN:
            for (int column = 0; column < key.getFirstKey().length; column++) {
                for (int row = 0; row < key.getFirstKey().length; row++) {
                    cipherText[index++] = plainText.charAt(start + key.getFirstKey()[row] * key.getFirstKey().length + key.getFirstKey()[column]);
                }
            }
            break;
        }
            
        return new CharArrayWrapper(cipherText);
    }

    // TODO decode longer texts
    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<Integer[], ReadMode> key) {

        int columns = key.getFirstKey().length;

        int[] reversedOrder = new int[columns];
        for (int i = 0; i < columns; i++)
            reversedOrder[key.getFirstKey()[i]] = i;
        
        int blockSize = key.getFirstKey().length * key.getFirstKey().length;
        if(cipherText.length() != blockSize) {
            for(int i = 0; i < cipherText.length(); i += blockSize) {
                plainText = decodeSection(cipherText, plainText, i, key, reversedOrder);
            }
        }
        else {
            plainText = decodeSection(cipherText, plainText, 0, key, reversedOrder);
        }

        return plainText;
    }
    
    public static char[] decodeSection(CharSequence cipherText, char[] plainText, int start, BiKey<Integer[], ReadMode> key, int[] reversedOrder) {
        int index = start;
        switch (key.getSecondKey()) {
        case ACROSS:
            for (int row = 0; row < key.getFirstKey().length; row++) {
                for (int column = 0; column < key.getFirstKey().length; column++) {
                    plainText[index++] = cipherText.charAt(start + reversedOrder[row] * key.getFirstKey().length + reversedOrder[column]);
                }
            }
            break;
        case DOWN:
            for (int column = 0; column < key.getFirstKey().length; column++) {
                for (int row = 0; row < key.getFirstKey().length; row++) {
                    plainText[index++] = cipherText.charAt(start + reversedOrder[row] * key.getFirstKey().length + reversedOrder[column]);
                }
            }
            break;
        }
        
        return plainText;
    }
}
