package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticPercentageOddRepeats extends TextStatistic<Double> {

    public StatisticPercentageOddRepeats(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = StatCalculator.calculateROD(text);
        return this;
    }
}
