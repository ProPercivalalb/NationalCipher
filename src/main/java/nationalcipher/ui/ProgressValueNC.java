package nationalcipher.ui;

import javax.swing.JProgressBar;

import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;

public class ProgressValueNC extends ProgressValue {

    public Settings settings;

    public ProgressValueNC(int scale, JProgressBar progressBar, Settings settings) {
        super(scale, progressBar);
        this.settings = settings;
    }

    @Override
    public void recalculatePercentage() {
        if (this.settings.updateProgress())
            super.recalculatePercentage();
    }
}
