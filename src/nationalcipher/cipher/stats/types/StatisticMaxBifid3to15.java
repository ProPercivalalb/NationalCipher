package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticMaxBifid3to15 extends StatisticBaseNumber {

	public StatisticMaxBifid3to15(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateMaxBifidDiagraphicIC(text, 3, 15);
	}

}
