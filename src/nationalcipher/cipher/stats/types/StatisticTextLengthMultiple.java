package nationalcipher.cipher.stats.types;

public class StatisticTextLengthMultiple extends StatisticBaseBoolean {

	public int multiple;
	
	public StatisticTextLengthMultiple(int multiple) {
		super(true);
		this.multiple = multiple;
	}

	@Override
	public boolean getOutcome(String text) {
		return text.length() % this.multiple == 0;
	}

}
