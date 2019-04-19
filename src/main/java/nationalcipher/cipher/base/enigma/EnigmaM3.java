package nationalcipher.cipher.base.enigma;

public class EnigmaM3 extends EnigmaMachine {

	public EnigmaM3(String name) {
		super(name);
		this.setRotors("EKMFLGDQVZNTOWYHXUSPAIBRCJ", "AJDKSIRUXBLHWTMCQGZNPYFVOE", "BDFHJLCPRTXVZNYEIWGAKMUSQO", "ESOVPZJAYQUIRHXLNFTGKDCMWB", "VZBRGITYUPSDNHLXAWMJQOFECK", "JPGVOUMFYQBENHZRDKASXLICTW", "NZJHGRCXMYSWBOUFAIVLPEKQDT", "FKQHTLXOCBJSPDZRAMEWNIUYGV");
		this.setNotches(new Integer[][] {{16},{4},{21},{9},{25},{25,12},{25,12},{25,12}});
		this.setReflectors("YRUHQSLDPXNGOKMIEBFZCWVJAT", "FVPJIAOYEDRZXWGCTKUQSBNMHL");
		this.setReflectorNames("UKW-B", "UKW-C");
		this.canPlugboard = true;
		this.canUhr = true;
	}
}
