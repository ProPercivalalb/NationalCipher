package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticMaxNicodemus3to15 extends TextStatistic<Double> {

    public StatisticMaxNicodemus3to15(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = StatCalculator.calculateMaxNicodemusIC(this.text, 3, 15);
        return this;
    }
}
