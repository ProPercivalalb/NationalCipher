package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphPorta extends TextStatistic {

	public StatisticLogDigraphPorta(String text) {
		super(text);
	}

	@Override
	public TextStatistic calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculatePortaLDI(this.text);
		return this;
	}
}
