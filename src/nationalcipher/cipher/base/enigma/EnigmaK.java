package nationalcipher.cipher.base.enigma;

public class EnigmaK extends EnigmaMachine {

	public EnigmaK(String name) {
		super(name);
		this.setRotors("LPGSZMHAEOQKVXRFYBUTNICJDW", "SLVGBTFXJQOHEWIRZYAMKPCNDU", "CJGDPSHKTURAWZXFMYNQOBVLIE");
		this.setNotches(new int[][] {{24},{4},{13}});
		this.setReflectors("IMETCGFRAYSQBZXWLHKDVUPOJN");
		this.setReflectorNames("UKW");
		this.setETW("QWERTZUIOASDFGHJKPYXCVBNML");
	}

}
