package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.Bazeries;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.ui.IApplication;

public class BazeriesAttack extends CipherAttack {

	public BazeriesAttack() {
		super("Bazeries");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		BazeriesTask task = new BazeriesTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			app.getProgress().addMaxValue(1000000);
			KeyIterator.iterateIntegerKey(task::onIteration, 1, 1000000, 1);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class BazeriesTask extends InternalDecryption {

		public BazeriesTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		public void onIteration(int no) {
			this.lastSolution = new Solution(Bazeries.decode(this.cipherText, no), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%d", no);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
}
