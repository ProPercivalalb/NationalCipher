package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticPercentageOddRepeats extends StatisticBaseNumber {

	public StatisticPercentageOddRepeats(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateROD(text);
	}

}
