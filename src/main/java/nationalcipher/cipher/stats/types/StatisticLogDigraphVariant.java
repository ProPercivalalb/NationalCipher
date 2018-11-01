package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphVariant extends TextStatistic<Double> {

	public StatisticLogDigraphVariant(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Double> calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateVariantLDI(this.text);
		return this;
	}
}
