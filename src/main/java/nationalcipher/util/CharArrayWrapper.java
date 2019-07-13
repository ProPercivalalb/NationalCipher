package nationalcipher.util;

import javalibrary.util.ArrayUtil;

public class CharArrayWrapper implements CharSequence {

    private final char[] array;

    public CharArrayWrapper(char[] array) {
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
        return new CharArrayWrapper(ArrayUtil.copyRange(this.array, start, end));
    }

}
