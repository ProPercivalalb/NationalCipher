package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;

public class StatisticLogDigraphSlidefair extends StatisticBaseNumber {

	public StatisticLogDigraphSlidefair(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return PolyalphabeticIdentifier.calculateSLDI(text);
	}

}
