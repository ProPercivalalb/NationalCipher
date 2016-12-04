package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.other.Playfair6x6;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class Playfair6x6Attack extends CipherAttack {

	public Playfair6x6Attack() {
		super("Playfair 6x6");
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		PlayfairTask task = new PlayfairTask(text, app);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class PlayfairTask extends SimulatedAnnealing {

		public String bestKey, bestMaximaKey, lastKey;
		
		public PlayfairTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeyGeneration.createLongKey36();
			return new Solution(Playfair6x6.decode(this.cipherText, this.plainText, this.bestMaximaKey), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeyManipulation.modifyKey(this.bestMaximaKey, 6, 6);
			return new Solution(Playfair6x6.decode(this.cipherText, this.plainText, this.lastKey), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString(this.bestKey);
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
