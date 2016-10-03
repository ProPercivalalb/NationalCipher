package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticConcecutiveValueDifference extends TextStatistic {

	public StatisticConcecutiveValueDifference(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = StatCalculator.averageDifferenceinCharacter(this.text);
	}
}
