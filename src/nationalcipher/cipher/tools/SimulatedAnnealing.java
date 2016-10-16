package nationalcipher.cipher.tools;

import javalibrary.Output;
import javalibrary.lib.Timer;
import javalibrary.math.Units;
import javalibrary.swing.ProgressValue;
import javalibrary.util.RandomUtil;
import nationalcipher.Settings;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.KeyPanel;

public abstract class SimulatedAnnealing extends InternalDecryption {

	public boolean iterationTimer;
	
	public SimulatedAnnealing(char[] text, IApplication app) {
		super(text, app);
		this.iterationTimer = false;
	}

	public Solution maxSolution;
	
	public void run() {
		Timer timer = new Timer();
		while(true) {
			timer.restart();
			this.bestSolution = this.generateKey();
			this.maxSolution = this.bestSolution;
			this.solutionFound();

			for(double TEMP = this.getSettings().getSATempStart(); TEMP >= 0; TEMP -= this.getSettings().getSATempStep()) {
				for(int count = 0; count < this.getSettings().getSACount(); count++) { 
					
					this.lastSolution = this.modifyKey(count);
					//this.addSolution(this.lastSolution);
					
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
		
			if(this.endIteration())  {
				if(this.iterationTimer)
					this.out().println("Iteration Time: %f", timer.getTimeRunning(Units.Time.MILLISECOND));
				break;
			}
			if(this.iterationTimer)
				this.out().println("Iteration Time: %f", timer.getTimeRunning(Units.Time.MILLISECOND));
		}	
	}
	
	public abstract Solution generateKey();
	
	public abstract Solution modifyKey(int count);
	
	public abstract void storeKey();

	public abstract void solutionFound();
	
	public abstract void onIteration();
	
	public abstract boolean endIteration();
}
