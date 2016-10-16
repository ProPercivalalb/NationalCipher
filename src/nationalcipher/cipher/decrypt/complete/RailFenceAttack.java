package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.transposition.RailFence;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.IntegerKey;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.ui.IApplication;

public class RailFenceAttack extends CipherAttack {

	public RailFenceAttack() {
		super("Rail Fence");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		RailFenceTask task = new RailFenceTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			app.getProgress().addMaxValue(30);
			KeyIterator.iterateIntegerKey(task, 2, 32, 1);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class RailFenceTask extends InternalDecryption implements IntegerKey {

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
