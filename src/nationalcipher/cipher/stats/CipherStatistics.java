package nationalcipher.cipher.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import nationalcipher.cipher.stats.types.*;

public class CipherStatistics {

	private static HashMap<String, List<StatisticType>> map;
	
	public static List<List<Object>> getResultsFromStats(String text) {
		List<List<Object>> num_dev = new ArrayList<List<Object>>();
		
		HashMap<String, List<StatisticType>> statistic = CipherStatistics.getOtherCipherStatistics();
		
		for(String cipherName : statistic.keySet()) {
			double total = 0.0D;
			List<StatisticType> statistics = statistic.get(cipherName);
			if(statistics == null) continue;
			
			for(StatisticType type : statistics) 
				total += type.quantifyType(text);
			
			
			num_dev.add(new ArrayList<Object>(Arrays.asList(cipherName, total)));
		}
		
		return num_dev;
	}
	
	public static HashMap<String, List<StatisticType>> getOtherCipherStatistics() {
		if(map == null) {
			map = new HashMap<String, List<StatisticType>>();
	
			
			List<StatisticType> substitution = createOrGetList("Substitution");
			substitution.add(new StatisticIC(0.066006, 0.00271));
			substitution.add(new StatisticMaxIC(0.0683227056097686200000000, 0.0030696963125843467000000));
			substitution.add(new StatisticKappaIC(0.0806305792187504600000000, 0.0089096132357407500000000));
			substitution.add(new StatisticDiagrahpicIC(0.007680, 0.0006698));
			substitution.add(new StatisticEvenDiagrahpicIC(0.007711297652244466, 0.0007212111871253584));
			substitution.add(new StatisticLongRepeat(23.965610058575958, 2.0102777216085546));
			substitution.add(new StatisticPercentageOddRepeats(49.65217391304348, 1.5680328944763842));
			substitution.add(new StatisticNormalOrder(225.3223188405797000000000000, 27.9080248261220500000000000));
			substitution.add(new StatisticMaxBifid3to15(474.1241846046231, 76.0084566498733));
			substitution.add(new StatisticMaxNicodemus3to15(66.01746637747263, 8.475323689459344));
			substitution.add(new StatisticMaxTrifid3to15(3739.0927263340480000000000000, 1076.8573199253747000000000000));
			
			List<StatisticType> vigenere = createOrGetList("Vigenere");
			vigenere.add(new StatisticIC(0.0429903119847777700000000, 0.0037536364623872560000000));
			vigenere.add(new StatisticMaxIC(0.0665520093832535500000000, 0.0031948433152494140000000));
			vigenere.add(new StatisticKappaIC(0.0689652945342396900000000, 0.0095675417417046400000000));
			vigenere.add(new StatisticDiagrahpicIC(0.0025086347126251030000000, 0.0008086035756847582000000));
			vigenere.add(new StatisticEvenDiagrahpicIC(0.0031009593136257833000000, 0.0015380576585325028000000));
			vigenere.add(new StatisticLongRepeat(10.5212546335566250000000000, 2.9517345341344905000000000));
			vigenere.add(new StatisticPercentageOddRepeats(39.8347101449275340000000000, 11.5407782805919010000000000));
			vigenere.add(new StatisticNormalOrder(224.0864492753623300000000000, 32.9728235330279060000000000));
			vigenere.add(new StatisticMaxBifid3to15(175.7798110435920000000000000, 58.2320049919340550000000000));
			vigenere.add(new StatisticMaxNicodemus3to15(45.7087715971444300000000000, 3.5810771476970964000000000));
			vigenere.add(new StatisticMaxTrifid3to15(1184.3713387205507000000000000, 907.8437292675648000000000000));
			
			List<StatisticType> playfair = createOrGetList("Playfair");
			playfair.add(new StatisticIC(0.0509790170170644550000000, 0.0029840413999321350000000));
			playfair.add(new StatisticMaxIC(0.0539654387252110140000000, 0.0034910024524792195000000));
			playfair.add(new StatisticKappaIC(0.0634864713576964900000000, 0.0075065326665879890000000));
			playfair.add(new StatisticDiagrahpicIC(0.0040383113549347744000000, 0.0003862845680305802000000));
			playfair.add(new StatisticEvenDiagrahpicIC(0.0076991903886331065000000, 0.0007244048281844931000000));
			playfair.add(new StatisticLongRepeat(13.2216252474114190000000000, 1.2690694983027242000000000));
			playfair.add(new StatisticPercentageOddRepeats(30.8497826086956500000000000, 3.7339181673248363000000000));
			playfair.add(new StatisticNormalOrder(233.4631159420290000000000000, 32.8259322483317000000000000));
			playfair.add(new StatisticMaxBifid3to15(253.8848368274454700000000000, 28.3074790701134380000000000));
			playfair.add(new StatisticMaxNicodemus3to15(52.4302649136329800000000000, 3.3217549175988060000000000));
			playfair.add(new StatisticMaxTrifid3to15(1210.2630829565220000000000000, 364.5870986315568400000000000));
			playfair.add(new StatisticTextLengthMultiple(2));
			playfair.add(new StatisticDoubleLetter(false));
			
			
			List<StatisticType> solitaire = createOrGetList("Solitaire");
			solitaire.add(new StatisticIC(0.038458, 0.00042));
			solitaire.add(new StatisticMaxIC(0.0499213604458725940000000, 0.0057333897281015460000000));
			solitaire.add(new StatisticKappaIC(0.0400259604816277200000000, 0.0016844319637870740000000));
			solitaire.add(new StatisticDiagrahpicIC(0.001479, 0.0000869));
			solitaire.add(new StatisticEvenDiagrahpicIC(0.0014782641532939235000000, 0.0001627226329222692200000));
			solitaire.add(new StatisticLongRepeat(5.1354845549403230000000000, 0.8892331387414816000000000));
			solitaire.add(new StatisticPercentageOddRepeats(49.6165217391304340000000000, 2.9584395740484672000000000));
			solitaire.add(new StatisticNormalOrder(222.4618840579710200000000000, 29.7462382849075200000000000));
			solitaire.add(new StatisticMaxBifid3to15(105.2713415046132200000000000, 13.8776832252436170000000000));
			solitaire.add(new StatisticMaxNicodemus3to15(40.0047256854201400000000000, 2.5816919104277085000000000));
			solitaire.add(new StatisticMaxTrifid3to15(233.8118871177359000000000000, 159.8055294692080700000000000));
			
			
			
			/**
			normalEnglish.add(new StatisticRange(StatisticType.MAX_IOC, 73.0D, 11.0D));
			normalEnglish.add(new StatisticRange(StatisticType.MAX_KAPPA, 95.0D, 19.0D));
			normalEnglish.add(new StatisticRange(StatisticType.DIGRAPHIC_IOC, 72.0D, 18.0D));
			normalEnglish.add(new StatisticRange(StatisticType.EVEN_DIGRAPHIC_IOC, 73.0D, 24.0D));
			normalEnglish.add(new StatisticRange(StatisticType.LONG_REPEAT_3, 22.0D, 5.0D));
			normalEnglish.add(new StatisticRange(StatisticType.LONG_REPEAT_ODD, 50.0D, 6.0D));
			normalEnglish.add(new StatisticRange(StatisticType.LOG_DIGRAPH, 756.0D, 13.0D));
			normalEnglish.add(new StatisticRange(StatisticType.SINGLE_LETTER_DIGRAPH, 303.0D, 23.0D));**/
			
		}
		return map;
	}
	
	public static List<StatisticType> createOrGetList(String key) {
		if(!map.containsKey(key))
			map.put(key, new ArrayList<StatisticType>());
		return map.get(key);
	}
}
