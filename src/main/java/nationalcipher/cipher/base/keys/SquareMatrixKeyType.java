package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;

import javalibrary.math.matrics.Matrix;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.KeyFunction;
import nationalcipher.cipher.tools.KeyGeneration;

public class SquareMatrixKeyType implements IKeyType<Matrix> {
    
    private int mod;
    private int min;
    private int max;
    
    public SquareMatrixKeyType(int mod, int min, int max) {
        this.mod = mod;
        this.min = min;
        this.max = max;
    }

    @Override
    public Matrix randomise() {
        Matrix matrix;
        do {
            matrix = KeyGeneration.createMatrix(RandomUtil.pickRandomInt(this.min, this.max), this.mod);
        }
        while(!matrix.hasInverseMod(this.mod));
        
        return matrix;
    }

    @Override
    public boolean iterateKeys(KeyFunction<Matrix> consumer) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Matrix alterKey(Matrix key) {
        return key;
    }

    @Override
    public boolean isValid(Matrix key) {
        return true;
    }

    @Override
    public String prettifyKey(Matrix key) {
        return key.toString();
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        BigInteger total = BigInteger.ONE; // 0 Plugs
        return total;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IRangedKeyBuilder<Matrix> {

        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();

        private Builder() {
        }

        @Override
        public Builder setMin(int min) {
            this.min = Optional.of(min);
            return this;
        }

        @Override
        public Builder setMax(int max) {
            this.max = Optional.of(max);
            return this;
        }
        
        @Override
        public Builder setRange(int min, int max) {
            return this.setMin(min).setMax(max);
        }

        @Override
        public Builder setSize(int size) {
            return this.setRange(size, size);
        }

        @Override
        public SquareMatrixKeyType create() {
            SquareMatrixKeyType handler = new SquareMatrixKeyType(26, this.min.orElse(2), this.max.orElse(4));
            return handler;
        }
    }
}
