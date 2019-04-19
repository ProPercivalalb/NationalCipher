package nationalcipher.cipher.base.enigma;

public class Norenigma extends EnigmaMachine {

	public Norenigma(String name) {
		super(name);
		this.setRotors("WTOKASUYVRBXJHQCPZEFMDINLG", "GJLPUBSWEMCTQVHXAOFZDRKYNI", "JWFMHNBPUSDYTIXVZGRQLAOEKC", "ESOVPZJAYQUIRHXLNFTGKDCMWB", "HEJXQOTZBVFDASCILWPGYNMURK");
		this.setNotches(new Integer[][] {{16},{4},{21},{9},{25}});
		this.setReflectors("MOWJYPUXNDSRAIBFVLKZGQCHET");
		this.setReflectorNames("UKW");
		this.canPlugboard = true;
		this.canUhr = true;
	}
}
