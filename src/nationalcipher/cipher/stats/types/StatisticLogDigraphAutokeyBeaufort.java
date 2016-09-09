package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;

public class StatisticLogDigraphAutokeyBeaufort extends StatisticBaseNumber {

	public StatisticLogDigraphAutokeyBeaufort(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return PolyalphabeticIdentifier.calculateAutokeyBeaufortLDI(text);
	}

}
