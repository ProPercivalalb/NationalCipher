package nationalcipher.registry;

import javax.annotation.Nullable;

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

	public static IRegistry<IRandEncrypter> RAND_ENCRYPTERS = Registry.builder(IRandEncrypter.class).build();
	
	public static IRandEncrypter getFromName(String key) {
		return RAND_ENCRYPTERS.get(key);
	}
	
	public static int getDifficulty(String name) {
		return RAND_ENCRYPTERS.apply(name, IRandEncrypter::getDifficulty).orElse(0);
	}
	
	@Nullable
	public static IRandEncrypter getEncrypterWith(int maxDifficulty) {
		return RAND_ENCRYPTERS.getValues().stream()
				.filter(v -> v.getDifficulty() <= maxDifficulty)
				.findAny()
				.orElse(null);
	}
	
	public static void registerAll() {
		RAND_ENCRYPTERS.registerAll(new ADFGX(), new Affine(), new Autokey(), new AMSCO());
		RAND_ENCRYPTERS.register(new Bazeries());
		RAND_ENCRYPTERS.register(new Bifid());
		RAND_ENCRYPTERS.register(new Caesar());
		RAND_ENCRYPTERS.register(new Cadenus());
		RAND_ENCRYPTERS.register(new ColumnarTransposition());
		RAND_ENCRYPTERS.register(new ConjugatedBifid());
		RAND_ENCRYPTERS.register(new Digrafid());
		RAND_ENCRYPTERS.register(new Enigma());
		RAND_ENCRYPTERS.register(new FourSquare());
		RAND_ENCRYPTERS.register(new FractionatedMorse());
		RAND_ENCRYPTERS.register(new Grille());
		RAND_ENCRYPTERS.registerAll(new Hill(), new HillExtended(), new HillSubstitution());
		RAND_ENCRYPTERS.register(new Homophonic());
		RAND_ENCRYPTERS.register(new Keyword());
		RAND_ENCRYPTERS.register(new Morbit());
		RAND_ENCRYPTERS.register(new Myszkowski());
		RAND_ENCRYPTERS.register(new Nicodemus());
		RAND_ENCRYPTERS.register(new NihilistSubstitution());
		RAND_ENCRYPTERS.register(new NihilistTransposition());
		RAND_ENCRYPTERS.register(new PeriodicGromark());
		RAND_ENCRYPTERS.register(new Phillips());
		RAND_ENCRYPTERS.register(new Playfair());
		RAND_ENCRYPTERS.register(new Pollux());
		RAND_ENCRYPTERS.register(new Porta());
		RAND_ENCRYPTERS.register(new Portax());
		RAND_ENCRYPTERS.register(new ProgressiveKey());
		RAND_ENCRYPTERS.registerAll(new QuagmireI(), new QuagmireII(), new QuagmireIII(), new QuagmireIV());
		RAND_ENCRYPTERS.registerAll(new RailFence(), new Redefence());
		RAND_ENCRYPTERS.register(new RouteTransposition());
		RAND_ENCRYPTERS.register(new RunningKey());
		RAND_ENCRYPTERS.register(new SeriatedPlayfair());
		RAND_ENCRYPTERS.register(new Solitaire());
		RAND_ENCRYPTERS.register(new Slidefair());
		//TODO RAND_ENCRYPTERS.register(new Swagman());
		RAND_ENCRYPTERS.register(new Trifid());
		RAND_ENCRYPTERS.register(new TwoSquare());
		RAND_ENCRYPTERS.register(new TriSquare());
		RAND_ENCRYPTERS.register(new VigenereFamily());
	}
	
	static {
		registerAll();
	}
}
