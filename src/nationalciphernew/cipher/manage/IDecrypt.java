package nationalciphernew.cipher.manage;

import java.util.List;

import javax.swing.JDialog;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;

public interface IDecrypt {

	public String getName();
	
	public List<DecryptionMethod> getDecryptionMethods();

	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress);

	public void createSettingsUI(JDialog dialog);
}