package nationalcipherold;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.language.ILanguage;

public interface IStatisticsPanel {

	public static List<IStatisticsPanel> panels = new ArrayList<IStatisticsPanel>(Arrays.asList(new StatisticsPanel(), new CaesarPanel(), new ADFGVXPanel(), new BifidPeriodPanel(), new NicodemusPeriodPanel()));
	
	public String getName();
	
	public String getDescription();
	
	public Icon getIcon();
	
	public JPanel createPanel();
	
	public void update(String inputText, ILanguage language, Output output);
}
