package nationalcipher.cipher.decrypt.methods;

import javax.annotation.Nullable;

import javalibrary.Output;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.NationalCipherUI;

public class DecryptionTracker {
	
	private final CharSequence cipherText;
	private final char[] plainText;
	private final IApplication app;
	private final double UPPER_ESTIMATE;
	
	/**
	 * Just a place to store the most recent solution
	 */
    public Solution bestSolution;
    /**
     * Just a place to store the most recent solution
     */
    @Nullable
    public Solution lastSolution;
    public long iteration;
	
	public DecryptionTracker(CharSequence cipherText, IApplication app) {
		this.app = app;	
		this.iteration = 1;
		this.bestSolution = Solution.WORST_SOLUTION;
		
		this.cipherText = cipherText;
        this.plainText = new char[this.getOutputTextLength(cipherText.length())];
        this.UPPER_ESTIMATE = TextFitness.getEstimatedFitness(this.plainText.length, this.getLanguage().getQuadgramData()) * 1.1;
	}
	
	public int getOutputTextLength(int inputLength) {
		return inputLength;
	}
	
	public CharSequence getCipherText() {
	    return this.cipherText;
	}
	
	public char[] getPlainTextHolder() {
        return this.plainText;
    }
	
	public void addSolution(Solution solution) {
		// Some quick easy checks
		if(!this.getSettings().collectSolutions() || solution.score <= this.UPPER_ESTIMATE) return;
			
		if(NationalCipherUI.topSolutions.addSolution(solution)) { // Added successfully
			solution.bakeSolution();
		}
	}
	
	public void resetSolution() {
		this.bestSolution = Solution.WORST_SOLUTION;
		NationalCipherUI.topSolutions.reset();
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
