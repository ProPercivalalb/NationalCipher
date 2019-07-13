package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;

import nationalcipher.cipher.base.transposition.Swagman;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.ui.IApplication;

public class SwagmanAttack extends CipherAttack {

	public SwagmanAttack() {
		super("Swagman");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		SwagmanTask task = new SwagmanTask(text, app);
		
		//Setting grab
		task.size = 3;
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			app.out().println("" + StatCalculator.calculateSSTD(text));

		}
		
		//app.out().println(task.getBestSolution());
	}
	
	public class SwagmanTask extends DecryptionTracker {

		public int size;
		
		public SwagmanTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		public void onIteration(int[] key) {
			this.lastSolution = new Solution(Swagman.decode(this.cipherText, key, this.size), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("Size: %d %s", this.size, Arrays.toString(key));
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
}
