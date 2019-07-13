package nationalcipher.cipher.decrypt.methods;

public enum DecryptionMethod {

	BRUTE_FORCE("Brute Force"), //Attempts all keys
	SIMULATED_ANNEALING("Simulated Annealing"),
	CALCULATED("Calculated"),
	DICTIONARY("Dictionary"),
	PERIODIC_KEY("Periodic Key"),
	EXAMINE("Examine");
	
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
