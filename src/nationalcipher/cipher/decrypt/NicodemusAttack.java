package nationalcipher.cipher.decrypt;

import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.dict.Dictionary;
import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.SettingsUtil;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.PermutateString;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.decrypt.methods.KeySearch;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public abstract class NicodemusAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public NicodemusAttack(String displayName) {
		super(displayName);
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}	
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		NicodemusTask task = new NicodemusTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			for(String word : Dictionary.words)
				task.onIteration(word);
		}
		else if(method == DecryptionMethod.BRUTE_FORCE) {
			String keyAlphabet = this.keyAlphabet();
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.pow(keyAlphabet.length(), length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateShortCustomKey(task, keyAlphabet, length, true);
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().setIndeterminate(true);
			task.run(periodRange[0], periodRange[1]);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class NicodemusTask extends KeySearch implements ShortCustomKey, PermutateString {
		
		public NicodemusTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(NicodemusAttack.this.decode(this.cipherText, key), this.getLanguage());
			
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
			return new Solution(NicodemusAttack.this.decode(this.cipherText, key), this.getLanguage()).setKeyString(key);
		}
		
		@Override
		public int alphaIncrease() {
			return NicodemusAttack.this.alphaIncrease();
		}
		
		//Key search finds the characters in the key, permutate to find the real solution
		@Override
		public void foundBestSolutionForLength(Solution currentBestSolution) {
			KeyIterator.permutateString(this, currentBestSolution.keyString);
		}
	

		@Override
		public void onPermutate(String key) {
			this.lastSolution = new Solution(NicodemusAttack.this.decode(this.cipherText, key), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(key);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
		}	
	}
	
	@Override
	public void writeTo(Map<String, Object> map) {
		map.put("period_min", this.rangeSpinner[0].getValue());
		map.put("period_max", this.rangeSpinner[1].getValue());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		this.rangeSpinner[0].setValue(SettingsUtil.getSetting("period_min", map, Integer.TYPE, 2));
		this.rangeSpinner[1].setValue(SettingsUtil.getSetting("period_max", map, Integer.TYPE, 15));
	}
	
	public abstract char[] decode(char[] cipherText, String key);
	
	public int alphaIncrease() { return 1; }
	
	public String keyAlphabet() { return "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; }
}
