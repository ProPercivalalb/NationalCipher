package nationalcipher.cipher.manage;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.cipher.ADFGX;
import nationalcipher.cipher.AMSCO;
import nationalcipher.cipher.Affine;
import nationalcipher.cipher.Bazeries;
import nationalcipher.cipher.Beaufort;
import nationalcipher.cipher.BeaufortAutokey;
import nationalcipher.cipher.BeaufortProgressiveKey;
import nationalcipher.cipher.BeaufortSlidefair;
import nationalcipher.cipher.Bifid;
import nationalcipher.cipher.Caesar;
import nationalcipher.cipher.ConjugatedBifid;
import nationalcipher.cipher.FourSquare;
import nationalcipher.cipher.FractionatedMorse;
import nationalcipher.cipher.Homophonic;
import nationalcipher.cipher.Keyword;
import nationalcipher.cipher.Morbit;
import nationalcipher.cipher.Myszkowski;
import nationalcipher.cipher.NihilistSubstitution;
import nationalcipher.cipher.PeriodicGromark;
import nationalcipher.cipher.Phillips;
import nationalcipher.cipher.Playfair;
import nationalcipher.cipher.Pollux;
import nationalcipher.cipher.Porta;
import nationalcipher.cipher.PortaAutokey;
import nationalcipher.cipher.PortaProgressiveKey;
import nationalcipher.cipher.Portax;
import nationalcipher.cipher.QuagmireI;
import nationalcipher.cipher.RailFence;
import nationalcipher.cipher.RunningKey;
import nationalcipher.cipher.SeriatedPlayfair;
import nationalcipher.cipher.Solitaire;
import nationalcipher.cipher.Swagman;
import nationalcipher.cipher.TriSquare;
import nationalcipher.cipher.Trifid;
import nationalcipher.cipher.TwoSquare;
import nationalcipher.cipher.Variant;
import nationalcipher.cipher.VariantAutokey;
import nationalcipher.cipher.VariantProgressiveKey;
import nationalcipher.cipher.VariantSlidefair;
import nationalcipher.cipher.Vigenere;
import nationalcipher.cipher.VigenereAutokey;
import nationalcipher.cipher.VigenereProgressiveKey;
import nationalcipher.cipher.VigenereSlidefair;
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
		ciphers.add(new Bazeries());
		ciphers.add(new ConjugatedBifid());
		ciphers.add(new FourSquare());
		ciphers.add(new FractionatedMorse());
		//ciphers.add(new Hill());
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
		ciphers.add(new Swagman());
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
