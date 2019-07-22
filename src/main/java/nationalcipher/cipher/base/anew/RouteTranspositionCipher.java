package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.QuadKeyCipher;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.base.keys.ObjectKeyType;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.transposition.RouteCipherType;
import nationalcipher.cipher.transposition.Routes;
import nationalcipher.util.CharArrayWrapper;

public class RouteTranspositionCipher extends QuadKeyCipher<Integer, Integer, RouteCipherType, RouteCipherType, IntegerKeyType.Builder, IntegerKeyType.Builder, ObjectKeyType.Builder<RouteCipherType>, ObjectKeyType.Builder<RouteCipherType>> {

    public RouteTranspositionCipher() {
        super(IntegerKeyType.builder().setRange(1, Integer.MAX_VALUE),
                IntegerKeyType.builder().setRange(1, Integer.MAX_VALUE),
                ObjectKeyType.<RouteCipherType>builder().setUniverse(Routes.getRoutes().toArray(new RouteCipherType[0])),
                ObjectKeyType.<RouteCipherType>builder().setUniverse(Routes.getRoutes().toArray(new RouteCipherType[0])));
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, QuadKey<Integer, Integer, RouteCipherType, RouteCipherType> key) {
        int blockSize = key.getFirstKey() * key.getSecondKey();

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
    public IKeyBuilder<Integer> limitDomainForFirstKey(IntegerKeyType.Builder firstKey) {
        return firstKey.setRange(1, 100);
    }
    
    @Override
    public IKeyBuilder<Integer> limitDomainForSecondKey(IntegerKeyType.Builder secondKey) {
        return secondKey.setRange(1, 100);
    }
    
    @Override
    public CharSequence encode(CharSequence plainText, QuadKey<Integer, Integer, RouteCipherType, RouteCipherType> key, IFormat format) {
        // Create pattern
        int[] gridWrite = key.getThirdKey().getPattern(key.getFirstKey(), key.getSecondKey(), plainText.length());
        int[] gridRead = key.getFourthKey().getPattern(key.getFirstKey(), key.getSecondKey(), plainText.length());
        // Reads across the grid
        char[] gridString = new char[plainText.length()];
        for (int i = 0; i < plainText.length(); i++)
            gridString[gridWrite[i]] = plainText.charAt(i);
        
        char[] gridString2 = new char[plainText.length()];
        for (int i = 0; i < plainText.length(); i++)
            gridString2[i] = gridString[gridRead[i]];
        
        return new CharArrayWrapper(gridString2);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, QuadKey<Integer, Integer, RouteCipherType, RouteCipherType> key) {
        // Create pattern
        int[] gridWrite = key.getThirdKey().getPattern(key.getFirstKey(), key.getSecondKey(), cipherText.length());
        int[] gridRead = key.getFourthKey().getPattern(key.getFirstKey(), key.getSecondKey(), cipherText.length());

        char[] gridString = new char[cipherText.length()];
        for (int i = 0; i < cipherText.length(); i++)
            gridString[gridRead[i]] = cipherText.charAt(i);

        for (int i = 0; i < cipherText.length(); i++)
            plainText[i] = gridString[gridWrite[i]];

        return plainText;
    }
}
