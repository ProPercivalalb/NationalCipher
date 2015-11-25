package nationalcipher.cipher.manage;

public enum DecryptionMethod {

	BRUTE_FORCE("Brute Force"),
	SIMULATED_ANNEALING("Simulated Annealing"),
	CALCULATED("Calculated"),
	DICTIONARY("Dictionary"),
	KEY_MANIPULATION("Key Manipulation");
	
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
