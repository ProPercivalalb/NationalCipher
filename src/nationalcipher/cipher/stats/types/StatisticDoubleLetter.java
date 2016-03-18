package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;

public class StatisticDoubleLetter extends StatisticBaseBoolean {

	public StatisticDoubleLetter(boolean doubleLetter) {
		super(doubleLetter);
	}

	@Override
	public boolean getOutcome(String text) {
		return StatCalculator.calculateDBL(text);
	}

}
