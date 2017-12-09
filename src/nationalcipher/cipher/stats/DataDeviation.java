package nationalcipher.cipher.stats;

public class DataDeviation extends DataHolder<Double> {

	public final double sD;
	
	public DataDeviation(Double value, double sD) {
		super(value);
		this.sD = Math.max(sD, 0.001D);
	}

	@Override
	public double quantify(Double value, double weight) {
		return Math.abs(value - this.value) / this.sD * weight;
	}
	
	@Override
	public String toString() {
		return String.format("[%f, %f]", this.value, this.sD);
	}
}
