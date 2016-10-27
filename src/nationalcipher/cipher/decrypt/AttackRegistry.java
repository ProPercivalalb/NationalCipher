package nationalcipher.cipher.decrypt;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.Settings;
import nationalcipher.cipher.decrypt.complete.*;

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

		registerCipher(new BeaufortAKAttack(), settings);
		registerCipher(new PortaAKAttack(), settings);
		registerCipher(new VariantAKAttack(), settings);
		registerCipher(new VigenereAKAttack(), settings);
		
		registerCipher(new BeaufortPKAttack(), settings);
		registerCipher(new PortaPKAttack(), settings);
		registerCipher(new VariantPKAttack(), settings);
		registerCipher(new VigenerePKAttack(), settings);
		
		registerCipher(new BeaufortNCAttack(), settings);
		registerCipher(new PortaNCAttack(), settings);
		registerCipher(new VariantNCAttack(), settings);
		registerCipher(new VigenereNCAttack(), settings);
		
		registerCipher(new BeaufortSFAttack(), settings);
		registerCipher(new VariantSFAttack(), settings);
		registerCipher(new VigenereSFAttack(), settings);
		
		registerCipher(new QuagmireIAttack(), settings);
		registerCipher(new QuagmireIIAttack(), settings);
		
		registerCipher(new FractionatedMorseAttack(), settings);
		//registerCipher(new NicodemusAttack(), settings);
		
		//Transpostion
		registerCipher(new CadenusAttack(), settings);
		registerCipher(new RailFenceAttack(), settings);
		registerCipher(new RedefenceAttack(), settings);
		registerCipher(new AMSCOAttack(), settings);
		registerCipher(new RouteAttack(), settings);
		registerCipher(new SwagmanAttack(), settings);
		
		//Other
		registerCipher(new PlayfairAttack(), settings);
		registerCipher(new BifidAttack(), settings);
		registerCipher(new ConjugatedBifidAttack(), settings);
		registerCipher(new TrifidAttack(), settings);
		registerCipher(new HillAttack(), settings);
		registerCipher(new SeriatedPlayfairAttack(), settings);
				
		//
		//registerCipher(new NicodemusAttack(), settings);
		//registerCipher(new SeriatedPlayfairAttack(), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
	}
}
