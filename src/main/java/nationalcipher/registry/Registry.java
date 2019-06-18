package nationalcipher.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

public class Registry<T> implements IRegistry<T> {

	private final Class<T> type;
	private Map<String, T> map;
	private int maxSize;
	private Optional<T> fallback;
	private Optional<BiFunction<IRegistry<T>, T, String>> namingScheme;
	private Optional<AddCallback<T>> addCallback;
	
	public Registry(Class<T> type) {
		this.type = type;
		this.map = new HashMap<>();
		this.maxSize = Integer.MAX_VALUE;
		this.fallback = Optional.empty();
		this.namingScheme = Optional.empty();
		this.addCallback = Optional.empty();
	}
	
	@Override
	public void register(@Nonnull String key, T value) {
		if(this.contains(key)) {
			System.err.println("Already contains key.");
		} else if(this.size() >= this.maxSize) {
			System.err.println("Exceeded max size.");
		} else {
			this.map.put(key, value);
			this.addCallback.ifPresent(callback -> callback.onAdd(value));
			System.out.println("Registered " + key + " to a " + this.type.getSimpleName() + " registry.");
		}
	}
	
	@Override
	public T get(String key) {
		if(this.contains(key)) {
			return this.map.get(key);
		} else {
			return this.fallback.orElse(null);
		}
	}
	

    @Override
    public boolean remove(String key) {
        if(this.contains(key)) {
            this.map.remove(key);
            System.out.println("Removed " + key + " from a " + this.type.getSimpleName() + " registry.");
            return true;
        }

        return false;
    }
	
    @Nonnull
	public BiFunction<IRegistry<T>, T, String> getDefaultNamingScheme() {
        return this.namingScheme.orElse((reg, value) -> value.getClass().getSimpleName().toLowerCase());
    }
	
	@Override
	public Stream<String> getKeys(T value) {
		return this.map.entrySet().stream()
			      .filter(entry -> value.equals(entry.getValue()))
			      .map(Entry::getKey);
	}
	
	@Override
	public Collection<String> getKeys() {
		return this.map.keySet();
	}
	
	@Override
	public Collection<T> getValues() {
		return this.map.values();
	}

	@Override
	public Collection<Entry<String, T>> getEntries() {
		return this.map.entrySet();
	}
	
	@Override
	public boolean contains(@Nonnull String key) {
		return this.map.containsKey(key);
	}
	
	@Override
	public int size() {
		return this.map.size();
	}
	
	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public void freeze() {
		this.map = Collections.unmodifiableMap(this.map);
	}
	
	@Override
	public Class<T> getType() {
		return this.type;
	}
	
	public interface AddCallback<T> {
	    
	    public void onAdd(T value);
	}
	
	public static <U> Builder<U> builder(Class<U> typeIn) {
		return new Builder<>(typeIn);
	}
	
	public static class Builder<T> {
		
		private Class<T> type;
		private Optional<Integer> maxSize;
		private Optional<T> fallback;
		private Optional<Function<T, String>> namingScheme;
	    private Optional<BiFunction<IRegistry<T>, T, String>> namingSchemeFull;
		private Optional<AddCallback<T>> addCallback;
	    
		public Builder(Class<T> typeIn) {
			this.type = typeIn;
			this.maxSize = Optional.empty();
			this.fallback = Optional.empty();
			this.namingScheme = Optional.empty();
			this.namingSchemeFull = Optional.empty();
			this.addCallback = Optional.empty();
		}
		
		public final Builder<T> setMaxSize(int sizeIn) {
			this.maxSize = Optional.of(sizeIn);
			return this;
		}
		
		public final Builder<T> setFallback(@Nonnull T fallback) {
			this.fallback = Optional.of(fallback);
			return this;
		}
		
		public final Builder<T> setNamingScheme(Function<T, String> scheme) {
            this.namingScheme = Optional.of(scheme);
            return this;
        }
		
		public final Builder<T> setNamingScheme(BiFunction<IRegistry<T>, T, String> scheme) {
            this.namingSchemeFull = Optional.of(scheme);
            return this;
        }
		
		public final Builder<T> setAddCallback(AddCallback<T> callback) {
            this.addCallback = Optional.of(callback);
            return this;
        }
		
		public final Registry<T> build() {
			Registry<T> target = new Registry<>(this.type);
			this.maxSize.ifPresent(s -> target.maxSize = s);
			this.fallback.ifPresent(f -> target.fallback = Optional.of(f));
			this.namingScheme.ifPresent(f -> target.namingScheme = Optional.of((reg, value) -> f.apply(value)));
			this.namingSchemeFull.ifPresent(f -> target.namingScheme = Optional.of(f));
			this.addCallback.ifPresent(c -> target.addCallback = Optional.of(c));
			return target;
		}
	}
}

