package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.FourSquare;
import nationalcipher.cipher.decrypt.complete.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class FourSquareAttack extends CipherAttack {

	public FourSquareAttack() {
		super("Four Square");
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		FourSquareTask task = new FourSquareTask(text, app);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class FourSquareTask extends SimulatedAnnealing {

		public String bestKey1, bestMaximaKey1, lastKey1;
		public String bestKey2, bestMaximaKey2, lastKey2;
		
		public FourSquareTask(String text, IApplication app) {
			super(text.toCharArray(), app);
			this.iterationTimer = true;
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeySquareManipulation.generateRandKeySquare();
			this.bestMaximaKey2 = KeySquareManipulation.generateRandKeySquare();
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			return new Solution(FourSquare.decode(this.cipherText, this.plainText, this.bestMaximaKey1, this.bestMaximaKey2), this.getLanguage());
		}

		@Override
		public Solution modifyKey(int count) {
			if(count % 2 == 0)
				this.lastKey1 = KeySquareManipulation.modifyKey(this.bestMaximaKey1);
			else
				this.lastKey2 = KeySquareManipulation.modifyKey(this.bestMaximaKey2);
			
			return new Solution(FourSquare.decode(this.cipherText, this.plainText, this.lastKey1, this.lastKey2), this.getLanguage());
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
}
