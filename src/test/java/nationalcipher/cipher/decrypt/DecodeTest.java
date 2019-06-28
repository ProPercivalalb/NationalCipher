package nationalcipher.cipher.decrypt;

import org.junit.Test;

import javalibrary.dict.Dictionary;
import javalibrary.util.RandomUtil;
import nationalcipher.api.CaesarCipher;
import nationalcipher.api.HuttonCipher;
import nationalcipher.api.ICipher;
import nationalcipher.api.PlayfairCipher;
import nationalcipher.api.PolluxCipher;
import nationalcipher.api.ProgressiveCipher;
import nationalcipher.api.SeriatedPlayfairCipher;
import nationalcipher.api.SwagmanCipher;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.TriKey;

import static org.junit.Assert.*;

import org.junit.BeforeClass;

public class DecodeTest {
	
    @BeforeClass
    public static void setup() {
        Dictionary.onLoad();
    }
    
    @Test
    public void testCaesar() {
        CaesarCipher caesarCipher = new CaesarCipher();
        
        String plainText = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG";
        String cipherText = "QEBNRFZHYOLTKCLUGRJMPLSBOQEBIXWVALD";
        int key = 23;
        
        assertEncodeDecode(caesarCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 100; i++) {
            assertCipherLogic(caesarCipher);
        }
    }
    
    @Test
    public void testHutton() {
        HuttonCipher huttonCipher = new HuttonCipher();
        
        String plainText = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG";
        String cipherText = "DOCBBDFUFVBXWVECHLCAYSKFKUIKKDFFLBH";
        BiKey<String, String> key = new BiKey<>("FEDORA", "JUPITER");
        
        assertEncodeDecode(huttonCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 100; i++) {
            assertCipherLogic(huttonCipher);
        }
    }
    
    @Test
    public void testSwagman() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Swagman.pdf
        SwagmanCipher swagCipher = new SwagmanCipher();
        
        String plainText = "DONTBEAFRAIDTOTAKEABIGLEAPIFONEISINDICATEDYOUCANNOTCROSSARIVERORACHASMINTWOSMALLJUMPS";
        String cipherText = "ENDSCMORDANIBOISICTNASTGBLTEWAOAREEFSAIDVPYRMOEAIAFUILRLDOCOTJNRAAENOUNCMITSOAPHSKATI";
        int[] key = new int[] {2,1,0,3,4,0,4,2,1,3,1,3,4,2,0,4,2,3,0,1,3,0,1,4,2};
        
        assertEncodeDecode(swagCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 100; i++) {
            assertCipherLogic(swagCipher);
        }
    }
    
    @Test
    public void testPollux() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Pollux.pdf
        String plainText = "LUCK HELPS";
        String cipherText = "086393425702417685963041456234908745360";
        PolluxCipher polluxCipher = new PolluxCipher();
        Character[] key = new Character[] {'.', 'X', '-', '.', '.', 'X', '.', '-', '-', 'X'};
        //assertEquals(polluxCipher.encode(plainText, key), cipherText);
        assertEquals(plainText, polluxCipher.decode(cipherText, key));
        
        for(int i = 0; i < 100; i++) {
            assertCipherLogic(polluxCipher, Dictionary.generateRandomText(RandomUtil.pickRandomInt(4, 10)));
        }
    }
    
    @Test
    public void testPlayfair() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Playfair.pdf
        PlayfairCipher playfairCipher = new PlayfairCipher();
        
        // Padding testing
        assertEquals("WEEXEXXQ", playfairCipher.padPlainText("WEEEXX", null));
        assertEquals("XQXQXQXQXQXQXQXQ", playfairCipher.padPlainText("XXXXXXXX", null));
        assertEquals("EXEXQX", playfairCipher.padPlainText("EEXQ", null));
        assertEquals("IXIXIXIX", playfairCipher.padPlainText("JIJI", null));
        
        String plainText = "COMEQUICKLYWENEEDHELP";
        String cipherText = "DLHFSNCNCRZXCQQGFEEQON";
        String key = "LOGARITHMBCDEFKNPQSUVWXYZ";
        
        assertEncodeDecode(playfairCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 100; i++) {
            assertCipherLogic(playfairCipher);
        }
    }
    
    @Test
    public void testSeriatedPlayfair() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/SeriatedPlayfair.pdf
        SeriatedPlayfairCipher playfairCipher = new SeriatedPlayfairCipher();
        
        // Padding testing
        assertEquals("WEEEXXEXXE", playfairCipher.padPlainText("WEEEEE", new BiKey<>(null, 3)));
        
        String plainText = "COMEQUICKLYWENEEDHELPIMMEDIATELYTOM";

        String cipherText = "NLBCSPCDFGXZQQCDCMGCGQTBHCFTRHFGWHGB";
        BiKey<String, Integer> key = new BiKey<>("LOGARITHMBCDEFKNPQSUVWXYZ", 6);
        System.out.println(playfairCipher.padPlainText(plainText, key)); 
        
        assertEncodeDecode(playfairCipher, key, plainText, cipherText);
        
        //for(int i = 0; i < 100; i++) {
            //assertCipherLogic(playfairCipher);
       // }
    }
    
    @Test
    public void testProgressiveKey() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/ProgressiveKey.pdf
        ProgressiveCipher progCipher = new ProgressiveCipher(VigenereType.VIGENERE);
        
        String plainText = "THISCIPHERCANBEUSEDWITHANYOFTHEPERIODICS";
        String cipherText = "ZYIHGNGBMKJSORJAKZMQQMJRTFHBDCNJHJPWXFNO";
        TriKey<String, Integer, Integer> key = new TriKey<>("GRAPEFRUIT", 10, 1);
        
        assertEncodeDecode(progCipher, key, plainText, cipherText);
        
        for(VigenereType type : VigenereType.NORMAL_LIST) {
            ProgressiveCipher cipher = new ProgressiveCipher(type);
            
            for(int i = 0; i < 100; i++) {
                assertCipherLogic(cipher);
            }
        }
    }
    
    private <K> void assertCipherLogic(ICipher<K> cipher, String plainText) {
        K key = cipher.randomise();
        plainText = cipher.padPlainText(plainText, key);
        String cipherText = cipher.encode(plainText, key);
        assertEquals(plainText, cipher.decode(cipherText, key));
    }
    
    private <K> void assertEncodeDecode(ICipher<K> cipher, K key, String plainText, String cipherText) {
        plainText = cipher.padPlainText(plainText, key);
        
        assertEquals(cipherText, cipher.encode(plainText, key));
        assertEquals(plainText, cipher.decode(cipherText, key));
    }
    
    private <K> void assertCipherLogic(ICipher<K> cipher) {
        this.assertCipherLogic(cipher, Dictionary.generateRandomText(RandomUtil.pickRandomInt(10, 40)));
    }
}
