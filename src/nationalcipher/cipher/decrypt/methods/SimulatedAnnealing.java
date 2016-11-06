package nationalcipher.cipher.decrypt.methods;

import java.util.Arrays;

import javalibrary.lib.Timer;
import javalibrary.math.Units;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public abstract class SimulatedAnnealing extends InternalDecryption {

	public boolean iterationTimer;
	
	public SimulatedAnnealing(char[] text, IApplication app) {
		super(text, app);
		this.iterationTimer = false;
	}

	public Solution maxSolution;
	public double lastDF;
	
	public void run() {
		Timer timer = new Timer();
		while(true) {
			timer.restart();
			this.bestSolution = this.generateKey();
			this.maxSolution = this.bestSolution;
			this.solutionFound();

			for(double TEMP = this.getSettings().getSATempStart(); TEMP >= 0; TEMP -= this.getSettings().getSATempStep()) {
				for(int count = 0; count < this.getSettings().getSACount(); count++) { 
					
					this.lastSolution = this.modifyKey(TEMP, count, this.lastDF);
					this.addSolution(this.lastSolution);
					
					this.lastDF = this.lastSolution.score - this.maxSolution.score;
					
				    if(this.lastDF >= 0) {
				    	this.maxSolution = this.lastSolution;
				        this.storeKey();
				    }
				    else if(TEMP > 0) { 
				    	double prob = Math.exp(this.lastDF / TEMP);
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
			if(this.iterationTimer)
				this.out().println("Iteration Time: %f", timer.getTimeRunning(Units.Time.MILLISECOND));
			
			if(this.endIteration())
				break;
		}	
	}
	
	public abstract Solution generateKey();
	
	public abstract Solution modifyKey(double temp, int count, double lastDF);
	
	public abstract void storeKey();

	public abstract void solutionFound();
	
	public abstract void onIteration();
	
	public abstract boolean endIteration();
}
