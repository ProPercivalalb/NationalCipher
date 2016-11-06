package nationalcipher.cipher.decrypt.complete;

import java.util.List;

import javalibrary.algebra.SimultaneousEquations;
import javalibrary.string.StringAnalyzer;
import nationalcipher.cipher.base.substitution.Affine;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.decrypt.methods.KeyIterator.AffineKey;
import nationalcipher.ui.IApplication;

public class AffineAttack extends CipherAttack {

	public AffineAttack() {
		super("Affine");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		AffineTask task = new AffineTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			app.getProgress().addMaxValue(312);
			KeyIterator.iterateAffineKey(task);
		}
		else if(method == DecryptionMethod.CALCULATED) {
			List<Character> chars = StringAnalyzer.getOrderedCharacterCount(task.cipherText);
	
			char language0 = app.getLanguage().getLetterLargestFirst().get(0);
			char language1 = app.getLanguage().getLetterLargestFirst().get(1);
			
			char sorted0 = chars.get(0);
			char sorted1 = chars.get(1);
			
			app.out().println("%s --> %s  %s --> %s", language0, sorted0, language1, sorted1);
			int[] solution = SimultaneousEquations.solveSimEquationsMod(new int[][] {new int[] {language1 - 'A', 1, sorted1 - 'A'},  new int[] {language0 - 'A', 1, sorted0 - 'A'}}, 26);
			task.onIteration(solution[0], solution[1]);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class AffineTask extends InternalDecryption implements AffineKey {

		public AffineTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(int a, int b) {
			this.lastSolution = new Solution(Affine.decode(this.cipherText, a, b), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("A-%d, B-%d", a, b);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
}
