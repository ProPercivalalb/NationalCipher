package nationalcipher.cipher.stats.types;

import javalibrary.language.Languages;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticNormalOrder extends TextStatistic<Double> {

	public StatisticNormalOrder(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Double> calculateStatistic() {
		this.value = StatCalculator.calculateNormalOrder(text, Languages.ENGLISH);
		return this;
	}
}
