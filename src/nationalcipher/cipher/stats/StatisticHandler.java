package nationalcipher.cipher.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javalibrary.math.Statistics;
import javalibrary.streams.FileReader;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.stats.types.StatisticDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticDoubleLetter;
import nationalcipher.cipher.stats.types.StatisticDoubleLetter2to40;
import nationalcipher.cipher.stats.types.StatisticEvenDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticICx1000;
import nationalcipher.cipher.stats.types.StatisticKappaICx1000;
import nationalcipher.cipher.stats.types.StatisticLogDigraph;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyBeaufort;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyPorta;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyVariant;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyVigenere;
import nationalcipher.cipher.stats.types.StatisticLogDigraphBeaufort;
import nationalcipher.cipher.stats.types.StatisticLogDigraphPorta;
import nationalcipher.cipher.stats.types.StatisticLogDigraphPortax;
import nationalcipher.cipher.stats.types.StatisticLogDigraphReversed;
import nationalcipher.cipher.stats.types.StatisticLogDigraphSlidefairBeaufort;
import nationalcipher.cipher.stats.types.StatisticLogDigraphSlidefairVariant;
import nationalcipher.cipher.stats.types.StatisticLogDigraphSlidefairVigenere;
import nationalcipher.cipher.stats.types.StatisticLogDigraphVariant;
import nationalcipher.cipher.stats.types.StatisticLogDigraphVigenere;
import nationalcipher.cipher.stats.types.StatisticLongRepeat;
import nationalcipher.cipher.stats.types.StatisticBifid0;
import nationalcipher.cipher.stats.types.StatisticMaxBifid3to15;
import nationalcipher.cipher.stats.types.StatisticMaxICx1000;
import nationalcipher.cipher.stats.types.StatisticMaxNicodemus3to15;
import nationalcipher.cipher.stats.types.StatisticMaxTrifid3to15;
import nationalcipher.cipher.stats.types.StatisticNormalOrder;
import nationalcipher.cipher.stats.types.StatisticPercentageOddRepeats;
import nationalcipher.cipher.stats.types.StatisticTextLengthMultiple;
import nationalcipher.cipher.stats.types.StatisticTrigraphNoOverlapICx100000;

public class StatisticHandler {
	
	public static LinkedHashMap<String, Class<? extends TextStatistic>> map = new LinkedHashMap<String, Class<? extends TextStatistic>>();
	public static HashMap<String, String> shortNameMap = new HashMap<String, String>();
	
	public static boolean registerStatistic(String id, Class<? extends TextStatistic> textStatistic) {
		return registerStatistic(id, textStatistic, id);
	}
	
	public static boolean registerStatistic(String id, Class<? extends TextStatistic> textStatistic, String shortName) {
		if(map.containsKey(id)) return false;
		
		map.put(id, textStatistic);
		shortNameMap.put(id, shortName);
		return true;
	}
	
	public static List<List<Object>> orderCipherProbibitly(String text) {
		return orderCipherProbibitly(createTextStatistics(text));
	}
	
	public static List<List<Object>> orderCipherProbibitly(HashMap<String, TextStatistic> stats) {
		return orderCipherProbibitly(stats, new ArrayList<String>(map.keySet()));
	}
	
	public static List<List<Object>> orderCipherProbibitly(HashMap<String, TextStatistic> stats, List<String> doOnly) {
		List<List<Object>> num_dev = new ArrayList<List<Object>>();
		
		TreeMap<String, HashMap<String, DataHolder>> statistic = CipherStatistics.getOtherCipherStatistics();
		
		for(String cipherName : statistic.keySet()) {

			double value = scoreCipher(stats, statistic.get(cipherName), doOnly);
			num_dev.add(Arrays.asList(cipherName, value));
		}
		
		return num_dev;
	}
	
	public static double scoreCipher(HashMap<String, TextStatistic> stats, HashMap<String, DataHolder> data, List<String> doOnly) {
		double value = 0.0D;
		
		for(String id : data.keySet()) 
			if(doOnly.contains(id))
				value += stats.get(id).quantify(data.get(id));
		
		return value;
	}
	
	public static HashMap<String, TextStatistic> createTextStatistics(String text) {
		HashMap<String, TextStatistic> stats = new HashMap<String, TextStatistic>();
		try {
			for(String id : map.keySet()) {
				TextStatistic stat = map.get(id).getConstructor(String.class).newInstance(text);
				stat.calculateStatistic();
				stats.put(id, stat);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return stats;
	}
	
	public static void calculateStatPrint(IRandEncrypter randEncrypt, Class<? extends TextStatistic> textStatistic, int times) {
		List<String> list = FileReader.compileTextFromResource("/plainText.txt", true);
		
		List<Double> values = new ArrayList<Double>();
		TextStatistic test = null;
		try {
			test = textStatistic.getConstructor(String.class).newInstance("");
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		
		for(String line : list) {
			String plainText = line;
			
			
			
			for(int i = 0; i < times; i++) {
				test.text = randEncrypt.randomlyEncrypt(plainText);
				//System.out.println(test.text);
				test.calculateStatistic();
				try {
				//	if(test.value != 0)
						values.add(test.value);
				} 
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

	    Statistics stats = new Statistics(values);
	    
	    String name = randEncrypt.getClass().getSimpleName();
		String variableName = "";
	    if(!Character.isJavaIdentifierStart(name.charAt(0)))
	    	variableName += "_";
	    for (char c : name.toCharArray())
	        if(!Character.isJavaIdentifierPart(c))
	        	variableName += "_";
	        else
	        	variableName += c;

		System.out.println(variableName + ".put(" + textStatistic.getSimpleName() + ", new DataHolder(" + String.format("%.2f", stats.getMean()) + ", " + String.format("%.2f", stats.getStandardDeviation()) + ")); //Min: " + String.format("%.2f", stats.getMin()) + " Max: " + String.format("%.2f", stats.getMax()));
	}
	
	public static void registerStatistics() {
		//Default all ciphers
		registerStatistic(StatisticsRef.IC_x1000, StatisticICx1000.class, "IC");
		registerStatistic(StatisticsRef.IC_MAX_1to15_x1000, StatisticMaxICx1000.class, "MIC");
		registerStatistic(StatisticsRef.IC_2_TRUE_x10000, StatisticDiagrahpicICx10000.class, "DIC");
		registerStatistic(StatisticsRef.IC_2_FALSE_x10000, StatisticEvenDiagrahpicICx10000.class, "DIC_E");
		registerStatistic(StatisticsRef.IC_3_FALSE_x100000, StatisticTrigraphNoOverlapICx100000.class, "TIC_E");
		registerStatistic(StatisticsRef.IC_KAPPA_x1000, StatisticKappaICx1000.class, "MKA");
		registerStatistic(StatisticsRef.LOG_DIGRAPH, StatisticLogDigraph.class, "LDI");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_REVERSED, StatisticLogDigraphReversed.class, "LDI_R");
		registerStatistic(StatisticsRef.LONG_REPEAT, StatisticLongRepeat.class, "LR");
		registerStatistic(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, StatisticPercentageOddRepeats.class, "LR_OP");
		registerStatistic(StatisticsRef.NORMAL_ORDER, StatisticNormalOrder.class, "NOR");
		
		registerStatistic(StatisticsRef.BIFID_MAX_3to15, StatisticMaxBifid3to15.class, "BIC");
		registerStatistic(StatisticsRef.BIFID_0, StatisticBifid0.class, "BIC_Z");
		registerStatistic(StatisticsRef.NICODEMUS_MAX_3to15, StatisticMaxNicodemus3to15.class, "NIC");
		registerStatistic(StatisticsRef.TRIFID_MAX_3to15, StatisticMaxTrifid3to15.class, "TIC");
		
		//Vigenere Family
		registerStatistic(StatisticsRef.LOG_DIGRAPH_BEAUFORT, StatisticLogDigraphBeaufort.class, "LDI_BEU");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_PORTA, StatisticLogDigraphPorta.class, "LDI_POR");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_PORTAX, StatisticLogDigraphPortax.class, "LDI_PTX");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_VARIANT, StatisticLogDigraphVariant.class, "LDI_VAR");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_VIGENERE, StatisticLogDigraphVigenere.class, "LDI_VIG");
		
		registerStatistic(StatisticsRef.LOG_DIGRAPH_AUTOKEY_BEAUFORT, StatisticLogDigraphAutokeyBeaufort.class, "LDI_A_BEU");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_AUTOKEY_PORTA, StatisticLogDigraphAutokeyPorta.class, "LDI_A_POR");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_AUTOKEY_VARIANT, StatisticLogDigraphAutokeyVariant.class, "LDI_A_VAR");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_AUTOKEY_VIGENERE, StatisticLogDigraphAutokeyVigenere.class, "LDI_A_VIG");
		
		registerStatistic(StatisticsRef.LOG_DIGRAPH_SLIDEFAIR_BEAUFORT, StatisticLogDigraphSlidefairBeaufort.class, "LDI_SLI_BEU");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_SLIDEFAIR_VARIANT, StatisticLogDigraphSlidefairVariant.class, "LDI_SLI_VAR");
		registerStatistic(StatisticsRef.LOG_DIGRAPH_SLIDEFAIR_VIGENERE, StatisticLogDigraphSlidefairVigenere.class, "LDI_SLI_VIG");
		
		//Boolean statitics
		registerStatistic(StatisticsRef.DOUBLE_LETTER_EVEN, StatisticDoubleLetter.class, "DBL_PLAY");
		registerStatistic(StatisticsRef.DOUBLE_LETTER_EVEN_2to40, StatisticDoubleLetter2to40.class, "DBL_SERP");
		registerStatistic(StatisticsRef.TEXT_LENGTH_MULTIPLE, StatisticTextLengthMultiple.class, "DIV_N");
	}
}
