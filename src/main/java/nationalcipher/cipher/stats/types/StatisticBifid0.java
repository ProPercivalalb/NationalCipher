package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticBifid0 extends TextStatistic<Double> {

    public StatisticBifid0(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = StatCalculator.calculateMaxBifidDiagraphicIC(this.text, 0, 0);
        return this;
    }
}
