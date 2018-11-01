package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;

import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.transposition.Grille;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.GrilleKey;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.ui.IApplication;

public class GrilleAttack extends CipherAttack {

	public GrilleAttack() {
		super("Turning Grille");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		GrilleTask task = new GrilleTask(text, app);
		
		//Settings grab
		task.size = 7;
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			KeyIterator.iterateGrille(task, task.size);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class GrilleTask extends InternalDecryption implements GrilleKey {

		public int size;
		
		public GrilleTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(int[] key) {
			this.lastSolution = new Solution(Grille.decode(this.cipherText, this.plainText, this.size, key), this.getLanguage());
			this.addSolution(this.lastSolution);
			
			if(this.lastSolution.isResultBetter(this.bestSolution)) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(Arrays.toString(key));
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
}
