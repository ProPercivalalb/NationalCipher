package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;

public class StatisticLogDigraphAutokeyVigenere extends StatisticBaseNumber {

	public StatisticLogDigraphAutokeyVigenere(double value, double sD) {
		super(value, sD);
	}

	@Override
	public double getValue(String text) {
		return PolyalphabeticIdentifier.calculateAutokeyVigenereLDI(text);
	}

}
