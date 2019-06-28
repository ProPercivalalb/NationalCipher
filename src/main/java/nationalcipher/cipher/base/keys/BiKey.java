package nationalcipher.cipher.base.keys;

import nationalcipher.util.Pair;

public class BiKey<F, S> extends Pair<F, S> {
    
    public BiKey(F firstKey, S secondKey) {
        super(firstKey, secondKey);
    }
    
    public F getFirstKey() {
        return this.getLeft();
    }
    
    public S getSecondKey() {
        return this.getRight();
    }
    
    @Override
    public String toString() {
        return String.join(" ", this.getFirstKey().toString(), this.getSecondKey().toString());
    }
}
