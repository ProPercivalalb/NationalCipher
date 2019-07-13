package nationalcipher.cipher.stats;

public abstract class TextStatistic<T> {

    public String text;
    public T value;

    public TextStatistic(String text) {
        this.text = text;
    }

    /**
     * Computes the associated statistic. Usually invoked directly after object
     * initialisation
     */
    public abstract TextStatistic<T> calculateStatistic();

    public double quantify(DataHolder<T> data) {
        return data.quantify(this.value);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " = [" + String.format("%.2f", this.value) + "]";
    }
}
