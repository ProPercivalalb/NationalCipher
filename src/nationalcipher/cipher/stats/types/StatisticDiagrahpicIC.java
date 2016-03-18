package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticDiagrahpicIC extends StatisticBaseNumber {

	public StatisticDiagrahpicIC(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateDiagrahpicIC(text);
	}

}
