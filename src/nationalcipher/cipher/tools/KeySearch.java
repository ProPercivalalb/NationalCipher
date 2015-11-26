package nationalcipher.cipher.tools;

import javalibrary.Output;
import javalibrary.string.StringTransformer;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.cipher.manage.Solution;

public abstract class KeySearch extends InternalDecryption {

	public KeySearch(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
		super(text, settings, keyPanel, output, progress);
	}
	
	public void run(int minLength, int maxLength) {
		for(int length = minLength; length <= maxLength; length++) {
			
			String parent = StringTransformer.repeat("A", length);

			Solution currentBestSolution = new Solution(new char[0], Double.NEGATIVE_INFINITY);
			
			while(true) {
				String startParent = parent;
				for(int i = 0; i < length; i++) {
					for(char j = 'A'; j <= 'Z'; j += alphaIncrease()) {
						String child = parent.substring(0, i) + j + parent.substring(i + 1, length);
						
						this.lastSolution = this.tryModifiedKey(child);
						//this.addSolution(this.lastSolution);
						
						if(this.lastSolution.score >= currentBestSolution.score) {
							parent = child;
							currentBestSolution = this.lastSolution;
						}
						
						this.onIteration();
					}
				}
				
				if(startParent.equals(parent)) 
					break;
			}
			
			if(currentBestSolution.score >= this.bestSolution.score) {
				this.bestSolution = currentBestSolution;
				this.solutionFound();
			}
			
		}
	}

	public int alphaIncrease() {
		return 1;
	}
	
	public abstract Solution tryModifiedKey(String key);
	
	public abstract void solutionFound();
	
	public abstract void onIteration();
}
