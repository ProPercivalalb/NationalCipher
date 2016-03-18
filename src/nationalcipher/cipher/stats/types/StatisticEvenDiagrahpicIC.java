package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticEvenDiagrahpicIC extends StatisticBaseNumber {

	public StatisticEvenDiagrahpicIC(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateEvenDiagrahpicIC(text);
	}

}
