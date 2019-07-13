package nationalcipher.cipher.util;

public abstract class AlphabetMap {

    private int[] map;

    public AlphabetMap() {
        this.map = new int[26];
    }

    public int get(Character key) {
        return this.map[CipherUtils.getAlphaIndex(key)];
    }

    public int put(Character key, int value) {
        this.map[CipherUtils.getAlphaIndex(key)] = value;
        return value;
    }
}
