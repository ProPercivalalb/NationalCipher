package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticMaxNicodemus3to15 extends StatisticBaseNumber {

	public StatisticMaxNicodemus3to15(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateMaxNicodemusIC(text, 3, 15);
	}

}
