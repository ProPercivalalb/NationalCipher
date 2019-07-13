package nationalcipher.util;

import javalibrary.util.ArrayUtil;

public class CharacterArrayWrapper implements CharSequence {

    private final Character[] array;
    
    public CharacterArrayWrapper(Character[] array) {
        this.array = array;
    }
    
    @Override
    public int length() {
        return this.array.length;
    }

    @Override
    public char charAt(int index) {
        return this.array[index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CharacterArrayWrapper(ArrayUtil.copyRange(this.array, start, end));
    }

}
