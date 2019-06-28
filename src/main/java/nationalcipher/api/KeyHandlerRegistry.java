package nationalcipher.api;

import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.registry.IRegistry;
import nationalcipher.registry.Registry;

public class KeyHandlerRegistry {
    
    @SuppressWarnings("rawtypes")
    private static IRegistry<String, ICipher> CIPHERS = Registry.builder(ICipher.class).build();
    
    public static <K> void register(String id, ICipher<K> cipher) {
        CIPHERS.register(id, cipher);
    }
    
    public static void main(String[] args) {
        register("progKey", new ProgressiveCipher(VigenereType.VIGENERE));
        
        ICipher<?> progKeyHandler = CIPHERS.get("progKey");
        System.out.println(progKeyHandler.randomEncode("HAHHAHAHAHHWELCOMETOTHEWAYOFTHEWALKERS"));
        
        VariableStringKeyType handler = VariableStringKeyType.builder().setRepeats().setAlphabet(KeyGeneration.ALL_POLLUX_CHARS).setRange(2, 9).create();
    }
} 
