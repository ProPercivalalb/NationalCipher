package nationalcipher.api;

import nationalcipher.api.IKeyType.IKeyBuilder;

public interface IRangedKeyBuilder<K> extends IKeyBuilder<K> {

    public IKeyBuilder<K> setMin(int min);

    public IKeyBuilder<K> setMax(int max);

    public IKeyBuilder<K> setRange(int min, int max);
    
    default IKeyBuilder<K> setRange(int[] range) {
        return this.setRange(range[0], range[1]);
    }

    public IKeyBuilder<K> setSize(int size);
}
