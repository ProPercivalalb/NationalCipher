package nationalcipher.util;

import org.junit.Assert;
import org.junit.Test;

import nationalcipher.cipher.decrypt.DecodeTest;

public class WrapperTest {

    @Test
    public void testWrappers() {
        CharSequence wrapper = new CharArrayWrapper("HELP".toCharArray());
        Assert.assertEquals("HELP", wrapper.toString());
        DecodeTest.assertCharSequenceEquals("EL", wrapper.subSequence(1, 3));
        
        wrapper = new CharacterArrayWrapper(new Character[] { 'L', 'O', 'O', 'K', 'U', 'P', 'N', 'O', 'W' });
        Assert.assertEquals("LOOKUPNOW", wrapper.toString());
        DecodeTest.assertCharSequenceEquals("UPNOW", wrapper.subSequence(4, 9));
        DecodeTest.assertCharSequenceEquals("LOOK", wrapper.subSequence(0, 4));
    }
}
