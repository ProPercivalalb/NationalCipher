package nationalcipher.util;

public class CharArrayWrapper implements CharSequence {

    private final char[] array;
    private int start, length;
    
    public CharArrayWrapper(char[] array) {
        this(array, 0, array.length);
    }
    
    public CharArrayWrapper(char[] array, int start, int length) {
        this.array = array;
        this.start = start;
        this.length = length;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index >= this.length) {
            throw new IndexOutOfBoundsException();
        }
        
        return this.array[index + this.start];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0 || end < 0 || start > end || end >= this.length) {
            throw new IndexOutOfBoundsException();
        }
            
        return new CharArrayWrapper(this.array, start, end - start);
    }
    
    @Override
    public String toString() {
        return new String(this.array, this.start, length);
    }
}
