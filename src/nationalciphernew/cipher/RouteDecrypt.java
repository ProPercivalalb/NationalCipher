package nationalciphernew.cipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;

import javalibrary.Output;
import javalibrary.cipher.Redefence;
import javalibrary.cipher.wip.Routes;
import javalibrary.cipher.wip.Routes.RouteCipherType;
import javalibrary.fitness.TextFitness;
import javalibrary.math.MathHelper;
import javalibrary.string.StringTransformer;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator.RedefenceKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.InternalDecryption;
import nationalciphernew.cipher.manage.Solution;

public class RouteDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Route";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		RedefenceTask task = new RedefenceTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			List<Integer> factors = MathHelper.getFactors(text.length());
			
			output.println("Factors - " + factors);
			
			double bestScore = Double.NEGATIVE_INFINITY;
			String bestText = "";
			
			for(Integer factor : factors) {
				if(factor == 1 || factor == text.length()) continue;
				int totalSize = text.length();
				int width = factor;
				int height = totalSize / width;
				
				
				for(RouteCipherType type : Routes.getRoutes()) {
					//Create pattern
					List<Integer> grid = new ArrayList<Integer>();
					grid = type.createPattern(width, height, totalSize);
					
					//Reads across the grid
					String gridString = "";
					for(int i = 0; i < text.length(); i++)
						gridString += text.charAt(grid.indexOf(i));
					
					//Read down columns
					String finalStr = "";
					for(int i = 0; i < factor; i++)
						finalStr += StringTransformer.getEveryNthChar(gridString, i, factor);
					
					double gridScore = TextFitness.scoreFitnessQuadgrams(gridString, settings.getLanguage());
					String reversed = StringTransformer.reverseString(gridString);
					double gridBackwardsScore = TextFitness.scoreFitnessQuadgrams(reversed, settings.getLanguage());
					double finalScore = TextFitness.scoreFitnessQuadgrams(finalStr, settings.getLanguage());
					
					if(gridScore > finalScore && gridScore > gridBackwardsScore) {
						if(gridScore > bestScore) {
							bestScore = gridScore;
							bestText = gridString;
							output.println(gridString + " --- " + gridScore + " --- read across --- " + type.getDescription());
						}
					}
					else if(finalScore > gridScore && finalScore > gridBackwardsScore) {
					
						if(finalScore > bestScore) {
							bestScore = finalScore;
							bestText = finalStr;
							output.println(finalStr + " --- " + finalScore + " --- read down --- " + type.getDescription());
						}
					}
					else {
						if(gridBackwardsScore > bestScore) {
							bestScore = gridBackwardsScore;
							bestText = reversed;
							output.println(reversed + " --- " + gridBackwardsScore + " --- backwards --- " + type.getDescription());
						}
					}
				}
			}
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog) {
		
	}
	
	public static class RedefenceTask extends InternalDecryption implements RedefenceKey {
		
		public RedefenceTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(int[] order) {
			this.lastSolution = new Solution(Redefence.decode(this.text, order), this.settings.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("Fitness: %f, Rows: %s, Plaintext: %s", this.bestSolution.score, Arrays.toString(order), new String(this.bestSolution.text));	
				this.keyPanel.fitness.setText("" + this.bestSolution.score);
				this.keyPanel.key.setText(Arrays.toString(order));
				UINew.BEST_SOULTION = new String(this.bestSolution.text);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
		}
	}
}
