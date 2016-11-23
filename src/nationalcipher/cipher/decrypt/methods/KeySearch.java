package nationalcipher.cipher.decrypt.methods;

import java.util.Arrays;

import nationalcipher.ui.IApplication;

public abstract class KeySearch extends InternalDecryption {

	public KeySearch(char[] text, IApplication app) {
		super(text, app);
	}
	
	public void run(int minLength, int maxLength) {
		for(int length = minLength; length <= maxLength; length++) {
			
			byte[] parent = new byte[length];
			Arrays.fill(parent, (byte)'A');

			Solution currentBestSolution = new Solution();
			
			while(true) {
				boolean change = false;
				for(int i = 0; i < length; i++) {
					for(char j = 'A'; j <= 'Z'; j += alphaIncrease()) {
						byte previous = parent[i];
						parent[i] = (byte)j;
						
						this.lastSolution = this.tryModifiedKey(new String(parent));
						this.addSolution(this.lastSolution);
						
						if(this.lastSolution.score >= currentBestSolution.score) {
							currentBestSolution = this.lastSolution;
							change = previous != j;
						}
						else //Last solution is worst so revert key
							parent[i] = previous;
						
						this.onIteration();
					}
				}
				
				if(!change) 
					break;
			}
			
			this.foundBestSolutionForLength(currentBestSolution);
			
			if(currentBestSolution.score >= this.bestSolution.score) {
				this.bestSolution = currentBestSolution;
				this.solutionFound();
			}
		}
	}

	//TODO
	public boolean duplicateLetters() {
		return true;
	}
	
	public int alphaIncrease() {
		return 1;
	}
	
	public abstract Solution tryModifiedKey(String key);
	
	public void foundBestSolutionForLength(Solution currentBestSolution) {
		
	}
	
	public void solutionFound() {
		this.out().println("%s", this.bestSolution);
		this.getKeyPanel().updateSolution(this.bestSolution);
	}

	public void onIteration() {
		this.getKeyPanel().updateIteration(this.iteration++);
	}
}
