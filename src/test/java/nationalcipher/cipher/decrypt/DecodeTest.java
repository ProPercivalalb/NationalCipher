package nationalcipher.cipher.decrypt;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javalibrary.dict.Dictionary;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.ICipher;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.anew.ADFGVXCipher;
import nationalcipher.cipher.base.anew.ADFGXCipher;
import nationalcipher.cipher.base.anew.AMSCOCipher;
import nationalcipher.cipher.base.anew.AffineCipher;
import nationalcipher.cipher.base.anew.AutokeyCipher;
import nationalcipher.cipher.base.anew.BazeriesCipher;
import nationalcipher.cipher.base.anew.BifidCipher;
import nationalcipher.cipher.base.anew.CaesarCipher;
import nationalcipher.cipher.base.anew.ColumnarTranspositionCipher;
import nationalcipher.cipher.base.anew.ConjugatedBifidCipher;
import nationalcipher.cipher.base.anew.DigrafidCipher;
import nationalcipher.cipher.base.anew.FourSquareCipher;
import nationalcipher.cipher.base.anew.FractionatedMorseCipher;
import nationalcipher.cipher.base.anew.HomophonicCipher;
import nationalcipher.cipher.base.anew.HuttonCipher;
import nationalcipher.cipher.base.anew.KeywordCipher;
import nationalcipher.cipher.base.anew.MorbitCipher;
import nationalcipher.cipher.base.anew.MyszkowskiCipher;
import nationalcipher.cipher.base.anew.NicodemusCipher;
import nationalcipher.cipher.base.anew.NihilistSubstitutionCipher;
import nationalcipher.cipher.base.anew.NihilistTranspositionCipher;
import nationalcipher.cipher.base.anew.PeriodicGromarkCipher;
import nationalcipher.cipher.base.anew.PhillipsCipher;
import nationalcipher.cipher.base.anew.PlayfairCipher;
import nationalcipher.cipher.base.anew.PolluxCipher;
import nationalcipher.cipher.base.anew.PortaxCipher;
import nationalcipher.cipher.base.anew.ProgressiveCipher;
import nationalcipher.cipher.base.anew.RagbabyCipher;
import nationalcipher.cipher.base.anew.RailFenceCipher;
import nationalcipher.cipher.base.anew.ReadMode;
import nationalcipher.cipher.base.anew.RedefenceCipher;
import nationalcipher.cipher.base.anew.SeriatedPlayfairCipher;
import nationalcipher.cipher.base.anew.SlidefairCipher;
import nationalcipher.cipher.base.anew.SolitaireCipher;
import nationalcipher.cipher.base.anew.SwagmanCipher;
import nationalcipher.cipher.base.anew.TriSquareCipher;
import nationalcipher.cipher.base.anew.TrifidCipher;
import nationalcipher.cipher.base.anew.TwoSquareCipher;
import nationalcipher.cipher.base.anew.VigenereCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.base.keys.TriKey;

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
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(caesarCipher);
        }
    }
    
    @Test
    public void testAffine() {
        // http://practicalcryptography.com/ciphers/affine-cipher/
        AffineCipher affineCipher = new AffineCipher();
        
        Assert.assertTrue(affineCipher.isValid(BiKey.of(3, 23)));
        Assert.assertTrue(affineCipher.isValid(BiKey.of(19, 2)));
        Assert.assertFalse(affineCipher.isValid(BiKey.of(4, 23)));
        Assert.assertFalse(affineCipher.isValid(BiKey.of(3, 32)));
        Assert.assertFalse(affineCipher.isValid(BiKey.of(-2, 12)));
        Assert.assertFalse(affineCipher.isValid(BiKey.of(3, 27)));
        Assert.assertFalse(affineCipher.isValid(BiKey.of(13, -2)));
        
        String plainText = "DEFENDTHEEASTWALLOFTHECASTLE";
        String cipherText = "VGRGBVPNGGOEPWOFFMRPNGKOEPFG";
        BiKey<Integer, Integer> key = BiKey.of(11, 14);
        
        assertEncodeDecode(affineCipher, key, plainText, cipherText);
        assertEncodeDecode(affineCipher, BiKey.of(5, 8), "AFFINECIPHER", "IHHWVCSWFRCP");
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(affineCipher);
        }
    }


    @Test
    public void testColumnarTransposition() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/CompleteColTransposition.pdf
        ColumnarTranspositionCipher transpositionCipher = new ColumnarTranspositionCipher();
        
        String plainText =  "FILLEDBLOCK";
        String cipherText = "IELKLDOFLBC";
        BiKey<Integer[], ReadMode> key = BiKey.of(new Integer[] {2, 0, 1}, ReadMode.DOWN);
        
        assertEncodeDecode(transpositionCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(transpositionCipher);
        }
    }
    
    @Test
    public void testADFGX() {
        // https://en.wikipedia.org/wiki/ADFGVX_cipher
        ADFGXCipher adfgxCipher = new ADFGXCipher();
        
        String plainText = "ATTACKATONCE";
        String cipherText = "FAXDFADDDGDGFFFAFAXAFAFX";
        QuadKey<String, Integer[], String, ReadMode> key = QuadKey.of("BTALPDHOZKQFVSNGICUXMREWY", new Integer[] {1, 0, 4, 2, 3}, "ADFGX", ReadMode.DOWN);
        
        assertEncodeDecode(adfgxCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(adfgxCipher);
        }
    }
    
    
    @Test
    public void testPeriodicGromark() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/PeriodicGromark.pdf
        PeriodicGromarkCipher periodicGromCipher = new PeriodicGromarkCipher();
        
        String plainText = "WINTRYSHOWERSWILLCONTINUEFORTHENEXTFEWDAYSACCORDINGTOTHEFORECAST";
        String cipherText = "RHNAAXNRUZBNIUARXCRTPATBRLIGDSVCIRCVOYPVRAAZZMUSREQYEVMMURGWTLUD";
        String key = "ENIGMA";
        
        assertEncodeDecode(periodicGromCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(periodicGromCipher);
        }
    }
    
    @Test
    public void testMorbit() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Morbit.pdf
        MorbitCipher morbitCipher = new MorbitCipher();
        
        String plainText = "ONCE UPON A TIME";
        String cipherText = "27435881512827465679378";
        Integer[] key = new Integer[] {8, 4, 7, 3, 1, 6, 0, 2, 5};
        
        assertEncodeDecode(morbitCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(morbitCipher);
        }
    }
    
    @Test
    public void testRagbaby() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Ragbaby.pdf
        RagbabyCipher ragbabyCipher = new RagbabyCipher();
        
        String plainText = "WORD DIVISIONS ARE KEPT";
        String cipherText = "YBBL HNGQDUFGL DEF HFYR";
        String key = "GROSBEAKCDFHILMNPQTUVWYZ";
        
        assertEncodeDecode(ragbabyCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(ragbabyCipher, Dictionary.generateRandomText(RandomUtil.pickRandomInt(10, 40), " "));
        }
    }
    
    @Test
    public void testSolitaire() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Ragbaby.pdf
        SolitaireCipher solitaireCipher = new SolitaireCipher();
        
        String plainText = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG";
        String cipherText = "XEOOCHUQFYIJGNESEMKZSNSDZWWADMVQYUT";
        Integer[] key = ArrayUtil.createRangeInteger(54);
        System.out.println(Arrays.toString(key));
        assertEncodeDecode(solitaireCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(solitaireCipher);
        }
    }
    
    @Test
    public void testHomophonic() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Homophonic.pdf
        HomophonicCipher homophonicCipher = new HomophonicCipher();
        
        String plainText = "WORDDIVISIONSMAYBEKEPT";
        String cipherText = "16261199694633038879548312063894672404002789";
        String key = "GOLF";
        
        assertEncodeDecode(homophonicCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(homophonicCipher);
        }
    }
    
    @Test
    public void testADFGVX() {
        // https://en.wikipedia.org/wiki/ADFGVX_cipher
        ADFGVXCipher adfgvxCipher = new ADFGVXCipher();
        
        String plainText = "ATTACKAT1200AM";
        String cipherText = "DGDDDAGDDGAFADDFDADVDVFAADVX";
        QuadKey<String, Integer[], String, ReadMode> key = QuadKey.of("NA1C3H8TB2OME5WRPD4F6G7I9J0KLQSUVXYZ", new Integer[] {3, 4, 2, 5, 0, 1, 6}, "ADFGVX", ReadMode.DOWN);
        
        assertEncodeDecode(adfgvxCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(adfgvxCipher);
        }
    }
    
    @Test
    public void testDigrafid() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Digrafid.pdf
        DigrafidCipher figrafidCipher = new DigrafidCipher();
        
        String plainText = "THISISTHEFORESTPRI";
        String cipherText = "HJMXWSWJADWGFCSPYI";
        TriKey<String, String, Integer> key = TriKey.of("KEYWORDABCFGHIJKMNPQSTUVXYZ#", "VDPEFQRGSTHUIJWCKXAMYLNZBO#", 3);
        
        assertEncodeDecode(figrafidCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(figrafidCipher);
        }
    }
    
    @Test
    public void testRedefence() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Redefence.pdf
        RedefenceCipher redefenceCipher = new RedefenceCipher();
        
        String plainText = "CIVILWARFIELDCIPHER";
        String cipherText = "IIWRILCPECLFDHVAEIR";
        BiKey<Integer[], Integer> key = BiKey.of(new Integer[] {1, 0, 2}, 0);
        
        assertEncodeDecode(redefenceCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(redefenceCipher);
        }
    }
    
    @Test
    public void testRailfence() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Railfence.pdf
        RailFenceCipher railfenceCipher = new RailFenceCipher();
        
        String plainText = "CIVILWARFIELDCIPHER";
        String cipherText = "CLFDHIIWRILCPEVAEIR";
        BiKey<Integer, Integer> key = new BiKey<>(3, 0);
        
        assertEncodeDecode(railfenceCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(railfenceCipher);
        }
    }
    
    @Test
    public void testAMSCO() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Amsco.pdf
        AMSCOCipher amscoCipher = new AMSCOCipher();
        
        String plainText = "INCOMPLETECOLUMNARWITHALTERNATINGSINGLELETTERSANDDIGRAPHS";
        String cipherText = "CECRTEGLENPHPLUTNANTEIOMOWIRSITDDSINTNALINESAALEMHATGLRGR";
        BiKey<Integer[], Boolean> key = new BiKey<>(new Integer[] {3, 0, 2, 1, 4}, true);
        
        assertEncodeDecode(amscoCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(amscoCipher);
        }
    }
    
    @Test
    public void testBifid() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Bifid.pdf
        BifidCipher bifidCipher = new BifidCipher();
        
        String plainText = "ODDPERIODSAREPOPULAR";
        String cipherText = "MWEINGIMGEOYYRLVEYWY";
        BiKey<String, Integer> key = BiKey.of("EXTRAKLMPOHWZQDGVUSIFCBYN", 7);
        
        assertEncodeDecode(bifidCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(bifidCipher);
        }
    }
    
    @Test
    public void testConjugatedBifid() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/CMBifid.pdf
        ConjugatedBifidCipher cmbifidCipher = new ConjugatedBifidCipher();
        
        String plainText = "ODDPERIODSAREPOPULAR";
        String cipherText = "FANXZEXFENUKKRBYNKAK";
        TriKey<String, String, Integer> key = TriKey.of("EXTRAKLMPOHWZQDGVUSIFCBYN", "NCDRSOBFQUVAGPWEYHMXLTIKZ", 7);
        
        assertEncodeDecode(cmbifidCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(cmbifidCipher);
        }
    }
    
    @Test
    public void testHutton() {
        HuttonCipher huttonCipher = new HuttonCipher();
        
        String plainText = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG";
        String cipherText = "DOCBBDFUFVBXWVECHLCAYSKFKUIKKDFFLBH";
        BiKey<String, String> key = new BiKey<>("FEDORA", "JUPITER");
        
        assertEncodeDecode(huttonCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(huttonCipher);
        }
    }
    
    @Test
    public void testKeyword() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Swagman.pdf
        KeywordCipher keywordCipher = new KeywordCipher();
        
        String plainText = "THISISASECRETMESSAGE";
        String cipherText = "QABPBPKPOYNOQHOPPKDO";
        String key = "KEYWORDABCFGHIJLMNPQRSTUVXZ";
        
        assertEncodeDecode(keywordCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(keywordCipher);
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
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(swagCipher);
        }
    }
    
    @Test
    public void testPollux() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Pollux.pdf
        PolluxCipher polluxCipher = new PolluxCipher();
        
        String plainText = "LUCK HELPS";
        String cipherText = "086393425702417685963041456234908745360";
        Character[] key = new Character[] {'.', 'X', '-', '.', '.', 'X', '.', '-', '-', 'X'};
        
        assertEncodeDecode(polluxCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(polluxCipher, Dictionary.generateRandomText(RandomUtil.pickRandomInt(4, 10)));
        }
    }
    
    @Test
    public void testTrifid() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Trifid.pdf
        TrifidCipher trifidCipher = new TrifidCipher();
        
        String plainText = "TRIFIDSAREFRACTIONATEDCIPHERS";
        String cipherText = "EYMXVUCRYYYYEAYVYOVVXITDPATHE";
        BiKey<String, Integer> key = BiKey.of("EXTRAODINYBCFGHJKLMPQSUVWZ#", 10);
        
        assertEncodeDecode(trifidCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(trifidCipher);
        }
    }
    
    @Test
    public void testFractionatedMorse() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Trifid.pdf
        FractionatedMorseCipher fractionatedMorseCipher = new FractionatedMorseCipher();
        
        String plainText = "COMEATONCE";
        String cipherText = "CBIILTMHVVFL";
        String key = "ROUNDTABLECFGHIJKMPQSVWXYZ";
        
        assertEncodeDecode(fractionatedMorseCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(fractionatedMorseCipher);
        }
    }
    
    @Test
    public void testBazeries() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Trifid.pdf
        BazeriesCipher bazeriesCipher = new BazeriesCipher();
        
        String plainText = "SIMPLESUBSTITUTIONPLUSTRANSPOSITION";
        String cipherText = "ACYYUXYMRQKXKCKGCRQIYITNKYXKCYGQGCI";
        Integer key = 3752;
        
        assertEncodeDecode(bazeriesCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(bazeriesCipher);
        }
    }
    
    @Test
    public void testFourSquare() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Foursquare.pdf
        FourSquareCipher fourSquareCipher = new FourSquareCipher();
        
        String plainText = "COMEQUICKLYWENEEDHELP";
        String cipherText = "LEWIXAFNEXCUDXUVDPGXHZ";
        BiKey<String, String> key = BiKey.of("GRDLUEYFNVOAHPWMBIQXTCKSZ", "LICNVOTDPWGHEQXAMFSYRBKUZ");
        
        assertEncodeDecode(fourSquareCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(fourSquareCipher);
        }
    }
    
    @Test
    public void testPhillips() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Phillips.pdf
        PhillipsCipher phillipsCipher = new PhillipsCipher();
        
        String plainText = "SQUARESONEANDFIVEAREACTUALLYTHESAMEASARESQUARESTWOANDEIGHTTHEOVERALLPERIODISFORTY";
        String cipherText = "KZWLYTGEDTQETARBTYGTLFXWLPPOXLTYKUTKGKYTKZWLYTGXSEQETIRZQAAQTCITYKPPVBLHEFHGREYXO";
        TriKey<String, Boolean, Boolean> key = TriKey.of("DIAGONALS", true, true);
        
        assertEncodeDecode(phillipsCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(phillipsCipher);
        }
    }
    
    @Test
    public void testNihilistSubstitution() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/NihilistSubstitution.pdf
        NihilistSubstitutionCipher nihilistSubCipher = new NihilistSubstitutionCipher();
        
        String plainText = "THEEARLYBIRD";
        String cipherText = "655532754365260844345479";
        BiKey<String, String> key = BiKey.of("SIMPLEABCDFGHKNOQRTUVWXYZ", "EASY");
        
        assertEncodeDecode(nihilistSubCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(nihilistSubCipher);
        }
    }
    
    @Test
    public void testNihilistTransposition() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/NihilistTransposition.pdf
        NihilistTranspositionCipher nihilistSubCipher = new NihilistTranspositionCipher();
        
        String plainText = "SQUARENEEDEDHERE";
        String cipherText = "EQDERSEHNUEREADE";
        String cipherText2 = "ERNEQSUADEEDEHRE";
        BiKey<Integer[], ReadMode> key = BiKey.of(new Integer[] {1, 0, 2, 3}, ReadMode.DOWN);
        BiKey<Integer[], ReadMode> key2 = BiKey.of(new Integer[] {1, 0, 2, 3}, ReadMode.ACROSS);
        assertEncodeDecode(nihilistSubCipher, key, plainText, cipherText);
        assertEncodeDecode(nihilistSubCipher, key2, plainText, cipherText2);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(nihilistSubCipher);
        }
    }
    
    @Test
    public void testMyszkowski() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Myszkowski.pdf
        MyszkowskiCipher myszkowskiCipher = new MyszkowskiCipher();
        
        String plainText = "INCOMPLETECOLUMNARWITHPATTERNWORDKEYANDLETTERSUNDERSAMENUMBERTAKENOFFBYROWFROMTOPTOBOTTOM";
        String cipherText = "NOPEEOUNRIHATRWRKYNLTESNESMNMETKNFBRWRMOTBTOILLWTOATDEROOTOCMTCMATPENDEDERURAUBAEFYFOPOTM";
        String key = "BANANA";
        
        assertEncodeDecode(myszkowskiCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(myszkowskiCipher);
        }
    }
    
    @Test
    public void testPlayfair() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Playfair.pdf
        PlayfairCipher playfairCipher = new PlayfairCipher();
        
        // Padding testing
        assertCharSequenceEquals("WEEXEXXQ", playfairCipher.padPlainText("WEEEXX", null));
        assertCharSequenceEquals("XQXQXQXQXQXQXQXQ", playfairCipher.padPlainText("XXXXXXXX", null));
        assertCharSequenceEquals("EXEXQX", playfairCipher.padPlainText("EEXQ", null));
        assertCharSequenceEquals("IXIXIXIX", playfairCipher.padPlainText("JIJI", null));
        
        String plainText = "COMEQUICKLYWENEEDHELP";
        String cipherText = "DLHFSNCNCRZXCQQGFEEQON";
        String key = "LOGARITHMBCDEFKNPQSUVWXYZ";
        
        assertEncodeDecode(playfairCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(playfairCipher);
        }
    }
    
    @Test
    public void testSeriatedPlayfair() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/SeriatedPlayfair.pdf
        SeriatedPlayfairCipher playfairCipher = new SeriatedPlayfairCipher();
        
        // Padding testing
        assertCharSequenceEquals("WEEEXXEXXE", playfairCipher.padPlainText("WEEEEE", new BiKey<>(null, 3)));
        
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
    public void testTwoSquare() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/TwoSquare.pdf
        TwoSquareCipher twoSquareCipher = new TwoSquareCipher();
        
        String plainText = "ANOTHERDIGRAPHICSETUP";
        String cipherText = "IRRTEHMKGIMEQGRUNMMZSV";
        BiKey<String, String> key = BiKey.of("DIALOGUEBCFHKMNPQRSTVWXYZ", "BIOGRAPHYCDEFKLMNQSTUVWXZ");
        
        assertEncodeDecode(twoSquareCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(twoSquareCipher);
        }
    }
    
    @Test
    public void testTriSquare() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Portax.pdf
        TriSquareCipher triSquareCipher = new TriSquareCipher();
        
        String plainText = "THREEKEYSQUARESUSED";
        String cipherText = "RHLQXRLXOEVZBATXSERXDDIUAAABFZ";
        TriKey<String, String, String> key = TriKey.of("NSFMUOAGPWVBHQXECIRYLDKTZ", "READINGBCFHKLMOPQSTUVWXYZ", "PASTINOQRMLYZUEKXWVBHGFDC");
        
        assertEncodeDecode(triSquareCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(triSquareCipher);
        }
    }
    
    @Test
    public void testPortax() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Portax.pdf
        PortaxCipher portaxCipher = new PortaxCipher();
        
        String plainText = "THEEARLYBIRDGETSTHEWORM";
        String cipherText = "NIJAMPBGQCWKHQJEUIKYMPAT";
        String key = "EASY";
        
        assertEncodeDecode(portaxCipher, key, plainText, cipherText);
        
        for(int i = 0; i < 1000; i++) {
            assertCipherLogic(portaxCipher);
        }
    }
    
    @Test
    public void testVigenere() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Slidefair.pdf
        VigenereCipher progCipher = new VigenereCipher(VigenereType.VIGENERE);
        
        String plainText = "INTHEVIGENERECEQUALSKPLUSPWHEREAISZEROBISONEETC";
        String cipherText = "XBEFEGXNEOIKMETEFYLDZWLVWIEJTFPYIDOLRPFBAQCSPRC";
        String key = "POLYALPHABETIC";
        
        assertEncodeDecode(progCipher, key, plainText, cipherText);
        
        for(VigenereType type : VigenereType.NORMAL_LIST) {
            NicodemusCipher cipher = new NicodemusCipher(type);
            
            for(int i = 0; i < 1000; i++) {
                assertCipherLogic(cipher);
            }
        }
    }
    
    @Test
    public void testAutoKey() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Autokey.pdf
        AutokeyCipher autokeyCipher = new AutokeyCipher(VigenereType.VIGENERE);
        
        String plainText = "THEAUTOKEYCANBEUSEDWITHVIGENEREVARIANTBEAUFORTORPORTA";
        String cipherText = "IYMMYKHRIYWTBLISUEQXMNZZLCMGLMMBEEMRROBVIUSHSXOLUCIMO";
        String key = "PRIMER";
        
        assertEncodeDecode(autokeyCipher, key, plainText, cipherText);
        
        for(VigenereType type : VigenereType.NORMAL_LIST) {
            ProgressiveCipher cipher = new ProgressiveCipher(type);
            
            for(int i = 0; i < 1000; i++) {
                assertCipherLogic(cipher);
            }
        }
    }
    
    @Test
    public void testProgressiveKey() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/ProgressiveKey.pdf
        ProgressiveCipher progCipher = new ProgressiveCipher(VigenereType.VIGENERE);
        
        String plainText = "THISCIPHERCANBEUSEDWITHANYOFTHEPERIODICS";
        String cipherText = "ZYIHGNGBMKJSORJAKZMQQMJRTFHBDCNJHJPWXFNO";
        TriKey<String, Integer, Integer> key = TriKey.of("GRAPEFRUIT", 10, 1);
        
        assertEncodeDecode(progCipher, key, plainText, cipherText);
        
        for(VigenereType type : VigenereType.NORMAL_LIST) {
            ProgressiveCipher cipher = new ProgressiveCipher(type);
            
            for(int i = 0; i < 1000; i++) {
                assertCipherLogic(cipher);
            }
        }
    }
    
    @Test
    public void testSlidefair() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Slidefair.pdf
        SlidefairCipher progCipher = new SlidefairCipher(VigenereType.VIGENERE);
        
        String plainText = "THESLIDEFAIRCANBEUSEDWITHVIGENEREVARIANTORBEAUFORT";
        String cipherText = "EWKMCRNUAFCXTJYQMMYYFUTIGWZPKHJMPKBSAIECKVCFMIILCI";
        String key = "DIGRAPH";
        
        assertEncodeDecode(progCipher, key, plainText, cipherText);
        
        for(VigenereType type : VigenereType.SLIDEFAIR_LIST) {
            NicodemusCipher cipher = new NicodemusCipher(type);
            
            for(int i = 0; i < 1000; i++) {
                assertCipherLogic(cipher);
            }
        }
    }
    
    @Test
    public void testNicodemus() {
        // http://www.cryptogram.org/downloads/aca.info/ciphers/Nicodemus.pdf
        NicodemusCipher progCipher = new NicodemusCipher(VigenereType.VIGENERE);
        
        String plainText = "THEEARLYBIRDGETSTHEWORM";
        String cipherText = "HAYREVGNKIXKUWMTWMUGTAH";
        BiKey<String, Integer> key = BiKey.of("CAT", 5);
        
        assertEncodeDecode(progCipher, key, plainText, cipherText);
        
        for(VigenereType type : VigenereType.NORMAL_LIST) {
            NicodemusCipher cipher = new NicodemusCipher(type);
            
            for(int i = 0; i < 1000; i++) {
                assertCipherLogic(cipher);
            }
        }
    }
    
    private <K> void assertCipherLogic(ICipher<K> cipher, CharSequence plainText) {
        K key = cipher.randomiseKey();
        plainText = cipher.padPlainText(plainText, key);
        CharSequence cipherText = cipher.encode(plainText, key);
        assertCharSequenceEquals(plainText, cipher.decode(cipherText, key));
    }
    
    private <K> void assertEncodeDecode(ICipher<K> cipher, K key, CharSequence plainText, CharSequence cipherText) {
        plainText = cipher.padPlainText(plainText, key);
        
        if(cipher.deterministic()) {
            assertCharSequenceEquals(cipherText, cipher.encode(plainText, key));
        }
        assertCharSequenceEquals(plainText, cipher.decode(cipherText, key));
    }
    
    private <K> void assertCipherLogic(ICipher<K> cipher) {
        this.assertCipherLogic(cipher, Dictionary.generateRandomText(RandomUtil.pickRandomInt(10, 40)));
    }
    
    public void assertCharSequenceEquals(CharSequence charSeq1, CharSequence charSeq2) {
        Assert.assertTrue(getErrorMessage(charSeq1, charSeq2), charSeqEqual(charSeq1, charSeq2));
    }
    
    public String getErrorMessage(CharSequence charSeq1, CharSequence charSeq2) {
        StringBuilder builder = new StringBuilder(19 + charSeq1.length() + charSeq2.length());
        builder.append("Expected: ");
        builder.append(charSeq1);
        builder.append(" ");
        builder.append("Actual: ");
        builder.append(charSeq2);
        return builder.toString();
    }
    
    public boolean charSeqEqual(CharSequence charSeq1, CharSequence charSeq2) {
        if(charSeq1.length() != charSeq2.length()) return false;
        
        for(int i = 0; i < charSeq1.length(); i++) {
            if(charSeq1.charAt(i) != charSeq2.charAt(i)) {
                return false;
            }
        }
        
        return true;
        
    }
}
