package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import javalibrary.math.MathUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.KeyFunction;
import nationalcipher.cipher.base.enigma.EnigmaUtil;
import nationalcipher.cipher.util.CipherUtils;

public class PlugboardKeyType implements IKeyType<Integer[]> {

    private int plugboardSize;
    private int minPlugs;
    private int maxPlugs;
    
    public PlugboardKeyType(int plugboardSize, int minPlugs, int maxPlugs) {
        this.plugboardSize = plugboardSize;
        this.minPlugs = minPlugs;
        this.maxPlugs = maxPlugs;
    }

    @Override
    public Integer[] randomise() {
        int numPlugs = RandomUtil.pickRandomInt(this.minPlugs, this.maxPlugs);
        Integer[] key = ArrayUtil.createRangeInteger(0, this.plugboardSize);
        List<Integer> plugs = ListUtil.range(0, this.plugboardSize - 1);
        for (int i = 0; i < numPlugs; i++) {
            int plug1 = RandomUtil.pickRandomElement(plugs);
            plugs.remove((Integer) plug1);
            int plug2 = RandomUtil.pickRandomElement(plugs);
            plugs.remove((Integer) plug2);
            key[plug1] = plug2;
            key[plug2] = plug1;
        }
        
        return key;
    }

    @Override
    public boolean iterateKeys(KeyFunction<Integer[]> consumer) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Integer[] alterKey(Integer[] key) {
        return key;
    }

    @Override
    public boolean isValid(Integer[] key) {
        if (key.length != this.plugboardSize) {
            return false;
        }
        
        for (int i = 0; i < this.plugboardSize; i++) {
            int other = ArrayUtil.indexOf(key, i);
            if (i == other) continue;

            if (key[i] != other) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String prettifyKey(Integer[] key) {
        return EnigmaUtil.displayPlugboard(key);
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        BigInteger total = BigInteger.ONE; // 0 Plugs
        
        BigInteger numTotal = BigInteger.valueOf(this.plugboardSize);
        
        for (int i = 1; i <= this.plugboardSize / 2; i++) {
            total= total.add(MathUtil.chooseNumPairs(BigInteger.valueOf(i), numTotal));
        }
        System.out.println("Total: " + CipherUtils.formatBigInteger(total));
        return total;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IRangedKeyBuilder<Integer[]> {

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
        public PlugboardKeyType create() {
            PlugboardKeyType handler = new PlugboardKeyType(26, this.min.orElse(0), this.max.orElse(13));
            return handler;
        }
    }
}
