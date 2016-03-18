package nationalcipher.cipher.stats.types;

public abstract class StatisticBaseBoolean extends StatisticType {
	
	private final boolean expected;
	
	public StatisticBaseBoolean(boolean expected) {
		this.expected = expected;
	}
		
	@Override
	public double quantifyType(String text) {
		return this.getOutcome(text) == this.expected ? 0 : 1000;
	}
	
	public abstract boolean getOutcome(String text);
}
