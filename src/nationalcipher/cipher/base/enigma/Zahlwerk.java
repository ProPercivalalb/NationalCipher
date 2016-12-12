package nationalcipher.cipher.base.enigma;

public class Zahlwerk extends EnigmaMachine {

	public Zahlwerk(String name) {
		super(name);
		this.setRotors("LPGSZMHAEOQKVXRFYBUTNICJDW", "SLVGBTFXJQOHEWIRZYAMKPCNDU", "CJGDPSHKTURAWZXFMYNQOBVLIE");
		this.setNotches("SUVWZABCEFGIKLOPQ", "STVYZACDFGHKMNQ", "UWXAEFHKMNR");
		this.setReflectors("IMETCGFRAYSQBZXWLHKDVUPOJN");
		this.setReflectorNames("UKW");
		this.setETW("QWERTZUIOASDFGHJKPYXCVBNML");
		this.stepping = false;
	}
		
	@Override
	public boolean isSolvable() {
		return false;
	}
}
