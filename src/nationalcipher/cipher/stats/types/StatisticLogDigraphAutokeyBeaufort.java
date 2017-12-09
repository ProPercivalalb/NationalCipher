package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphAutokeyBeaufort extends TextStatistic<Double> {

	public StatisticLogDigraphAutokeyBeaufort(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Double> calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateAutokeyBeaufortLDI(this.text);
		return this;
	}
}
