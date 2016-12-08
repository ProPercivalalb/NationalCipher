package nationalcipher.cipher.base.enigma;

public class EnigmaI extends EnigmaMachine {

	public EnigmaI(String name) {
		super(name);
		this.setRotors("EKMFLGDQVZNTOWYHXUSPAIBRCJ", "AJDKSIRUXBLHWTMCQGZNPYFVOE", "BDFHJLCPRTXVZNYEIWGAKMUSQO", "ESOVPZJAYQUIRHXLNFTGKDCMWB", "VZBRGITYUPSDNHLXAWMJQOFECK");
		this.setNotches(new int[][] {{16},{4},{21},{9},{25}});
		this.setReflectors("EJMZALYXVBWFCRQUONTSPIKHGD", "YRUHQSLDPXNGOKMIEBFZCWVJAT", "FVPJIAOYEDRZXWGCTKUQSBNMHL");
		this.setReflectorNames("UKW-A", "UKW-B", "UKW-C");
		this.canPlugboard = true;
	}
}
