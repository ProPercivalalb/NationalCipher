package nationalcipher.cipher.base.enigma;

public class RocketI extends EnigmaMachine {

	public RocketI(String name) {
		super(name);
		this.setRotors("JGDQOXUSCAMIFRVTPNEWKBLZYH", "NTZPSFBOKMWRCJDIVLAEYUXHGQ", "JVIUBHTCDYAKEQZPOSGXNRMWFL");
		this.setNotches(new int[][] {{13},{4},{24}});
		this.setReflectors("QYHOGNECVPUZTFDJAXWMKISRBL");
		this.setReflectorNames("UKW");
		this.setETW("QWERTZUIOASDFGHJKPYXCVBNML");
	}
}
