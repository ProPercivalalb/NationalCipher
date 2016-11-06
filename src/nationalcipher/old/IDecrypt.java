package nationalcipher.old;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.ui.KeyPanel;

public interface IDecrypt {

	public String getName();
	
	public List<DecryptionMethod> getDecryptionMethods();

	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress);

	public void onTermination();
	
	public void createSettingsUI(JDialog dialog, JPanel panel);
}
