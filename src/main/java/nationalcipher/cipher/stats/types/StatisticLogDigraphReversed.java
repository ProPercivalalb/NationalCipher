package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphReversed extends TextStatistic<Double> {

	public StatisticLogDigraphReversed(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Double> calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateRDI(this.text);
		return this;
	}
}
