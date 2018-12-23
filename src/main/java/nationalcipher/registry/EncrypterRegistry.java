package nationalcipher.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nationalcipher.cipher.base.other.ADFGX;
import nationalcipher.cipher.base.other.Bifid;
import nationalcipher.cipher.base.other.ConjugatedBifid;
import nationalcipher.cipher.base.other.Digrafid;
import nationalcipher.cipher.base.other.Hill;
import nationalcipher.cipher.base.other.HillExtended;
import nationalcipher.cipher.base.other.HillSubstitution;
import nationalcipher.cipher.base.other.Homophonic;
import nationalcipher.cipher.base.other.Morbit;
import nationalcipher.cipher.base.other.PeriodicGromark;
import nationalcipher.cipher.base.other.Playfair;
import nationalcipher.cipher.base.other.Pollux;
import nationalcipher.cipher.base.other.SeriatedPlayfair;
import nationalcipher.cipher.base.other.Solitaire;
import nationalcipher.cipher.base.other.Trifid;
import nationalcipher.cipher.base.substitution.Affine;
import nationalcipher.cipher.base.substitution.Autokey;
import nationalcipher.cipher.base.substitution.Bazeries;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Enigma;
import nationalcipher.cipher.base.substitution.FourSquare;
import nationalcipher.cipher.base.substitution.FractionatedMorse;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.base.substitution.Nicodemus;
import nationalcipher.cipher.base.substitution.NihilistSubstitution;
import nationalcipher.cipher.base.substitution.Porta;
import nationalcipher.cipher.base.substitution.Portax;
import nationalcipher.cipher.base.substitution.ProgressiveKey;
import nationalcipher.cipher.base.substitution.QuagmireI;
import nationalcipher.cipher.base.substitution.QuagmireII;
import nationalcipher.cipher.base.substitution.QuagmireIII;
import nationalcipher.cipher.base.substitution.QuagmireIV;
import nationalcipher.cipher.base.substitution.RunningKey;
import nationalcipher.cipher.base.substitution.Slidefair;
import nationalcipher.cipher.base.substitution.TriSquare;
import nationalcipher.cipher.base.substitution.TwoSquare;
import nationalcipher.cipher.base.substitution.VigenereFamily;
import nationalcipher.cipher.base.transposition.AMSCO;
import nationalcipher.cipher.base.transposition.Cadenus;
import nationalcipher.cipher.base.transposition.ColumnarTransposition;
import nationalcipher.cipher.base.transposition.Grille;
import nationalcipher.cipher.base.transposition.Myszkowski;
import nationalcipher.cipher.base.transposition.NihilistTransposition;
import nationalcipher.cipher.base.transposition.Phillips;
import nationalcipher.cipher.base.transposition.RailFence;
import nationalcipher.cipher.base.transposition.Redefence;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.transposition.RouteTransposition;

public class EncrypterRegistry {

	public static Map<String, IRandEncrypter> CIPHER = new HashMap<>();
	public static Map<String, Integer> CIPHER_DIFFICUTLY = new HashMap<>();
	
	public static IRandEncrypter getFromName(String name) {
		return CIPHER.get(name);
	}
	
	public static IRandEncrypter getDifficulty(String name) {
		return CIPHER.get(name);
	}
	
	/**
	 * @param maxDifficulty Picks all ciphers with this described difficulty or less
	 * @return A weighted list (towards the harder ciphers)
	 */
	public static List<String> getAllWithDifficulty(int maxDifficulty) {
		List<String> below = new ArrayList<String>();
		for(String key : CIPHER_DIFFICUTLY.keySet())
			if(CIPHER_DIFFICUTLY.get(key) <= maxDifficulty)
				for(int i = 0; i < Math.log(CIPHER_DIFFICUTLY.get(key)) / Math.log(2) + 1; i++) 
					below.add(key);

		return below;
	}
	
	//By Default difficulty is 5 (medium)
	public static void registerEncrypter(IRandEncrypter randEncrypter) {
		registerEncrypter(randEncrypter, 5);
	}
	
	/**
	 * 
	 * @param randEncrypter
	 * @param difficulty Integer 1 to 10 inclusive, 1 being easier to decrypt that 10
	 */
	public static void registerEncrypter(IRandEncrypter randEncrypter, int difficulty) {
		CIPHER.put(randEncrypter.getClass().getSimpleName(), randEncrypter);
		CIPHER_DIFFICUTLY.put(randEncrypter.getClass().getSimpleName(), difficulty);
	}
	
	static {
		registerEncrypter(new ADFGX(), 10);
		registerEncrypter(new Affine(), 1);
		registerEncrypter(new Autokey(), 2);
		registerEncrypter(new AMSCO(), 3);	
		registerEncrypter(new Bazeries(), 3);
		registerEncrypter(new Bifid(), 5);
		registerEncrypter(new Caesar(), 1);
		registerEncrypter(new Cadenus(), 7);
		registerEncrypter(new ColumnarTransposition(), 2);
		registerEncrypter(new ConjugatedBifid(), 6);
		registerEncrypter(new Digrafid(), 5);
		registerEncrypter(new Enigma(), 8);
		registerEncrypter(new FourSquare(), 7);
		registerEncrypter(new FractionatedMorse(), 5);
		registerEncrypter(new Grille(), 5);
		registerEncrypter(new Hill(), 7);
		registerEncrypter(new HillExtended(), 7);
		registerEncrypter(new HillSubstitution(), 8);
		registerEncrypter(new Homophonic(), 5);
		registerEncrypter(new Keyword(), 1);
		registerEncrypter(new Morbit());
		registerEncrypter(new Myszkowski(), 3);
		registerEncrypter(new Nicodemus(), 3);
		registerEncrypter(new NihilistSubstitution(), 4);
		registerEncrypter(new NihilistTransposition(), 6);
		registerEncrypter(new PeriodicGromark());
		registerEncrypter(new Phillips(), 7);
		registerEncrypter(new Playfair(), 8);
		registerEncrypter(new Pollux(), 5);
		registerEncrypter(new Porta(), 2);
		registerEncrypter(new Portax(), 3);
		registerEncrypter(new ProgressiveKey(), 4);
		registerEncrypter(new QuagmireI(), 6);
		registerEncrypter(new QuagmireII(), 6);
		registerEncrypter(new QuagmireIII(), 7);
		registerEncrypter(new QuagmireIV(), 8);
		registerEncrypter(new RailFence(), 1);
		registerEncrypter(new Redefence(), 2);
		registerEncrypter(new RouteTransposition(), 4);
		registerEncrypter(new RunningKey(), 10);
		registerEncrypter(new SeriatedPlayfair(), 8);
		registerEncrypter(new Solitaire(), 10);
		registerEncrypter(new Slidefair(), 2);
		//TODO registerEncrypter(new Swagman());
		registerEncrypter(new Trifid(), 8);
		registerEncrypter(new TwoSquare(), 7);
		registerEncrypter(new TriSquare(), 9);
		registerEncrypter(new VigenereFamily(), 2);
	}
}
