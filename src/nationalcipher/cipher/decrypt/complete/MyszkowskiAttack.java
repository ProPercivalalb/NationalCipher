package nationalcipher.cipher.decrypt.complete;

import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.dict.Dictionary;
import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.SettingsUtil;
import nationalcipher.cipher.base.transposition.Myszkowski;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class MyszkowskiAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public MyszkowskiAttack() {
		super("Myszkowski");
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 10, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}	
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		SlidefairTask task = new SlidefairTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			for(String word : Dictionary.words)
				task.onIteration(word);
		}
		else if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.pow(Math.min(length, 26), length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateShortCustomKey(task, "ABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(0, Math.min(length, 26)), length, true);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class SlidefairTask extends InternalDecryption implements ShortCustomKey {
		
		public SlidefairTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Myszkowski.decode(this.cipherText, key), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) { //Not equals to as there are alot of equivalent keys
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(key);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
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
}
