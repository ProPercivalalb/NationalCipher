package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;

public class StatisticLogDigraphBeaufort extends StatisticBaseNumber {

	public StatisticLogDigraphBeaufort(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return PolyalphabeticIdentifier.calculateBeaufortLDI(text);
	}

}
