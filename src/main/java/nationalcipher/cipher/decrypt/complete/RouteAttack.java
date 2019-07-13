package nationalcipher.cipher.decrypt.complete;

import java.util.List;

import javalibrary.math.MathUtil;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.transposition.RouteCipherType;
import nationalcipher.cipher.transposition.RouteTransposition;
import nationalcipher.cipher.transposition.Routes;
import nationalcipher.ui.IApplication;

public class RouteAttack extends CipherAttack {

	public RouteAttack() {
		super("Route");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		RouteTask task = new RouteTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			List<Integer> factors = MathUtil.getFactors(text.length());
			
			app.out().println("Factors - " + factors);
			app.getProgress().addMaxValue((factors.size() - 2) * (int)Math.pow(Routes.getRoutes().size(), 2));
			
			for(int factor : factors) {
				if(factor == 1 || factor == text.length()) continue;
				int totalSize = text.length();
				int width = factor;
				int height = totalSize / width;
				
				for(RouteCipherType type2 : Routes.getRoutes())
					for(RouteCipherType type : Routes.getRoutes())
						task.onIteration(width, height, type2, type);
			}
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class RouteTask extends DecryptionTracker {

		public RouteTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		public void onIteration(int columns, int rows, RouteCipherType writtenOn, RouteCipherType readOff) {
			this.lastSolution = new Solution(RouteTransposition.decode(this.cipherText, columns, rows, writtenOn, readOff), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("C:%d R:%d %s %s ", columns, rows, writtenOn.getDescription(), readOff.getDescription());
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
			
		}
	}
}
