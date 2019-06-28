package nationalcipher.registry;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRegistry<K, T> {
    
    public boolean register(@Nonnull K key, T value);
    
    @SuppressWarnings("unchecked")
    default void registerAll(T... values) {
        for(T value : values) {
            this.register(value);
        }
    }
    
	default boolean register(T value) {
		return this.register(this.getNamingScheme().getKey(this, value), value);
	}
	
	public boolean remove(K key);
	
	public INamingScheme<K, T> getNamingScheme();
	public String getRegistryName();
	
	
	public T get(K key);
	
	public Stream<K> getKeys(T value);
	
	default Optional<K> getKey(T value) {
		return this.getKeys(value).findFirst();
	}
	
	// Wraps value in Optional
	default Optional<T> getWrapped(K key) {
		return Optional.ofNullable(this.get(key));
	}
	
    public boolean contains(@Nonnull K key);
	
    public int size();
	
    public boolean isEmpty();
	
    public @Nullable Class<K> getKeyType();
    public @Nullable Class<T> getType();
	
    public @Nonnull Collection<K>           getKeys();
	public @Nonnull Collection<T> 				 getValues();
	public @Nonnull Collection<Entry<K, T>> getEntries();
	
	public void freeze();
	
	
	
	default void accept(K key, Consumer<T> fun) {
		this.getWrapped(key).ifPresent(v -> fun.accept(v));
	}
	
	default <R> Optional<R> apply(K key, Function<T, R> fun) {
		return this.getWrapped(key).map(fun);
	}
	
	default <R> Stream<R> mapValues(Function<T, R> fun) {
        return this.getValues().stream().map(fun);
    }
	
	default <R> Map<R, T> getMap(Function<T, R> fun) {
        return this.getValues().stream().collect(Collectors.toMap(fun, Function.identity()));
    }

	public static interface INamingScheme<K, T> {

		public @Nonnull K getKey(IRegistry<K, T> reg, T value);
	}
}
