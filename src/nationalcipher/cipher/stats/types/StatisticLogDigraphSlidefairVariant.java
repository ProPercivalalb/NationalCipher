package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphSlidefairVariant extends TextStatistic {

	public StatisticLogDigraphSlidefairVariant(String text) {
		super(text);
	}

	@Override
	public TextStatistic calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateSlidefairVariantLDI(this.text);
		return this;
	}
}
