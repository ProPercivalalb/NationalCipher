package nationalcipher.cipher.stats.types;

import javalibrary.language.Languages;
import nationalcipher.cipher.stats.StatCalculator;

public class StatisticNormalOrder extends StatisticBaseNumber {

	public StatisticNormalOrder(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return StatCalculator.calculateNormalOrder(text, Languages.english);
	}

}
