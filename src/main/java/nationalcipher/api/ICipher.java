package nationalcipher.api;

import java.math.BigInteger;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import javalibrary.streams.PrimTypeUtil;
import nationalcipher.util.CharArrayWrapper;
import nationalcipher.util.Pair;

public interface ICipher<K> {

    /**
     * Alters the plaintext so that the cipher can be applied to it E.g. Padding the
     * text so it is a multiple of N (Hill Cipher) Making sure there is no double
     * letters (Playfair Cipher)
     */
    default CharSequence padPlainText(CharSequence plainText, K key) {
        return plainText;
    }

    CharSequence encode(CharSequence plainText, K key, IFormat format);

    default CharSequence encode(CharSequence plainText, K key) {
        return this.encode(plainText, key, null);
    }

    default CharSequence decode(CharSequence cipherText, K key) {
        return new CharArrayWrapper(decodeEfficently(cipherText, key));
    }

    /**
     * Used in cipher solvers for the most memory and speed efficiency code
     */
    default char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, K key) {
        throw new UnsupportedOperationException();
    }

    default char[] decodeEfficently(CharSequence cipherText, K key) {
        return decodeEfficently(cipherText, new char[cipherText.length()], key);
    }

    default String encode(String plainText, K key) {
        return PrimTypeUtil.toString(encode(padPlainText(plainText, key), key, null));
    }

    /**
     * Will the same ciphertext be produced each run for the same PT and key
     */
    default boolean deterministic() {
        return true;
    }

    K randomiseKey();

    /**
     * Iterates thought all the ciphers keys, the key object K is not necessarily
     * immutable so if access is required after a copy is needed
     */
    void iterateKeys(Consumer<K> consumer);

    /**
     * Used for simulated annealing, returns a key that is similar to the given key
     */
    default K alterKey(K key) {
        return key;
    }

    default String prettifyKey(K key) {
        return key.toString();
    }

    boolean isValid(K key);

    BigInteger getNumOfKeys();

    default String randomEncode(String plainText) {
        return this.encode(plainText, this.randomiseKey());
    }

    default Pair<String, K> randomEncodePair(String plainText) {
        K key = this.randomiseKey();
        return new Pair<>(this.encode(plainText, key), key);
    }

    default Pair<String, String> randomEncodeKeyStr(String plainText) {
        K key = this.randomiseKey();
        return new Pair<>(this.encode(plainText, key), this.prettifyKey(key));
    }

    default Difficulty getDifficulty() {
        return Difficulty.EASY;
    }

    enum Difficulty {
        EASY(1), NORMAL(5), HARD(10);

        int level;

        Difficulty(int level) {
            this.level = level;
        }

        public int getLevel() {
            return this.level;
        }

        public boolean isEasierThan(int levelIn) {
            return this.level <= levelIn;
        }
    }

}
