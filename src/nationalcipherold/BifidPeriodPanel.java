package nationalcipherold;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.language.ILanguage;
import javalibrary.math.Statistics;
import javalibrary.swing.chart.ChartData;
import javalibrary.swing.chart.ChartList;
import javalibrary.swing.chart.JBarChart;
import nationalcipher.cipher.stats.StatCalculator;

public class BifidPeriodPanel implements IStatisticsPanel {

	public JPanel panel;
	public JBarChart barChart;
	public JBarChart barChartIC;
	
	@Override
	public JPanel createPanel() {
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		
		this.barChart = new JBarChart();
		this.barChartIC = new JBarChart();
		this.barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Steps 1-40"));
		this.barChartIC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periods 0-40"));
		this.barChart.setHasBarText(false);
		this.barChartIC.setHasBarText(false);
		this.panel.add(this.barChart);
		this.panel.add(this.barChartIC);
		return this.panel;
	}

	@Override
	public void update(String inputText, ILanguage language, Output output) {
		this.barChart.unselectAll();
		this.barChartIC.unselectAll();
		ChartList chartList = new ChartList();
		
		Map<Integer, Double> values = new HashMap<Integer, Double>();
		double maxValue = Double.MIN_VALUE;
		int maxStep = 0;
		
		double secondValue = Double.MIN_VALUE;
		
		for(int step = 1; step <= 40; step++) {
			HashMap<String, Integer> counts = new HashMap<String, Integer>();
			for(int i = 0; i < inputText.length() - step; i++) {
				String s = inputText.charAt(i) + "" + inputText.charAt(i + step);
				counts.put(s, counts.containsKey(s) ? counts.get(s) + 1 : 1);
			}
			
			Statistics stats = new Statistics(counts.values());

		    double variance = stats.getVariance();
		 
			chartList.add(new ChartData("Step: " + step, variance));
			values.put(step, variance);
			
			if(variance > maxValue) {
				secondValue = maxValue;
				maxValue = variance;
				maxStep = step;
			}
			else if(variance > secondValue) {
				secondValue = variance;
			}
		}

		
		int periodGuess = -1;

		if(maxValue - maxValue / 4 > secondValue)
			periodGuess = maxStep * 2;
		else {
			double max = Double.MAX_VALUE;
			int bestStep = 0;
			
			for(int step = maxStep - 1; step <= maxStep + 1; step++) {
				if(step < 1 || step == maxStep || maxStep > 20)
					continue;
				
				double diff = Math.abs(values.get(maxStep) - values.get(step));
				if(diff < max) {
					max = diff;
					bestStep = step;
				}
			}
			this.barChart.setSelected(bestStep - 1);
			
			periodGuess = Math.min(bestStep, maxStep) * 2 + Math.abs(bestStep - maxStep);
		}
		
		this.barChart.setSelected(maxStep - 1);
		this.barChart.values = chartList;
		this.barChart.updateUI();
		
		
		
		ChartList chartListIC = new ChartList();

		int bestPeriod = -1;
		double bestIC = Double.MIN_VALUE;
	    for(int period = 0; period <= 40; period++) {
	    	if(period == 1) continue;
	    	
	        double score = StatCalculator.calculateBifidDiagraphicIC(inputText, period);
	        chartListIC.add(new ChartData("Period: " + period, score));
	        if(bestIC < score)
	        	bestPeriod = period;
	        
	        bestIC = Math.max(bestIC, score);
	    }
	    this.barChartIC.setSelected(bestPeriod > 0 ? bestPeriod - 1 : 0);
	    this.barChartIC.values = chartListIC;
	    this.barChartIC.updateUI();
	    
	    output.println("----------- Bifid Period ----------- ");
	    output.println(" Step Calculation: " + periodGuess);
	    output.println(" IoC Calculation: " + bestPeriod);
	    output.println("");
	}


	@Override
	public String getName() {
		return "Bifid Period";
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

