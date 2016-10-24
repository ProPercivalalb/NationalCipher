package nationalcipher.cipher.decrypt.complete;

import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.math.MathUtil;
import javalibrary.string.StringTransformer;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.cipher.base.substitution.Beaufort;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.decrypt.complete.methods.KeySearch;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class BeaufortAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public BeaufortAttack() {
		super("Beaufort");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED, DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		BeaufortTask task = new BeaufortTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.pow(26, length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateShort26Key(task, length, true);
		}
		else if(method == DecryptionMethod.CALCULATED) {
			int keyLength = StatCalculator.calculateBestKappaIC(text, periodRange[0], periodRange[1], app.getLanguage());
			
			app.getProgress().addMaxValue(keyLength * 26);
			
			char[] invervedText = new char[task.cipherText.length];
			for(int i = 0; i < invervedText.length; i++)
				invervedText[i] = (char)('Z' - task.cipherText[i] + 'A');
			
			String keyword = "";
	        for(int i = 0; i < keyLength; ++i) {
	        	String temp = StringTransformer.getEveryNthChar(invervedText, i, keyLength);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), app.getLanguage(), app.getProgress());
	            keyword += (char)('Z' - shift);
	        }
			task.onIteration(keyword);
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().setIndeterminate(true);
			task.run(periodRange[0], periodRange[1]);
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
	
	public static class BeaufortTask extends KeySearch implements ShortCustomKey {

		public BeaufortTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Beaufort.decode(this.cipherText, key), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s", key);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(Beaufort.decode(this.cipherText, key), this.getLanguage()).setKeyString(key);
		}

		@Override
		public void solutionFound() {
			this.out().println("%s", this.bestSolution);
			this.getKeyPanel().updateSolution(this.bestSolution);
		}

		@Override
		public void onIteration() {
			this.getKeyPanel().updateIteration(this.iteration++);
		}
	}
	
	@Override
	public void write(HashMap<String, Object> map) {
		map.put("beaufort_period_range_min", this.rangeSpinner[0].getValue());
		map.put("beaufort_period_range_max", this.rangeSpinner[1].getValue());
	}

	@Override
	public void read(HashMap<String, Object> map) {
		if(map.containsKey("beaufort_period_range_max"))
			this.rangeSpinner[1].setValue(map.get("beaufort_period_range_max"));
		if(map.containsKey("beaufort_period_range_min"))
			this.rangeSpinner[0].setValue(map.get("beaufort_period_range_min"));
	}
}
