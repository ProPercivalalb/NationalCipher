package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraph extends TextStatistic<Double> {

    public StatisticLogDigraph(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = PolyalphabeticIdentifier.calculateLDI(this.text);
        return this;
    }
}
