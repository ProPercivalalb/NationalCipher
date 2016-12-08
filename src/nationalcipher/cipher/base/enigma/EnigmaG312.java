package nationalcipher.cipher.base.enigma;

public class EnigmaG312 extends EnigmaMachine {

	public EnigmaG312(String name) {
		super(name);
		this.setRotors("DMTWSILRUYQNKFEJCAZBPGXOHV", "HQZGPJTMOBLNCIFDYAWVEUSRKX", "UQNTLSZFMREHDPXKIBVYGJCWOA");
		this.setNotches("SUVWZABCEFGIKLOPQ", "STVYZACDFGHKMNQ", "UWXAEFHKMNR");
		this.setReflectors("RULQMZJSYGOCETKWDAHNBXPVIF");
		this.setReflectorNames("UKW");
		this.setETW("QWERTZUIOASDFGHJKPYXCVBNML");
		this.stepping = false;
	}
	
	@Override
	public boolean isSolvable() {
		return false;
	}
}
