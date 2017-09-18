package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphAutokeyPorta extends TextStatistic {

	public StatisticLogDigraphAutokeyPorta(String text) {
		super(text);
	}

	@Override
	public TextStatistic calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateAutokeyPortaLDI(this.text);
		return this;
	}
}
