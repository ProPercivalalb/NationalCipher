package nationalcipher.cipher.decrypt.complete;

import javalibrary.dict.Dictionary;
import nationalcipher.cipher.base.other.Playfair;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DictionaryAttack;
import nationalcipher.cipher.decrypt.methods.DictionaryAttack.DictionaryKey;
import nationalcipher.cipher.decrypt.methods.KeyIterator.Long25Key;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.cipher.transposition.RouteCipherType;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class PlayfairAttack extends CipherAttack {

	public PlayfairAttack() {
		super("Playfair");
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		PlayfairTask task = new PlayfairTask(text, app);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());

			DictionaryAttack.tryKeysWithOptions(task, Dictionary.WORDS_CHAR, KeyGeneration.ALL_25_CHARS, 5, 5, app.getSettings().checkShift(), app.getSettings().checkReverse(), app.getSettings().checkRoutes());
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class PlayfairTask extends SimulatedAnnealing implements Long25Key, DictionaryKey {

		public String bestKey, bestMaximaKey, lastKey;
		
		public PlayfairTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public void onKeyCreation(char[] complete, char[] word, int shift, boolean reversed, RouteCipherType route) {
			this.lastSolution = new Solution(Playfair.decode(this.cipherText, this.plainText, complete), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(DictionaryAttack.expressParameters(complete, word, shift, reversed, route));
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Playfair.decode(this.cipherText, this.plainText, key), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(key);
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeyGeneration.createLongKey25();
			return new Solution(Playfair.decode(this.cipherText, this.plainText, this.bestMaximaKey), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeyManipulation.modifyKey(this.bestMaximaKey, 5, 5);
			return new Solution(Playfair.decode(this.cipherText, this.plainText, this.lastKey), this.getLanguage());
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
