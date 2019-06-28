package nationalcipher.cipher.identify;

import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.stats.StatCalculator;

public class PolyalphabeticIdentifier {
	
	//?!?!? IMPORTANT ?!?!? These functions only check between period 3 and 15
	
	public static double calculateAutokeyVigenereLDI(String text) {
		return calculateSubTypeLDI(text, VigenereType.VIGENERE, true);
	}
	
	public static double calculateAutokeyPortaLDI(String text) {
		return calculateSubTypeLDI(text, VigenereType.PORTA, true);
	}
	
	public static double calculateAutokeyBeaufortLDI(String text) {
		return calculateSubTypeLDI(text, VigenereType.BEAUFORT, true);
	}
	
	public static double calculateAutokeyVariantLDI(String text) {
		return calculateSubTypeLDI(text, VigenereType.VARIANT, true);
	}

	public static double calculateBeaufortLDI(String text) {
		return calculateSubTypeLDI(text, VigenereType.BEAUFORT, false);
	}
	
	public static double calculatePortaLDI(String text) {
		return calculateSubTypeLDI(text, VigenereType.PORTA, false);
	}
	
	public static double calculateVigenereLDI(String text) {
		return calculateSubTypeLDI(text, VigenereType.VIGENERE, false);
	}
	
	public static double calculateVariantLDI(String text) {
		return calculateSubTypeLDI(text, VigenereType.VARIANT, false);
	}
	
	public static double calculateSlidefairBeaufortLDI(String text) {
		return calculateSubTypeSLDI(text, VigenereType.BEAUFORT);
	}

	public static double calculateSlidefairVariantLDI(String text) {
		return calculateSubTypeSLDI(text, VigenereType.VARIANT);
	}
	
	public static double calculateSlidefairVigenereLDI(String text) {
		return calculateSubTypeSLDI(text, VigenereType.VIGENERE);
	}
	
	public static double calculateLDI(String text) {
	    if(text.length() < 2) return 0.0D;
	    
		int score = 0;
		for(int i = 0; i < text.length() - 1; i++)
			if(Character.isLetter(text.charAt(i)) && Character.isLetter(text.charAt(i + 1)))
				score += logdi[text.charAt(i) - 'A'][text.charAt(i + 1) - 'A'];
		
		return score * 100 / (text.length() - 1);
	}
	
	public static double calculateCaesarLDI(String text) {
		if(StatCalculator.containsDigit(text) || StatCalculator.containsHash(text))
			return 0.0D;
		
		double scoreLargest = Double.MIN_VALUE;
		
		for(int shift = 1; shift <= 25; shift++) {
			double score = 0.0D;
			for(int i = 0; i < text.length() - 1; i++)
			    score += logdi[(26 + text.charAt(i) - shift - 'A') % 26][(26 + text.charAt(i + 1) - shift - 'A') % 26];

			scoreLargest = Math.max(scoreLargest, score);
		}
		
		return scoreLargest * 100 / (text.length() - 1);
	}
	
	public static double calculateAffineLDI(String text) {
		if(StatCalculator.containsDigit(text) || StatCalculator.containsHash(text))
			return 0.0D;
		
		double scoreLargest = Double.MIN_VALUE;
		
		for(int a : new int[] {3,5,7,9,11,15,17,19,21,23,25}) {
  			for(int b = 0; b < 26; b++) {
  				
				double score = 0.0D;
				for(int i = 0; i < text.length() - 1; i++)
				    score += logdi[(((25 - b) + (text.charAt(i) - 'A')) * a) % 26][(((25 - b) + (text.charAt(i + 1) - 'A')) * a) % 26];

				scoreLargest = Math.max(scoreLargest, score);
  			}
		}
		
		return scoreLargest * 100 / (text.length() - 1);
	}
	
	public static double calculateSubTypeLDI(String text, VigenereType type, boolean autokey) {
		if(StatCalculator.containsDigit(text) || StatCalculator.containsHash(text))
			return 0.0D;
		
	    double largestSum = Double.MIN_VALUE;
		char[] charArray = text.toCharArray();
	    
		for(int period = 2; period <= 15; period++) {
	        double sum = 0.0D;
	        int rows = (int)Math.ceil(charArray.length / (double)period);
	        
	        for(int col = 0; col < period; col++)
	        	sum += getBestVigenereDigramScore(charArray, col, type, rows, period, autokey);
	            
	        sum /= period;
	        largestSum = Math.max(largestSum, sum);
		}

		return largestSum * 100;
	}
	
	public static double calculateSubTypeSLDI(String text, VigenereType type) {
		if(StatCalculator.containsDigit(text) || StatCalculator.containsHash(text) || text.length() % 2 == 1)
			return 0;
		
		double largestSum = Double.MIN_VALUE;
		char[] charArray = text.toCharArray();
		
		for(int period = 3; period <= 15; period++) {
	        double sum = 0.0D;
			int rows = (int)Math.ceil(text.length() / (2D * period));
	        
	        for(int col = 0; col < period; col++) 
	        	sum += getBestSlidefairDigramScore(charArray, col, type, rows, period);
	            
	        sum /= period;
		    largestSum = Math.max(largestSum, sum);
	    }
		
		return largestSum * 100;
	}

	public static double getBestVigenereDigramScore(char[] text, int column, VigenereType type, int rows, int period, boolean autokey) {
		double bestScore = 0.0D;
		
	    for(byte keyL = 'A'; keyL <= 'Z'; keyL++) {
	    	for(byte keyR = 'A'; keyR <= 'Z'; keyR++) {
	    		byte keyLN = keyL;
	    		byte keyRN = keyR;
	    		
	    		double score = 0.0D;
	            int diCount = 0;
	            
			    for(int r = 0; r < rows; r++) {
			    	int pos = r * period + column;
			    	if(pos + 1 >= text.length) break;
			    	
	                byte pl = type.decode((byte)text[pos], keyLN);
	                byte pr = type.decode((byte)text[pos + 1], keyRN);
	                score += logdi[pl - 'A'][pr - 'A'];
	                if(autokey) {
	                	keyLN = pl;
	                	keyRN = pr;
	                }
	                diCount++;
	            }
			    
			    if(diCount == 0) return 0.0D;
	            score /= diCount;
	            
	            bestScore = Math.max(bestScore, score);
	    	}
	    }
	    
	    return bestScore;
    }
	
	public static double getBestSlidefairDigramScore(char[] text, int column, VigenereType type, int rows, int period) {
		double bestScore = 0.0D;

		for(int key = 0; key < 26; key++) {
			
			double score = 0.0D;
		    int diCount = 0;
		    
		    for(int r = 0; r < rows; r++) {
		    	int pos = 2 * (r * period + column);
		        if(pos + 1 >= text.length) break;
		        
		        int cl = text[pos] - 'A';
		        int cr = text[pos + 1] - 'A';
		        int[] result = decodeSL(cl, cr, key, type);
		        int pl = result[0];
		        int pr = result[1];
		        score += logdi[pl][pr];
		        diCount++;
		    }
		    
		    if(diCount == 0) return 0.0D;
		    score /= diCount;
		    
		    bestScore = Math.max(bestScore, score);
		}
		
		return bestScore;
	}
	
	public static int[] decodeSL(int cl, int cr, int key, VigenereType type) {
		int pl = 0;
		int pr = 0;

		if(type == VigenereType.BEAUFORT) {
        	pl = (26 + key - cr) % 26;
        	pr = (26 + key - cl) % 26;
		}
		else if(type == VigenereType.VARIANT) {
			pl = (cr + key) % 26;
	        pr = (26 + cl - key) % 26;
		}
	    else if(type == VigenereType.VIGENERE) {
			pl = (26 + cr - key) % 26;
	        pr = (cl + key) % 26;
	    }
		
        return new int[] {pl, pr};
	}
	
	public static double calculatePTX(String text) {    
	    if(StatCalculator.containsDigit(text) || StatCalculator.containsHash(text) || text.length() % 2 == 1)
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
	                int decodePairScore = ptxdelogdi[c1][c2];
	                if(decodePairScore >= 0) {
	                    score += decodePairScore;
	                    count++;
	                }
	        	}
	        }
	        score *= 100;
	        if(count == 0)
	        	break;
	        score /= count;
	        //System.out.println(period + " " + score);
	        best_score = Math.max(best_score, score);
	    }
	    return Math.floor(best_score);
	}
	
	public static double calculateRDI(String text) {
		if(StatCalculator.containsDigit(text) || StatCalculator.containsHash(text) || text.length() % 2 == 1)
			return 0; 
		
		int n = 0;
		double score = 0;
		for(int i = 0; i < text.length(); i += 2) {
			if(Character.isLetter(text.charAt(i + 1)) && Character.isLetter(text.charAt(i))) {
				score += logdi[text.charAt(i + 1) - 'A'][text.charAt(i) - 'A'];
				n += 1;
			}
		}
		
		return score * 100 / n;
	}
	
	//PORTAX Decode log di score, -1 is no score
	static byte[][] ptxdelogdi = new byte[][] {
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		{-1,-1,6,6,7,3,0,0,8,6,8,6,8,6,6,6,6,1,7,3,4,1,6,6,4,0},
		{7,8,-1,-1,3,3,0,0,6,7,7,6,6,5,7,6,0,0,3,4,4,2,6,6,0,0},
		{8,6,6,8,-1,-1,0,0,9,6,8,6,8,6,6,4,8,0,7,3,4,2,6,6,5,0},
		{9,6,6,6,2,6,-1,-1,6,5,5,7,5,9,6,4,0,0,2,8,0,3,5,5,0,0},
		{8,4,6,3,7,2,0,0,-1,-1,8,4,8,3,6,2,7,0,7,2,5,1,6,3,3,0},
		{6,6,6,7,1,7,0,0,6,6,-1,-1,3,6,3,7,0,0,2,4,0,1,3,5,0,2},
		{5,6,8,9,3,2,0,0,6,6,6,6,-1,-1,7,8,0,0,4,6,1,0,6,5,0,0},
		{8,5,7,7,7,6,0,0,8,6,8,7,9,6,-1,-1,5,0,7,3,3,5,8,6,3,0},
		{3,5,3,9,0,7,0,0,3,6,4,5,2,7,0,8,-1,-1,0,5,0,1,3,5,0,0},
		{8,9,7,8,6,6,0,0,8,8,8,9,8,8,8,8,1,0,-1,-1,2,5,7,6,1,0},
		{6,5,9,6,6,0,6,0,6,5,7,4,7,4,3,3,2,1,2,1,-1,-1,4,3,2,0},
		{6,3,8,4,3,0,0,0,6,0,7,0,7,0,4,3,0,0,4,0,2,2,-1,-1,0,0},
		{6,2,5,3,4,0,0,0,7,1,6,2,7,2,4,3,3,0,3,1,3,0,4,2,-1,-1}};
	
	//First row is AA - AZ, last ZA - ZZ etc score indicates how common the digraph is on a log scale, larger = more common
	static byte[][] logdi = new byte[][] {
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
}
