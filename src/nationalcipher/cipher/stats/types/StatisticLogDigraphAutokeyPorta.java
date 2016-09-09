package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;

public class StatisticLogDigraphAutokeyPorta extends StatisticBaseNumber {

	public StatisticLogDigraphAutokeyPorta(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return PolyalphabeticIdentifier.calculateAutokeyPortaLDI(text);
	}

}
