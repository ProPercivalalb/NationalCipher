package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.transposition.Redefence;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.IntegerOrderedKey;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class RedefenceAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public RedefenceAttack() {
		super("Redefence");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		RedefenceTask task = new RedefenceTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.factorialBig(length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateIntegerOrderedKey(task, length);
			
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class RedefenceTask extends InternalDecryption implements IntegerOrderedKey {

		public RedefenceTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(int[] order) {
			this.lastSolution = new Solution(Redefence.decode(this.cipherText, order), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(Arrays.toString(order));
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
	
	@Override
	public void write(HashMap<String, Object> map) {
		map.put("redefence_period_range_min", this.rangeSpinner[0].getValue());
		map.put("redefence_period_range_max", this.rangeSpinner[1].getValue());
	}

	@Override
	public void read(HashMap<String, Object> map) {
		if(map.containsKey("redefence_period_range_min"))
			this.rangeSpinner[0].setValue(map.get("redefence_period_range_min"));
		if(map.containsKey("redefence_period_range_max"))
			this.rangeSpinner[1].setValue(map.get("redefence_period_range_max"));
	}
}
