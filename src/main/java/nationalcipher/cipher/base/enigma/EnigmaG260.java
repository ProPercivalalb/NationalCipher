package nationalcipher.cipher.base.enigma;

public class EnigmaG260 extends EnigmaMachine {

    public EnigmaG260(String name) {
        super(name);
        this.setRotors("RCSPBLKQAUMHWYTIFZVGOJNEXD", "WCMIBVPJXAROSGNDLZKEYHUFQT", "FVDHZELSQMAXOKYIWPGCBUJTNR");
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
