package nationalcipher.api;

import java.math.BigInteger;
import java.util.function.Consumer;

public interface IKeyType<K> {

	K randomise();
	
	void iterateKeys(Consumer<K> consumer);
	
	BigInteger getNumOfKeys();
	
	boolean isValid(K key);
	
	default K inverseKey(K key) {
		return key;
	}
}
