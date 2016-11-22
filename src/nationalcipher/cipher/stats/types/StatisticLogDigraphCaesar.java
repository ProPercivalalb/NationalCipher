package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphCaesar extends TextStatistic {

	public StatisticLogDigraphCaesar(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateCaesarLDI(this.text);
	}
}
