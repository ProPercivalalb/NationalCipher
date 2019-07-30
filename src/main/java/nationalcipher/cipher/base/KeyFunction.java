package nationalcipher.cipher.base;

@FunctionalInterface
public interface KeyFunction<K> {

    public boolean apply(K key);
}
