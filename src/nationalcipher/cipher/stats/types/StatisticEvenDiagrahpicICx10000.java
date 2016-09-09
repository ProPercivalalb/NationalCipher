package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticEvenDiagrahpicICx10000 extends StatisticBaseNumber {

	public StatisticEvenDiagrahpicICx10000(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateEvenDiagrahpicIC(text) * 10000;
	}

}
