package nationalcipher.cipher;

import org.junit.Test;

import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;
import nationalcipher.cipher.base.keys.SwagmanKeyType;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

import static org.junit.Assert.*;

import java.util.Arrays;

public class ICipher {
    
    @Test
    public void testVariableStringKeyHandler() {
        VariableStringKeyType handler = VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setMax(15).create();
        assertFalse(handler.isValid("BIN1"));
        assertTrue(handler.isValid("FOX"));
        assertFalse(handler.isValid("BAMBOO"));
        assertFalse(handler.isValid("2HATTER"));

        for(int i = 0; i < 10; i++) {
            String key = handler.randomise();
            System.out.println("Checking if Short26 is valid: " + key);
            assertTrue(handler.isValid(key));
        }
        
        handler = VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setMax(15).setRepeats().create();
        assertFalse(handler.isValid("BIN1"));
        assertTrue(handler.isValid("FOX"));
        assertTrue(handler.isValid("BAMBOO"));
        assertFalse(handler.isValid("2HATTER"));
        
        for(int i = 0; i < 10; i++) {
            String key = handler.randomise();
            System.out.println("Checking if Short26Repeating is valid: " + key);
            assertTrue(handler.isValid(key));
        }
    }
    
    @Test
    public void testOrderedIntegerKeyHandler() {
        OrderedIntegerKeyType handler = OrderedIntegerKeyType.builder().setRange(2, 8).create();
        assertTrue(handler.isValid(new Integer[] {2, 1, 0}));
        assertFalse(handler.isValid(new Integer[] {7, 1, 2, 3, 4, 5, 6}));
        assertTrue(handler.isValid(new Integer[] {7, 1, 2, 3, 4, 5, 6, 0}));
        assertFalse(handler.isValid(new Integer[] {0, 0}));
        assertTrue(handler.isValid(new Integer[] {4, 1, 2, 3, 0}));
        
        for(int i = 0; i < 10; i++) {
            Integer[] key = handler.randomise();
            System.out.println("Checking if OrderInteger is valid: " + Arrays.toString(key));
            assertTrue(handler.isValid(key));
        }
        
    }
    
    @Test
    public void testSwagmanKeyHandler() {
        SwagmanKeyType handler = SwagmanKeyType.builder().setRange(2, 5).create();
        assertFalse(handler.isValid(new int[] {1, 0}));
        assertTrue(handler.isValid(new int[] {1, 0, 
                                              0, 1}));
        assertTrue(handler.isValid(new int[] {0, 1, 2, 
                                              2, 0, 1,
                                              1, 2, 0}));
        assertFalse(handler.isValid(new int[] {0, 1, 2, 
                                               2, 3, 1,
                                               1, 2, 0}));
        assertFalse(handler.isValid(new int[] {2, 1, 0, 
                                               0, 1, 2,
                                               1, 2, 0}));
        
        
        for(int i = 0; i < 10; i++) {
            int[] key = handler.randomise();
            System.out.println("Checking if Swagman is valid: " + Arrays.toString(key));
            assertTrue(handler.isValid(key));
        }
        
    }
}
