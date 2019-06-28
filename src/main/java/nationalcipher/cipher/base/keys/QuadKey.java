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
}
