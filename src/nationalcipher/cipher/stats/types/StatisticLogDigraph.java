package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;

public class StatisticLogDigraph extends StatisticBaseNumber {

	public StatisticLogDigraph(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return PolyalphabeticIdentifier.calculateLDI(text);
	}

}
