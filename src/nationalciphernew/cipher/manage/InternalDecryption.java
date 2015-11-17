package nationalciphernew.cipher.manage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;

public class InternalDecryption {

	public Solution bestSolution, lastSolution;
	public List<Solution> solutions;
	
	public int iteration;
	
	public char[] text;
	public Settings settings;
	public KeyPanel keyPanel;	
	public Output output;
	public ProgressValue progress;
	
	public InternalDecryption(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
		this.text = text;
		this.settings = settings;
		this.keyPanel = keyPanel;
		this.output = output;
		this.progress = progress;
		
		this.iteration = 1;
		this.bestSolution = new Solution(new char[0], Double.NEGATIVE_INFINITY);
		this.solutions = new ArrayList<Solution>();
	}

	public void sortSolutions() {
		Collections.sort(this.solutions);
	}
	
	public void resetSolution() {
		this.bestSolution = new Solution(new char[0], Double.NEGATIVE_INFINITY);
		this.solutions.clear();
	}
	
	public void resetIteration() {
		this.iteration = 1;
	}
	
	public String getBestSolution() {
		return new String(this.bestSolution.text);
	}
}
