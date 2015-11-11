package nationalciphernew.cipher;

import java.util.ArrayList;
import java.util.List;

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
		ciphers.add(new PlayfairDecrypt());
	}
}
