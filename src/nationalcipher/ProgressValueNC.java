package nationalcipher;

import javax.swing.JProgressBar;

import javalibrary.swing.ProgressValue;

public class ProgressValueNC extends ProgressValue {

	public Settings settings;
	
	public ProgressValueNC(int scale, JProgressBar progressBar, Settings settings) {
		super(scale, progressBar);
		this.settings = settings;
	}
	
	@Override
	public void updateProgress() {
		if(this.settings.updateProgress())
			super.updateProgress();
	}
}
