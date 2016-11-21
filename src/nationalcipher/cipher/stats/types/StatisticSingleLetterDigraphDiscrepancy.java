package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticSingleLetterDigraphDiscrepancy extends TextStatistic {

	public StatisticSingleLetterDigraphDiscrepancy(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = StatCalculator.calculateSDD(this.text);
	}
}
