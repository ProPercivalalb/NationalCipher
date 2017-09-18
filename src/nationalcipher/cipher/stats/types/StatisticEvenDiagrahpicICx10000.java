package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticEvenDiagrahpicICx10000 extends TextStatistic {

	public StatisticEvenDiagrahpicICx10000(String text) {
		super(text);
	}

	@Override
	public TextStatistic calculateStatistic() {
		this.value = StatCalculator.calculateIC(this.text, 2, false) * 10000;
		return this;
	}
}
