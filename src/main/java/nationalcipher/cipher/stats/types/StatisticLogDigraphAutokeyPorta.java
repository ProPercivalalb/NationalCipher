package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphAutokeyPorta extends TextStatistic<Double> {

    public StatisticLogDigraphAutokeyPorta(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = PolyalphabeticIdentifier.calculateAutokeyPortaLDI(this.text);
        return this;
    }
}
