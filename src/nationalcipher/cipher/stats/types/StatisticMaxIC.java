package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticMaxIC extends StatisticBaseNumber {

	public StatisticMaxIC(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateMaxIC(text, 1, 15);
	}

}
