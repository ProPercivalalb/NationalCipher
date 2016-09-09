package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticDiagrahpicICx10000 extends StatisticBaseNumber {

	public StatisticDiagrahpicICx10000(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateDiagrahpicIC(text) * 10000;
	}

}
