package nationalcipher.cipher.base;

@FunctionalInterface
public interface KeyConsumer<K> extends KeyFunction<K> {

    @Override
    default boolean apply(K key) {
        this.accept(key);
        return true;
    }
    
    public void accept(K Key);
}
