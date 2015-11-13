package nationalciphernew.cipher.manage;

import java.util.ArrayList;
import java.util.List;

import nationalciphernew.cipher.AffineDecrypt;
import nationalciphernew.cipher.BeaufortDecrypt;
import nationalciphernew.cipher.CaesarDecrypt;
import nationalciphernew.cipher.NihilistSubstitutionDecrypt;
import nationalciphernew.cipher.PlayfairDecrypt;
import nationalciphernew.cipher.RailFenceDecrypt;
import nationalciphernew.cipher.SingleTranspostion;
import nationalciphernew.cipher.SubstitutionDecrypt;
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
		ciphers.add(new NihilistSubstitutionDecrypt());
		ciphers.add(new VigenereDecrypt());
		ciphers.add(new BeaufortDecrypt());
		
		ciphers.add(new SingleTranspostion());
		ciphers.add(new DoubleTranspostionDecrypt());
		ciphers.add(new RailFenceDecrypt());
		
		
		ciphers.add(new PlayfairDecrypt());
	}
}
