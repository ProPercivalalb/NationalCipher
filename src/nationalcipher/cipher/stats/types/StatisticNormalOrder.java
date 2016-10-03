package nationalcipher.cipher.stats.types;

import javalibrary.language.Languages;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticNormalOrder extends TextStatistic {

	public StatisticNormalOrder(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = StatCalculator.calculateNormalOrder(text, Languages.english);
	}
}
