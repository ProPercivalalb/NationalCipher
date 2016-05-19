package nationalcipher.cipher.stats;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javalibrary.language.ILanguage;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.util.ArrayUtil;

/**
 * @author Alex Barter (10AS)
 */
public class StatCalculator {

	/*                   Index of coincidence calculations                   */
	
	public static double calculateIC(String text) {
		TreeMap<String, Integer> letters = StringAnalyzer.getEmbeddedStrings(text, 1, 1);

		double total = 0.0D;
		
		for(String letter : letters.keySet())
			total += letters.get(letter) * (letters.get(letter) - 1);

		return total / (text.length() * (text.length() - 1));
	}
	
	public static double calculateMaxIC(String text, int minPeriod, int maxPeriod) {
		double maxIC = 0.0D;
		
	    for(int period = minPeriod; period <= maxPeriod; ++period) {
	    	double totalIOC = 0.0D;
	    	
	    	for(int i = 0; i < period; ++i)
		    	totalIOC += StatCalculator.calculateIC(StringTransformer.getEveryNthChar(text, i, period));
	    	
	    	maxIC = Math.max(maxIC, totalIOC / period);
	    }
	    return maxIC;
	}
	
	public static double calculateDiagrahpicIC(String text) {
		TreeMap<String, Integer> letters = StringAnalyzer.getEmbeddedStrings(text, 2, 2);

		double sum = 0.0;
		for(String letter : letters.keySet())
			sum += letters.get(letter) * (letters.get(letter) - 1);
		
		int n = text.length() - 1;
		return sum / (n * (n - 1));
	}
	
	public static double calculateEvenDiagrahpicIC(String text) {
		TreeMap<String, Integer> letters = StringAnalyzer.getEmbeddedStrings(text, 2, 2, false);
		
		double sum = 0.0;
		for(String letter : letters.keySet())
			sum += letters.get(letter) * (letters.get(letter) - 1);
		
		int n = text.length() / 2;
		return sum / (n * (n - 1));
	}
	
	/*                         Friedman's Kappa test                         */
	
	/**
	 * Calculates how many times a letter appears in the same place in two sets of text.
	 * In English for a length of 1000 characters you would expect 66 or 67 coincidences between them.
	 */
	public static double calculateKappaIC(String text, int period) {
		return calculateKappaIC(text, StringTransformer.rotateRight(text, period));
	}
	
	private static double calculateKappaIC(String text1, String text2) {
		double coincidence = 0;
		for(int i = 0; i < text1.length(); ++i)
			if(text1.charAt(i) == text2.charAt(i))
				coincidence += 1;
	    	
	    return coincidence / text1.length();
	}
	
	public static double calculateMaxKappaIC(String text, int minPeriod, int maxPeriod) {
		if(text.length() == 0) return 0.0D;
		
	    double maxKappa = Double.MIN_VALUE;
	    
	    for(int period = minPeriod; period <= maxPeriod; ++period)
	    	maxKappa = Math.max(maxKappa, calculateKappaIC(text, period));
	    
	    return maxKappa;
	}
	
	public static double calculateMinKappaIC(String text, int minPeriod, int maxPeriod) {
	    double minKappa = Double.MAX_VALUE;
	    
	    for(int period = minPeriod; period <= maxPeriod; ++period)
	    	minKappa = Math.min(minKappa, calculateKappaIC(text, period));
	    
	    return minKappa;
	}
	
	public static int calculateBestKappaIC(String text, int minPeriod, int maxPeriod, ILanguage language) {
		int bestPeriod = -1;
	    double bestKappa = Double.MAX_VALUE;
	    
	    for(int period = minPeriod; period <= maxPeriod; ++period) {
	    	double sqDiff = Math.pow(calculateKappaIC(text, period) - language.getNormalCoincidence(), 2);
	    	
	    	if(sqDiff < bestKappa)
	    		bestPeriod = period;
	    	
	    	bestKappa = Math.min(bestKappa, sqDiff);
	    }
	    
	    return bestPeriod;
	}
	

	public static double calculateDiagraphicKappaIC(String text, int period) {
		return calculateDiagraphicKappaIC(text, StringTransformer.rotateRight(text, period));
	}
	
	private static double calculateDiagraphicKappaIC(String text1, String text2) {
		double coincidence = 0;
		 for(int j = 0; j < text1.length(); j += 2)
		       if(text1.substring(j, j + 2).equals(text2.substring(j, j + 2)))
		    	   coincidence += 1;
	    	
	    return coincidence / (text1.length() / 2);
	}
	
	public static double calculateMaxDiagraphicKappaIC(String text, int minPeriod, int maxPeriod) {
	    double maxKappa = Double.MIN_VALUE;
	    
	    for(int period = minPeriod; period <= maxPeriod; ++period)
	    	maxKappa = Math.max(maxKappa, calculateDiagraphicKappaIC(text, period));
	    
	    return maxKappa * 1000.0D;
	}
	
	public static double calculateMinDiagraphicKappaIC(String text, int minPeriod, int maxPeriod) {
	    double minKappa = Double.MAX_VALUE;
	    
	    for(int period = minPeriod; period <= maxPeriod; ++period)
	    	minKappa = Math.min(minKappa, calculateDiagraphicKappaIC(text, period));
	    
	    return minKappa * 1000.0D;
	}
	
	public static int calculateBestDiagraphicKappaIC(String text, int minPeriod, int maxPeriod, ILanguage language) {
		int bestPeriod = -1;
	    double bestKappa = Double.POSITIVE_INFINITY;
	    
	    for(int period = minPeriod; period <= maxPeriod; ++period) {
	    	double sqDiff = Math.pow(calculateDiagraphicKappaIC(text, period) - language.getNormalCoincidence(), 2);
	    	
	    	if(sqDiff < bestKappa)
	    		bestPeriod = period;
	    	
	    	bestKappa = Math.min(bestKappa, sqDiff);
	    }
	    
	    return bestPeriod;
	}
	
	public static double calculateBifidDiagraphicIC(String text, int period) {
		if(period == 0) period = text.length();
		
		Map<String, Integer> theatricalDiagram = new HashMap<String, Integer>();
		int count = 0;
		for(int i = 0; i < text.length(); i += period) {
			int columns = Math.min(period / 2, (text.length() - i) / 2);
			int limit = Math.min(i + period, text.length());
			
			for(int j = i; j < limit - columns; j++) {
				String theatrical = text.charAt(j) + "" + text.charAt(j + columns);
				theatricalDiagram.put(theatrical, 1 + (theatricalDiagram.containsKey(theatrical) ? theatricalDiagram.get(theatrical) : 0));
			}
			count += limit - columns - i;
		}
		
		double sum = 0.0;
		for(String diagram : theatricalDiagram.keySet())
			sum += theatricalDiagram.get(diagram) * (theatricalDiagram.get(diagram) - 1);
		
		return 62500 * sum / (count * (count - 1));
	}
	
	public static double calculateTrifidDiagraphicIC(String text, int period) {
		if(period == 0) period = text.length();
		
		Map<String, Integer> theatricalDiagram = new HashMap<String, Integer>();
		int count = 0;
		for(int i = 0; i < text.length(); i += period) {
			int columns = Math.min(period / 3, (text.length() - i) / 3);
			int limit = Math.min(i + period, text.length());
			
			for(int j = i; j < limit - columns * 2; j++) {
				String theatrical = text.charAt(j) + "" + text.charAt(j + columns) + "" + text.charAt(j + columns * 2);
				theatricalDiagram.put(theatrical, 1 + (theatricalDiagram.containsKey(theatrical) ? theatricalDiagram.get(theatrical) : 0));
			}
			count += limit - columns * 2 - i;
		}
		
		double sum = 0.0;
		for(String diagram : theatricalDiagram.keySet())
			sum += theatricalDiagram.get(diagram) * (theatricalDiagram.get(diagram) - 1);
		
		return 27 * 27 * 2700 * sum / (count * (count - 1));
	}
	

	public static double calculateMaxTrifidDiagraphicIC(String text, int minPeriod, int maxPeriod) {
		if(containsDigit(text))
			return 0.0D;
		
		double bestIC = 0;
	    for(int period = minPeriod; period <= maxPeriod; period++){
	    	if(period == 1) continue;
	    	
	        bestIC = Math.max(bestIC, calculateTrifidDiagraphicIC(text, period));
	    }
	    
	    return bestIC;
	}
	
	public static double calculateMaxBifidDiagraphicIC(String text, int minPeriod, int maxPeriod) {
		if(containsDigit(text) || containsHash(text))
			return 0.0D;
		
		double bestIC = 0;
	    for(int period = minPeriod; period <= maxPeriod; period++){
	    	if(period == 1) continue;
	    	
	        bestIC = Math.max(bestIC, calculateBifidDiagraphicIC(text, period));
	    }
	    
	    return bestIC;
	}
	
	public static int calculateBestBifidDiagraphicIC(String text, int minPeriod, int maxPeriod) {
		if(containsDigit(text) || containsHash(text))
			return -1;
		
		int bestPeriod = -1;
		double bestIC = Double.MIN_VALUE;
	    for(int period = minPeriod; period <= maxPeriod; period++){
	    	if(period == 1) continue;
	    	
	        double score = calculateBifidDiagraphicIC(text, period);
	        
	        if(bestIC < score)
	        	bestPeriod = period;
	        
	        bestIC = Math.max(bestIC, score);
	    }
	    
	    return bestPeriod;
	}
	
	public static double calculateNicodemusIC(String text, int noOfRows, int period) {
		int[][] counts = new int[period][26];

        int blockNo = (int)Math.floor(text.length() / (noOfRows * period));
        if(blockNo == 0) return 0.0D;
        
        int limit = blockNo * noOfRows * period;
		int index = 0;
		for(int i = 0; i < limit; i++) {
			counts[index][text.charAt(i) - 'A'] += 1;
            if((i + 1) % noOfRows == 0)
                index = (index + 1) % period;
		}
		
		double averagedIC = 0.0D;
		for(int i = 0; i < period; i++) {
			double total = 0.0D;
			int count = 0;
			for(int j = 0; j < counts[i].length; j++) {
				total += counts[i][j] * (counts[i][j] - 1);
				count += counts[i][j];
			}
			if(count > 1)
				averagedIC += total / (count * (count - 1));
		}
        
		return averagedIC / period;
	}
	
	public static double calculateMaxNicodemusIC(String text, int minPeriod, int maxPeriod) {
		if(containsDigit(text) || containsHash(text))
			return 0.0D;
		
		double maxIC = Double.NEGATIVE_INFINITY;
		
		for(int period = minPeriod; period <= maxPeriod; period++)
			maxIC = Math.max(maxIC, calculateNicodemusIC(text, 5, period));
		
	    return 1000.0D * maxIC;
	}
	
	public static int calculateBestNicodemusIC(String text, int minPeriod, int maxPeriod, ILanguage language) {
		if(containsDigit(text) || containsHash(text))
			return -1;
		
		int bestPeriod = -1;
	    double bestIC = Double.POSITIVE_INFINITY;
	    
	    for(int period = minPeriod; period <= maxPeriod; ++period) {
	    	double sqDiff = Math.pow(calculateNicodemusIC(text, 5, period) - language.getNormalCoincidence(), 2);
	    	
	    	if(sqDiff < bestIC)
	    		bestPeriod = period;
	    	
	    	bestIC = Math.min(bestIC, sqDiff);
	    }
		
	    return bestPeriod;
	}
	
	//public static double calculatePortaIC(String text, int period) {
	//	
	//}
	
	/*                              Normor test                              */
	
	public static double calculateNormalOrder(String text, ILanguage language) {
		if(containsDigit(text) || containsHash(text))
			return 0.0D;

		List<Character> normalLetterOrder = language.getLetterLargestFirst();
		
		List<String> textLetterOrder = StringAnalyzer.orderByCount(StringAnalyzer.getEmbeddedStrings(text, 1, 1));
		
		double total = 0.0D;
		for(int i = 0; i < normalLetterOrder.size(); i++) {
			String target = "" + normalLetterOrder.get(i);
			
			if(textLetterOrder.contains(target))
				total += Math.abs(i - textLetterOrder.indexOf(target));
			else
				total += i;
		}
		
		return total;
	}
	
	public static double calculateLR(String text) {
		int count = 0;
		for(int i = 0; i < text.length(); i++) {
			for(int j = i + 1 ; j < text.length(); j++) {
				int n = 0;
				while(j + n < text.length() && text.charAt(i + n) == text.charAt(j + n))
					n++;
				
				if(n == 3)
					count += 1;
			}
		}

		return Math.sqrt(count) / text.length() * 1000.0D;
	}
	
	public static double calculateROD(String text) {
		int sumAll = 0;
		int sumOdd = 0;
		for(int i = 0; i < text.length(); i++) {
			for(int j = i + 1; j < text.length(); j++) {
				int n = 0;
				while(j + n < text.length() && text.charAt(i + n) == text.charAt(j + n))
					n++;
				
				if(n > 1) {
					sumAll++;
					if(((j - i) & 1) == 1)
						sumOdd++;
				}
			}
		}

		if(sumAll == 0)
			return 0.0D;
		
		return 100 * sumOdd / sumAll;
	}
	
	public static double calculateLDI(String text) {
		double score = 0;
		for(int i = 0; i < text.length() - 1; i++)
			if(Character.isLetter(text.charAt(i)) && Character.isLetter(text.charAt(i + 1)))
				score += logdi[text.charAt(i) - 'A'][text.charAt(i + 1) - 'A'];
		
		return score * 100 / (text.length() - 1);
	}
	
	public static double calculateSDD(String text) {
		double score = 0;
		
		for(int i = 0; i < text.length() - 1; i++) 
			if(Character.isLetter(text.charAt(i)) && Character.isLetter(text.charAt(i + 1)))
				score += sdd[text.charAt(i) - 'A'][text.charAt(i + 1) - 'A'];
		
		return score * 100 / (text.length() - 1);
	}

	public static boolean isLengthDivisible2(String text) {
		return text.length() % 2 == 0;
	}
	
	public static boolean isLengthDivisible3(String text) {
		return text.length() % 3 == 0;
	}
	
	public static boolean isLengthDivisible5(String text) {
		return text.length() % 5 == 0;
	}
	
	public static boolean isLengthDivisible25(String text) {
		return text.length() % 25 == 0;
	}
	
	public static boolean isLengthDivisible4_15(String text) {
		for(int n = 4; n <= 15; n++) 
			if(text.length() % n == 0)
				return true;
		
		return false;
	}
	
	public static boolean isLengthDivisible4_30(String text) {
		for(int n = 4; n <= 30; n++) 
			if(text.length() % n == 0)
				return true;
		
		return false;
	}
	
	public static boolean isLengthPerfectSquare(String text) {
		int n = (int)Math.floor(Math.sqrt(text.length()));
		return Math.pow(n, 2) == text.length();
	}
	
	public static boolean containsLetter(String text) {
		for(int i = 0; i < text.length(); i++)
	        if('A' <= text.charAt(i) && text.charAt(i) <= 'Z')
	        	return true;
	    return false;
	}
	
	public static boolean containsDigit(String text) {
		for(int i = 0; i < text.length(); i++)
	        if('0' <= text.charAt(i) && text.charAt(i) <= '9')
	        	return true;
	    return false;
	}

	public static boolean containsJ(String text) {
		return text.contains("J");
	}
	
	public static boolean containsHash(String text) {
		return text.contains("#");
	}

	public static boolean calculateDBL(String text) {
		if(text.length() % 2 == 0)
	        for(int i = 0; i < text.length(); i += 2)
	            if (text.charAt(i) == text.charAt(i + 1))
	            	return true;
		return false;
	}
	

	public static double calculateALDI(String text) {
		if(containsDigit(text) || containsHash(text))
			return 0.0D;
		
	    List<Integer> xlate_indices = Arrays.asList(4,4,1,2,0,0,0,0);
		
	    int largestSum = Integer.MIN_VALUE;
		
		for(int type = 4; type <= 7; type += 1) {
			for(int period = 3; period <= 15; period++) {
	        	double sum = 0.0D;
	            for(int col = 0; col < period; col++) 
	            	sum += best_di(col, type, period, text);
	            
	                sum /= period;
	                int n = (int)Math.floor(sum);
	                if (n > largestSum)
	                	largestSum = n;
	         }
		}

		
		return largestSum;
	}

	public static double best_di(int col, int type, int period, String text){
	
		int best_score = 0;
		int rows = (int)Math.floor(text.length() / period);
	    for (int kl = 0;kl < 26;kl++) {
	    	for (int kr = 0; kr < 26;kr++) {
	    		int score = 0;
	            int ct = 0;
	            int kl1 = kl;
	            int kr1 = kr;
			    for (int j = 0; j < rows; j++) {
			    	if(col + j * period + 1 >= text.length())
			    		break;
	                int cl = text.charAt(col + j * period) - 'A';
	                int cr = text.charAt(col + j * period + 1) - 'A';
	                int pl = decodeLet(cl, kl1, type);
	                int pr = decodeLet(cr, kr1, type);
	                score += logdi[pl][pr];
	                ct++;
	                if (type <= 7 // PAUTOKEY 
	                	&& type >= 4 ){ //VAUTOKEY) 
	                	kl1 = pl;
	                	kr1 = pr;
	                }
	            }
			    if(ct == 0)
			    	return 0.0D;
			    
	            score *= 100;
	            score /= ct;
	            best_score = Math.max(best_score, score);
	    	}
	    }
	    return best_score;
    }
	
	/**
	 * @param type Can either be 0 (VSlidefair) or 1 (BSlidefair)
	 */
	public static double best_sldi(int col, int type, int period, String text){
		int best_score = 0;
		int rows = (int)Math.floor(text.length() / (2 * period));
		int rowb = 2 * col;
		for(int k = 0; k < 26; k++) {
			int score = 0;
		    int ct = 0;
		    for(int j = 0; j < rows; j++) {
		    	int posn = j * period * 2 + rowb;
		        if(posn + 1 >= text.length())
		        	break;
		         int cl = text.charAt(posn) - 'A';
		         int cr = text.charAt(posn + 1) - 'A';
		         int [] result = decodeSL(cl, cr, k, type);
		         int pl = result[0];
		         int pr = result[1];
		         score += logdi[pl][pr];
		         ct++;
		    }
		    
		    if(ct == 0)
		    	return 0.0D;
		    score *= 100;
		    score /= ct;
		    
		    best_score = Math.max(best_score, score);
		}
		return best_score ;
	}
	
	/**
	 * @param type Can either be 0 (Vigenere), 1 (Variant), 2 (Beaufort), 3 (Porta), 4 (VAutokey), 
	 * 							 	5 (BAutokey), 6 (VEAutokey), 7 (PAutokey)
	 */
	public static int decodeLet(int ct, int key, int type){
        int cp = 0;

        switch(type) {
        case 0: // VIGENERE
        case 6: //VEAUTOKEY
        	cp = (26 + ct - key) % 26;
            break;
        case 1: //VARIANT
        case 4: //VAUTOKEY
        	cp = (ct + key) % 26;
            break;
        case 2: //BEAUFORT
        case 5: // BAUTOKEY
        	cp = (26 + key - ct) % 26;
            break;
        default: /* must be porta */
        	key = (int)Math.floor(key / 2);
            cp = ct;
            if (cp < 13) {
            	cp += key;
                if (cp < 13)
                	cp += 13;
            }
            else {
            	cp -= key;
                if (cp > 12)
                	cp -= 13;
            }
        }
        return cp;
	}
	
	public static int[] decodeSL(int cl, int cr, int key, int ciph_type){
		int pl = 0;
		int pr = 0;

        if(ciph_type == 1) { //BSLIDEFAIR
        	pl = (26 + key - cr) % 26;
        	pr = (26 + key - cl) % 26;
        }
        else {
        	pl = (26 + cr - key) % 26;
        	pr = (cl + key) % 26;
        }
        return new int[] {pl, pr};
	}
	
	public static double calculateBLDI(String text) {
		if(containsDigit(text) || containsHash(text))
			return 0.0D;
		
		 int largestSum = Integer.MIN_VALUE;

		 for(int period = 3; period <= 15; period++) {
			 double sum = 0.0D;
			 for(int col = 0; col < period; col++) 
				 sum += best_di(col, 2, period, text);
		            
		     sum /= period;
		     int n = (int)Math.floor(sum);
		     largestSum = Math.max(largestSum, n);
		 }

			
		return largestSum;
	}

	public static double calculatePLDI(String text) {
		if(containsDigit(text) || containsHash(text))
			return 0.0D;
		
		int largestSum = Integer.MIN_VALUE;

		for(int period = 3; period <= 15; period++) {
	    	double sum = 0.0D;
	        	
	    	for(int col = 0; col < period; col++) 
	        	sum += best_di(col, 3, period, text);
	            
	    	sum /= period;
	        int n = (int)Math.floor(sum);
	        largestSum = Math.max(largestSum, n);
		}
		
		return largestSum;
	}

	public static int calculateSLDI(String text) {
		if(containsDigit(text) || containsHash(text) || text.length() % 2 == 1)
			return 0;
		
		int largestSum = Integer.MIN_VALUE;
		
		for(int type = 0; type <= 1; type += 1) {
			for(int period = 3; period <= 15; period++) {
	        	double sum = 0.0D;
	        	
	            for(int col = 0; col < period; col++) 
	                sum += best_sldi(col, type, period, text);
	            
	            sum /= period;
	            int n = (int)Math.floor(sum);
	            largestSum = Math.max(largestSum, n);
	         }
		}
		
		return largestSum;
	}

	public static double calculateVLDI(String text) {
		if(containsDigit(text) || containsHash(text))
			return 0.0D;
		
		int largestSum = Integer.MIN_VALUE;
		
		for(int type = 0; type <= 1; type++) {
			for(int period = 3; period <= 15; period++) {
	        	double sum = 0.0D;
	            for(int col = 0; col < period; col++) 
	            	sum += best_di(col, type, period, text);
	            
	            sum /= period;
	            int n = (int)Math.floor(sum);
	            largestSum = Math.max(largestSum, n);
	         }
		}
		
		return largestSum;
	}
	
	public static int calculateRDI(String text) {
		if(containsDigit(text) || containsHash(text) || text.length() % 2 == 1)
			return 0; 
		
		int n = 0;
		double score = 0;
		for(int i = 0; i < text.length(); i += 2) {
			if(Character.isLetter(text.charAt(i + 1)) && Character.isLetter(text.charAt(i))) {
				score += logdi[text.charAt(i + 1) - 'A'][text.charAt(i) - 'A'];
				n += 1;
			}
		}
		
		return (int)Math.floor(score * 100 / n);
	}
	
	public static double calculatePTX(String text) {    
	    if(containsDigit(text) || containsHash(text) || text.length() % 2 == 1)
			return 0; 

	    double best_score = 0.0D;
		for(int period = 3; period <= 15; period++) {
	        int big_step = 2 * period;
	        int count = 0;
	        int score = 0;
	        for(int j = 0; j < text.length(); j += big_step) {
	        	for(int k = 0;k < period; k++) {
	                if(j + k + period >= text.length()) 
	                	break;
	        		int c1 = text.charAt(j + k) - 'A';
	                int c2 = text.charAt(j + k + period) - 'A';
	                int[] result = decodePair(k, c1, c2);
	                if (result[0] == 1) {
	                	int c3 = result[1];
	                	int c4 = result[2];
	                    score += logdi[c3][c4];
	                    count++;
	                }
	        	}
	        }
	        score *= 100;
	        if(count == 0)
	        	break;
	        score /= count;
	        System.out.println(period + " " + score);
	        best_score = Math.max(best_score, score);
	    }
	    return Math.floor(best_score);
	}
	
	private static int[] decodePair(int k, int c1, int c2) {
		int t_flag = 0;
		int b_flag = 0;
        if (c1 < 13) 
        	t_flag = 0;
        else 
        	t_flag = 2;
        if (c2 % 2 == 1) 
        	b_flag = 1;
        else 
        	b_flag = 0;
        int[] rvalue = new int[] {0,0,0};
        int sum = t_flag + b_flag;
        if(sum == 2)
			if(c1 - 13 != (c2 >> 1))
				rvalue = new int[] {1, (c2 >> 1) + 13, (c1 - 13) << 1};
        if(sum == 3)
			if(c1 - 13 != (c2 >> 1))
				rvalue = new int[] {1, (c2 >> 1) + 13, ((c1 - 13) << 1 ) + 1};
        return rvalue;
	} 
	
	public static double calculatePHIC(String text) {
	    int col_len = 5;
	    List<Integer> combine_alpha = Arrays.asList(0,1,2,3,0,4,5,1);

	    if(containsDigit(text) || containsHash(text))
			return 0.0D; 

	    int period = 8;
	    int[][] ct = new int[period - 2][26];
	    
		for(int i = 0; i < period - 2; i++){ // combine 2 of the alphbets so 6 altogether
			Arrays.fill(ct[i], 0);
	    }
	    int block_len = (int)Math.floor(text.length() / (col_len * period));
	    if (block_len == 0)
	    	return 0.0D; 
	        
	    int limit = block_len * col_len * period; // round off to nearest multiple of period*5
		int index = 0;
		for(int i = 0; i < limit; i++) {
			ct[combine_alpha.get(index)][text.charAt(i) - 'A'] += 1;
	        if((i + 1) % col_len == 0)
	        	index = (index + 1) % period;
		}
		double z = 0.0D;
		for(int i = 0; i < period - 2; i++) {
			double x = 0.0D, y = 0.0D;
			for(int j = 0; j < 26; j++) {
				x += ct[i][j] * (ct[i][j] - 1);
				y += ct[i][j];
			}
			if(y > 1) 
				z += x / (y * (y - 1));
		}
		z = z / (period - 2);
	    return Math.floor(1000.0D * z);
	}
	
	public static boolean calculateHAS0(String text) {
		if(containsLetter(text) || containsHash(text))
			return false;
		
		return text.contains("0");
	}
	
	public static double calculateCDD(String text) {
		if(containsDigit(text) || containsHash(text))
			return 0.0D;
		
        double best_score = 0.0D;
        for(int key_len = 4; key_len <= 15; key_len++){
            int numb_long_cols = text.length() % key_len;
            int numb_short_cols = key_len - numb_long_cols;
            
            int numb_rows = (int)Math.floor(text.length() / key_len);
            
            int[] min_start = new int[key_len];
            int[] max_start = new int[key_len];
            int[] max_diff = new int[key_len];
            int[] col_pos = new int[key_len];
            int[] col_array = new int[key_len];
            boolean[] cols_in_use = new boolean[key_len];
            Arrays.fill(cols_in_use, false);
            int[] diff_array = new int[key_len];
            min_start[0] = 0;
            int n = 0;
            for (int j = 1; j< key_len; j++) {
                if (n < numb_short_cols) {
                    min_start[j] = min_start[j - 1] + numb_rows;
                    n++;
                }
                else {
                    min_start[j] = min_start[j - 1] + numb_rows + 1;
                }
            }
            max_start[0] = max_diff[0] = 0;
            n = 0;
            for (int j = 1; j < key_len; j++) {
                if(n < numb_long_cols) {
                    max_start[j] = max_start[j - 1]+numb_rows + 1;
                    n++;
                }
                else {
                    max_start[j] = max_start[j - 1]+numb_rows;
                }
                max_diff[j] = max_start[j] - min_start[j];
            }
            
            	
            for(int j = 0; j< key_len; j++)
                col_pos[j] = min_start[j];
            
            for(int t0 = 0; t0 < key_len; t0++) {
                col_array[0] = t0;
                cols_in_use[t0] = true;
                int long_corr = 0;
                
                if(0 < numb_long_cols && t0 >= numb_long_cols)
                    long_corr = 1;
  
                for(int current_dif = 0; current_dif <= max_diff[t0] - long_corr; current_dif++) {	
                    diff_array[0] = current_dif;
                    int index = 1;
                    for(int j = 0;j<key_len;j++)
                        if(!cols_in_use[j])
                            col_array[index++] = j;
                    double score = 0;
                    for(int j = 1; j < key_len; j++) {
                    	try {
	                    	int[] tn = getBestDI(text, j, key_len, numb_long_cols, numb_short_cols, numb_rows, col_array, max_diff, col_pos, diff_array);
	                        score += tn[0];
	                        int swap = col_array[tn[1]];
	                        col_array[tn[1]] = col_array[j];
	                        col_array[j] = swap;
	                        diff_array[j] = tn[2];
                    	}
                    	catch(Exception e) {
                    		return 0D;
                    	}
                    }
                    score = 100 * score / (numb_rows * (key_len - 1));
                    best_score = Math.max(best_score, score);
                }
                cols_in_use[t0] = false;
            }
        }
        return Math.floor(best_score);
	}
	
	public static int[] getBestDI(String text, int col, int key_len, int numb_long_cols, int numb_short_cols, int numb_rows, int[] col_array, int[] max_diff, int[] col_pos, int[] diff_array) {
		int max = 0;
		int next_col = 0;
	    int next_dif = 0;
		
	    for(int j= col; j < key_len;j++) {
	    	int long_corr = 0, short_corr = 0;
	    	if (col >= numb_long_cols && col_array[j] >= numb_short_cols)
	    		short_corr = 1;
	    	else if (col < numb_long_cols && col_array[j] >= numb_long_cols)
	    		long_corr = 1;	
	    	for(int dif = short_corr; dif <= max_diff[col_array[j]] - long_corr; dif++) {
	    		int sum = 0;
	    		for(int k = 0; k < numb_rows; k++) 
	    			sum += sdd[text.charAt(col_pos[col_array[col - 1]] + k + diff_array[col - 1]) - 'A'][text.charAt(col_pos[col_array[j]] + k + dif) - 'A'];		
	            
	    		if(sum > max) {
	                max = sum;
	                next_col = j;
	                next_dif = dif;
	            }
	        }
	    }
	    return new int[] {max, next_col, next_dif};
	}
	
	public static double calculateSSTD(String text) {
		if(containsDigit(text) || containsHash(text))
			return 0.0D;
		
		int best_score = 0;
        for(int period = 4; period <= 8; period++){
            if(text.length() % period != 0) 
            	continue;
            
            if (3 * period * period > text.length()) 
            	break;
            
            int result = swagTest(text, period);
            System.out.println(period + " " + result);
            if (result > best_score)
                best_score = result;
        }

        
        return best_score;
	}
	
	public static int swagTest(String text, int period) {

		int numb_columns = text.length() / period;
	        
	    int[] row_order = ArrayUtil.range(0, period);
	    int[][] swag_array = new int[period][numb_columns];
	    int index = 0, i = 0;
	        
	    for(int j = 0; j < text.length(); j++){
	    	int c = text.charAt(j) - 'A';
	        swag_array[i][index] = c;
	        i += 1;
	        if(i == period) {
	            index += 1;
	            i = 0;
	        }
	    }
	    int[] row = construct_row(row_order, swag_array, period, numb_columns);
	    double score = score_row(row);
	    double best_score = score;
	    int[] best_row = row;
	        
	    while(true) {
	    	Object[] per = next_per(row_order, row_order.length);
	    	row_order = (int[])per[1];
	        	
	        if((int)per[0] == 0)
	        	break;
	        	
	        row = construct_row(row_order, swag_array, period, numb_columns);
	        score = score_row(row);
	        if(score > best_score) { 
	            best_score = score;
	            best_row = row;
	        }
	    }
	    return (int)Math.floor(100.0D * best_score / (numb_columns - 2));
	}
	
	public static int[] construct_row(int[] row_order, int[][] swag_array, int period, int numb_columns){
		int[] row = new int[numb_columns];
		int index = 0;
        for(int i = 0; i < numb_columns; i++){
			int c = swag_array[row_order[index]][i];
            row[i] = c;
			index += 1;
			if (index == period) 
				index = 0;
        }
		return row;
    }
	
	public static int score_row(int[] row){
		int score = 0;
        for(int i = 0; i < row.length - 2; i++)
			score += bstd[row[i] + 26 * row[i + 1] + 26 * 26 * row[i + 2]];
		return score;
    }
	
	public static Object[] next_per(int[] str, int le) {
		if(le < 2) 
			return new Object[] {0, str};
		
		int last = le - 2;
		while (str[last] >= str[last + 1]){
			if(last == 0) 
				return new Object[] {0, str};
			last -= 1;
	    }
		int fst = le - 1;
		while(str[fst] <= str[last])
			fst -= 1;

		int c = str[last];
		str[last] = str[fst];
		str[fst] = c;

		if (str[last + 1] != str[le - 1]){
			int i = 1;
			while(last + i < le - i){
				c = str[last + i];
				str[last + i] = str[le - i];
				str[le - i] = c;
				i += 1;
	        }
	    }
		return new Object[] {1, str};
	}
	


	public static double calculateMPIC(String text) {
	    if(containsDigit(text) || containsHash(text))
			return 0.0D;

		double mx = 0.0D;
	    
		int max_period = 15;
		int[][] ct = new int[max_period + 1][26];
		for(int i = 0;i <= max_period; i++)
			Arrays.fill(ct[i], 0);
		
		for(int period = 1; period <= max_period; period++) {	
			for(int prog_index = 1; prog_index < 26; prog_index++){
				
				for(int i = 0; i < period; i++)
					Arrays.fill(ct[i], 0);
				
				int index = 0;
				int prog_incr = 0;
				for(int i = 0; i < text.length(); i++) {
					int c = (26 + text.charAt(i) - 'A' - prog_incr) % 26;
					ct[index][c] += 1;
					if(++index == period){
						index = 0;
						prog_incr = (prog_incr + prog_index)%26;
					}
				}
				
				double z = 0.0D;
				for (int i = 0;i < period; i++) {
					double x = 0.0D, y = 0.0D;
					for (int j = 0; j < 26; j++) {
						x += ct[i][j] * (ct[i][j] - 1);
						y += ct[i][j];
					}
					if (y > 1) 
						z += x / (y * (y - 1));
				}
				z = z / period;
				if(z > mx)
					mx = z;
			} 
		}
		return(Math.floor(1000.0 * mx));
	}
	
	public static boolean calculateSeriatedPlayfair(String text) {
		if(text.length() % 2 == 1)
	        return false;
		
		int final_period = 15;
		if(final_period > 10)
			final_period = 10;
		
		for(int period = 3; period <= final_period; period++) {
			int x = (int)Math.floor(text.length() / (2 * period));
			int left_over = text.length() - x * 2 * period;
			boolean flag = true;
			int pos = 0;
			while(pos < text.length() - left_over) {
	            for(int j = 0; j < period; j++) {
	                if(text.charAt(pos + j) == text.charAt(pos + j + period)) {
	                    flag = false;
	                    break;
	                }
	            }
	            pos += 2 * period;
			}
			if(left_over == 1) {
				pos = text.length() - left_over;
				for(int j = 0; j < Math.floor(left_over / 2); j++){
		      		if(text.charAt(pos + j) == text.charAt(pos + j + left_over / 2)) {
						flag = false;
						break;
					}
				}
			}

			if (flag)
	            return true;
	    }
	    return false;
	}
	

	static int[][] logdi = new int[][] {
			{4,7,8,7,4,6,7,5,7,3,6,8,7,9,3,7,3,9,8,9,6,7,6,5,7,4},
			{7,4,2,0,8,1,1,1,6,3,0,7,2,1,7,1,0,6,5,3,7,1,2,0,6,0},
			{8,2,5,2,7,3,2,8,7,2,7,6,2,1,8,2,2,6,4,7,6,1,3,0,4,0},
			{7,6,5,6,8,6,5,5,8,4,3,6,6,5,7,5,3,6,7,7,6,5,6,0,6,2},
			{9,7,8,8,8,7,6,6,7,4,5,8,7,9,7,7,5,9,9,8,5,7,7,6,7,3},
			{7,4,5,3,7,6,4,4,7,2,2,6,5,3,8,4,0,7,5,7,6,2,4,0,5,0},
			{7,5,5,4,7,5,5,7,7,3,2,6,5,5,7,5,2,7,6,6,6,3,5,0,5,1},
			{8,5,4,4,9,4,3,4,8,3,1,5,5,4,8,4,2,6,5,7,6,2,5,0,5,0},
			{7,5,8,7,7,7,7,4,4,2,5,8,7,9,7,6,4,7,8,8,4,7,3,5,0,5},
			{5,0,0,0,4,0,0,0,3,0,0,0,0,0,5,0,0,0,0,0,6,0,0,0,0,0},
			{5,4,3,2,7,4,2,4,6,2,2,4,3,6,5,3,1,3,6,5,3,0,4,0,5,0},
			{8,5,5,7,8,5,4,4,8,2,5,8,5,4,8,5,2,4,6,6,6,5,5,0,7,1},
			{8,6,4,3,8,4,2,4,7,1,0,4,6,4,7,6,1,3,6,5,6,1,4,0,6,0},
			{8,6,7,8,8,6,9,6,8,4,6,6,5,6,8,5,3,5,8,9,6,5,6,3,6,2},
			{6,6,7,7,6,8,6,6,6,3,6,7,8,9,7,7,3,9,7,8,9,6,8,4,5,3},
			{7,3,3,3,7,3,2,6,7,2,1,7,3,2,7,6,0,7,6,6,6,0,3,0,4,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,0,0,0},
			{8,6,6,7,9,6,6,5,8,3,6,6,6,6,8,6,3,6,8,8,6,5,6,0,7,1},
			{8,6,7,6,8,6,5,7,8,4,6,6,6,6,8,7,4,5,8,9,7,4,7,0,6,2},
			{8,6,6,5,8,6,5,9,8,3,3,6,6,5,9,6,2,7,8,8,7,4,7,0,7,2},
			{6,6,7,6,6,4,6,4,6,2,3,7,7,8,5,6,0,8,8,8,3,3,4,3,4,3},
			{6,1,0,0,8,0,0,0,7,0,0,0,0,0,5,0,0,0,1,0,2,1,0,0,3,0},
			{7,3,3,4,7,3,2,8,7,2,2,4,4,6,7,3,0,5,5,5,2,1,4,0,3,1},
			{4,1,4,2,4,2,0,3,5,1,0,1,1,0,3,5,0,1,2,5,2,0,2,2,3,0},
			{6,6,6,6,6,6,5,5,6,3,3,5,6,5,8,6,3,5,7,6,4,3,6,2,4,2},
			{4,0,0,0,5,0,0,0,3,0,0,2,0,0,3,0,0,0,1,0,2,0,0,0,4,4}};

	static int[][] sdd = new int[][] {
			{0,3,4,2,0,0,1,0,0,0,4,5,2,6,0,2,0,4,4,3,0,6,0,0,3,5},
			{0,0,0,0,6,0,0,0,0,9,0,7,0,0,0,0,0,0,0,0,7,0,0,0,7,0},
			{3,0,0,0,2,0,0,6,0,0,8,0,0,0,6,0,5,0,0,0,3,0,0,0,0,0},
			{1,6,0,0,1,0,0,0,4,4,0,0,0,0,0,0,0,0,0,1,0,0,4,0,1,0},
			{0,0,4,5,0,0,0,0,0,3,0,0,3,2,0,3,6,5,4,0,0,4,3,8,0,0},
			{3,0,0,0,0,5,0,0,2,1,0,0,0,0,5,0,0,2,0,4,1,0,0,0,0,0},
			{2,0,0,0,1,0,0,6,1,0,0,0,0,0,2,0,0,1,0,0,2,0,0,0,0,0},
			{5,0,0,0,7,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,5,0,0,0,4,0,0,0,1,1,3,7,0,0,0,0,5,3,0,5,0,0,0,8},
			{0,0,0,0,6,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,9,0,0,0,0,0},
			{0,0,0,0,6,0,0,0,5,0,0,0,0,4,0,0,0,0,0,0,0,0,1,0,0,0},
			{2,0,0,4,2,0,0,0,3,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,7,0},
			{5,5,0,0,5,0,0,0,2,0,0,0,0,0,2,6,0,0,0,0,2,0,0,0,6,0},
			{0,0,4,7,0,0,8,0,0,2,2,0,0,0,0,0,3,0,0,4,0,0,0,0,0,0},
			{0,2,0,0,0,8,0,0,0,0,4,0,5,5,0,2,0,4,0,0,7,4,5,0,0,0},
			{3,0,0,0,3,0,0,0,0,0,0,5,0,0,5,7,0,6,0,0,3,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0},
			{1,0,0,0,4,0,0,0,2,0,4,0,0,0,2,0,0,0,0,0,0,0,0,0,5,0},
			{1,1,0,0,0,0,0,1,2,0,0,0,0,0,1,4,4,0,1,4,2,0,4,0,0,0},
			{0,0,0,0,0,0,0,8,3,0,0,0,0,0,3,0,0,0,0,0,0,0,2,0,0,0},
			{0,4,3,0,0,0,5,0,0,0,0,6,2,3,0,6,0,6,5,3,0,0,0,0,0,6},
			{0,0,0,0,8,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{6,0,0,0,2,0,0,6,6,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0},
			{3,0,7,0,1,0,0,0,2,0,0,0,0,0,0,9,0,0,0,5,0,0,0,6,0,0},
			{1,6,2,0,0,2,0,0,0,6,0,0,2,0,6,2,1,0,2,1,0,0,6,0,0,0},
			{2,0,0,0,8,0,0,0,0,6,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,9}};
	

	public static int[] bstd = new int[17580];
}