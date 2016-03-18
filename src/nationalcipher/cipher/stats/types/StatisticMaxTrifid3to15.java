package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticMaxTrifid3to15 extends StatisticBaseNumber {

	public StatisticMaxTrifid3to15(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateMaxTrifidDiagraphicIC(text, 3, 15);
	}

}
