package nationalcipher.cipher.base.enigma;

public class EnigmaM4 extends EnigmaMachine {

    public EnigmaM4(String name) {
        super(name);
        this.setRotors("EKMFLGDQVZNTOWYHXUSPAIBRCJ", "AJDKSIRUXBLHWTMCQGZNPYFVOE", "BDFHJLCPRTXVZNYEIWGAKMUSQO", "ESOVPZJAYQUIRHXLNFTGKDCMWB", "VZBRGITYUPSDNHLXAWMJQOFECK", "JPGVOUMFYQBENHZRDKASXLICTW", "NZJHGRCXMYSWBOUFAIVLPEKQDT", "FKQHTLXOCBJSPDZRAMEWNIUYGV");
        this.setNotches("Q", "E", "V", "J", "Z", "ZM", "ZM", "ZM");
        this.setThinRotors("LEYJVCNIXWPBQMDRTAKZGFUHOS", "FSOKANUERHMBTIYCWLQPZXVGJD");
        this.setThinRotorNames((char) 945 + "|Alpha", (char) 946 + "|Beta");
        this.setReflectors("ENKQAUYWJICOPBLMDXZVFTHRGS", "RDOBJNTKVEHMLFCWZAXGYIPSUQ");
        this.setReflectorNames("B-THIN", "C-THIN");
        this.canPlugboard = true;
        this.canUhr = true;
    }
}
