package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticBifid0 extends TextStatistic {

	public StatisticBifid0(String text) {
		super(text);
	}

	@Override
	public TextStatistic calculateStatistic() {
		this.value = StatCalculator.calculateMaxBifidDiagraphicIC(this.text, 0, 0);
		return this;
	}
}
