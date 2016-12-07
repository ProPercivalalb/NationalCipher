package nationalcipher.cipher.base.enigma;

public class SwissK extends EnigmaMachine {

	public SwissK(String name) {
		super(name);
		this.setRotors("PEZUOHXSCVFMTBGLRINQJWAYDK", "ZOUESYDKFWPCIQXHMVBLGNJRAT", "EHRVXGAOBQUSIMZFLYNWKTPDJC");
		this.setNotches(new int[][] {{24},{4},{13}});
		this.setReflectors("IMETCGFRAYSQBZXWLHKDVUPOJN");
		this.setETW("QWERTZUIOASDFGHJKPYXCVBNML");
	}

}
