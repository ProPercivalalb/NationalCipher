package nationalcipher.cipher.base.enigma;

public class Norenigma extends EnigmaMachine {

	public Norenigma(String name) {
		super(name);
		this.setRotors("WTOKASUYVRBXJHQCPZEFMDINLG", "GJLPUBSWEMCTQVHXAOFZDRKYNI", "JWFMHNBPUSDYTIXVZGRQLAOEKC", "ESOVPZJAYQUIRHXLNFTGKDCMWB", "HEJXQOTZBVFDASCILWPGYNMURK");
		this.setNotches(new int[][] {{16},{4},{21},{9},{25}});
		this.setReflectors("MOWJYPUXNDSRAIBFVLKZGQCHET");
	}

	@Override
	public boolean canPlugboard() {
		return true;
	}
}
