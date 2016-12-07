package nationalcipher.cipher.base.enigma;

public class RocketI extends EnigmaMachine {

	public RocketI(String name) {
		super(name);
		this.setRotors("JGDQOXUSCAMIFRVTPNEWKBLZYH", "NTZPSFBOKMWRCJDIVLAEYUXHGQ", "JVIUBHTCDYAKEQZPOSGXNRMWFL");
		this.setNotches(new int[][] {{24},{4},{13}});
		this.setReflectors("QYHOGNECVPUZTFDJAXWMKISRBL");
		this.setETW("QWERTZUIOASDFGHJKPYXCVBNML");
	}
}
