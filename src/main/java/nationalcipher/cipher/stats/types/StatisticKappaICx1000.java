package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticKappaICx1000 extends TextStatistic<Double> {

    public StatisticKappaICx1000(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = StatCalculator.calculateMaxKappaIC(this.text, 1, 15) * 1000;
        return this;
    }
}
