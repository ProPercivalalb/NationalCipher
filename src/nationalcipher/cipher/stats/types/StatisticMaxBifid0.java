package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticMaxBifid0 extends StatisticBaseNumber {

	public StatisticMaxBifid0(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateMaxBifidDiagraphicIC(text, 0, 0);
	}

}
