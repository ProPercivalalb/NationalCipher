package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticDoubleLetter2to40 extends TextStatistic {
	
	public StatisticDoubleLetter2to40(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = StatCalculator.calculateSeriatedPlayfair(this.text, 2, 40) ? 0 : 1;
	}
}
