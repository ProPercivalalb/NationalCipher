package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.dict.Dictionary;
import javalibrary.language.Languages;
import javalibrary.lib.BooleanLib;
import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.transposition.ColumnarTransposition;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ArrayPermutations;
import nationalcipher.cipher.decrypt.methods.KeyIterator.IntegerOrderedKey;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class Challenge7Attack extends CipherAttack {

	private JSpinner[] rangeSpinner;
	private JComboBox<Boolean> readOffDefaultChose;
	private JSpinner saSpinner;
	
	public Challenge7Attack() {
		super("Challenge 7");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(5, 5, 2, 100, 1);
		this.readOffDefaultChose = new JComboBox<Boolean>(BooleanLib.OBJECT_REVERSED);
		this.saSpinner = JSpinnerUtil.createSpinner(14, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
		panel.add(new SubOptionPanel("Read down columns (T) or across rows (F)? ", this.readOffDefaultChose));
		panel.add(new SubOptionPanel("SA Period:", this.saSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		ColumnarTranspositionTask task = new ColumnarTranspositionTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		task.readOffDefault = SettingParse.getBooleanValue(this.readOffDefaultChose);
		task.period1 = SettingParse.getInteger(this.saSpinner);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.factorialBig(length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateIntegerOrderedKey(task, length);
		}
		app.out().println(task.getBestSolution());
	}
	
	public class ColumnarTranspositionTask extends InternalDecryption implements IntegerOrderedKey {

		public int period1;
		public boolean readOffDefault;
		public int[] bestKey1, bestMaximaKey1, lastKey1;
		
		public ColumnarTranspositionTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public void onIteration(int[] order) {
			this.lastSolution = new Solution(ColumnarTransposition.decode(this.cipherText, this.plainText, order, this.readOffDefault), this.getLanguage());
			
			//GeneralPeriodAttack generalAttack = new GeneralPeriodAttack();
			int bestPeriod = -1;
		    double bestKappa = Double.MAX_VALUE;
	    	String nText = new String(this.lastSolution.getText());		    
		    for(int period = 2; period <= 40; ++period) {

		    	double sqDiff = Math.pow(StatCalculator.calculateKappaIC(nText, period) - Languages.ENGLISH.getNormalCoincidence(), 2);
		    	
		    	if(sqDiff < bestKappa)
		    		bestPeriod = period;

		    	
		    	bestKappa = Math.min(bestKappa, sqDiff);
		    }
		    if(bestPeriod % 7 == 0) {
		    	this.out().println(bestPeriod + " " + Arrays.toString(order) + " "+ nText);
		    	
		    }
		    
		}

	}

	@Override
	public void writeTo(Map<String, Object> map) {
		map.put("period_range_min", this.rangeSpinner[0].getValue());
		map.put("period_range_max", this.rangeSpinner[1].getValue());
		map.put("read_off_default", this.readOffDefaultChose.getSelectedItem());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		if(map.containsKey("period_range_min"))
			this.rangeSpinner[0].setValue(map.get("period_range_min"));
		if(map.containsKey("period_range_max"))
			this.rangeSpinner[1].setValue(map.get("period_range_max"));
		if(map.containsKey("ouble_letter_first"))
			this.readOffDefaultChose.setSelectedItem(map.get("read_off_default"));
	}
}
