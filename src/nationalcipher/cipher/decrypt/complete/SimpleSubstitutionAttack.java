package nationalcipher.cipher.decrypt.complete;

import javalibrary.dict.Dictionary;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.decrypt.complete.methods.DictionaryAttack;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.Long26Key;
import nationalcipher.cipher.decrypt.complete.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class SimpleSubstitutionAttack extends CipherAttack {

	public SimpleSubstitutionAttack() {
		super("Simple Substitution");
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		SimpleSubstitutionTask task = new SimpleSubstitutionTask(text, app);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			for(String word : Dictionary.words) {
				String complete = new String[]{"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "NOPQRSTUVWXYZABCDEFGHIJKLM", "ZYXWVUTSRQPONMLKJIHGFEDCBA"}[app.getSettings().getKeywordCreationId()];
	
				task.onIteration(DictionaryAttack.createLong26Key(word, complete, '?'));
			}
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class SimpleSubstitutionTask extends SimulatedAnnealing implements Long26Key {

		public String bestKey, bestMaximaKey, lastKey;
		
		public SimpleSubstitutionTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Keyword.decode(this.cipherText, key), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s", key);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKey();
			return new Solution(Keyword.decode(this.cipherText, this.bestMaximaKey), this.getLanguage());
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.exchange2letters(this.bestMaximaKey);
			return new Solution(Keyword.decode(this.cipherText, this.lastKey), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString(this.bestKey);
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
