package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticMaxICx1000 extends StatisticBaseNumber {

	public StatisticMaxICx1000(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateMaxIC(text, 1, 15) * 1000;
	}

}
