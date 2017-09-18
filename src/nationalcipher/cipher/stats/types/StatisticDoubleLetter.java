package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticDoubleLetter extends TextStatistic {
	
	public StatisticDoubleLetter(String text) {
		super(text);
	}

	@Override
	public TextStatistic calculateStatistic() {
		this.value = StatCalculator.calculateDBL(this.text) ? 1 : 0;
		return this;
	}
}
