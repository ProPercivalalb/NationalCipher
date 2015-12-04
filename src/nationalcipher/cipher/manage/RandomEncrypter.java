package nationalcipher.cipher.manage;

import java.util.ArrayList;
import java.util.List;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.*;
import nationalcipher.cipher.transposition.RouteTransposition;

public class RandomEncrypter {

	public static List<IRandEncrypter> ciphers = new ArrayList<IRandEncrypter>();
	
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
