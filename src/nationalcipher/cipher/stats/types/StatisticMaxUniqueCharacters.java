package nationalcipher.cipher.stats.types;

import javalibrary.string.StringTransformer;
import nationalcipher.cipher.stats.DataHolder;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticMaxUniqueCharacters extends TextStatistic {

	public StatisticMaxUniqueCharacters(String text) {
		super(text);
	}

	@Override
	public TextStatistic calculateStatistic() {
		this.value = StringTransformer.getUniqueCharSet(this.text).size();
		return this;
	}
	
	@Override
	public double quantify(DataHolder data) {
		return this.value <= data.value ? 0 : 1000;
	}
}
