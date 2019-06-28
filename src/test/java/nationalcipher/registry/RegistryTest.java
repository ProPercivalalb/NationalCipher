package nationalcipher.registry;

import static org.junit.Assert.*;

import java.util.function.Function;

import org.junit.Test;

public class RegistryTest {

    @Test
    public void testRegistry() {
        IRegistry<String, String> REGISTRY = Registry.builder(String.class).setNamingScheme(x -> x).setMaxSize(1).build();
        assertEquals("Intial registry size wrong", 0, REGISTRY.size());
        REGISTRY.register("1");
        assertEquals("Mismatching registry size", 1, REGISTRY.size());
        REGISTRY.register("2");
        assertEquals("Mismatching registry size", 1, REGISTRY.size());
    }
}
