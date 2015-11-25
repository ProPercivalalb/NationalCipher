package nationalcipherold;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.swing.chart.ChartData;
import javalibrary.swing.chart.ChartList;
import javalibrary.swing.chart.JBarChart;
import nationalcipher.cipher.Caesar;

public class CaesarPanel implements IStatisticsPanel {

	public JPanel panel;
	public JBarChart barChartIC;
	
	@Override
	public JPanel createPanel() {
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		
		this.barChartIC = new JBarChart();
		this.barChartIC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Shift 0-25"));
		this.barChartIC.setHasBarText(false);
		this.panel.add(this.barChartIC);
		return this.panel;
	}

	@Override
	public void update(String inputText, ILanguage language, Output output) {
		this.barChartIC.unselectAll();

		
		
		ChartList chartListIC = new ChartList();
		
		String plainText = "";
		double bestScore = Double.NEGATIVE_INFINITY;
		int best = 0;
		
		for(int i = 0; i < 26; ++i) {
			String lastText = new String(Caesar.decode(inputText.toCharArray(), i));
			double currentScore = TextFitness.scoreFitnessQuadgrams(lastText, language);
			if(currentScore > bestScore) {
				bestScore = currentScore;
				plainText = lastText;
				best = i;
			}
			chartListIC.add(new ChartData("Shift: " + i, -currentScore));
		}
		
	    this.barChartIC.setSelected(best);
	    this.barChartIC.values = chartListIC;
	    this.barChartIC.updateUI();
	    
	    output.println("----------- Caesar ----------- ");
	    output.println(plainText);
	    output.println("");
	}


	@Override
	public String getName() {
		return "Caesar";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public Icon getIcon() {
		return null;
	}
}

