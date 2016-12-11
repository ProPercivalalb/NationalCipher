package nationalcipher.cipher.decrypt;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.Settings;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.decrypt.complete.ADFGXAttack;
import nationalcipher.cipher.decrypt.complete.AMSCOAttack;
import nationalcipher.cipher.decrypt.complete.AffineAttack;
import nationalcipher.cipher.decrypt.complete.BazeriesAttack;
import nationalcipher.cipher.decrypt.complete.BeaufortAKAttack;
import nationalcipher.cipher.decrypt.complete.BeaufortAttack;
import nationalcipher.cipher.decrypt.complete.BeaufortPKAttack;
import nationalcipher.cipher.decrypt.complete.BeaufortSFAttack;
import nationalcipher.cipher.decrypt.complete.BifidAttack;
import nationalcipher.cipher.decrypt.complete.CadenusAttack;
import nationalcipher.cipher.decrypt.complete.CaesarAttack;
import nationalcipher.cipher.decrypt.complete.ColumnarTranspositionAttack;
import nationalcipher.cipher.decrypt.complete.ConjugatedBifidAttack;
import nationalcipher.cipher.decrypt.complete.DigrafidAttack;
import nationalcipher.cipher.decrypt.complete.DoubleTranspositionAttack;
import nationalcipher.cipher.decrypt.complete.EnigmaPlainAttack;
import nationalcipher.cipher.decrypt.complete.EnigmaPlugboardAttack;
import nationalcipher.cipher.decrypt.complete.EnigmaThinRotor;
import nationalcipher.cipher.decrypt.complete.EnigmaUhrAttack;
import nationalcipher.cipher.decrypt.complete.FourSquareAttack;
import nationalcipher.cipher.decrypt.complete.FractionatedMorseAttack;
import nationalcipher.cipher.decrypt.complete.GeneralPeriodAttack;
import nationalcipher.cipher.decrypt.complete.HillAttack;
import nationalcipher.cipher.decrypt.complete.HillExtendedAttack;
import nationalcipher.cipher.decrypt.complete.HillSubstitutionAttack;
import nationalcipher.cipher.decrypt.complete.HomophonicAttack;
import nationalcipher.cipher.decrypt.complete.MyszkowskiAttack;
import nationalcipher.cipher.decrypt.complete.NihilistSubstitutionAttack;
import nationalcipher.cipher.decrypt.complete.NihilistTranspositionAttack;
import nationalcipher.cipher.decrypt.complete.PeriodicGromarkAttack;
import nationalcipher.cipher.decrypt.complete.PhillipsAttack;
import nationalcipher.cipher.decrypt.complete.Playfair6x6Attack;
import nationalcipher.cipher.decrypt.complete.PlayfairAttack;
import nationalcipher.cipher.decrypt.complete.PolluxAttack;
import nationalcipher.cipher.decrypt.complete.PolybusSquareAttack;
import nationalcipher.cipher.decrypt.complete.PortaAKAttack;
import nationalcipher.cipher.decrypt.complete.PortaAttack;
import nationalcipher.cipher.decrypt.complete.PortaPKAttack;
import nationalcipher.cipher.decrypt.complete.PortaxAttack;
import nationalcipher.cipher.decrypt.complete.QuagmireIAttack;
import nationalcipher.cipher.decrypt.complete.QuagmireIIAttack;
import nationalcipher.cipher.decrypt.complete.RailFenceAttack;
import nationalcipher.cipher.decrypt.complete.RedefenceAttack;
import nationalcipher.cipher.decrypt.complete.RouteAttack;
import nationalcipher.cipher.decrypt.complete.SeriatedPlayfairAttack;
import nationalcipher.cipher.decrypt.complete.SimpleSubstitutionAttack;
import nationalcipher.cipher.decrypt.complete.SolitaireAttack;
import nationalcipher.cipher.decrypt.complete.StraddleCheckerboardAttack;
import nationalcipher.cipher.decrypt.complete.SwagmanAttack;
import nationalcipher.cipher.decrypt.complete.TriSquareAttack;
import nationalcipher.cipher.decrypt.complete.TrifidAttack;
import nationalcipher.cipher.decrypt.complete.TwoSquareAttack;
import nationalcipher.cipher.decrypt.complete.VariantAKAttack;
import nationalcipher.cipher.decrypt.complete.VariantAttack;
import nationalcipher.cipher.decrypt.complete.VariantPKAttack;
import nationalcipher.cipher.decrypt.complete.VariantSFAttack;
import nationalcipher.cipher.decrypt.complete.VigenereAKAttack;
import nationalcipher.cipher.decrypt.complete.VigenereAttack;
import nationalcipher.cipher.decrypt.complete.VigenerePKAttack;
import nationalcipher.cipher.decrypt.complete.VigenereSFAttack;
import nationalcipher.cipher.tools.KeyGeneration;

public class AttackRegistry {

	public static List<CipherAttack> ciphers = new ArrayList<CipherAttack>();
	
	public static String[] getNames() {
		String[] names = new String[ciphers.size()];
		for(int i = 0; i < ciphers.size(); ++i)
			names[i] = ciphers.get(i).getDisplayName();
		return names;
	}
	
	public static CipherAttack[] getObjects() {
		CipherAttack[] names = new CipherAttack[ciphers.size()];
		for(int i = 0; i < ciphers.size(); ++i)
			names[i] = ciphers.get(i);
		return names;
	}
	
	public static void registerCipher(CipherAttack cipherAttack, Settings settings) {
		ciphers.add(cipherAttack);
		settings.addLoadElement(cipherAttack);
	}
	
	public static void loadCiphers(Settings settings) {
		//Substitution
		registerCipher(new CaesarAttack(), settings);		
		registerCipher(new AffineAttack(), settings);
		registerCipher(new SimpleSubstitutionAttack(), settings);
		registerCipher(new BazeriesAttack(), settings);
		registerCipher(new TwoSquareAttack(), settings);
		registerCipher(new FourSquareAttack(), settings);
		registerCipher(new NihilistSubstitutionAttack(), settings);
		registerCipher(new BeaufortAttack(), settings);
		registerCipher(new PortaAttack(), settings);
		registerCipher(new VariantAttack(), settings);
		registerCipher(new VigenereAttack(), settings);
		registerCipher(new PortaxAttack(), settings);
		registerCipher(new GeneralPeriodAttack(), settings);
		
		registerCipher(new BeaufortAKAttack(), settings);
		registerCipher(new PortaAKAttack(), settings);
		registerCipher(new VariantAKAttack(), settings);
		registerCipher(new VigenereAKAttack(), settings);
		
		registerCipher(new BeaufortPKAttack(), settings);
		registerCipher(new PortaPKAttack(), settings);
		registerCipher(new VariantPKAttack(), settings);
		registerCipher(new VigenerePKAttack(), settings);
		
		registerCipher(new NicodemusAttack("Nicodemus Beaufort", VigenereType.BEAUFORT), settings);
		registerCipher(new NicodemusAttack("Nicodemus Porta", VigenereType.PORTA), settings);
		registerCipher(new NicodemusAttack("Nicodemus Variant", VigenereType.VARIANT), settings);
		registerCipher(new NicodemusAttack("Nicodemus Vigenere", VigenereType.VIGENERE), settings);
		
		registerCipher(new BeaufortSFAttack(), settings);
		registerCipher(new VariantSFAttack(), settings);
		registerCipher(new VigenereSFAttack(), settings);
		
		registerCipher(new QuagmireIAttack(), settings);
		registerCipher(new QuagmireIIAttack(), settings);
		
		registerCipher(new FractionatedMorseAttack(), settings);
		registerCipher(new TriSquareAttack(), settings);
		
		//6x6 Polybius Squares
		registerCipher(new Playfair6x6Attack(), settings);
		
		//Transpostion
		registerCipher(new CadenusAttack(), settings);
		registerCipher(new RailFenceAttack(), settings);
		registerCipher(new RedefenceAttack(), settings);
		registerCipher(new AMSCOAttack(), settings);
		registerCipher(new RouteAttack(), settings);
		registerCipher(new SwagmanAttack(), settings);
		registerCipher(new PhillipsAttack(), settings);
		registerCipher(new ColumnarTranspositionAttack(), settings);
		registerCipher(new MyszkowskiAttack(), settings);
		registerCipher(new DoubleTranspositionAttack(), settings);
		registerCipher(new NihilistTranspositionAttack(), settings);
		
		//Other
		registerCipher(new PlayfairAttack(), settings);
		registerCipher(new BifidAttack(), settings);
		registerCipher(new ConjugatedBifidAttack(), settings);
		registerCipher(new TrifidAttack(), settings);
		registerCipher(new HillAttack(), settings);
		registerCipher(new HillExtendedAttack(), settings);
		registerCipher(new HillSubstitutionAttack(), settings);
		registerCipher(new SeriatedPlayfairAttack(), settings);
		registerCipher(new PolluxAttack(), settings);
		registerCipher(new ADFGXAttack("ADFGX", "ADFGX", KeyGeneration.ALL_26_CHARS), settings);
		registerCipher(new ADFGXAttack("ADFGVX", "ADFGVX", KeyGeneration.ALL_36_CHARS), settings);
		registerCipher(new EnigmaPlainAttack(), settings);
		registerCipher(new EnigmaPlugboardAttack(), settings);
		registerCipher(new EnigmaUhrAttack(), settings);
		registerCipher(new EnigmaThinRotor(), settings);
		registerCipher(new HomophonicAttack(), settings);
		registerCipher(new DigrafidAttack(), settings);
		registerCipher(new SolitaireAttack(), settings);
		registerCipher(new StraddleCheckerboardAttack(), settings);
		registerCipher(new PeriodicGromarkAttack(), settings);
		registerCipher(new PolybusSquareAttack(), settings);
		
		//
		//registerCipher(new PeriodicGromarkAttack(), settings);
		//registerCipher(new EnigmaThinRotor(), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
	}
}
