package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticLongRepeat extends StatisticBaseNumber {

	public StatisticLongRepeat(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateLR(text);
	}

}
