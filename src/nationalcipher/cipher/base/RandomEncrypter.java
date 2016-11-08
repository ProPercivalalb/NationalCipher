package nationalcipher.cipher.base;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.cipher.base.other.ADFGX;
import nationalcipher.cipher.base.other.Bifid;
import nationalcipher.cipher.base.other.ConjugatedBifid;
import nationalcipher.cipher.base.other.Digrafid;
import nationalcipher.cipher.base.other.Hill;
import nationalcipher.cipher.base.other.Homophonic;
import nationalcipher.cipher.base.other.Morbit;
import nationalcipher.cipher.base.other.PeriodicGromark;
import nationalcipher.cipher.base.other.Playfair;
import nationalcipher.cipher.base.other.Pollux;
import nationalcipher.cipher.base.other.SeriatedPlayfair;
import nationalcipher.cipher.base.other.Solitaire;
import nationalcipher.cipher.base.other.Trifid;
import nationalcipher.cipher.base.substitution.Affine;
import nationalcipher.cipher.base.substitution.Bazeries;
import nationalcipher.cipher.base.substitution.Beaufort;
import nationalcipher.cipher.base.substitution.BeaufortAutokey;
import nationalcipher.cipher.base.substitution.BeaufortProgressiveKey;
import nationalcipher.cipher.base.substitution.BeaufortSlidefair;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Enigma;
import nationalcipher.cipher.base.substitution.FourSquare;
import nationalcipher.cipher.base.substitution.FractionatedMorse;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.base.substitution.NihilistSubstitution;
import nationalcipher.cipher.base.substitution.Porta;
import nationalcipher.cipher.base.substitution.PortaAutokey;
import nationalcipher.cipher.base.substitution.PortaProgressiveKey;
import nationalcipher.cipher.base.substitution.Portax;
import nationalcipher.cipher.base.substitution.QuagmireI;
import nationalcipher.cipher.base.substitution.QuagmireII;
import nationalcipher.cipher.base.substitution.QuagmireIII;
import nationalcipher.cipher.base.substitution.QuagmireIV;
import nationalcipher.cipher.base.substitution.RunningKey;
import nationalcipher.cipher.base.substitution.TriSquare;
import nationalcipher.cipher.base.substitution.TwoSquare;
import nationalcipher.cipher.base.substitution.Variant;
import nationalcipher.cipher.base.substitution.VariantAutokey;
import nationalcipher.cipher.base.substitution.VariantProgressiveKey;
import nationalcipher.cipher.base.substitution.VariantSlidefair;
import nationalcipher.cipher.base.substitution.Vigenere;
import nationalcipher.cipher.base.substitution.VigenereAutokey;
import nationalcipher.cipher.base.substitution.VigenereProgressiveKey;
import nationalcipher.cipher.base.substitution.VigenereSlidefair;
import nationalcipher.cipher.base.transposition.AMSCO;
import nationalcipher.cipher.base.transposition.Cadenus;
import nationalcipher.cipher.base.transposition.Columnar;
import nationalcipher.cipher.base.transposition.Myszkowski;
import nationalcipher.cipher.base.transposition.NihilistTransposition;
import nationalcipher.cipher.base.transposition.Phillips;
import nationalcipher.cipher.base.transposition.RailFence;
import nationalcipher.cipher.transposition.RouteTransposition;

public class RandomEncrypter {

	public static List<IRandEncrypter> ciphers = new ArrayList<IRandEncrypter>();
	
	public static IRandEncrypter getFromName(String name) {
		for(IRandEncrypter randEncrypt : ciphers)
			if(randEncrypt.getClass().getSimpleName().equals(name))
				return randEncrypt;
		
		return null;
	}
	
	static {
		ciphers.add(new ADFGX());
		ciphers.add(new Affine());
		ciphers.add(new AMSCO());	
		ciphers.add(new Bazeries());
		ciphers.add(new Beaufort());
		ciphers.add(new BeaufortAutokey());
		ciphers.add(new BeaufortProgressiveKey());
		ciphers.add(new BeaufortSlidefair());
		ciphers.add(new Bifid());
		ciphers.add(new Caesar());
		ciphers.add(new Cadenus());
		ciphers.add(new Columnar());
		ciphers.add(new ConjugatedBifid());
		ciphers.add(new Digrafid());
		ciphers.add(new Enigma());
		ciphers.add(new FourSquare());
		ciphers.add(new FractionatedMorse());
		ciphers.add(new Hill());
		ciphers.add(new Homophonic());
		ciphers.add(new Keyword());
		ciphers.add(new Morbit());
		ciphers.add(new Myszkowski());
		ciphers.add(new NihilistSubstitution());
		ciphers.add(new NihilistTransposition());
		ciphers.add(new PeriodicGromark());
		ciphers.add(new Phillips());
		ciphers.add(new Playfair());
		ciphers.add(new Pollux());
		ciphers.add(new Porta());
		ciphers.add(new PortaAutokey());
		ciphers.add(new PortaProgressiveKey());
		ciphers.add(new Portax());
		ciphers.add(new QuagmireI());
		ciphers.add(new QuagmireII());
		ciphers.add(new QuagmireIII());
		ciphers.add(new QuagmireIV());
		ciphers.add(new RailFence());
		ciphers.add(new RouteTransposition());
		ciphers.add(new RunningKey());
		ciphers.add(new SeriatedPlayfair());
		ciphers.add(new Solitaire());
		//TODO ciphers.add(new Swagman());
		ciphers.add(new Trifid());
		ciphers.add(new TwoSquare());
		ciphers.add(new TriSquare());
		ciphers.add(new Variant());
		ciphers.add(new VariantAutokey());
		ciphers.add(new VariantProgressiveKey());
		ciphers.add(new VariantSlidefair());
		ciphers.add(new Vigenere());
		ciphers.add(new VigenereAutokey());
		ciphers.add(new VigenereProgressiveKey());
		ciphers.add(new VigenereSlidefair());
	}
}
