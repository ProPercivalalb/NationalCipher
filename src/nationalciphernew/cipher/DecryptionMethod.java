package nationalciphernew.cipher;

public enum DecryptionMethod {

	BRUTE_FORCE("Brute Force"),
	SIMULATED_ANNEALING("Simulated Annealing"),
	CALCULATED("Calculated"),
	DICTIONARY("Dictionary");
	
	private String name;
	
	private DecryptionMethod(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
