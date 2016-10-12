package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticICx1000 extends TextStatistic {

	public StatisticICx1000(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = StatCalculator.calculateIC(this.text) * 1000;
	}
}