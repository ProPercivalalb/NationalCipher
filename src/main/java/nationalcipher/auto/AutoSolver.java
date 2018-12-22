package nationalcipher.auto;

import java.util.Collections;
import java.util.List;

import javalibrary.Output;
import nationalcipher.cipher.decrypt.AttackRegistry;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.stats.IdentifyOutput;
import nationalcipher.cipher.stats.StatisticHandler;

public class AutoSolver {

	public static String solve(String cipherText, Output output) {
		
		List<IdentifyOutput> ciphers = StatisticHandler.orderCipherProbibitly(cipherText);
		Collections.sort(ciphers);
		
		for(int i = 0; i < Math.min(ciphers.size(), 12); i++) {
			IdentifyOutput cipher = ciphers.get(i);
			CipherAttack attack = AttackRegistry.getFromId(cipher.id);
			
			
			output.println(cipher.id);
		}
		
		
		
		
		
		
		
		
		
		
		return "";
	}
}
