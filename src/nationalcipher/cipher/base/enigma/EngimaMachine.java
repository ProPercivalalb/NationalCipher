package nationalcipher.cipher.base.enigma;

public class EngimaMachine {

	public String name;
	public char[][] rotors;
	public char[][] rotorsInverse;
	public int[][] notches;
	public int rotorCount;
	
	public char[][] reflector;
	public int reflectorCount;
	
	public char[] etw;
	public char[] etwInverse;
	
	public EngimaMachine(String name) {
		this.name = name;
	}
	
	public final void setRotors(String... input) {
		char[][] normal = new char[input.length][26];
		char[][] inverse = new char[normal.length][26];
		
		for(int r = 0; r < input.length; r++) {
			for(int i = 0; i < 26; i++) {
				normal[r][i] = input[r].charAt(i);
				inverse[r][normal[r][i] - 'A'] = (char)(i + 'A');
			}
		}
		
		this.rotors = normal;
		this.rotorsInverse = inverse;
		this.rotorCount = input.length;
	}
	
	public final void setNotches(int[][] input) {
		this.notches = input;
	}
	
	public final void setReflectors(String... input) {
		char[][] normal = new char[input.length][];
		
		for(int r = 0; r < input.length; r++)
			normal[r] = input[r].toCharArray();
			
		this.reflector = normal;
		this.reflectorCount = input.length;
	}

	public void setETW(String input) {
		this.etw = input.toCharArray();
		
		char[] inverse = new char[26];
		for(int i = 0; i < 26; i++)
			inverse[this.etw[i] - 'A'] = (char)(i + 'A');
			
		this.etwInverse = inverse;
	}
	
	public final int getNumberOfRotors() {
		return this.rotorCount;
	}
	
	public final int getNumberOfReflectors() {
		return this.reflectorCount;
	}
	
	public boolean canPlugboard() {
		return false;
	}
}
