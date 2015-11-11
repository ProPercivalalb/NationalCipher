package nationalciphernew.cipher;

import javalibrary.cipher.Playfair;
import javalibrary.cipher.wip.KeySquareManipulation;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.util.RandomUtil;
import nationalciphernew.Settings;
import nationalciphernew.UINew;

public abstract class SimulatedAnnealing {

	public Solution bestSolution, maxSolution, lastSolution;
	
	public void run(String text, Settings settings) {
		double bestFitnessFinal = Double.NEGATIVE_INFINITY;
		
		int iteration = 0;
		
		while(true) {
			this.bestSolution = this.generateKey();
			this.maxSolution = this.bestSolution;


			for(double TEMP = settings.simulatedAnnealing.get(0); TEMP >= 0; TEMP -= settings.simulatedAnnealing.get(1)) {
				for(int count = 0; count < settings.simulatedAnnealing.get(2); count++){ 
					this.lastSolution = this.modifyKey();
					double score = this.lastSolution.score;
					double dF = score - this.maxSolution.score;
					
				    if(dF >= 0) {
				    	this.maxSolution = this.lastSolution;
				        this.storeKey(true);
				    }
				    else if(TEMP > 0) { 
				    	double prob = Math.exp(dF / TEMP);
				        if(prob > RandomUtil.pickDouble()) {
				        	this.maxSolution = this.lastSolution;
				        	this.storeKey(true);
				        }
					}
				    
				    if(this.maxSolution.score > this.bestSolution.score) {
				    	this.bestSolution = this.maxSolution;
				        this.storeKey(false);
					}
				}
			}
			
			if(this.bestSolution.score > bestFitnessFinal) {
				bestFitnessFinal = bestscore;
				this.solutionFound();
			}
		}	
	}
	
	public abstract Solution generateKey();
	
	public abstract Solution modifyKey();
	
	public abstract void storeKey(boolean searchingMaximas);

	public abstract void solutionFound();
	
	public static class Solution {
		
		public final char[] text;
		public final double score;
		
		public Solution(char[] text, double score) {
			this.text = text;
			this.score = score;
		}
		
		public Solution(char[] text, ILanguage language) {
			this.text = text;
			this.score = TextFitness.scoreFitnessQuadgrams(text, language);
		}
	}
}
