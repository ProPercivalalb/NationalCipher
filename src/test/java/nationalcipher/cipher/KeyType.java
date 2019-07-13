package nationalcipher.cipher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;
import nationalcipher.cipher.base.keys.SwagmanKeyType;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class KeyType {
    
    @Test
    public void testVariableStringKeyHandler() {
        VariableStringKeyType handler = VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setMax(15).create();
        assertFalse(handler.isValid("BIN1", null));
        assertTrue(handler.isValid("FOX", null));
        assertFalse(handler.isValid("BAMBOO", null));
        assertFalse(handler.isValid("2HATTER", null));

        for(int i = 0; i < 10; i++) {
            String key = handler.randomise(null);
            System.out.println("Checking if Short26 is valid: " + key);
            assertTrue(handler.isValid(key, null));
        }
        
        handler = VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setMax(15).setRepeats().create();
        assertFalse(handler.isValid("BIN1", null));
        assertTrue(handler.isValid("FOX", null));
        assertTrue(handler.isValid("BAMBOO", null));
        assertFalse(handler.isValid("2HATTER", null));
        
        for(int i = 0; i < 10; i++) {
            String key = handler.randomise(null);
            System.out.println("Checking if Short26Repeating is valid: " + key);
            assertTrue(handler.isValid(key, null));
        }
    }
    
    @Test
    public void testOrderedIntegerKeyHandler() {
        OrderedIntegerKeyType handler = OrderedIntegerKeyType.builder().setRange(2, 8).create();
        assertTrue(handler.isValid(new Integer[] {2, 1, 0}, null));
        assertFalse(handler.isValid(new Integer[] {7, 1, 2, 3, 4, 5, 6}, null));
        assertTrue(handler.isValid(new Integer[] {7, 1, 2, 3, 4, 5, 6, 0}, null));
        assertFalse(handler.isValid(new Integer[] {0, 0}, null));
        assertTrue(handler.isValid(new Integer[] {4, 1, 2, 3, 0}, null));
        
        for(int i = 0; i < 10; i++) {
            Integer[] key = handler.randomise(null);
            System.out.println("Checking if OrderInteger is valid: " + Arrays.toString(key));
            assertTrue(handler.isValid(key, null));
        }
        
    }
    
    @Test
    public void testSwagmanKeyHandler() {
        SwagmanKeyType handler = SwagmanKeyType.builder().setRange(2, 5).create();
        assertFalse(handler.isValid(new int[] {1, 0}, null));
        assertTrue(handler.isValid(new int[] {1, 0, 
                                              0, 1}, null));
        assertTrue(handler.isValid(new int[] {0, 1, 2, 
                                              2, 0, 1,
                                              1, 2, 0}, null));
        assertFalse(handler.isValid(new int[] {0, 1, 2, 
                                               2, 3, 1,
                                               1, 2, 0}, null));
        assertFalse(handler.isValid(new int[] {2, 1, 0, 
                                               0, 1, 2,
                                               1, 2, 0}, null));
        
        
        for(int i = 0; i < 10; i++) {
            int[] key = handler.randomise(null);
            System.out.println("Checking if Swagman is valid: " + Arrays.toString(key));
            assertTrue(handler.isValid(key, null));
        }
        
    }
}
