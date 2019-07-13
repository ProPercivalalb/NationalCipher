package nationalcipher.cipher.base.keys;

public class TriKey<F, S, T> implements Cloneable {

    private F firstKey;
    private S secondKey;
    private T thirdKey;

    private TriKey(F firstKey, S secondKey, T thirdKey) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
        this.thirdKey = thirdKey;
    }

    public TriKey<F, S, T> setFirst(F first) {
        this.firstKey = first;
        return this;
    }

    public TriKey<F, S, T> setSecond(S second) {
        this.secondKey = second;
        return this;
    }

    public TriKey<F, S, T> setThird(T third) {
        this.thirdKey = third;
        return this;
    }

    public F getFirstKey() {
        return this.firstKey;
    }

    public S getSecondKey() {
        return this.secondKey;
    }

    public T getThirdKey() {
        return this.thirdKey;
    }

    @Override
    public String toString() {
        return String.join(" ", this.firstKey.toString(), this.secondKey.toString(), this.thirdKey.toString());
    }

    public static <F, S, T> TriKey<F, S, T> empty() {
        return new TriKey<>(null, null, null);
    }

    public static <F, S, T> TriKey<F, S, T> of(F first, S second, T third) {
        return new TriKey<>(first, second, third);
    }

    @Override
    public TriKey<F, S, T> clone() {
        return new TriKey<>(this.firstKey, this.secondKey, this.thirdKey);
    }
}
