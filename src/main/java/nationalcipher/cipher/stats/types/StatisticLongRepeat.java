package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLongRepeat extends TextStatistic<Double> {

	public StatisticLongRepeat(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Double> calculateStatistic() {
		this.value = StatCalculator.calculateLR(this.text);
		return this;
	}
}
