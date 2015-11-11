package nationalciphernew.cipher;

import java.util.List;

import javalibrary.Output;
import javalibrary.language.ILanguage;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;

public interface IDecrypt {

	public String getName();
	
	public List<DecryptionMethod> getDecryptionMethods();

	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, ILanguage language, Output output, KeyPanel keyPanel, ProgressValue progress);
}
