package nationalcipher.cipher.stats;

public class DataBoolean extends DataHolder<Boolean> {

    public DataBoolean(Boolean value) {
        super(value);
    }

    @Override
    public double quantify(Boolean value, double weight) {
        return value != this.value ? 1000 * weight : 0;
    }

    @Override
    public String toString() {
        return String.format("[%b]", this.value);
    }
}
