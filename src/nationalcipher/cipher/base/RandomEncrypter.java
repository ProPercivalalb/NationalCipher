package nationalcipher.cipher.base;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.cipher.ADFGX;
import nationalcipher.cipher.Bazeries;
import nationalcipher.cipher.BeaufortAutokey;
import nationalcipher.cipher.BeaufortProgressiveKey;
import nationalcipher.cipher.BeaufortSlidefair;
import nationalcipher.cipher.Enigma;
import nationalcipher.cipher.FractionatedMorse;
import nationalcipher.cipher.Hill;
import nationalcipher.cipher.Homophonic;
import nationalcipher.cipher.Morbit;
import nationalcipher.cipher.NihilistSubstitution;
import nationalcipher.cipher.PeriodicGromark;
import nationalcipher.cipher.Phillips;
import nationalcipher.cipher.Pollux;
import nationalcipher.cipher.PortaAutokey;
import nationalcipher.cipher.PortaProgressiveKey;
import nationalcipher.cipher.QuagmireI;
import nationalcipher.cipher.RunningKey;
import nationalcipher.cipher.SeriatedPlayfair;
import nationalcipher.cipher.Swagman;
import nationalcipher.cipher.VariantAutokey;
import nationalcipher.cipher.VariantProgressiveKey;
import nationalcipher.cipher.VariantSlidefair;
import nationalcipher.cipher.VigenereAutokey;
import nationalcipher.cipher.VigenereProgressiveKey;
import nationalcipher.cipher.VigenereSlidefair;
import nationalcipher.cipher.base.onetimepad.Solitaire;
import nationalcipher.cipher.base.polybiussquare.Bifid;
import nationalcipher.cipher.base.polybiussquare.ConjugatedBifid;
import nationalcipher.cipher.base.polybiussquare.FourSquare;
import nationalcipher.cipher.base.polybiussquare.Playfair;
import nationalcipher.cipher.base.polybiussquare.TriSquare;
import nationalcipher.cipher.base.polybiussquare.Trifid;
import nationalcipher.cipher.base.polybiussquare.TwoSquare;
import nationalcipher.cipher.base.polygraphic.Beaufort;
import nationalcipher.cipher.base.polygraphic.Porta;
import nationalcipher.cipher.base.polygraphic.Portax;
import nationalcipher.cipher.base.polygraphic.Variant;
import nationalcipher.cipher.base.polygraphic.Vigenere;
import nationalcipher.cipher.base.substitution.Affine;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.base.transposition.AMSCO;
import nationalcipher.cipher.base.transposition.Cadenus;
import nationalcipher.cipher.base.transposition.Myszkowski;
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
		ciphers.add(new Beaufort());
		ciphers.add(new BeaufortAutokey());
		ciphers.add(new BeaufortProgressiveKey());
		ciphers.add(new BeaufortSlidefair());
		ciphers.add(new Bifid());
		ciphers.add(new Caesar());
		ciphers.add(new Cadenus());
		ciphers.add(new Bazeries());
		ciphers.add(new ConjugatedBifid());
		ciphers.add(new Enigma());
		ciphers.add(new FourSquare());
		ciphers.add(new FractionatedMorse());
		ciphers.add(new Hill());
		ciphers.add(new Homophonic());
		ciphers.add(new Keyword());
		ciphers.add(new Morbit());
		ciphers.add(new Myszkowski());
		ciphers.add(new NihilistSubstitution());
		ciphers.add(new PeriodicGromark());
		ciphers.add(new Phillips());
		ciphers.add(new Playfair());
		ciphers.add(new Pollux());
		ciphers.add(new Porta());
		ciphers.add(new PortaAutokey());
		ciphers.add(new PortaProgressiveKey());
		ciphers.add(new Portax());
		ciphers.add(new QuagmireI());
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
