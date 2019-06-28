package nationalcipher.cipher.base.keys;

public class TriKey<F, S, T> {

    private F firstKey;
    private S secondKey;
    private T thirdKey;
    
    public TriKey(F firstKey, S secondKey, T thirdKey) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
        this.thirdKey = thirdKey;
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
}
