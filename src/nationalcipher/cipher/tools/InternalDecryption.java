package nationalcipher.cipher.tools;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.UINew;
import nationalcipher.cipher.manage.Solution;

public class InternalDecryption {

	public Solution bestSolution, lastSolution;
	public int iteration;
	
	public char[] text;
	public char[] outputText;
	public Settings settings;
	public KeyPanel keyPanel;	
	public Output output;
	public ProgressValue progress;
	
	public InternalDecryption(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
		this.text = text;
		this.outputText = new char[this.getOutputTextLength(text.length)];
		this.settings = settings;
		this.keyPanel = keyPanel;
		this.output = output;
		this.progress = progress;
		
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
}
