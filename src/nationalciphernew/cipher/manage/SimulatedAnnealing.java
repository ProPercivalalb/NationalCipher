package nationalciphernew.cipher.manage;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import javalibrary.util.RandomUtil;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;

public abstract class SimulatedAnnealing extends InternalDecryption {

	public SimulatedAnnealing(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
		super(text, settings, keyPanel, output, progress);
	}

	public Solution maxSolution;
	
	public void run() {
		while(true) {
			this.bestSolution = this.generateKey();
			this.maxSolution = this.bestSolution;


			for(double TEMP = this.settings.getSATempStart(); TEMP >= 0; TEMP -= this.settings.getSATempStep()) {
				for(int count = 0; count < this.settings.getSACount(); count++) { 
					
					this.lastSolution = this.modifyKey(count);
					double score = this.lastSolution.score;
					double dF = score - this.maxSolution.score;
					
				    if(dF >= 0) {
				    	this.maxSolution = this.lastSolution;
				        this.storeKey();
				    }
				    else if(TEMP > 0) { 
				    	double prob = Math.exp(dF / TEMP);
				        if(prob > RandomUtil.pickDouble()) {
				        	this.maxSolution = this.lastSolution;
				        	this.storeKey();
				        }
					}
				    
					if(this.maxSolution.score > this.bestSolution.score) {
						this.bestSolution = this.maxSolution;
						this.solutionFound();
					}
					
					this.onIteration();
				}
			}

			if(this.endIteration()) break;
		}	
	}
	
	public abstract Solution generateKey();
	
	public abstract Solution modifyKey(int count);
	
	public abstract void storeKey();

	public abstract void solutionFound();
	
	public abstract void onIteration();
	
	public abstract boolean endIteration();
}
