package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.Settings;

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
		registerCipher(new FourSquareAttack(), settings);
		registerCipher(new NihilistSubstitutionAttack(), settings);
		registerCipher(new PortaxAttack(), settings);
		
		//Transpostion
		registerCipher(new CadenusAttack(), settings);
		registerCipher(new RailFenceAttack(), settings);
		registerCipher(new RedefenceAttack(), settings);
		registerCipher(new AMSCOAttack(), settings);
		registerCipher(new RouteAttack(), settings);
		registerCipher(new SwagmanAttack(), settings);
		
		//Other
		registerCipher(new BeaufortAttack(), settings);
		registerCipher(new PortaAttack(), settings);
		registerCipher(new VariantAttack(), settings);
		registerCipher(new VigenereAttack(), settings);
		registerCipher(new PlayfairAttack(), settings);
		registerCipher(new BifidAttack(), settings);
		registerCipher(new ConjugatedBifidAttack(), settings);
		registerCipher(new ProgressiveKeyBeaufortAttack(), settings);
		
		//
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
		//registerCipher(new (), settings);
	}
}
