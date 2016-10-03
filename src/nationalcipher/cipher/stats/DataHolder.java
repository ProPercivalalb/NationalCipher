package nationalcipher.cipher.stats;

public class DataHolder {

	public final double value, sD;
	
	public DataHolder(double value, double sD) {
		this.value = value;
		this.sD = Math.max(sD, 0.001D);
	}
	
	public DataHolder(double value) {
		this(value, 0);
	}
	
	public DataHolder(boolean value) {
		this(value ? 1 : 0, 0);
	}
	
	//Default calculator see TextStatistic.quantify(DataHolder);
	public double quantify(double value) {
		return Math.abs((value - this.value)) / this.sD;
	}
}
