package nationalcipher.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

public class Registry<T> implements IRegistry<T> {

	private final Class<T> type;
	private Map<String, T> map;
	private int maxSize;
	private Optional<T> fallback;
	
	public Registry(Class<T> type) {
		this.type = type;
		this.map = new HashMap<>();
	}
	
	@Override
	public void register(@Nonnull String key, T value) {
		if(this.contains(key)) {
			System.err.println("Already contains key.");
		} else if(this.size() >= this.maxSize) {
			System.err.println("Exceeded max size.");
		} else {
			this.map.put(key, value);
			System.out.println("Registered " + key + " to a " + this.type.getSimpleName() + " registry.");
		}
	}
	
	@Override
	public T get(String key) {
		if(this.contains(key)) {
			return this.map.get(key);
		} else if(this.fallback.isPresent()) {
			return this.fallback.get();
		} else {
			return null;
		}
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
	
	public static <U> Builder<U> builder(Class<U> typeIn) {
		return new Builder<>(typeIn);
	}
	
	public static class Builder<T> {
		
		private Class<T> type;
		private Optional<Integer> maxSize;
		private Optional<T> fallback;
		
		public Builder(Class<T> typeIn) {
			this.type = typeIn;
			this.maxSize = Optional.empty();
			this.fallback = Optional.empty();
		}
		
		public final Builder<T> setMaxSize(int sizeIn) {
			this.maxSize = Optional.of(sizeIn);
			return this;
		}
		
		public final Builder<T> setFallback(T fallback) {
			this.fallback = Optional.of(fallback);
			return this;
		}
		
		public final Registry<T> build() {
			Registry<T> reg = new Registry<>(this.type);
			reg.maxSize = this.maxSize.orElse(Integer.MAX_VALUE);
			reg.fallback = this.fallback;
			return reg;
		}
	}
}

