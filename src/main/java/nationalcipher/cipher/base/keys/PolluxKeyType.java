package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Optional;

import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.KeyFunction;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.KeyGeneration;

public class PolluxKeyType implements IKeyType<Character[]> {

    // Both inclusive
    private final Character[] alphabet;

    private PolluxKeyType(Character[] alphabet) {
        this.alphabet = alphabet;
    }

    @Override
    public Character[] randomise() {
        return KeyGeneration.createPolluxKey();
    }

    @Override
    public boolean isValid(Character[] key) {
        // Contains at least one of each character
        for (int i = 0; i < this.alphabet.length; i++) {
            if (!ArrayUtil.contains(key, this.alphabet[i])) {
                return false;
            }
        }

        // Contains only characters from alphabet
        for (int i = 0; i < key.length; i++) {
            if (!ArrayUtil.contains(this.alphabet, key[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean iterateKeys(KeyFunction<Character[]> consumer) {
        return KeyIterator.iterateObject(consumer, 10, this.alphabet);
    }

    @Override
    public Character[] alterKey(Character[] key) {
        int pos = RandomUtil.pickRandomInt(key.length);
        key[pos] = RandomUtil.pickRandomElement(this.alphabet);
        return key;
    }

    @Override
    public String prettifyKey(Character[] key) {
        return PrimTypeUtil.toString(key);
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(59049); // Calculate number
    }
    
    @Override
    public Character[] parse(String input) throws ParseException {
        if (input.length() != 10) {
            throw new ParseException(input, 0);
        }
        
        Character[] key = new Character[10];
        for (int i = 0; i < 10; i++) {
            key[i] = input.charAt(i);
        }
        
        return key;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IKeyBuilder<Character[]> {

        private Optional<Character[]> alphabet = Optional.empty();

        private Builder() {
        }

        public Builder setAlphabet(String alphabet) {
            return this.setAlphabet(PrimTypeUtil.toCharacterArray(alphabet));
        }

        public Builder setAlphabet(Character[] alphabet) {
            this.alphabet = Optional.of(alphabet);
            return this;
        }

        @Override
        public PolluxKeyType create() {
            PolluxKeyType handler = new PolluxKeyType(this.alphabet.orElse(KeyGeneration.ALL_POLLUX_CHARS));
            return handler;
        }

    }
}
