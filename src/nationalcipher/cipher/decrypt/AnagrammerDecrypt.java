package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;

public class AnagrammerDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Anagrammer";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		if(method == DecryptionMethod.BRUTE_FORCE) {
		
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}

	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
	
	}
	
	@Override
	public void onTermination() {
		
	}
}
