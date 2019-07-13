package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticTrigraphNoOverlapICx100000 extends TextStatistic<Double> {

    public StatisticTrigraphNoOverlapICx100000(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = StatCalculator.calculateIC(this.text, 3, false) * 100000;
        return this;
    }
}
