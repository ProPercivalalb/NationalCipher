package nationalcipher.cipher.stats;

public abstract class DataHolder<T> {

	public T value;
	
	public DataHolder(T value) {
		this.value = value;
	}
	
	//Default calculator see TextStatistic.quantify(DataHolder);
    public abstract double quantify(T value, double weight);
    
    public double quantify(T value) {
    	return this.quantify(value, 1.0D);
    }
}
