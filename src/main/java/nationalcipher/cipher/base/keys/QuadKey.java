package nationalcipher.cipher.base.keys;

public class QuadKey<F, S, T, N> {

    private F firstKey;
    private S secondKey;
    private T thirdKey;
    private N fourthKey;
    
    public QuadKey(F firstKey, S secondKey, T thirdKey, N fourthKey) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
        this.thirdKey = thirdKey;
        this.fourthKey = fourthKey;
    }
    
    public QuadKey<F, S, T, N> setFirst(F first) {
        this.firstKey = first;
        return this;
    }
    
    public QuadKey<F, S, T, N> setSecond(S second) {
        this.secondKey = second;
        return this;
    }
    
    public QuadKey<F, S, T, N> setThird(T third) {
        this.thirdKey = third;
        return this;
    }
    
    public QuadKey<F, S, T, N> setFourth(N fourth) {
        this.fourthKey = fourth;
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
    
    @Override
    public String toString() {
        return String.join(" ", this.firstKey.toString(), this.secondKey.toString(), this.thirdKey.toString(), this.fourthKey.toString());
    }
    
    public static <F, S, T, N> QuadKey<F, S, T, N> empty() {
        return new QuadKey<>(null, null, null, null);
    }
    
    public static <F, S, T, N> QuadKey<F, S, T, N> of(F first, S second, T third, N fourth) {
        return new QuadKey<>(first, second, third, fourth);
    }
    
    @Override
    public QuadKey<F, S, T, N> clone() {
        return new QuadKey<>(this.firstKey, this.secondKey, this.thirdKey, this.fourthKey);
    }
}
