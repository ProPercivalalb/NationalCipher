package nationalciphernew.cipher.manage;

import java.util.ArrayList;
import java.util.List;

import nationalciphernew.cipher.AMSCODecrypt;
import nationalciphernew.cipher.AffineDecrypt;
import nationalciphernew.cipher.BazeriesDecrypt;
import nationalciphernew.cipher.BeaufortAutokeyDecrypt;
import nationalciphernew.cipher.BeaufortDecrypt;
import nationalciphernew.cipher.BifidDecrypt;
import nationalciphernew.cipher.CadenusDecrypt;
import nationalciphernew.cipher.CaesarDecrypt;
import nationalciphernew.cipher.CheckerboardDecrypt;
import nationalciphernew.cipher.ConjugatedBifidDecrypt;
import nationalciphernew.cipher.DoubleTranspostionDecrypt;
import nationalciphernew.cipher.FourSquareDecrypt;
import nationalciphernew.cipher.HillDecrypt;
import nationalciphernew.cipher.HomophonicDecrypt;
import nationalciphernew.cipher.MyszkowskiDecrypt;
import nationalciphernew.cipher.NihilistSubstitutionDecrypt;
import nationalciphernew.cipher.NihilistTranspositionDecrypt;
import nationalciphernew.cipher.PlayfairDecrypt;
import nationalciphernew.cipher.PortaDecrypt;
import nationalciphernew.cipher.RailFenceDecrypt;
import nationalciphernew.cipher.ReddefenceDecrypt;
import nationalciphernew.cipher.RouteDecrypt;
import nationalciphernew.cipher.SingleTranspostion;
import nationalciphernew.cipher.SubstitutionDecrypt;
import nationalciphernew.cipher.TriSquareDecrypt;
import nationalciphernew.cipher.TwoSquareDecrypt;
import nationalciphernew.cipher.VariantDecrypt;
import nationalciphernew.cipher.VigenereAutoKeyDecrypt;
import nationalciphernew.cipher.VigenereDecrypt;

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
		ciphers.add(new PortaDecrypt());
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
		
		ciphers.add(new BifidDecrypt());
		ciphers.add(new ConjugatedBifidDecrypt());
		ciphers.add(new PlayfairDecrypt());
		ciphers.add(new HillDecrypt());
		
		ciphers.add(new NihilistSubstitutionDecrypt());
		ciphers.add(new NihilistTranspositionDecrypt());
		ciphers.add(new HomophonicDecrypt());
		//ciphers.add(new CheckerboardDecrypt());
	}
}
