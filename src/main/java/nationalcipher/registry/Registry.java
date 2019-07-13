package nationalcipher.registry;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nationalcipher.util.ClassDiscoverer;

public class Registry<K, T> implements IRegistry<K, T> {

    @Nullable
    private final Class<K> keyType;
    @Nullable
    private final Class<T> type;
    private String registryName;
    private Map<K, T> map;
    private int maxSize;
    private Optional<T> fallback;
    private Optional<INamingScheme<K, T>> namingScheme;
    private Optional<ValueValidation<T>> validation;
    private Optional<AddCallback<K, T>> addCallback;
    private Optional<RemoveCallback<K, T>> removeCallback;

    private Registry(Class<K> keyType, Class<T> type, Supplier<Map<K, T>> mapSupplier) {
        this.keyType = keyType;
        this.type = type;
        this.map = mapSupplier.get();
        this.maxSize = Integer.MAX_VALUE;
        this.fallback = Optional.empty();
        this.namingScheme = Optional.empty();
        this.validation = Optional.empty();
        this.addCallback = Optional.empty();
        this.removeCallback = Optional.empty();
    }

    @Override
    public boolean register(@Nonnull K key, T value) {
        if (this.validation.isPresent() && !this.validation.get().isValid(value)) {
            System.err.println(this.validation.get().getErrorMessage(value));
            return false;
        } else if (this.contains(key)) {
            System.err.println("Already contains key.");
            return false;
        } else if (this.size() >= this.maxSize) {
            System.err.println("Exceeded max size.");
            return false;
        } else {
            this.map.put(key, value);
            this.addCallback.ifPresent(callback -> callback.onAdd(key, value));
            System.out.println("Registered " + key + " to '" + this.getRegistryName() + "' registry.");
            return true;
        }
    }

    @Override
    public T get(K key) {
        if (this.contains(key)) {
            return this.map.get(key);
        } else {
            return this.fallback.orElse(null);
        }
    }

    @Override
    public boolean remove(K key) {
        if (this.contains(key)) {
            T value = this.map.remove(key);
            this.removeCallback.ifPresent(callback -> callback.onRemove(key, value));
            System.out.println("Removed " + key + " from '" + this.getRegistryName() + "' registry.");
            return true;
        }

        return false;
    }

    @Nonnull
    @Override
    public INamingScheme<K, T> getNamingScheme() {
        return this.namingScheme.orElseGet(null);
    }

    @Override
    public String getRegistryName() {
        return this.registryName;
    }

    @Override
    public Stream<K> getKeys(T value) {
        return this.map.entrySet().stream().filter(entry -> value.equals(entry.getValue())).map(Entry::getKey);
    }

    @Override
    public Collection<K> getKeys() {
        return this.map.keySet();
    }

    @Override
    public Collection<T> getValues() {
        return this.map.values();
    }

    @Override
    public Collection<Entry<K, T>> getEntries() {
        return this.map.entrySet();
    }

    @Override
    public boolean contains(@Nonnull K key) {
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

    @Nullable
    @Override
    public Class<K> getKeyType() {
        return this.keyType;
    }

    @Nullable
    @Override
    public Class<T> getType() {
        return this.type;
    }

    @FunctionalInterface
    public interface AddCallback<K, T> {
        public void onAdd(@Nonnull K key, T value);
    }

    @FunctionalInterface
    public interface RemoveCallback<K, T> {
        public void onRemove(@Nonnull K key, T value);
    }

    @FunctionalInterface
    public interface ValueValidation<T> {
        public boolean isValid(T value);

        default String getErrorMessage(T value) {
            return "Unable to add " + value;
        }
    }

    public static <K, U> Builder<K, U> builderNull() {
        return builder(null, null);
    }

    public static <U> Builder<String, U> builder() {
        return builder(null);
    }

    public static <U> Builder<String, U> builder(Class<U> type) {
        return new Builder<>(String.class, type).setNamingScheme((reg, value) -> value.getClass().getSimpleName().toLowerCase());
    }

    public static <K, U> Builder<K, U> builder(Class<K> keyType, Class<U> type) {
        return new Builder<>(keyType, type);
    }

    public static class Builder<K, T> {

        private Class<K> keyType;
        private Class<T> type;
        private Supplier<Map<K, T>> mapSupplier;
        private Optional<String> registryName;
        private Optional<Integer> maxSize;
        private Optional<T> fallback;
        private Optional<Function<T, K>> namingScheme;
        private Optional<INamingScheme<K, T>> namingSchemeFull;
        private Optional<ValueValidation<T>> validation;
        private Optional<AddCallback<K, T>> addCallback;
        private Optional<RemoveCallback<K, T>> removeCallback;
        private boolean autoDiscover;
        private Optional<Class<? extends Annotation>> annotationAutoDisco;

        public Builder(Class<K> keyType, Class<T> typeIn) {
            this.keyType = keyType;
            this.type = typeIn;
            this.mapSupplier = HashMap::new;
            this.registryName = Optional.empty();
            this.maxSize = Optional.empty();
            this.fallback = Optional.empty();
            this.namingScheme = Optional.empty();
            this.namingSchemeFull = Optional.empty();
            this.validation = Optional.empty();
            this.addCallback = Optional.empty();
            this.removeCallback = Optional.empty();
            this.autoDiscover = false;
            this.annotationAutoDisco = Optional.empty();
        }

        public final Builder<K, T> setMapType(Supplier<Map<K, T>> mapSupplier) {
            this.mapSupplier = mapSupplier;
            return this;
        }

        public final Builder<K, T> setRegistryName(String registryName) {
            this.registryName = Optional.of(registryName);
            return this;
        }

        public final Builder<K, T> setMaxSize(int sizeIn) {
            if (sizeIn < 0) {
                sizeIn = 0;
            }

            this.maxSize = Optional.of(sizeIn);
            return this;
        }

        public final Builder<K, T> setFallback(@Nonnull T fallback) {
            this.fallback = Optional.of(fallback);
            return this;
        }

        public final Builder<K, T> setNamingScheme(Function<T, K> scheme) {
            this.namingScheme = Optional.of(scheme);
            return this;
        }

        public final Builder<K, T> setNamingScheme(INamingScheme<K, T> scheme) {
            this.namingSchemeFull = Optional.of(scheme);
            return this;
        }

        public final Builder<K, T> addValidation(ValueValidation<T> validation) {
            this.validation = Optional.of(validation);
            return this;
        }

        public final Builder<K, T> setAddCallback(AddCallback<K, T> callback) {
            this.addCallback = Optional.of(callback);
            return this;
        }

        public final Builder<K, T> setRemoveCallback(RemoveCallback<K, T> callback) {
            this.removeCallback = Optional.of(callback);
            return this;
        }

        public final Builder<K, T> setAutoDiscover() {
            this.autoDiscover = true;
            return this;
        }

        public final Builder<K, T> setDiscoverAnnotation(Class<? extends Annotation> annotationClass) {
            this.annotationAutoDisco = Optional.of(annotationClass);
            return this;
        }

        public final Registry<K, T> build() {
            Registry<K, T> target = new Registry<K, T>(this.keyType, this.type, this.mapSupplier);
            target.registryName = this.registryName.orElse(this.type != null ? this.type.getSimpleName() : "Unknown");
            this.maxSize.ifPresent(s -> target.maxSize = s);
            this.fallback.ifPresent(f -> target.fallback = Optional.of(f));
            this.namingScheme.ifPresent(f -> target.namingScheme = Optional.of((reg, value) -> f.apply(value)));
            this.namingSchemeFull.ifPresent(f -> target.namingScheme = Optional.of(f));
            this.validation.ifPresent(v -> target.validation = Optional.of(v));
            this.addCallback.ifPresent(c -> target.addCallback = Optional.of(c));
            this.removeCallback.ifPresent(c -> target.removeCallback = Optional.of(c));

            if (this.autoDiscover) {
                if (this.type != null) {
                    ClassDiscoverer.getInstances("nationalcipher", this.annotationAutoDisco.orElse(null), this.type).stream().forEach(target::register);
                } else {
                    System.err.println("Cannot auto discover as target class is null.");
                }
            }

            return target;
        }
    }
}
