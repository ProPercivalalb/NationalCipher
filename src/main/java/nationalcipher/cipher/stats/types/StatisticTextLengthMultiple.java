package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.DataHolder;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticTextLengthMultiple extends TextStatistic<Integer> {

	public StatisticTextLengthMultiple(String text) {
		super(text);
	}

	@Override
	public TextStatistic<Integer> calculateStatistic() {
		this.value = this.text.length();
		return this;
	}
	
	@Override
	public double quantify(DataHolder<Integer> data) {
		return this.value % data.value == 0 ? 0 : 1000;
	}
}
