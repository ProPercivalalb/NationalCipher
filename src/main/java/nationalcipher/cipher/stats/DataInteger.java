package nationalcipher.cipher.stats;

public class DataInteger extends DataHolder<Integer> {

    public DataInteger(Integer value) {
        super(value);
    }

    @Override
    public double quantify(Integer value, double weight) {
        return value != this.value ? 1000 * weight : 0;
    }

    @Override
    public String toString() {
        return String.format("[%b]", this.value);
    }
}
