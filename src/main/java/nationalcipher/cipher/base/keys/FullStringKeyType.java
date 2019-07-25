package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.math.MathUtil;
import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;

public class FullStringKeyType implements IKeyType<String> {

    // Both inclusive
    private final Character[] alphabet;

    private FullStringKeyType(Character[] alphabet) {
        this.alphabet = alphabet;
    }

    @Override
    public String randomise(Object partialKey) {
        return KeyGeneration.createLongKeyUniversal(this.alphabet);
    }

    @Override
    public boolean isValid(Object partialKey, String key) {
        if (key.length() != this.alphabet.length) {
            return false;
        }

        for (int i = 0; i < key.length(); i++) {
            if (!ArrayUtil.contains(this.alphabet, key.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<String> consumer) {
        KeyIterator.permuteString(consumer, this.alphabet);
    }

    @Override
    public String alterKey(Object fullKey, String key) {
        return KeyManipulation.swapTwoCharacters(key);
    }

    @Override
    public BigInteger getNumOfKeys() {
        return MathUtil.factorialBig(BigInteger.valueOf(this.alphabet.length));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IKeyBuilder<String> {

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
        public FullStringKeyType create() {
            FullStringKeyType handler = new FullStringKeyType(this.alphabet.orElse(KeyGeneration.ALL_26_CHARS));
            return handler;
        }

    }
}
