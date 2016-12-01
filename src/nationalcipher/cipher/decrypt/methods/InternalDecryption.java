package nationalcipher.cipher.decrypt.methods;

import javalibrary.Output;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;

public class InternalDecryption {

	public Solution bestSolution, lastSolution;
	public int iteration;
	
	public char[] cipherText;
	public byte[] plainText;
	public IApplication app;
	public double UPPER_ESTIMATE;
	
	public InternalDecryption(char[] cipherText, IApplication app) {
		this.app = app;	
		this.iteration = 1;
		this.bestSolution = new Solution();
		
		this.setCipherText(cipherText);
	}
	
	public void setCipherText(char[] cipherText) {
		this.cipherText = cipherText;
		this.plainText = new byte[this.getOutputTextLength(cipherText.length)];
		this.UPPER_ESTIMATE = TextFitness.getEstimatedFitness(this.plainText.length, this.getLanguage().getQuadgramData()) * 1.1;
	}
	
	public int getOutputTextLength(int inputLength) {
		return inputLength;
	}
	
	public void addSolution(Solution solution) {
		if(this.getSettings().collectSolutions())
			if(solution.score > this.UPPER_ESTIMATE) {
				solution.bakeSolution();
				UINew.topSolutions.addSolution(solution);
			}
	}
	
	public void resetSolution() {
		this.bestSolution = new Solution();
		UINew.topSolutions.reset();
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
