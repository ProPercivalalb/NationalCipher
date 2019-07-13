package nationalcipher.registry;

import static nationalcipher.cipher.decrypt.methods.DecryptionMethod.BRUTE_FORCE;

import nationalcipher.Settings;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.anew.AffineCipher;
import nationalcipher.cipher.base.anew.BazeriesCipher;
import nationalcipher.cipher.base.anew.CaesarCipher;
import nationalcipher.cipher.base.anew.NicodemusCipher;
import nationalcipher.cipher.base.anew.PlayfairCipher;
import nationalcipher.cipher.base.anew.PolluxCipher;
import nationalcipher.cipher.base.anew.ProgressiveCipher;
import nationalcipher.cipher.base.anew.RailFenceCipher;
import nationalcipher.cipher.base.anew.SlidefairCipher;
import nationalcipher.cipher.base.anew.VigenereCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.NicodemusAttack;
import nationalcipher.cipher.decrypt.PeriodicKeyAttack;
import nationalcipher.cipher.decrypt.ProgressiveKeyAttack;
import nationalcipher.cipher.decrypt.SACipherAttack;
import nationalcipher.cipher.decrypt.complete.BifidAttack;
import nationalcipher.cipher.decrypt.complete.SeriatedPlayfairAttack;
import nationalcipher.lib.CipherLib;

public class AttackRegistry {

    public static final IRegistry<String, CipherAttack> CIPHERS = Registry.builder(CipherAttack.class).setNamingScheme((reg, value) -> "CIPHER_" + reg.size()).build();

    public static void registerCipher(CipherAttack cipherAttack, Settings settings) {
        registerCipher("CIPHER_" + CIPHERS.size(), cipherAttack, settings);
    }


    public static void registerCipher(String id, CipherAttack cipherAttack, Settings settings) {
        CIPHERS.register(id, cipherAttack);
        settings.addLoadElement(cipherAttack);
    }

    public static void loadCiphers(Settings settings) {
        registerCipher(CipherLib.AFFINE, new CipherAttack<>(new AffineCipher(), "Affine").setAttackMethods(BRUTE_FORCE), settings);
        registerCipher(CipherLib.BAZERIES, new CipherAttack<>(new BazeriesCipher(), "Bazeries").setAttackMethods(BRUTE_FORCE), settings);
        registerCipher(CipherLib.CAESAR, new CipherAttack<>(new CaesarCipher(), "Caesar").setAttackMethods(BRUTE_FORCE), settings);
        registerCipher(CipherLib.POLLUX, new CipherAttack<>(new PolluxCipher(), "Pollux").setAttackMethods(BRUTE_FORCE), settings);
        registerCipher(CipherLib.RAILFENCE, new CipherAttack<>(new RailFenceCipher(), "Railfence").setAttackMethods(BRUTE_FORCE), settings);
        registerCipher(CipherLib.BIFID, new BifidAttack(), settings);   
        registerCipher(CipherLib.PLAYFAIR, new SACipherAttack<>(new PlayfairCipher(), "Playfair"), settings);
        registerCipher(CipherLib.PLAYFAIR_SERIATED, new SeriatedPlayfairAttack(), settings);
        registerCipher(CipherLib.VIGENERE, new PeriodicKeyAttack(new VigenereCipher(VigenereType.VIGENERE), "Vigenere"), settings);
        registerCipher(CipherLib.PORTA, new PeriodicKeyAttack(new VigenereCipher(VigenereType.PORTA), "Porta"), settings);
        registerCipher(CipherLib.VARIANT, new PeriodicKeyAttack(new VigenereCipher(VigenereType.VARIANT), "Variant"), settings);
        registerCipher(CipherLib.BEAUFORT, new PeriodicKeyAttack(new VigenereCipher(VigenereType.BEAUFORT), "Beaufort"), settings);

        registerCipher(CipherLib.VIGENERE + ".progressive_key", new ProgressiveKeyAttack(new ProgressiveCipher(VigenereType.VIGENERE), "Vigenere Progressive Key"), settings);
        registerCipher(CipherLib.PORTA + ".progressive_key", new ProgressiveKeyAttack(new ProgressiveCipher(VigenereType.PORTA), "Porta Progressive Key"), settings);
        registerCipher(CipherLib.VARIANT + ".progressive_key", new ProgressiveKeyAttack(new ProgressiveCipher(VigenereType.VARIANT), "Variant Progressive Key"), settings);
        registerCipher(CipherLib.BEAUFORT + ".progressive_key", new ProgressiveKeyAttack(new ProgressiveCipher(VigenereType.BEAUFORT), "Beaufort Progressive Key"), settings);

        registerCipher(CipherLib.VIGENERE + ".slidefair", new PeriodicKeyAttack(new SlidefairCipher(VigenereType.VIGENERE), "Vigenere Slidefair"), settings); //TODO Add dictionary attack
        registerCipher(CipherLib.VARIANT + ".slidefair", new PeriodicKeyAttack(new SlidefairCipher(VigenereType.VARIANT), "Variant Slidefair"), settings);
        registerCipher(CipherLib.BEAUFORT + ".slidefair", new PeriodicKeyAttack(new SlidefairCipher(VigenereType.BEAUFORT), "Beaufort Slidefair"), settings);

        registerCipher(CipherLib.VIGENERE + ".progressive_key", new ProgressiveKeyAttack(new ProgressiveCipher(VigenereType.VIGENERE), "Vigenere Progressive Key"), settings);
        registerCipher(CipherLib.PORTA + ".progressive_key", new ProgressiveKeyAttack(new ProgressiveCipher(VigenereType.PORTA), "Porta Progressive Key"), settings);
        registerCipher(CipherLib.VARIANT + ".progressive_key", new ProgressiveKeyAttack(new ProgressiveCipher(VigenereType.VARIANT), "Variant Progressive Key"), settings);
        registerCipher(CipherLib.BEAUFORT + ".progressive_key", new ProgressiveKeyAttack(new ProgressiveCipher(VigenereType.BEAUFORT), "Beaufort Progressive Key"), settings);

        registerCipher(CipherLib.VIGENERE + ".nicodemus", new NicodemusAttack(new NicodemusCipher(VigenereType.VIGENERE), "Vigenere Nicodemus"), settings);
        registerCipher(CipherLib.PORTA + ".nicodemus", new NicodemusAttack(new NicodemusCipher(VigenereType.PORTA), "Porta Nicodemus"), settings);
        registerCipher(CipherLib.VARIANT + ".nicodemus", new NicodemusAttack(new NicodemusCipher(VigenereType.VARIANT), "Variant Nicodemus"), settings);
        registerCipher(CipherLib.BEAUFORT + ".nicodemus", new NicodemusAttack(new NicodemusCipher(VigenereType.BEAUFORT), "Beaufort Nicodemus"), settings);
        
//		Substitution
//		registerCipher(new HuttonAttack(), settings);
//
//		registerCipher(CipherLib.CAESAR, new CaesarAttack(), settings);		
//		registerCipher(CipherLib.AFFINE, new AffineAttack(), settings);
//		registerCipher(new SimpleSubstitutionAttack(), settings);
//		registerCipher(new BazeriesAttack(), settings);
//		registerCipher(new TwoSquareAttack(), settings);
//		registerCipher(new FourSquareAttack(), settings);
//		registerCipher(new NihilistSubstitutionAttack(), settings);
//		registerCipher(new BeaufortAttack(), settings);
//		registerCipher(new PortaAttack(), settings);
//		registerCipher(new VariantAttack(), settings);
//		registerCipher(new VigenereAttack(), settings);
//		registerCipher(new PortaxAttack(), settings);
//		registerCipher(new GeneralPeriodAttack(), settings);
//
//		registerCipher(new BeaufortAKAttack(), settings);
//		registerCipher(new PortaAKAttack(), settings);
//		registerCipher(new VariantAKAttack(), settings);
//		registerCipher(new VigenereAKAttack(), settings);
//
//		registerCipher(new BeaufortPKAttack(), settings);
//		registerCipher(new PortaPKAttack(), settings);
//		registerCipher(new VariantPKAttack(), settings);
//		registerCipher(new VigenerePKAttack(), settings);
//
//		registerCipher(new NicodemusAttack("Nicodemus Beaufort", VigenereType.BEAUFORT), settings);
//		registerCipher(new NicodemusAttack("Nicodemus Porta", VigenereType.PORTA), settings);
//		registerCipher(new NicodemusAttack("Nicodemus Variant", VigenereType.VARIANT), settings);
//		registerCipher(new NicodemusAttack("Nicodemus Vigenere", VigenereType.VIGENERE), settings);
//
//		registerCipher(new BeaufortSFAttack(), settings);
//		registerCipher(new VariantSFAttack(), settings);
//		registerCipher(new VigenereSFAttack(), settings);
//
//		registerCipher(new QuagmireIAttack(), settings);
//		registerCipher(new QuagmireIIAttack(), settings);
//
//		registerCipher(new FractionatedMorseAttack(), settings);
//		registerCipher(new TriSquareAttack(), settings);
//
//		//6x6 Polybius Squares
//		registerCipher(new Playfair6x6Attack(), settings);
//
//		//Transposition
//		registerCipher(new CadenusAttack(), settings);
//		registerCipher(new RailFenceAttack(), settings);
//		registerCipher(new RedefenceAttack(), settings);
//		registerCipher(new AMSCOAttack(), settings);
//		registerCipher(new RouteAttack(), settings);
//		registerCipher(new SwagmanAttack(), settings);
//		registerCipher(new PhillipsAttack(), settings);
//		registerCipher(new ColumnarTranspositionAttack(), settings);
//		registerCipher(new MyszkowskiAttack(), settings);
//		registerCipher(new DoubleTranspositionAttack(), settings);
//		registerCipher(new NihilistTranspositionAttack(), settings);
//		registerCipher(new GrilleAttack(), settings);
//
//		//Other
//		registerCipher(new PlayfairAttack(), settings);
//		registerCipher(new ConjugatedBifidAttack(), settings);
//		registerCipher(new TrifidAttack(), settings);
//		registerCipher(new HillAttack(), settings);
//		registerCipher(new HillExtendedAttack(), settings);
//		registerCipher(new HillSubstitutionAttack(), settings);
//		registerCipher(new SeriatedPlayfairAttack(), settings);
//		registerCipher(new PolluxAttack(), settings);
//		registerCipher(new ADFGXAttack("ADFGX", "ADFGX", KeyGeneration.ALL_26_CHARS), settings);
//		registerCipher(new ADFGXAttack("ADFGVX", "ADFGVX", KeyGeneration.ALL_36_CHARS), settings);
//		registerCipher(new EnigmaPlainAttack(), settings);
//		registerCipher(new EnigmaPlugboardAttack(), settings);
//		registerCipher(new EnigmaUhrAttack(), settings);
//		//registerCipher(new EnigmaThinRotor(), settings);
//		registerCipher(new HomophonicAttack(), settings);
//		registerCipher(new DigrafidAttack(), settings);
//		registerCipher(new SolitaireAttack(), settings);
//		registerCipher(new StraddleCheckerboardAttack(), settings);
//		registerCipher(new PeriodicGromarkAttack(), settings);
//		registerCipher(new PolybusSquareAttack(), settings);
//		registerCipher(new RagbabyAttack(), settings);
        CIPHERS.freeze();

        //registerCipher(new Challenge7Attack(), settings);

        //
        //registerCipher(new RagbabyAttack(), settings);
        //registerCipher(new EnigmaThinRotor(), settings);
        //registerCipher(new (), settings);
        //registerCipher(new (), settings);
        //registerCipher(new (), settings);
        //registerCipher(new (), settings);
        //registerCipher(new (), settings);
        //registerCipher(new (), settings);
    }
}
