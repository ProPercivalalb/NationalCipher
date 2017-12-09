package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticSingleLetterDigraphDiscrepancy extends TextStatistic<Double> {

	public StatisticSingleLetterDigraphDiscrepancy(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Double> calculateStatistic() {
		this.value = StatCalculator.calculateSDD(this.text);
		return this;
	}
}
