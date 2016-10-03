package nationalcipher.cipher.stats;

public abstract class TextStatistic {

	public String text;
	public double value;
	
	public TextStatistic(String text) {
		this.text = text;
		this.calculateStatistic();
	}
	
	public abstract void calculateStatistic();
	
	public double quantify(DataHolder data) {
		return data.quantify(this.value);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " = [" + String.format("%.2f", this.value) + "]";
	}
}
