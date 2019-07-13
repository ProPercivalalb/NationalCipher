package nationalcipher.cipher.base.enigma;

public class EnigmaKD extends EnigmaMachine {

    public EnigmaKD(String name) {
        super(name);
        this.setRotors("VEZIOJCXKYDUNTWAPLQGBHSFMR", "HGRBSJZETDLVPMQYCXAOKINFUW", "NWLHXGRBYOJSAZDVTPKFQMEUIC");
        this.setNotches("SUYAEHLNQ", "SUYAEHLNQ", "SUYAEHLNQ");
        this.setReflectors("KOTVPNLMJIAGHFBEWYXCZDQSRU");
        this.setReflectorNames("UKW");
        this.setETW("QWERTZUIOASDFGHJKPYXCVBNML");
    }

}
