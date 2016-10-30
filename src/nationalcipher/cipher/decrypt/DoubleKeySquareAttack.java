package nationalcipher.cipher.decrypt;

import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public abstract class DoubleKeySquareAttack extends CipherAttack {

	private DoubleLongKeyTask task;
	
	public DoubleKeySquareAttack(String displayName) {
		super(displayName);
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		this.task = new DoubleLongKeyTask(text, app);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			this.task.run();
		}
		
		app.out().println(this.task.getBestSolution());
	}
	
	public class DoubleLongKeyTask extends SimulatedAnnealing {

		public String bestKey1, bestMaximaKey1, lastKey1;
		public String bestKey2, bestMaximaKey2, lastKey2;
		
		public DoubleLongKeyTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeyGeneration.createLongKey25();
			this.bestMaximaKey2 = KeyGeneration.createLongKey25();
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			return new Solution(DoubleKeySquareAttack.this.decode(this.cipherText, this.plainText, this.bestMaximaKey1, this.bestMaximaKey2), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			if(count % 2 == 0)
				this.lastKey1 = KeySquareManipulation.modifyKey(this.bestMaximaKey1);
			else
				this.lastKey2 = KeySquareManipulation.modifyKey(this.bestMaximaKey2);
			
			return new Solution(DoubleKeySquareAttack.this.decode(this.cipherText, this.plainText, this.lastKey1, this.lastKey2), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
			this.bestMaximaKey2 = this.lastKey2;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.bestKey2 = this.bestMaximaKey2;
			this.bestSolution.setKeyString(this.bestKey1 + " " + this.bestKey2);
			this.bestSolution.bakeSolution();
			this.getKeyPanel().updateSolution(this.bestSolution);
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
	
	@Override
	public void onTermination(boolean forced) {
		if(forced)
			this.task.app.out().println("%s", this.task.bestSolution);
	}
	
	public abstract char[] decode(char[] cipherText, char[] plainText, String key1, String key2);
}
