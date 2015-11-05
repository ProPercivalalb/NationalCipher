package nationalcipher;

import java.awt.Dimension;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javalibrary.Output;
import javalibrary.cipher.stats.StatCalculator;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.math.MathHelper;

public class StatisticsPanel implements IStatisticsPanel {

	public JPanel panel;
	public JTextArea output;
	@Override
	public JPanel createPanel() {
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		
		this.output = new JTextArea();
		JScrollPane outputScrollPanel = new JScrollPane(this.output);
		outputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		outputScrollPanel.setPreferredSize(new Dimension(1000, 200));
		this.output.setEditable(false);
		this.output.setLineWrap(true);
		String text = "Length: ?";
		text += "\nSuggested Fitness: ?";

		text += "\n IC: ?";
		text += "\n MIC: ?";
	    text += "\n MKA: ?";
	    text += "\n DIC: ?";
	    text += "\n EDI: ?";
	    text += "\n LR: ?";
	    text += "\n ROD: ?";
	    text += "\n LDI: ?";
	    
	    text += "\n SDD: ?";
	    
	    text += "\n A_LDI: ?";
	    text += "\n B_LDI: ?";
	    text += "\n P_LDI: ?";
	    text += "\n S_LDI: ?";
	    text += "\n V_LDI: ?";
	    
	    text += "\n NOMOR: ?";
	    text += "\n RDI: ?";
	    text += "\n PTX: ?";
	    text += "\n NIC: ?";
	    text += "\n PHIC: ?";
	    text += "\n BDI: ?";
	    text += "\n CDD: ?";
	    text += "\n SSTD: ?";
	    text += "\n MPIC: ?";
	    text += "\n SERP: ?";
	    
	    
	    text += "\n DIV_2: ?";
	    text += "\n DIV_3: ?";
	    text += "\n DIV_5: ?";
	    text += "\n DIV_25: ?";
	    text += "\n DIV_4_15: ?";
	    text += "\n DIV_4_30: ?";
	    text += "\n DIV_N: [?...]";
	    text += "\n PSQ: ?";
	    text += "\n HAS_LETTERS: ?";
	    text += "\n HAS_DIGITS: ?";
	    text += "\n HAS_J: ?";
	    text += "\n HAS_#: ?";
	    text += "\n HAS_0: ?";
	    text += "\n DBL: ?";
	    
		this.output.setText(text);
		this.panel.add(outputScrollPanel);
		return this.panel;
	}

	@Override
	public void update(String inputText, ILanguage language, Output output) {

		String cipherText = inputText;
		int length = cipherText.length();
		
		String text = "Length: " + length;
		text += "\nEstimated Fitness for length: " + TextFitness.getEstimatedFitness(cipherText, language);

		text += "\n IC: " + StatCalculator.calculateIC(cipherText);
		text += "\n MIC: " + StatCalculator.calculateMaxIC(cipherText, 1, 15);
	    text += "\n MKA: " + StatCalculator.calculateMaxKappaIC(cipherText, 1, 15);
	    text += "\n DIC: " + StatCalculator.calculateDiagrahpicIC(cipherText);
	    text += "\n EDI: " + StatCalculator.calculateEvenDiagrahpicIC(cipherText) * 10000;
	    text += "\n LR: " + StatCalculator.calculateLR(cipherText);
	    text += "\n ROD: " + StatCalculator.calculateROD(cipherText);
	    text += "\n LDI: " + StatCalculator.calculateLDI(cipherText);
	   // text += "\n FITNESSLOG: " + TextFitness.scoreFitnessDiagrams(cipherText, Main.instance.language) * -100 / (cipherText.length() - 1);
	    
	    text += "\n SDD: " + StatCalculator.calculateSDD(cipherText);

	    text += "\n A_LDI: " + StatCalculator.calculateALDI(cipherText);
	    text += "\n B_LDI: " + StatCalculator.calculateBLDI(cipherText);
	    text += "\n P_LDI: " + StatCalculator.calculatePLDI(cipherText);
	    text += "\n S_LDI: " + StatCalculator.calculateSLDI(cipherText);
	    text += "\n V_LDI: " + StatCalculator.calculateVLDI(cipherText);
	    
	    text += "\n NOMOR: " + StatCalculator.calculateNormalOrder(cipherText, language);
	    text += "\n RDI: " + StatCalculator.calculateRDI(cipherText);
	    text += "\n PTX: " + StatCalculator.calculatePTX(cipherText);
	    text += "\n NIC: " +  StatCalculator.calculateMaxNicodemusIC(cipherText, 3, 15);
	    text += "\n PHIC: " + StatCalculator.calculatePHIC(cipherText);
	    text += "\n BDI: " +  StatCalculator.calculateBestBifidDiagraphicIC(cipherText, 3, 15);
	    text += "\n CDD: " +  StatCalculator.calculateCDD(cipherText);
	    text += "\n SSTD: " +  StatCalculator.calculateSSTD(cipherText);
	    text += "\n MPIC: " + StatCalculator.calculateMPIC(cipherText);
	    text += "\n SERP: " + StatCalculator.calculateSeriatedPlayfair(cipherText);
	    
	    
	    text += "\n DIV_2: " + StatCalculator.isLengthDivisible2(cipherText);
	    text += "\n DIV_3: " + StatCalculator.isLengthDivisible3(cipherText);
	    text += "\n DIV_5: " + StatCalculator.isLengthDivisible5(cipherText);
	    text += "\n DIV_25: " + StatCalculator.isLengthDivisible25(cipherText);
	    text += "\n DIV_4_15: " + StatCalculator.isLengthDivisible4_15(cipherText);
	    text += "\n DIV_4_30: " + StatCalculator.isLengthDivisible4_30(cipherText);
	    List<Integer> factors =  MathHelper.getFactors(length);
	    Collections.sort(factors);
	    text += "\n DIV_N: " + factors;
	    text += "\n PSQ: " + StatCalculator.isLengthPerfectSquare(cipherText);
	    text += "\n HAS_LETTERS: " + StatCalculator.containsLetter(cipherText);
	    text += "\n HAS_DIGITS: " + StatCalculator.containsDigit(cipherText);
	    text += "\n HAS_J: " + StatCalculator.containsJ(cipherText);
	    text += "\n HAS_#: " + StatCalculator.containsHash(cipherText);
	    text += "\n HAS_0: " + StatCalculator.calculateHAS0(cipherText);
	    text += "\n DBL: " + StatCalculator.calculateDBL(cipherText);
	    
		
		this.output.setText(text);
	}


	@Override
	public String getName() {
		return "Statistics";
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

