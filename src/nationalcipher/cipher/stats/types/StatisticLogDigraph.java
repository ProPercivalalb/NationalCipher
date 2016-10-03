package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraph extends TextStatistic {

	public StatisticLogDigraph(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateLDI(this.text);
	}
}
