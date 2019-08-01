package nationalcipher.api;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.function.Function;

import nationalcipher.cipher.base.KeyFunction;

public interface IKeyType<K> {

    /**
     * 
     * @param fullKey
     * @return
     */
    K randomise();

    public boolean iterateKeys(KeyFunction<K> consumer);

    K alterKey(K key);

    boolean isValid(K key);

    BigInteger getNumOfKeys();

    default K inverseKey(K key) {
        return key;
    }

    default String prettifyKey(K key) {
        return key.toString();
    }

    default K parse(String input) throws ParseException {
        throw new UnsupportedOperationException();
    }

    default String getHelp() {
        return this.getClass().getSimpleName();
    }
    
    public interface IKeyBuilder<K> {

        public IKeyType<K> create();
        
        default IKeyBuilder<K> setDisplay(Function<K, String> displayFunc) {
            throw new UnsupportedOperationException();
        }
    }
}
