package nationalcipher.registry;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

public interface IRegistry<T> {

    public void register(@Nonnull String key, T value);
    
    @SuppressWarnings("unchecked")
    default void registerAll(T... values) {
        for(T value : values) {
            this.register(value);
        }
    }
    
	default void register(T value) {
		String key = null;
		
		if(value instanceof INameProvider) {
			key = ((INameProvider)value).getKey();
		} else {
			key = value.getClass().getSimpleName().toLowerCase();
		}
		
		this.register(key, value);
	}
	
	public T get(String key);
	
	
	public Stream<String> getKeys(T value);
	
	default Optional<String> getKey(T value) {
		return this.getKeys(value).findFirst();
	}
	
	default Optional<T> getOptional(String key) {
		return Optional.ofNullable(this.get(key));
	}
	
    public boolean contains(@Nonnull String key);
	
    public int size();
	
    public boolean isEmpty();
	
    public Class<T> getType();
	
    public @Nonnull Collection<String>           getKeys();
	public @Nonnull Collection<T> 				 getValues();
	public @Nonnull Collection<Entry<String, T>> getEntries();
	
	public void freeze();
	
	default void accept(String key, Consumer<T> fun) {
		this.getOptional(key).ifPresent(v -> fun.accept(v));
	}
	
	default <R> Optional<R> apply(String key, Function<T, R> fun) {
		return this.getOptional(key).map(fun);
	}

	public static interface INameProvider {

		public @Nonnull String getKey();
	}
}
