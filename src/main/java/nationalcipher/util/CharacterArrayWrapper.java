package nationalcipher.util;

public class CharacterArrayWrapper implements CharSequence {

    private final Character[] array;
    private int start, length;
    
    public CharacterArrayWrapper(Character[] array) {
        this(array, 0, array.length);
    }
    
    public CharacterArrayWrapper(Character[] array, int start, int length) {
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
        if (start < 0 || end < 0 || start > end || end > this.length) {
            throw new IndexOutOfBoundsException();
        }
        
        return new CharacterArrayWrapper(this.array, start, end - start);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.length);
        for (int i = this.start; i < this.start + this.length; i++) {
            builder.append((char) this.array[i]);
        }
        return builder.toString();
    }
}
