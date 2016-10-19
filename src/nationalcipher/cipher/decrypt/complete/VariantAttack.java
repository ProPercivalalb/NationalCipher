package nationalcipher.cipher.decrypt.complete;

import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.string.StringTransformer;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.cipher.base.polygraphic.Variant;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class VariantAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public VariantAttack() {
		super("Variant");
		this.setAttackMethods(DecryptionMethod.CALCULATED);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		VariantTask task = new VariantTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		if(method == DecryptionMethod.CALCULATED) {
			int keyLength = StatCalculator.calculateBestKappaIC(text, periodRange[0], periodRange[1], app.getLanguage());
			
			app.getProgress().addMaxValue(keyLength * 26);
			
			String keyword = "";
	        for(int i = 0; i < keyLength; ++i) {
	        	String temp = StringTransformer.getEveryNthChar(text, i, keyLength);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), app.getLanguage(), app.getProgress());
	            keyword += (char)('A' - (shift - 26) % 26);
	        }
			task.onIteration(keyword);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public int findBestCaesarShift(char[] text, ILanguage language, ProgressValue progressBar) {
		int best = 0;
	    double smallestSum = Double.MAX_VALUE;
	    for(int shift = 0; shift < 26; ++shift) {
	    	char[] encodedText = Caesar.decode(text, shift);
	        double currentSum = ChiSquared.calculate(encodedText, language);
	    
	        if(currentSum < smallestSum) {
	        	best = shift;
	            smallestSum = currentSum;
	        }
	            
	        progressBar.increase();
	    }
	    return best;
	}
	
	public static class VariantTask extends InternalDecryption {

		public VariantTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		public void onIteration(String key) {
			this.lastSolution = new Solution(Variant.decode(this.cipherText, key), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s", key);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
	
	@Override
	public void write(HashMap<String, Object> map) {
		map.put("variant_period_range_min", this.rangeSpinner[0].getValue());
		map.put("variant_period_range_max", this.rangeSpinner[1].getValue());
	}

	@Override
	public void read(HashMap<String, Object> map) {
		if(map.containsKey("variant_period_range_min"))
			this.rangeSpinner[0].setValue(map.get("variant_period_range_min"));
		if(map.containsKey("variant_period_range_max"))
			this.rangeSpinner[1].setValue(map.get("variant_period_range_max"));
	}
}
