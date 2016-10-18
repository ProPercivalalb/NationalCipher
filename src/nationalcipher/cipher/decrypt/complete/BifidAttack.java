package nationalcipher.cipher.decrypt.complete;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.polybiussquare.Bifid;
import nationalcipher.cipher.base.polybiussquare.Playfair;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator;
import nationalcipher.cipher.decrypt.complete.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.IntegerKey;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.Long25Key;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class BifidAttack extends CipherAttack {

	public JSpinner spinner;
	
	public BifidAttack() {
		super("Bifid");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.SIMULATED_ANNEALING);
		this.spinner = JSpinnerUtil.createSpinner(3, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period:", this.spinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		BifidTask task = new BifidTask(text, app);
		
		//Settings grab
		task.period = SettingParse.getInteger(this.spinner);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			app.getProgress().addMaxValue(26);
			//KeyIterator.iterateLong26Key(task);
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class BifidTask extends SimulatedAnnealing implements Long25Key {

		public int period;
		public String bestKey, bestMaximaKey, lastKey;
		
		public BifidTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Bifid.decode(this.cipherText, key, this.period), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s, p:%d", key, this.period);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(Bifid.decode(this.cipherText, this.bestMaximaKey, this.period), this.getLanguage());
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(Bifid.decode(this.cipherText, this.lastKey, this.period), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString(this.bestKey);
			this.getKeyPanel().fitness.setText("" + this.bestSolution.score);
			this.getKeyPanel().key.setText(this.bestKey);
		}
		
		@Override
		public void onIteration() {
			this.getProgress().increase();
			this.getKeyPanel().updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.out().println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
	}
}
