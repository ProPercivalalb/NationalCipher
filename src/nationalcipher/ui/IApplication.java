package nationalcipher.ui;

import javalibrary.Output;
import javalibrary.language.ILanguage;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;

public interface IApplication {

	public Settings getSettings();
	
	public ILanguage getLanguage();
	
	public Output out();
	
	public KeyPanel getKeyPanel();
	
	public ProgressValue getProgress();
}
