package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphAutokeyVariant extends TextStatistic<Double> {

    public StatisticLogDigraphAutokeyVariant(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = PolyalphabeticIdentifier.calculateAutokeyVariantLDI(this.text);
        return this;
    }
}
