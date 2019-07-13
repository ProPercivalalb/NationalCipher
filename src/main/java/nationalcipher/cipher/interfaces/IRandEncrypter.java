package nationalcipher.cipher.interfaces;

@FunctionalInterface
public interface IRandEncrypter {

    public String randomlyEncrypt(String plainText);

    default int getDifficulty() {
        return 5;
    }
}
