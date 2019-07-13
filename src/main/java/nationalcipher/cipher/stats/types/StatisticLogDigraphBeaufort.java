package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphBeaufort extends TextStatistic<Double> {

    public StatisticLogDigraphBeaufort(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = PolyalphabeticIdentifier.calculateBeaufortLDI(this.text);
        return this;
    }
}
