package nationalcipher.cipher.decrypt.complete;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.transposition.RailFence;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.decrypt.methods.KeyIterator.IntegerKey;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class RailFenceAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public RailFenceAttack() {
		super("Rail Fence");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 100, 2, Integer.MAX_VALUE, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		RailFenceTask task = new RailFenceTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			app.getProgress().addMaxValue(periodRange[1] - periodRange[0]);
			KeyIterator.iterateIntegerKey(task, periodRange[0], periodRange[1], 1);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class RailFenceTask extends InternalDecryption implements IntegerKey {

		public RailFenceTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(int no) {
			this.lastSolution = new Solution(RailFence.decode(this.cipherText, no), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("Rows-%d", no);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
}
