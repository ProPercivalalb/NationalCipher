package nationalcipher.ui;

import javax.swing.JDialog;

import javalibrary.Output;
import javalibrary.language.ILanguage;
import javalibrary.swing.ProgressValue;
import javalibrary.swing.chart.JBarChart;
import nationalcipher.Settings;

public interface IApplication {

    public Settings getSettings();

    public ILanguage getLanguage();

    public Output out();

    public KeyPanel getKeyPanel();

    public ProgressValue getProgress();

    public JDialog openGraph(JBarChart barChart);
}
