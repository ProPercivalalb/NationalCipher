package nationalcipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.ColumnarRow;
import javalibrary.cipher.permentate.PermentateArray;
import javalibrary.cipher.permentate.Permentations;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.math.ArrayHelper;
import javalibrary.string.StringAnalyzer;
import javalibrary.swing.chart.ChartList;
import javalibrary.swing.chart.JBarChart;
import javalibrary.util.MapHelper;
import nationalciphernew.cipher.stats.StatCalculator;

public class ADFGVXPanel implements IStatisticsPanel {

	public JPanel panel;
	public JBarChart barChartIC;
	
	@Override
	public JPanel createPanel() {
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		
		this.barChartIC = new JBarChart();
		this.barChartIC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "ADFGVX"));
		this.barChartIC.setHasBarText(false);
		this.panel.add(this.barChartIC);
		return this.panel;
	}
	
	

	@Override
	public void update(String inputText, ILanguage language, Output output) {
		this.barChartIC.unselectAll();
		
		ChartList chartListIC = new ChartList();
	

		Figure figure = new Figure(inputText, language);
		
		for(int length = 2; length <= 6; length++)
			Permentations.permutate(figure, ArrayHelper.range(0, length));
		
		String s = "";
		
		s += "Like english: " + figure.smallest + "\n";
		for(Integer[] i : figure.orders1) {
			s += Arrays.toString(i) + "\n";
		}
		
		s += "\nIOC: " + figure.closestIC + " off normal\n";
		
		for(Integer[] i : figure.orders2) {
			s += Arrays.toString(i) + "\n";
		}

	    //this.barChartIC.setSelected(bestPeriod - 2);
	    this.barChartIC.values = chartListIC;
	    this.barChartIC.updateUI();
	    
	    output.println("----------- ADFGVX ----------- ");
		output.print(s);
	  //  output.println(" IoC Calculation: " + bestPeriod);
	    output.println("");
	}
	
	public static class Figure implements PermentateArray {

		public String text;
		public ILanguage language;
		public double smallest = Double.MAX_VALUE;
		public List<Integer[]> orders1 = new ArrayList<Integer[]>();
		public double closestIC = Double.MAX_VALUE;
		public List<Integer[]> orders2 = new ArrayList<Integer[]>();
		
		
		public Figure(String text, ILanguage language) {
			this.text = text;
			this.language = language;
		}
		
		@Override
		public void onPermentate(int[] array) {
			System.out.println(""+ Arrays.toString(array));
			String s = ColumnarRow.decode(this.text, array);
			double n = calculate(s, this.language);
	    	double evenDiagraphicIC = StatCalculator.calculateEvenDiagrahpicIC(s);
	    	
	    	Integer[] arr = new Integer[array.length];
	    	for(int i = 0; i <array.length; i++)
	    		arr[i] = Integer.valueOf(array[i]);
	    	
	    	
	    	if(n <= smallest) {
	    		if(n != smallest)
	    			orders1.clear();
	    		orders1.add(arr);
	    		smallest = n;
	    	}
	    	double sqDiff = Math.pow(Languages.english.getNormalCoincidence() - evenDiagraphicIC, 2);
	    	if(sqDiff <= closestIC) {
	    		if(n != smallest)
	    			orders2.clear();
	    		orders2.add(arr);
	    		
	    		closestIC = sqDiff;
	    	}
		}
		
	}
	
	public static double calculate(String text, ILanguage language) {
		Map<String, Integer> letters = MapHelper.sortMapByValue(StringAnalyzer.getEmbeddedStrings(text, 2, 2, false), false);
		double total = 0.0D;
		
		List<Double> normalOrder = language.getFrequencyLargestFirst();
		
		int index = 0;
		for(String letter : letters.keySet()) {
			
			double count = letters.get(letter);
			double expectedCount = normalOrder.get(index) * (text.length() / 2) / 100;
			
			double sum = Math.abs(count - expectedCount);
			index += 1;
			total += sum;
			if(index >= normalOrder.size())
				break;
		}
		
		return total;
	}

	@Override
	public String getName() {
		return "ADFGVX";
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

