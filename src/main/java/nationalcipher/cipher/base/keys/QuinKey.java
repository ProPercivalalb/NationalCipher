package nationalcipher.cipher.base.keys;

import nationalcipher.api.IKey;

public class QuinKey<F, S, T, N, Q> implements Cloneable, IKey<QuinKey<F, S, T, N, Q>> {

    private F firstKey;
    private S secondKey;
    private T thirdKey;
    private N fourthKey;
    private Q fifthKey;
    
    public QuinKey(F firstKey, S secondKey, T thirdKey, N fourthKey, Q fifthKey) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
        this.thirdKey = thirdKey;
        this.fourthKey = fourthKey;
        this.fifthKey = fifthKey;
    }

    public QuinKey<F, S, T, N, Q> setFirst(F first) {
        this.firstKey = first;
        return this;
    }

    public QuinKey<F, S, T, N, Q> setSecond(S second) {
        this.secondKey = second;
        return this;
    }

    public QuinKey<F, S, T, N, Q> setThird(T third) {
        this.thirdKey = third;
        return this;
    }

    public QuinKey<F, S, T, N, Q> setFourth(N fourth) {
        this.fourthKey = fourth;
        return this;
    }
    
    public QuinKey<F, S, T, N, Q> setFifth(Q fifth) {
        this.fifthKey = fifth;
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

    public N getFourthKey() {
        return this.fourthKey;
    }
    
    public Q getFifthKey() {
        return this.fifthKey;
    }

    public static <F, S, T, N, Q> QuinKey<F, S, T, N, Q> empty() {
        return new QuinKey<>(null, null, null, null, null);
    }

    public static <F, S, T, N, Q> QuinKey<F, S, T, N, Q> of(F first, S second, T third, N fourth, Q fifth) {
        return new QuinKey<>(first, second, third, fourth, fifth);
    }

    @Override
    public QuinKey<F, S, T, N, Q> clone() {
        return new QuinKey<>(this.firstKey, this.secondKey, this.thirdKey, this.fourthKey, this.fifthKey);
    }
    
    @Override
    public QuinKey<F, S, T, N, Q> copy() {
        return this.clone();
    }
}
