package nationalcipher.cipher.base.enigma;

public class EnigmaG31 extends EnigmaMachine {

    public EnigmaG31(String name) {
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
