package nationalcipher.api;

import java.math.BigInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

public interface IKeyType<K> {

    /**
     * 
     * @param fullKey
     * @return
     */
    K randomise(@Nullable Object partialKey);

    void iterateKeys(@Nullable Object partialKey, Consumer<K> consumer);

    K alterKey(Object fullKey, K key);

    boolean isValid(@Nullable Object fullKey, K key);

    BigInteger getNumOfKeys();

    default K inverseKey(K key) {
        return key;
    }

    default String prettifyKey(K key) {
        return key.toString();
    }

    public interface IKeyBuilder<K> {

        public IKeyType<K> create();
        
        default IKeyBuilder<K> setDisplay(Function<K, String> displayFunc) {
            throw new UnsupportedOperationException();
        }
    }
}
