package nationalcipher.cipher.decrypt.complete;

import java.util.HashMap;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Portax;
import nationalcipher.cipher.base.substitution.Vigenere;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.IntegerKey;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.decrypt.complete.methods.KeySearch;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class PortaxAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public PortaxAttack() {
		super("Portax");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		PortaxTask task = new PortaxTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.pow(13, length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateShortCustomKey(task, "ACEGIKMOQSUWY", length, true);
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().setIndeterminate(true);
			//for(int length = periodRange[0]; length <= periodRange[1]; ++length)
			task.run(periodRange[0], periodRange[1]);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class PortaxTask extends KeySearch implements ShortCustomKey {

		public PortaxTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Portax.decode(this.cipherText, key), this.getLanguage());
			
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
			return new Solution(Portax.decode(this.cipherText, key), this.getLanguage()).setKeyString(key);
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
		
		@Override
		public int alphaIncrease() {
			return 2;
		}
	}
	
	@Override
	public void write(HashMap<String, Object> map) {
		map.put("portax_period_range_min", this.rangeSpinner[0].getValue());
		map.put("portax_period_range_max", this.rangeSpinner[1].getValue());
	}

	@Override
	public void read(HashMap<String, Object> map) {
		if(map.containsKey("portax_period_range_min"))
			this.rangeSpinner[0].setValue(map.get("portax_period_range_min"));
		if(map.containsKey("portax_period_range_max"))
			this.rangeSpinner[1].setValue(map.get("portax_period_range_max"));
	}
}
