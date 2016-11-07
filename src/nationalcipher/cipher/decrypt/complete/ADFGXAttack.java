package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;

import nationalcipher.cipher.base.other.ADFGX;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class ADFGXAttack extends CipherAttack {

	public ADFGXAttack() {
		super("ADFGX");
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		ADFGXTask task = new ADFGXTask(text, app);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class ADFGXTask extends SimulatedAnnealing {

		public int[] order;
		public String bestKey, bestMaximaKey, lastKey;
		
		public ADFGXTask(String text, IApplication app) {
			super(text.toCharArray(), app);
			this.order = new int[] {0, 1, 2, 3}; //TODO Dynamically determine order
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(ADFGX.decode(this.cipherText, this.bestMaximaKey, this.order, false), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(ADFGX.decode(this.cipherText, this.lastKey, this.order, false), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString("%s, Order: %s", this.bestKey, Arrays.toString(this.order));
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
		
		@Override
		public int getOutputTextLength(int inputLength) {
			return inputLength;
		}
	}
}
