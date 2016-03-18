package nationalcipher.cipher.stats.types;

public abstract class StatisticBaseNumber extends StatisticType {

	public double value;
	public double sD;
	
	public StatisticBaseNumber(double value, double sD) {
		this.value = value;
		this.sD = Math.max(sD, 0.001D);
	}
		
	@Override
	public double quantifyType(String text) {

		return Math.abs((this.getValue(text) - this.value)) / this.sD;
	}
	
	public abstract double getValue(String text);
}
