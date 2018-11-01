package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphAutokeyVigenere extends TextStatistic<Double> {

	public StatisticLogDigraphAutokeyVigenere(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Double> calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateAutokeyVigenereLDI(this.text);
		return this;
	}
}
