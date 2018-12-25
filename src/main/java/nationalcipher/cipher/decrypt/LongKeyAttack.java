package nationalcipher.cipher.decrypt;

import javalibrary.dict.Dictionary;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DictionaryAttack;
import nationalcipher.cipher.decrypt.methods.DictionaryAttack.DictionaryKey;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.cipher.transposition.RouteCipherType;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.NationalCipherUI;

public abstract class LongKeyAttack extends CipherAttack {

	private LongKeyTask task;
	
	public LongKeyAttack(String displayName) {
		super(displayName);
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		this.task = new LongKeyTask(text, app);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());

			DictionaryAttack.tryKeysWithOptions(task, Dictionary.WORDS_CHAR, KeyGeneration.ALL_26_CHARS, app.getSettings().checkShift(), app.getSettings().checkReverse());
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			this.task.run();
		}
		
		app.out().println(this.task.getBestSolution());
	}
	
	public class LongKeyTask extends SimulatedAnnealing implements DictionaryKey {

		public String bestKey, bestMaximaKey, lastKey;
		
		public LongKeyTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onKeyCreation(char[] complete, char[] word, int shift, boolean reversed, RouteCipherType route) {
			String key = new String(complete);
			this.lastSolution = new Solution(LongKeyAttack.this.decode(this.cipherText, this.plainText, key), this.getLanguage());
			this.addSolution(this.lastSolution);
			
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
		public Solution generateKey() {
			this.bestMaximaKey = KeyGeneration.createLongKey26();
			return new Solution(LongKeyAttack.this.decode(this.cipherText, this.plainText, this.bestMaximaKey), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeyManipulation.swapTwoCharacters(this.bestMaximaKey);
			return new Solution(LongKeyAttack.this.decode(this.cipherText, this.plainText, this.lastKey), this.getLanguage());
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
			NationalCipherUI.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
		
		@Override
		public int getOutputTextLength(int inputLength) {
			return LongKeyAttack.this.getOutputTextLength(inputLength);
		}
	}
	
	@Override
	public void onTermination(boolean forced) {
		if(forced)
			this.task.app.out().println("%s", this.task.bestSolution);
	}
	
	public abstract byte[] decode(char[] cipherText, byte[] plainText, String key);
	
	public int getOutputTextLength(int inputLength) {
		return inputLength;
	}
}
