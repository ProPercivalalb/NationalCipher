package nationalcipher.cipher.decrypt.complete;

import java.math.BigInteger;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.substitution.BeaufortProgressiveKey;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Variant;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.IntegerKey;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.decrypt.complete.methods.KeySearch;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class ProgressiveKeyBeaufortAttack extends CipherAttack {

	public JSpinner[] rangeSpinner1;
	public JSpinner[] rangeSpinner2;
	public JSpinner[] rangeSpinner3;
	
	public ProgressiveKeyBeaufortAttack() {
		super("Progressive Key Beaufort");
		this.setAttackMethods(DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner1 = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
		this.rangeSpinner2 = JSpinnerUtil.createRangeSpinners(1, 30, 1, 100, 1);
		this.rangeSpinner3 = JSpinnerUtil.createRangeSpinners(1, 30, 1, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner1));
		panel.add(new SubOptionPanel("Prog Period:", this.rangeSpinner2));
		panel.add(new SubOptionPanel("Prog Index:", this.rangeSpinner3));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		ProgressiveKeyBeaufortTask task = new ProgressiveKeyBeaufortTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner1);
		int[] progPeriodRange = SettingParse.getIntegerRange(this.rangeSpinner2);
		int[] progIndexRange = SettingParse.getIntegerRange(this.rangeSpinner3);
		
		if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().setIndeterminate(true);
			for(int i = progPeriodRange[0]; i <= progPeriodRange[1]; i++) {
				for(int j = progIndexRange[0]; j <= progIndexRange[1]; j++) {
					task.progPeriod = i;
					task.progIndex = j;

					task.run(periodRange[0], periodRange[1]);
				}
			}
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class ProgressiveKeyBeaufortTask extends KeySearch {

		public int progPeriod;
		public int progIndex;
		
		public ProgressiveKeyBeaufortTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(BeaufortProgressiveKey.decode(this.cipherText, key, this.progPeriod, this.progIndex), this.getLanguage()).setKeyString(key);
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
}
