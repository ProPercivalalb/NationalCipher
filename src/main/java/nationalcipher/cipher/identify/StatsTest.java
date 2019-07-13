package nationalcipher.cipher.identify;

import javalibrary.language.ILanguage;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.stats.StatCalculator;

public class StatsTest {

	public static double calculateSubTypeLDI(String text, VigenereType type, ILanguage language) {
		if(StatCalculator.containsDigit(text) || StatCalculator.containsHash(text))
			return 0.0D;
		
	    double largestSum = 0.0D;
		
		for(int period = 2; period <= 15; period++) {
	        double sum = 0.0D;
	        for(int col = 0; col < period; col++) 
	        	sum += best_di(text, period, col, type, language);
	        
	        sum /= period;
	        	largestSum = Math.min(largestSum, sum);
		}

		return largestSum;
	}
	
	public static double best_di(String text, int period, int col, VigenereType type, ILanguage language) {
		double bestScore = 0.0D;
		
		int rows = (int)Math.ceil(text.length() / period);
	    for(char keyLeft = 'A'; keyLeft <= 'Z'; keyLeft++) {
	    	for(char keyRight = 'A'; keyRight <= 'Z'; keyRight++) {
	    		double score = 0.0D;
	            int ct = 0;
	            char kl1 = keyLeft;
	            char kr1 = keyRight;
			    for(int r = 0; r < rows; r++) {
			    	if(col + r * period + 1 >= text.length()) break;

			    	char pl = type.decode(text.charAt(col + r * period), kl1);
	                char pr = type.decode(text.charAt(col + r * period + 1), kr1);
	                score += language.getDiagramData().maxValue - language.getDiagramData().getValue(new char[] {pl, pr}, 0);
	                ct++;
	            }
			    if(ct == 0)
			    	return 0.0D;
			    
	            score *= 100;
	            score /= ct;
	            if(score < bestScore)
	            	bestScore = score;
	    	}
	    }
	    return bestScore;
    }
}
