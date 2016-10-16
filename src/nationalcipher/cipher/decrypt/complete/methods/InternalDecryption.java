package nationalcipher.cipher.decrypt.complete.methods;

import javalibrary.Output;
import javalibrary.language.ILanguage;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;

public class InternalDecryption {

	public Solution bestSolution, lastSolution;
	public int iteration;
	
	public char[] cipherText;
	public char[] plainText;
	public IApplication app;
	
	public InternalDecryption(char[] cipherText, IApplication app) {
		this.cipherText = cipherText;
		this.plainText = new char[this.getOutputTextLength(cipherText.length)];
		this.app = app;
		
		this.iteration = 1;
		this.bestSolution = new Solution(null, Double.NEGATIVE_INFINITY);
	}
	
	public int getOutputTextLength(int inputLength) {
		return inputLength;
	}
	
	public void addSolution(Solution solution) {
		UINew.topSolutions.solutions.add(solution);
	}
	
	public void resetSolution() {
		this.bestSolution = new Solution(null, Double.NEGATIVE_INFINITY);
		UINew.topSolutions.solutions.clear();
	}
	
	public void resetIteration() {
		this.iteration = 1;
	}
	
	public String getBestSolution() {
		return new String(this.bestSolution.getText());
	}
	
	//IApplication methods
	public ILanguage getLanguage() {
		return this.app.getLanguage();
	}
	
	public Settings getSettings() {
		return this.app.getSettings();
	}
	
	public ProgressValue getProgress() {
		return this.app.getProgress();
	}
	
	public Output out() {
		return this.app.out();
	}
	
	public KeyPanel getKeyPanel() {
		return this.app.getKeyPanel();
	}
}
