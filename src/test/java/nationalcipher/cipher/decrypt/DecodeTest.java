package nationalcipher.cipher.decrypt;

import org.junit.Test;

import nationalcipher.cipher.base.substitution.Caesar;

import static org.junit.Assert.*;

public class DecodeTest {
	
    @Test
    public void testCipherLogic() {
    	assertEquals(Caesar.encode("THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG", 23), "QEBNRFZHYOLTKCLUGRJMPLSBOQEBIXWVALD");
    }
}
