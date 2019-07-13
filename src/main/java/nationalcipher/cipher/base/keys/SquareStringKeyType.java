package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;

public class SquareStringKeyType implements IKeyType<String> {

    // Both inclusive
    private final Character[] alphabet;
    private int rows;
    private int columns;

    private SquareStringKeyType(Character[] alphabet, int rows, int columns) {
        this.alphabet = alphabet;
        this.rows = rows;
        this.columns = columns;
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
        return KeyManipulation.modifyKeySquare(key, this.columns, this.rows);
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.ZERO;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IKeyBuilder<String, SquareStringKeyType> {

        private Optional<Character[]> alphabet = Optional.empty();
        private Optional<Integer> rows = Optional.empty();
        private Optional<Integer> columns = Optional.empty();

        private Builder() {
        }

        public Builder setAlphabet(String alphabet) {
            return this.setAlphabet(PrimTypeUtil.toCharacterArray(alphabet));
        }

        public Builder setAlphabet(Character[] alphabet) {
            this.alphabet = Optional.of(alphabet);
            return this;
        }

        public Builder setDim(int rows, int columns) {
            this.rows = Optional.of(rows);
            this.columns = Optional.of(columns);
            return this;
        }

        @Override
        public SquareStringKeyType create() {
            Character[] alphabet = this.alphabet.orElse(KeyGeneration.ALL_26_CHARS);
            SquareStringKeyType handler = new SquareStringKeyType(alphabet, this.rows.orElse(1), this.columns.orElse(alphabet.length));
            return handler;
        }

    }
}
