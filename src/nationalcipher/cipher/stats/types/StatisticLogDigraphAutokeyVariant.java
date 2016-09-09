package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;

public class StatisticLogDigraphAutokeyVariant extends StatisticBaseNumber {

	public StatisticLogDigraphAutokeyVariant(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return PolyalphabeticIdentifier.calculateAutokeyVariantLDI(text);
	}

}
