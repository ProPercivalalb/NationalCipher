package nationalcipher.cipher.base.enigma;

public class EnigmaD extends EngimaMachine {

	public EnigmaD(String name) {
		super(name);
		this.setRotors("LPGSZMHAEOQKVXRFYBUTNICJDW", "SLVGBTFXJQOHEWIRZYAMKPCNDU", "CJGDPSHKTURAWZXFMYNQOBVLIE");
		this.setNotches(new int[][] {{24},{4},{13}});
		this.setReflectors("IMETCGFRAYSQBZXWLHKDVUPOJN");
		this.setETW("QWERTZUIOASDFGHJKPYXCVBNML");
	}

}
