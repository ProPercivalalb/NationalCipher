package nationalcipher.cipher.decrypt.complete;

import javalibrary.dict.Dictionary;
import javalibrary.math.MathUtil;
import nationalcipher.cipher.base.substitution.TriSquare;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DictionaryAttack;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class TriSquareAttack extends CipherAttack {

	private TriSquareTask task;
	
	public TriSquareAttack() {
		super("Tri Square");
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		this.task = new TriSquareTask(text, app);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(MathUtil.pow(Dictionary.wordCount(), 3));
			for(String word : Dictionary.words)
				for(String word1 : Dictionary.words)
					for(String word2 : Dictionary.words)
						this.task.onIteration(DictionaryAttack.createLong26Key(word, app.getSettings().getKeywordFiller(), 'J'), DictionaryAttack.createLong26Key(word1, app.getSettings().getKeywordFiller(), 'J'), DictionaryAttack.createLong26Key(word2, app.getSettings().getKeywordFiller(), 'J'));
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			this.task.run();
		}
		
		app.out().println(this.task.getBestSolution());
	}
	
	public class TriSquareTask extends SimulatedAnnealing {

		public String bestKey1, bestMaximaKey1, lastKey1;
		public String bestKey2, bestMaximaKey2, lastKey2;
		public String bestKey3, bestMaximaKey3, lastKey3;
		
		public TriSquareTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		//@Override
		public void onIteration(String key1, String key2, String key3) {
			this.lastSolution = new Solution(TriSquare.decode(this.cipherText, this.plainText, key1, key2, key3), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s %s %s", key1, key2, key3);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeyGeneration.createLongKey25();
			this.bestMaximaKey2 = KeyGeneration.createLongKey25();
			this.bestMaximaKey3 = KeyGeneration.createLongKey25();
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			this.lastKey3 = this.bestMaximaKey3;
			return new Solution(TriSquare.decode(this.cipherText, this.plainText, this.bestMaximaKey1, this.bestMaximaKey2, this.bestMaximaKey3), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			if(count % 2 == 0)
				this.lastKey1 = KeySquareManipulation.modifyKey(this.bestMaximaKey1);
			if(count % 2 == 1)
				this.lastKey2 = KeySquareManipulation.modifyKey(this.bestMaximaKey2);
			else
				this.lastKey3 = KeySquareManipulation.modifyKey(this.bestMaximaKey3);
			
			return new Solution(TriSquare.decode(this.cipherText, this.plainText, this.lastKey1, this.lastKey2, this.lastKey3), this.getLanguage());
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
		
		@Override
		public int getOutputTextLength(int inputLength) {
			return inputLength / 3 * 2;
		}
	}
	
	@Override
	public void onTermination(boolean forced) {
		if(forced)
			this.task.app.out().println("%s", this.task.bestSolution);
	}
}
