package nationalcipher.cipher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;
import nationalcipher.cipher.base.keys.PlugboardKeyType;
import nationalcipher.cipher.base.keys.SwagmanKeyType;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class KeyType {
    
    @Test
    public void testVariableStringKeyHandler() {
        VariableStringKeyType handler = VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setMax(15).create();
        assertFalse(handler.isValid(null, "BIN1"));
        assertTrue(handler.isValid(null, "FOX"));
        assertFalse(handler.isValid(null, "BAMBOO"));
        assertFalse(handler.isValid(null, "2HATTER"));

        for(int i = 0; i < 10; i++) {
            String key = handler.randomise(null);
            System.out.println("Checking if Short26 is valid: " + key);
            assertTrue(handler.isValid(null, key));
        }
        
        handler = VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setMax(15).setRepeats().create();
        assertFalse(handler.isValid(null, "BIN1"));
        assertTrue(handler.isValid(null, "FOX"));
        assertTrue(handler.isValid(null, "BAMBOO"));
        assertFalse(handler.isValid(null, "2HATTER"));
        
        for(int i = 0; i < 10; i++) {
            String key = handler.randomise(null);
            System.out.println("Checking if Short26Repeating is valid: " + key);
            assertTrue(handler.isValid(null, key));
        }
    }
    
    @Test
    public void testOrderedIntegerKeyHandler() {
        OrderedIntegerKeyType handler = OrderedIntegerKeyType.builder().setRange(2, 8).create();
        assertTrue(handler.isValid(null, new Integer[] {2, 1, 0}));
        assertFalse(handler.isValid(null, new Integer[] {7, 1, 2, 3, 4, 5, 6}));
        assertTrue(handler.isValid(null, new Integer[] {7, 1, 2, 3, 4, 5, 6, 0}));
        assertFalse(handler.isValid(null, new Integer[] {0, 0}));
        assertTrue(handler.isValid(null, new Integer[] {4, 1, 2, 3, 0}));
        
        for(int i = 0; i < 10; i++) {
            Integer[] key = handler.randomise(null);
            System.out.println("Checking if OrderInteger is valid: " + Arrays.toString(key));
            assertTrue(handler.isValid(null, key));
        }
    }
    
    @Test
    public void testSwagmanKeyHandler() {
        SwagmanKeyType handler = SwagmanKeyType.builder().setRange(2, 5).create();
        assertFalse(handler.isValid(null, new int[] {1, 0}));
        assertTrue(handler.isValid(null, new int[] {1, 0, 
                                                    0, 1}));
        assertTrue(handler.isValid(null, new int[] {0, 1, 2, 
                                                    2, 0, 1,
                                                    1, 2, 0}));
        assertFalse(handler.isValid(null, new int[] {0, 1, 2, 
                                                     2, 3, 1,
                                                     1, 2, 0}));
        assertFalse(handler.isValid(null, new int[] {2, 1, 0, 
                                                     0, 1, 2,
                                                     1, 2, 0}));
        
        
        for(int i = 0; i < 10; i++) {
            int[] key = handler.randomise(null);
            System.out.println("Checking if Swagman is valid: " + Arrays.toString(key));
            assertTrue(handler.isValid(null, key));
        }
        
    }
    
    @Test
    public void testPlugboardKeyHandler() {
        PlugboardKeyType handler = PlugboardKeyType.builder().setRange(2, 8).create();
        assertTrue(handler.isValid(null, new Integer[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25}));
        assertTrue(handler.isValid(null, new Integer[] {1,0,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,25,18,19,20,21,22,23,24,17}));
        assertFalse(handler.isValid(null, new Integer[] {1,0,2,3,5,6,4,7,8,9,10,11,12,13,14,15,16,25,18,19,20,21,22,23,24,17}));
        assertFalse(handler.isValid(null, new Integer[] {0, 1, 2, 3}));
        assertFalse(handler.isValid(null, new Integer[] {25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25}));
        
        for(int i = 0; i < 10; i++) {
            Integer[] key = handler.randomise(null);
            System.out.println("Checking if Plugboard is valid: " + Arrays.toString(key));
            assertTrue(handler.isValid(null, key));
        }
    }
}
