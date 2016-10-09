package nationalcipher.cipher.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javalibrary.math.Statistics;
import javalibrary.streams.FileReader;
import javalibrary.string.StringTransformer;
import nationalcipher.cipher.manage.IRandEncrypter;
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
import nationalcipher.cipher.stats.types.StatisticLogDigraphSlidefair;
import nationalcipher.cipher.stats.types.StatisticLogDigraphVariant;
import nationalcipher.cipher.stats.types.StatisticLogDigraphVigenere;
import nationalcipher.cipher.stats.types.StatisticLongRepeat;
import nationalcipher.cipher.stats.types.StatisticMaxBifid0;
import nationalcipher.cipher.stats.types.StatisticMaxBifid3to15;
import nationalcipher.cipher.stats.types.StatisticMaxICx1000;
import nationalcipher.cipher.stats.types.StatisticMaxNicodemus3to15;
import nationalcipher.cipher.stats.types.StatisticMaxTrifid3to15;
import nationalcipher.cipher.stats.types.StatisticNormalOrder;
import nationalcipher.cipher.stats.types.StatisticPercentageOddRepeats;
import nationalcipher.cipher.stats.types.StatisticTextLengthMultiple;

public class StatisticHandler {
	
	public static HashMap<String, Class<? extends TextStatistic>> map = new HashMap<String, Class<? extends TextStatistic>>();
	
	public static boolean registerStatistic(String id, Class<? extends TextStatistic> textStatistic) {
		if(map.containsKey(id)) return false;
		
		map.put(id, textStatistic);
		
		return true;
	}
	
	public static List<List<Object>> orderCipherProbibitly(String text) {
		List<List<Object>> num_dev = new ArrayList<List<Object>>();
		
		HashMap<String, TextStatistic> stats = createTextStatistics(text);
		HashMap<String, HashMap<String, DataHolder>> statistic = CipherStatistics.getOtherCipherStatistics();
		System.out.println(stats);
		
		for(String cipherName : statistic.keySet()) {

			double value = scoreCipher(stats, statistic.get(cipherName));
			num_dev.add(Arrays.asList(cipherName, value));
		}
		
		return num_dev;
	}
	
	public static double scoreCipher(HashMap<String, TextStatistic> stats, HashMap<String, DataHolder> data) {
		double value = 0.0D;
		
		for(String id : data.keySet()) 
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
	
	public static void calculateStatPrint(IRandEncrypter randEncrypt, Class<? extends TextStatistic> textStatistic) {
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
			
			
			
			for(int i = 0; i < 20; i++) {
				test.text = randEncrypt.randomlyEncrypt(plainText);
				//System.out.println(test.text);
				test.calculateStatistic();
				try {
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
		registerStatistic(StatisticsRef.DIAGRAPHIC_IC_x10000, StatisticDiagrahpicICx10000.class);
		registerStatistic(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, StatisticEvenDiagrahpicICx10000.class);
		registerStatistic(StatisticsRef.IC_x1000, StatisticICx1000.class);
		registerStatistic(StatisticsRef.IC_KAPPA_x1000, StatisticKappaICx1000.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH, StatisticLogDigraph.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_BEAUFORT, StatisticLogDigraphAutokeyBeaufort.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_PORTA, StatisticLogDigraphAutokeyPorta.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VARIANT, StatisticLogDigraphAutokeyVariant.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VIGENERE, StatisticLogDigraphAutokeyVigenere.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, StatisticLogDigraphBeaufort.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_PORTA, StatisticLogDigraphPorta.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_PORTAX, StatisticLogDigraphPortax.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_REVERSED, StatisticLogDigraphReversed.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_SLIDEFAIR, StatisticLogDigraphSlidefair.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_VARIANT, StatisticLogDigraphVariant.class);
		registerStatistic(StatisticsRef.LOG_DIAGRAPH_VIGENERE, StatisticLogDigraphVigenere.class);
		registerStatistic(StatisticsRef.LONG_REPEAT, StatisticLongRepeat.class);
		registerStatistic(StatisticsRef.BIFID_MAX_0, StatisticMaxBifid0.class);
		registerStatistic(StatisticsRef.BIFID_MAX_3to15, StatisticMaxBifid3to15.class);
		registerStatistic(StatisticsRef.IC_MAX_x1000, StatisticMaxICx1000.class);
		registerStatistic(StatisticsRef.NICODEMUS_MAX_3to15, StatisticMaxNicodemus3to15.class);
		registerStatistic(StatisticsRef.TRIFID_MAX_3to15, StatisticMaxTrifid3to15.class);
		registerStatistic(StatisticsRef.NORMAL_ORDER, StatisticNormalOrder.class);
		registerStatistic(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, StatisticPercentageOddRepeats.class);
		
		//Boolean statitics
		registerStatistic(StatisticsRef.DOUBLE_LETTER_EVEN, StatisticDoubleLetter.class);
		registerStatistic(StatisticsRef.DOUBLE_LETTER_EVEN_2to40, StatisticDoubleLetter2to40.class);
		registerStatistic(StatisticsRef.TEXT_LENGTH_MULTIPLE, StatisticTextLengthMultiple.class);
	}
}
