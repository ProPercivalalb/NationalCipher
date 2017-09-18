package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphSlidefairBeaufort extends TextStatistic {

	public StatisticLogDigraphSlidefairBeaufort(String text) {
		super(text);
	}

	@Override
	public TextStatistic calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateSlidefairBeaufortLDI(this.text);
		return this;
	}
}
