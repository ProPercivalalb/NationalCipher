package nationalcipher.cipher.manage;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.LoadElement;
import nationalcipher.Settings;
import nationalcipher.cipher.decrypt.ADFGXDecrypt;
import nationalcipher.cipher.decrypt.AMSCODecrypt;
import nationalcipher.cipher.decrypt.AffineDecrypt;
import nationalcipher.cipher.decrypt.BazeriesDecrypt;
import nationalcipher.cipher.decrypt.BeaufortAutokeyDecrypt;
import nationalcipher.cipher.decrypt.BeaufortDecrypt;
import nationalcipher.cipher.decrypt.BeaufortProgressiveKeyDecrypt;
import nationalcipher.cipher.decrypt.BeaufortSlidefairDecrypt;
import nationalcipher.cipher.decrypt.BifidDecrypt;
import nationalcipher.cipher.decrypt.CadenusDecrypt;
import nationalcipher.cipher.decrypt.CaesarDecrypt;
import nationalcipher.cipher.decrypt.ConjugatedBifidDecrypt;
import nationalcipher.cipher.decrypt.DigrafidDecrypt;
import nationalcipher.cipher.decrypt.DoubleTranspostionDecrypt;
import nationalcipher.cipher.decrypt.FourSquareDecrypt;
import nationalcipher.cipher.decrypt.FractionatedMorseDecrypt;
import nationalcipher.cipher.decrypt.GeneralPeriodDecrypt;
import nationalcipher.cipher.decrypt.HillDecrypt;
import nationalcipher.cipher.decrypt.HomophonicDecrypt;
import nationalcipher.cipher.decrypt.MorbitDecrypt;
import nationalcipher.cipher.decrypt.MyszkowskiDecrypt;
import nationalcipher.cipher.decrypt.NihilistSubstitutionDecrypt;
import nationalcipher.cipher.decrypt.NihilistTranspositionDecrypt;
import nationalcipher.cipher.decrypt.PeriodicGromarkDecrypt;
import nationalcipher.cipher.decrypt.PhillipsDecrypt;
import nationalcipher.cipher.decrypt.PlayfairDecrypt;
import nationalcipher.cipher.decrypt.PolluxDecrypt;
import nationalcipher.cipher.decrypt.PortaAutokeyDecrypt;
import nationalcipher.cipher.decrypt.PortaDecrypt;
import nationalcipher.cipher.decrypt.PortaProgressiveKeyDecrypt;
import nationalcipher.cipher.decrypt.PortaxDecrypt;
import nationalcipher.cipher.decrypt.QuagmireIDecrypt;
import nationalcipher.cipher.decrypt.RailFenceDecrypt;
import nationalcipher.cipher.decrypt.ReddefenceDecrypt;
import nationalcipher.cipher.decrypt.RunningKeyDecrypt;
import nationalcipher.cipher.decrypt.SeriatedPlayfairDecrypt;
import nationalcipher.cipher.decrypt.SingleTranspostion;
import nationalcipher.cipher.decrypt.SolitaireDecrypt;
import nationalcipher.cipher.decrypt.SubstitutionDecrypt;
import nationalcipher.cipher.decrypt.SwagmanDecrypt;
import nationalcipher.cipher.decrypt.TriSquareDecrypt;
import nationalcipher.cipher.decrypt.TrifidDecrypt;
import nationalcipher.cipher.decrypt.TwoSquareDecrypt;
import nationalcipher.cipher.decrypt.VariantAutokeyDecrypt;
import nationalcipher.cipher.decrypt.VariantDecrypt;
import nationalcipher.cipher.decrypt.VariantProgressiveKeyDecrypt;
import nationalcipher.cipher.decrypt.VariantSlidefairDecrypt;
import nationalcipher.cipher.decrypt.VigenereAutoKeyDecrypt;
import nationalcipher.cipher.decrypt.VigenereDecrypt;
import nationalcipher.cipher.decrypt.VigenereProgressiveKeyDecrypt;
import nationalcipher.cipher.decrypt.VigenereSlidefairDecrypt;
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
		registerCipher(new QuagmireIDecrypt(), settings);
		
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
