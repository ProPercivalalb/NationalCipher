package nationalcipher.cipher.decrypt;

import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.ui.IApplication;

/**
 * For ciphers that can be converted into a substitution cipher and solved
 * e.g. ADFGX or Hill Substitution
 */
public class SubstitutionHack extends SimulatedAnnealing {

	public String bestKey, bestMaximaKey, lastKey;
	
	public SubstitutionHack(char[] text, IApplication app) {
		super(text, app);
	}
	
	@Override
	public Solution generateKey() {
		this.bestMaximaKey = KeyGeneration.createLongKeyUniversal(this.getAlphabet());
		return new Solution(Keyword.decodeWithAlphabet(this.cipherText, this.plainText, this.getAlphabet(), this.bestMaximaKey), this.getLanguage());
	}

	@Override
	public Solution modifyKey(double temp, int count, double lastDF) {
		this.lastKey = KeySquareManipulation.exchange2letters(this.bestMaximaKey);
		return new Solution(Keyword.decodeWithAlphabet(this.cipherText, this.plainText, this.getAlphabet(), this.lastKey), this.getLanguage());
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
	}

	@Override
	public boolean endIteration() {
		return true;
	}

	@Override
	public void onIteration() {
		
	}
	
	public char[] getAlphabet() {
		return KeyGeneration.ALL_26_CHARS;
	}
}