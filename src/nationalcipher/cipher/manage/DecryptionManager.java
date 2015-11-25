package nationalcipher.cipher.manage;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.cipher.decrypt.AMSCODecrypt;
import nationalcipher.cipher.decrypt.AffineDecrypt;
import nationalcipher.cipher.decrypt.BazeriesDecrypt;
import nationalcipher.cipher.decrypt.BeaufortAutokeyDecrypt;
import nationalcipher.cipher.decrypt.BeaufortDecrypt;
import nationalcipher.cipher.decrypt.BifidDecrypt;
import nationalcipher.cipher.decrypt.CadenusDecrypt;
import nationalcipher.cipher.decrypt.CaesarDecrypt;
import nationalcipher.cipher.decrypt.ConjugatedBifidDecrypt;
import nationalcipher.cipher.decrypt.DigrafidDecrypt;
import nationalcipher.cipher.decrypt.DoubleTranspostionDecrypt;
import nationalcipher.cipher.decrypt.FourSquareDecrypt;
import nationalcipher.cipher.decrypt.FractionatedMorseDecrypt;
import nationalcipher.cipher.decrypt.HillDecrypt;
import nationalcipher.cipher.decrypt.HomophonicDecrypt;
import nationalcipher.cipher.decrypt.MorbitDecrypt;
import nationalcipher.cipher.decrypt.MyszkowskiDecrypt;
import nationalcipher.cipher.decrypt.NihilistSubstitutionDecrypt;
import nationalcipher.cipher.decrypt.NihilistTranspositionDecrypt;
import nationalcipher.cipher.decrypt.PhillipsDecrypt;
import nationalcipher.cipher.decrypt.PlayfairDecrypt;
import nationalcipher.cipher.decrypt.PolluxDecrypt;
import nationalcipher.cipher.decrypt.PortaAutokeyDecrypt;
import nationalcipher.cipher.decrypt.PortaDecrypt;
import nationalcipher.cipher.decrypt.RailFenceDecrypt;
import nationalcipher.cipher.decrypt.ReddefenceDecrypt;
import nationalcipher.cipher.decrypt.RouteDecrypt;
import nationalcipher.cipher.decrypt.SeriatedPlayfairDecrypt;
import nationalcipher.cipher.decrypt.SingleTranspostion;
import nationalcipher.cipher.decrypt.SubstitutionDecrypt;
import nationalcipher.cipher.decrypt.TriSquareDecrypt;
import nationalcipher.cipher.decrypt.TrifidDecrypt;
import nationalcipher.cipher.decrypt.TwoSquareDecrypt;
import nationalcipher.cipher.decrypt.VariantAutokeyDecrypt;
import nationalcipher.cipher.decrypt.VariantDecrypt;
import nationalcipher.cipher.decrypt.VigenereAutoKeyDecrypt;
import nationalcipher.cipher.decrypt.VigenereDecrypt;

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
	
	static {
		ciphers.add(new AffineDecrypt());
		ciphers.add(new CaesarDecrypt());
		ciphers.add(new SubstitutionDecrypt());
		ciphers.add(new VigenereDecrypt());
		ciphers.add(new VigenereAutoKeyDecrypt());
		ciphers.add(new BeaufortDecrypt());
		ciphers.add(new BeaufortAutokeyDecrypt());
		ciphers.add(new VariantDecrypt());
		ciphers.add(new VariantAutokeyDecrypt());
		ciphers.add(new PortaDecrypt());
		ciphers.add(new PortaAutokeyDecrypt());
		ciphers.add(new TwoSquareDecrypt());
		ciphers.add(new TriSquareDecrypt());
		ciphers.add(new FourSquareDecrypt());
		ciphers.add(new BazeriesDecrypt());
		
		ciphers.add(new SingleTranspostion());
		ciphers.add(new DoubleTranspostionDecrypt());
		ciphers.add(new MyszkowskiDecrypt());
		ciphers.add(new RouteDecrypt());
		ciphers.add(new RailFenceDecrypt());
		ciphers.add(new ReddefenceDecrypt());
		ciphers.add(new AMSCODecrypt());
		ciphers.add(new CadenusDecrypt());
		ciphers.add(new PhillipsDecrypt());
		
		ciphers.add(new BifidDecrypt());
		ciphers.add(new ConjugatedBifidDecrypt());
		ciphers.add(new TrifidDecrypt());
		ciphers.add(new DigrafidDecrypt());
		ciphers.add(new PlayfairDecrypt());
		ciphers.add(new SeriatedPlayfairDecrypt());
		ciphers.add(new HillDecrypt());
		
		ciphers.add(new NihilistSubstitutionDecrypt());
		ciphers.add(new NihilistTranspositionDecrypt());
		ciphers.add(new HomophonicDecrypt());
		
		ciphers.add(new FractionatedMorseDecrypt());
		ciphers.add(new MorbitDecrypt());
		ciphers.add(new PolluxDecrypt());
		//ciphers.add(new CheckerboardDecrypt());
	}
}
