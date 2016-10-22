package nationalcipher.cipher.decrypt.complete;

import java.util.List;

import javalibrary.dict.Dictionary;
import javalibrary.math.MathUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.transposition.Cadenus;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.ui.IApplication;

public class CadenusAttack extends CipherAttack {

	public CadenusAttack() {
		super("Cadenus");
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		CadenusTask task = new CadenusTask(text, app);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			List<Integer> factors = MathUtil.getFactors(text.length() / 25);			
			app.out().println("" + factors);
			
			
			if(!factors.isEmpty()) {
				
				for(int factor : factors) {
					app.out().println("Factor: %d", factor);
					for(String word : Dictionary.words) {
					
						if(word.length() == factor)
							task.onIteration(word);
						
					}
				}
			}
		}
		else if(method == DecryptionMethod.BRUTE_FORCE) {
			List<Integer> factors = MathUtil.getFactors(text.length() / 25);			
			app.out().println("" + factors);
			app.getProgress().addMaxValue(1000);
			
			
			if(!factors.isEmpty()) {
				
				for(int factor : factors)
					KeyIterator.iterateShort26Key(task, factor, false);

			}
			else
				app.out().println("NOT SQUARE FACTORS");
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class CadenusTask extends InternalDecryption implements ShortCustomKey {

		public CadenusTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(String key) {
			int blockSize = key.length() * 25;
			char[] plainText = new char[0];
			for(int i = 0; i < this.cipherText.length / blockSize; i++)
				plainText = ArrayUtil.concat(plainText, Cadenus.decode(ArrayUtil.copyOfRange(this.cipherText, i * blockSize, blockSize), key));

			
			this.lastSolution = new Solution(plainText, this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(key);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
}
