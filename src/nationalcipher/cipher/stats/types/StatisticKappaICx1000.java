package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticKappaICx1000 extends StatisticBaseNumber {

	public StatisticKappaICx1000(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateMaxKappaIC(text, 1, 15) * 1000;
	}

}
