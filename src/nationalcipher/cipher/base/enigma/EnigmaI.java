package nationalcipher.cipher.base.enigma;

public class EnigmaI extends EngimaMachine {

	public EnigmaI(String name) {
		super(name);
		this.setRotors("EKMFLGDQVZNTOWYHXUSPAIBRCJ", "AJDKSIRUXBLHWTMCQGZNPYFVOE", "BDFHJLCPRTXVZNYEIWGAKMUSQO", "ESOVPZJAYQUIRHXLNFTGKDCMWB", "VZBRGITYUPSDNHLXAWMJQOFECK");
		this.setNotches(new int[][] {{16},{4},{21},{9},{25}});
		this.setReflectors("EJMZALYXVBWFCRQUONTSPIKHGD", "YRUHQSLDPXNGOKMIEBFZCWVJAT", "FVPJIAOYEDRZXWGCTKUQSBNMHL");
	}

	@Override
	public boolean canPlugboard() {
		return true;
	}
}
