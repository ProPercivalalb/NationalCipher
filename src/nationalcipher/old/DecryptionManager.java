package nationalcipher.old;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.LoadElement;
import nationalcipher.Settings;
import nationalcipher.cipher.decrypt.AffineDecrypt;
import nationalcipher.cipher.decrypt.BazeriesDecrypt;
import nationalcipher.cipher.decrypt.CadenusDecrypt;
import nationalcipher.cipher.decrypt.CaesarDecrypt;
import nationalcipher.cipher.decrypt.HillDecrypt;
import nationalcipher.cipher.decrypt.PortaxDecrypt;
import nationalcipher.cipher.decrypt.RailFenceDecrypt;
import nationalcipher.cipher.decrypt.ReddefenceDecrypt;
import nationalcipher.cipher.transposition.RouteDecrypt;

/**
 * @author Alex Barter (10AS)
 */
public class DecryptionManager {

	public static List<IDecrypt> ciphers = new ArrayList<IDecrypt>();
	
	public static String[] getNames() {
		String[] names = new String[ciphers.size()];
		for(int i = 0; i < ciphers.size(); ++i)
			names[i] = ciphers.get(i).getName();
		return names;
	}
	
	public static IDecrypt[] getObjects() {
		IDecrypt[] names = new IDecrypt[ciphers.size()];
		for(int i = 0; i < ciphers.size(); ++i)
			names[i] = ciphers.get(i);
		return names;
	}
	
	public static void registerCipher(Object decrypt, Settings settings) {
		if(decrypt instanceof IDecrypt)
			ciphers.add((IDecrypt)decrypt);
		if(decrypt instanceof LoadElement)
			settings.addLoadElement((LoadElement)decrypt);
	}
	
	public static void loadCiphers(Settings settings) {
		registerCipher(new AffineDecrypt(), settings);
		registerCipher(new CaesarDecrypt(), settings);
		registerCipher(new SubstitutionDecrypt(), settings);
		registerCipher(new GeneralPeriodDecrypt(), settings);
		registerCipher(new VigenereDecrypt(), settings);
		registerCipher(new VigenereAutoKeyDecrypt(), settings);
		registerCipher(new VigenereProgressiveKeyDecrypt(), settings);
		registerCipher(new VigenereSlidefairDecrypt(), settings);
		registerCipher(new RunningKeyDecrypt(), settings);
		registerCipher(new BeaufortDecrypt(), settings);
		registerCipher(new BeaufortAutokeyDecrypt(), settings);
		registerCipher(new BeaufortProgressiveKeyDecrypt(), settings);
		registerCipher(new BeaufortSlidefairDecrypt(), settings);
		registerCipher(new VariantDecrypt(), settings);
		registerCipher(new VariantAutokeyDecrypt(), settings);
		registerCipher(new VariantProgressiveKeyDecrypt(), settings);
		registerCipher(new VariantSlidefairDecrypt(), settings);
		registerCipher(new PortaDecrypt(), settings);
		registerCipher(new PortaAutokeyDecrypt(), settings);
		registerCipher(new PortaProgressiveKeyDecrypt(), settings);
		registerCipher(new PortaxDecrypt(), settings);
		registerCipher(new TwoSquareDecrypt(), settings);
		registerCipher(new TriSquareDecrypt(), settings);
		registerCipher(new FourSquareDecrypt(), settings);
		registerCipher(new BazeriesDecrypt(), settings);
		registerCipher(new PeriodicGromarkDecrypt(), settings);

		registerCipher(new SingleTranspostion(), settings);
		registerCipher(new DoubleTranspostionDecrypt(), settings);
		registerCipher(new MyszkowskiDecrypt(), settings);
		registerCipher(new RouteDecrypt(), settings);
		registerCipher(new RailFenceDecrypt(), settings);
		registerCipher(new ReddefenceDecrypt(), settings);
		registerCipher(new AMSCODecrypt(), settings);
		registerCipher(new CadenusDecrypt(), settings);
		registerCipher(new PhillipsDecrypt(), settings);
		registerCipher(new SwagmanDecrypt(), settings);
		
		registerCipher(new BifidDecrypt(), settings);
		registerCipher(new ConjugatedBifidDecrypt(), settings);
		registerCipher(new TrifidDecrypt(), settings);
		registerCipher(new DigrafidDecrypt(), settings);
		registerCipher(new PlayfairDecrypt(), settings);
		registerCipher(new SeriatedPlayfairDecrypt(), settings);
		registerCipher(new HillDecrypt(), settings);
		registerCipher(new ADFGXDecrypt(), settings);
		registerCipher(new QuagmireIandIIDecrypt(), settings);
		registerCipher(new QuagmireIIIDecrypt(), settings);
		
		registerCipher(new NihilistSubstitutionDecrypt(), settings);
		registerCipher(new NihilistTranspositionDecrypt(), settings);
		registerCipher(new HomophonicDecrypt(), settings);
		
		registerCipher(new FractionatedMorseDecrypt(), settings);
		registerCipher(new MorbitDecrypt(), settings);
		registerCipher(new PolluxDecrypt(), settings);
		registerCipher(new SolitaireDecrypt(), settings);
		//registerCipher(new CheckerboardDecrypt(), settings);
	}
}
