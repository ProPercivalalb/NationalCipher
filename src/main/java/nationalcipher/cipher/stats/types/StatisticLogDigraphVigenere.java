package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphVigenere extends TextStatistic<Double> {

	public StatisticLogDigraphVigenere(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Double> calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateVigenereLDI(this.text);
		return this;
	}
}
