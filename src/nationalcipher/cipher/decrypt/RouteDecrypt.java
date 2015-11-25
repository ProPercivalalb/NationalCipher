package nationalcipher.cipher.decrypt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.wip.Routes;
import javalibrary.cipher.wip.Routes.RouteCipherType;
import javalibrary.fitness.TextFitness;
import javalibrary.math.MathHelper;
import javalibrary.string.StringTransformer;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;

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
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
}
