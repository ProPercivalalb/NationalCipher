package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.api.IKeyType;
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
        for(int i = 0; i < this.alphabet.length; i++) {
            if(!ArrayUtil.contains(key, this.alphabet[i])) {
                return false;
            }
        }
        
        // Contains only characters from alphabet
        for(int i = 0; i < key.length; i++) {
            if(!ArrayUtil.contains(this.alphabet, key[i])) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void iterateKeys(Consumer<Character[]> consumer) {
        KeyIterator.iterateObject(consumer, Character.class, 10, alphabet);
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(59049); // Calculate number
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        
        private Optional<Character[]> alphabet = Optional.empty();
        
        private Builder() {}
        
        public Builder setAlphabet(String alphabet) {
            return this.setAlphabet(PrimTypeUtil.toCharacterArray(alphabet));
        }
        
        public Builder setAlphabet(Character[] alphabet) {
            this.alphabet = Optional.of(alphabet);
            return this;
        }
        
        public PolluxKeyType create() {
            PolluxKeyType handler = new PolluxKeyType(this.alphabet.orElse(KeyGeneration.ALL_POLLUX_CHARS));
            return handler;
        }

    }
}
