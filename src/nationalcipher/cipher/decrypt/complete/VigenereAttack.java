package nationalcipher.cipher.decrypt.complete;

import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.math.MathUtil;
import javalibrary.string.StringTransformer;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Vigenere;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.decrypt.methods.KeySearch;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class VigenereAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public VigenereAttack() {
		super("Vigenere");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED, DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		VigenereTask task = new VigenereTask(text, app);
		
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
			
			String keyword = "";
	        for(int i = 0; i < keyLength; ++i) {
	        	String temp = StringTransformer.getEveryNthChar(text, i, keyLength);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), app.getLanguage(), app.getProgress());
	            keyword += (char)('A' + shift);
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
	
	public class VigenereTask extends KeySearch implements ShortCustomKey {

		public VigenereTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Vigenere.decode(this.cipherText, key), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(key);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(Vigenere.decode(this.cipherText, key), this.getLanguage()).setKeyString(key);
		}
	}
	
	@Override
	public void writeTo(Map<String, Object> map) {
		map.put("vigenere_period_range_min", this.rangeSpinner[0].getValue());
		map.put("vigenere_period_range_max", this.rangeSpinner[1].getValue());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		if(map.containsKey("vigenere_period_range_max"))
			this.rangeSpinner[1].setValue(map.get("vigenere_period_range_max"));
		if(map.containsKey("vigenere_period_range_min"))
			this.rangeSpinner[0].setValue(map.get("vigenere_period_range_min"));
	}
}
