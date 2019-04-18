package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.ui.IApplication;

public class RagbabyAttack extends CipherAttack {

	public RagbabyAttack() {
		super("Ragbaby");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		RagbabyTask task = new RagbabyTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			app.getProgress().addMaxValue(26);
			KeyIterator.iterateIntegerKey(task::onIteration, 0, 26, 1);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class RagbabyTask extends InternalDecryption {

		public RagbabyTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		public void onIteration(int no) {
			this.lastSolution = new Solution(Caesar.decode(this.cipherText, no), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("Shift-%d", no);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
}
