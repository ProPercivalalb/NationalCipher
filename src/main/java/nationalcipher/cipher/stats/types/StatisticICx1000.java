package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticICx1000 extends TextStatistic<Double> {

	public StatisticICx1000(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Double> calculateStatistic() {
		this.value = StatCalculator.calculateIC(this.text, 1, true) * 1000;
		return this;
	}
}
