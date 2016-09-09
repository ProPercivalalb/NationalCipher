package nationalcipher.cipher.identify;

import java.util.Arrays;
import java.util.List;

import nationalcipher.cipher.stats.StatCalculator;

public class PolyalphabeticIdentifier {
	
	public enum SubType {
		BEAUFORT(),
		VARIANT(),
		VIGENERE(),
		PORTA(),
		
		AUTOKEY_BEAUFORT(),
		AUTOKEY_VARIANT(),
		AUTOKEY_VIGENERE(),
		AUTOKEY_PORTA(),
		AUTOKEY_PORTA_LEFT();
	}
	
	public static double calculateLDI(String text) {
		int score = 0;
		for(int i = 0; i < text.length() - 1; i++)
			if(Character.isLetter(text.charAt(i)) && Character.isLetter(text.charAt(i + 1)))
				score += logdi[text.charAt(i) - 'A'][text.charAt(i + 1) - 'A'];
		
		return score * 100 / (text.length() - 1);
	}
	
	public static double calculateSubTypeLDI(String text, SubType type) {
		if(StatCalculator.containsDigit(text) || StatCalculator.containsHash(text))
			return 0.0D;
		
	    double largestSum = Double.MIN_VALUE;
		
		for(int period = 2; period <= 15; period++) {
	        double sum = 0.0D;
	        for(int col = 0; col < period; col++) 
	        	sum += best_di(col, type, period, text);
	            
	        sum /= period;
	        largestSum = Math.max(largestSum, sum);
		}

		return largestSum;
	}
	
	public static double calculateAutokeyVigenereLDI(String text) {
		return calculateSubTypeLDI(text, SubType.AUTOKEY_VIGENERE);
	}
	
	public static double calculateAutokeyPortaLDI(String text) {
		return Math.max(calculateSubTypeLDI(text, SubType.AUTOKEY_PORTA), calculateSubTypeLDI(text, SubType.AUTOKEY_PORTA_LEFT));
	}
	
	public static double calculateAutokeyBeaufortLDI(String text) {
		return calculateSubTypeLDI(text, SubType.AUTOKEY_BEAUFORT);
	}
	
	public static double calculateAutokeyVariantLDI(String text) {
		return calculateSubTypeLDI(text, SubType.AUTOKEY_VARIANT);
	}

	public static double calculateBeaufortLDI(String text) {
		return calculateSubTypeLDI(text, SubType.BEAUFORT);
	}
	
	public static double calculatePortaLDI(String text) {
		return calculateSubTypeLDI(text, SubType.PORTA);
	}
	
	public static double calculateVigenereLDI(String text) {
		return calculateSubTypeLDI(text, SubType.VIGENERE);
	}
	
	public static double calculateVariantLDI(String text) {
		return calculateSubTypeLDI(text, SubType.VARIANT);
	}

	public static double calculateSLDI(String text) {
		if(StatCalculator.containsDigit(text) || StatCalculator.containsHash(text) || text.length() % 2 == 1)
			return 0;
		
		double largestSum = Double.MIN_VALUE;
		
		for(int type = 0; type <= 1; type += 1) {
			for(int period = 3; period <= 15; period++) {
	        	double sum = 0.0D;
	        	
	            for(int col = 0; col < period; col++) 
	                sum += best_sldi(col, type, period, text);
	            
	            sum /= period;
		        largestSum = Math.max(largestSum, sum);
	         }
		}
		
		return largestSum;
	}

	public static double best_di(int col, SubType type, int period, String text){
		int bestScore = 0;
		
		int rows = (int)Math.floor(text.length() / period);
	    for (int kl = 0;kl < 26;kl++) {
	    	for (int kr = 0; kr < 26;kr++) {
	    		int score = 0;
	            int ct = 0;
	            int kl1 = kl;
	            int kr1 = kr;
			    for(int j = 0; j < rows; j++) {
			    	if(col + j * period + 1 >= text.length())
			    		break;
	                int cl = text.charAt(col + j * period) - 'A';
	                int cr = text.charAt(col + j * period + 1) - 'A';
	                int pl = decodeLet(cl, kl1, type);
	                int pr = decodeLet(cr, kr1, type);
	                score += logdi[pl][pr];
	                ct++;
	                if(type == SubType.AUTOKEY_BEAUFORT 
	                		|| type == SubType.AUTOKEY_PORTA 
	                				|| type == SubType.AUTOKEY_PORTA_LEFT 
	                		|| type == SubType.AUTOKEY_VIGENERE 
	                		|| type == SubType.AUTOKEY_VARIANT){
	                	kl1 = pl;
	                	kr1 = pr;
	                }
	            }
			    if(ct == 0)
			    	return 0.0D;
			    
	            score *= 100;
	            score /= ct;
	            bestScore = Math.max(bestScore, score);
	    	}
	    }
	    return bestScore;
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
	 * 							 	5 (BAutokey), 6 (VIGAutokey), 7 (PAutokey)
	 */
	public static int decodeLet(int cipherChar, int key, SubType type){
        int plainChar = 0;

        switch(type) {
        case VIGENERE: // VIGENERE
        case AUTOKEY_VIGENERE: //VIGAUTOKEY
        	plainChar = (26 + cipherChar - key) % 26;
            break;
        case VARIANT: //VARIANT
        case AUTOKEY_VARIANT: //VAUTOKEY
        	plainChar = (cipherChar + key) % 26;
            break;
        case BEAUFORT: //BEAUFORT
        case AUTOKEY_BEAUFORT: // BAUTOKEY
        	plainChar = (26 + key - cipherChar) % 26;
            break;
        case PORTA: //PORTA
        case AUTOKEY_PORTA: // PAUTOKEY
        	key = (int)Math.floor(key / 2);
            plainChar = cipherChar;
            if(plainChar < 13) {
            	plainChar += key;
                if(plainChar < 13)
                	plainChar += 13;
            }
            else {
            	plainChar -= key;
                if(plainChar > 12)
                	plainChar -= 13;
            }
            break;
        case AUTOKEY_PORTA_LEFT: // PAUTOKEY
        	key = (int)Math.floor(key / 2);
            plainChar = cipherChar;
            if(plainChar < 13) {
            	plainChar += 13;
            	plainChar += key;
            	if(plainChar > 25)
            		plainChar -= 13;
            }
            else {
            	plainChar -= 13;
            	plainChar -= key;
            	if(plainChar < 0)
            		plainChar += 13;
            }
            break;
        default:
        	System.out.println("INVALID SUBTYPE GIVEN");
		}
        
        return plainChar;
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
	        //System.out.println(period + " " + score);
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
	
	public static int calculateRDI(String text) {
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
		
		return (int)Math.floor(score * 100 / n);
	}
	
	//First row is AA - AZ, last ZA - ZZ etc score indicates how common the digraph is on a log scale, larger = more common
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
}
