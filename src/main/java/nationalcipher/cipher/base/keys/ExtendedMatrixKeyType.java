package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.function.Consumer;

import javalibrary.math.matrics.Matrix;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
          
public class ExtendedMatrixKeyType implements IKeyType<Matrix> {
    
    private int mod;
    
    public ExtendedMatrixKeyType(int mod) {
        this.mod = mod;
    }

    @Override
    public Matrix randomise(Object partialKey) {
        return KeyGeneration.createMatrix(((BiKey<Matrix, Matrix>)partialKey).getFirstKey().squareSize(), 1, this.mod);
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<Matrix> consumer) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Matrix alterKey(Object fullKey, Matrix key) {
        return key;
    }

    @Override
    public boolean isValid(Object fullKey, Matrix key) {
        return true; //TODO
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

    public static class Builder implements IKeyBuilder<Matrix> {

        private Builder() {
        }

        @Override
        public ExtendedMatrixKeyType create() {
            ExtendedMatrixKeyType handler = new ExtendedMatrixKeyType(26);
            return handler;
        }
    }
}
