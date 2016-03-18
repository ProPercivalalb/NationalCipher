package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticKappaIC extends StatisticBaseNumber {

	public StatisticKappaIC(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateMaxKappaIC(text, 1, 15);
	}

}
