package nationalcipher;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.language.ILanguage;
import javalibrary.math.MathHelper;
import javalibrary.swing.chart.ChartData;
import javalibrary.swing.chart.ChartList;
import javalibrary.swing.chart.JBarChart;
import nationalciphernew.cipher.stats.StatCalculator;

public class NicodemusPeriodPanel implements IStatisticsPanel {

	public JPanel panel;
	public JBarChart barChartIC;
	
	@Override
	public JPanel createPanel() {
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		
		this.barChartIC = new JBarChart();
		this.barChartIC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periods 2-40, difference from standard ioc"));
		this.barChartIC.setHasBarText(false);
		this.panel.add(this.barChartIC);
		return this.panel;
	}

	@Override
	public void update(String inputText, ILanguage language, Output output) {
		this.barChartIC.unselectAll();

		
		
		ChartList chartListIC = new ChartList();


		int bestPeriod = -1;
	    double bestIC = Double.POSITIVE_INFINITY;
	    
	    for(int period = 2; period <= 40; ++period) {
	    	double sqDiff = Math.pow(StatCalculator.calculateNicodemusIC(inputText, 5, period) - language.getNormalCoincidence(), 2) * 10000;
	    	
	    	if(sqDiff < bestIC)
	    		bestPeriod = period;
	    	chartListIC.add(new ChartData("Period: " + period, sqDiff));
	    	
	    	bestIC = Math.min(bestIC, sqDiff);
	    }
		
	    this.barChartIC.setSelected(bestPeriod - 2);
	    this.barChartIC.values = chartListIC;
	    this.barChartIC.updateUI();
	    
	    output.println("----------- Nicodemus Period ----------- ");
	    output.println(" IoC Calculation: " + bestPeriod);
	    output.println(" However could be a factor - " + MathHelper.getFactors(bestPeriod));
	    output.println("");
	}


	@Override
	public String getName() {
		return "Nicodemus Period";
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

