package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticMaxBifid0 extends TextStatistic {

	public StatisticMaxBifid0(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = StatCalculator.calculateMaxBifidDiagraphicIC(this.text, 0, 0);
	}
}
