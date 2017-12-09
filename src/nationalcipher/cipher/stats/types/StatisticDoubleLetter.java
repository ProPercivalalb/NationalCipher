package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticDoubleLetter extends TextStatistic<Boolean> {
	
	public StatisticDoubleLetter(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Boolean> calculateStatistic() {
		this.value = StatCalculator.calculateDBL(this.text);
		return this;
	}
}
