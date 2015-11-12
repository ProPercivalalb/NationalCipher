package nationalciphernew.cipher;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;

public class InternalDecryption {

	public Solution bestSolution, lastSolution;
	
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
		
		this.bestSolution = new Solution(new char[0], Double.NEGATIVE_INFINITY);
	}
}
