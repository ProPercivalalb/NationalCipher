package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticMaxTrifid3to15 extends TextStatistic {

	public StatisticMaxTrifid3to15(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = StatCalculator.calculateMaxTrifidDiagraphicIC(this.text, 3, 15);
	}
}
