package nationalcipher.old;

import java.util.HashMap;
import java.util.Map;

import javalibrary.cipher.stats.TraverseTree;
import javalibrary.util.MapHelper;
import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.StatCalculator;

public class OldCipherIdentifty {

	public static void main(String[] args) {
			
		String text = "";
		Object[] statValues = new Object[38];
	    
	    //Numerical values
	    statValues[0] = StatCalculator.calculateIC(text, 1, true) * 1000.0D;
		statValues[1] = StatCalculator.calculateMaxIC(text, 1, 15) * 1000.0D;
		statValues[2] = StatCalculator.calculateMaxKappaIC(text, 1, 15);
		statValues[3] = StatCalculator.calculateIC(text, 2, true) * 10000.0D;
		statValues[4] = StatCalculator.calculateIC(text, 2, false) * 10000;
		statValues[5] = StatCalculator.calculateLR(text);
		statValues[6] = StatCalculator.calculateROD(text);
		statValues[7] = PolyalphabeticIdentifier.calculateLDI(text);
		statValues[8] = StatCalculator.calculateSDD(text);
	    statValues[22] = PolyalphabeticIdentifier.calculateAutokeyPortaLDI(text);
		statValues[23] = PolyalphabeticIdentifier.calculateBeaufortLDI(text);
		statValues[24] = PolyalphabeticIdentifier.calculatePortaLDI(text);
		statValues[25] = PolyalphabeticIdentifier.calculateSLDI(text);
		statValues[26] = PolyalphabeticIdentifier.calculateVigenereLDI(text);
		//statValues[27] = StatCalculator.calculateNormalOrder(text, settings.getLanguage());
		statValues[28] = PolyalphabeticIdentifier.calculateRDI(text);
		statValues[29] = PolyalphabeticIdentifier.calculatePTX(text);
		statValues[30] = StatCalculator.calculateMaxNicodemusIC(text, 3, 15);
		statValues[31] = StatCalculator.calculatePHIC(text);
	    statValues[33] = StatCalculator.calculateMaxBifidDiagraphicIC(text, 3, 15);
		statValues[34] = StatCalculator.calculateCDD(text);
		statValues[35] = StatCalculator.calculateSSTD(text);
		statValues[36] = StatCalculator.calculateMPIC(text);
		statValues[9] = "CIPHER";
		statValues[10] = StatCalculator.isLengthDivisible2(text);
		statValues[11] = StatCalculator.isLengthDivisible3(text);
		statValues[12] = StatCalculator.isLengthDivisible5(text);
		statValues[13] = StatCalculator.isLengthDivisible25(text);
		statValues[14] = StatCalculator.isLengthDivisible4_15(text);
		statValues[15] = StatCalculator.isLengthDivisible4_30(text);
		statValues[16] = StatCalculator.isLengthPerfectSquare(text);
		statValues[17] = StatCalculator.containsLetter(text);
		statValues[18] = StatCalculator.containsDigit(text);
		statValues[19] = StatCalculator.containsJ(text);
		statValues[20] = StatCalculator.containsHash(text);
		statValues[21] = StatCalculator.calculateDBL(text);
		statValues[32] = StatCalculator.calculateHAS0(text);
		statValues[37] = StatCalculator.calculateSeriatedPlayfair(text, 3, 10);
	    
		Map<String, Integer> answers = new HashMap<String, Integer>();
		
		for(Map map : TraverseTree.trees) {
			String answer = TraverseTree.traverse_tree(map, statValues);
			answers.put(answer, 1 + (answers.containsKey(answer) ? answers.get(answer) : 0));
		}
		answers = MapHelper.sortMapByValue(answers, false);
		
		System.out.println(answers);
	}

}
