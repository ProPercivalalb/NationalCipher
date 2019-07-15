package nationalcipher.cipher.base.keys;

import nationalcipher.api.IKey;

public class BiKey<F, S> implements Cloneable, IKey<BiKey<F, S>> {

    private F firstKey;
    private S secondKey;

    public BiKey(F firstKey, S secondKey) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
    }

    public BiKey<F, S> setFirst(F first) {
        this.firstKey = first;
        return this;
    }

    public BiKey<F, S> setSecond(S second) {
        this.secondKey = second;
        return this;
    }

    public F getFirstKey() {
        return this.firstKey;
    }

    public S getSecondKey() {
        return this.secondKey;
    }

    public static <F, S> BiKey<F, S> empty() {
        return new BiKey<>(null, null);
    }

    public static <F, S> BiKey<F, S> of(F first, S second) {
        return new BiKey<>(first, second);
    }

    @Override
    public BiKey<F, S> clone() {
        return new BiKey<>(this.firstKey, this.secondKey);
    }
    
    @Override
    public BiKey<F, S> copy() {
        return this.clone();
    }
}
