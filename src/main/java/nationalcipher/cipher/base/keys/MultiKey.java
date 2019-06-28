package nationalcipher.cipher.base.keys;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiKey {

    private final List<Object> keys;
    
    public MultiKey(Object... keysIn) {
        this.keys = new ArrayList<>();
        for(Object key : keysIn)
            this.keys.add(key);
    }
    
    public Object get(int index) {
        return this.keys.get(index);
    }
    
    public int numKeys() {
        return this.keys.size();
    }
    
    @Override
    public String toString() {
        return this.keys.stream().map(Object::toString).collect(Collectors.joining(" "));
    }
}
