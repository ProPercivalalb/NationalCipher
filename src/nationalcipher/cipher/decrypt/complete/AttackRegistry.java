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
		registerCipher(new CaesarAttack(), settings);
		registerCipher(new BazeriesAttack(), settings);
		registerCipher(new AffineAttack(), settings);
		registerCipher(new SimpleSubstitutionAttack(), settings);
		registerCipher(new RailFenceAttack(), settings);
		registerCipher(new VigenereAttack(), settings);
		//registerCipher(new (), settings);
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
