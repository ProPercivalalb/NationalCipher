package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticDoubleLetter2to40 extends StatisticBaseBoolean {

	public StatisticDoubleLetter2to40(boolean doubleLetter) {
		super(doubleLetter);
	}

	@Override
	public boolean getOutcome(String text) {
		return !StatCalculator.calculateSeriatedPlayfair(text, 2, 40);
	}

}
