package nationalcipher.cipher.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javalibrary.language.Languages;
import javalibrary.math.Statistics;
import javalibrary.streams.FileReader;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.manage.RandomEncrypter;
import nationalcipher.cipher.stats.types.StatisticBaseNumber;
import nationalcipher.cipher.stats.types.StatisticDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticDoubleLetter;
import nationalcipher.cipher.stats.types.StatisticDoubleLetter2to40;
import nationalcipher.cipher.stats.types.StatisticEvenDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticICx1000;
import nationalcipher.cipher.stats.types.StatisticKappaICx1000;
import nationalcipher.cipher.stats.types.StatisticLongRepeat;
import nationalcipher.cipher.stats.types.StatisticMaxBifid0;
import nationalcipher.cipher.stats.types.StatisticMaxBifid3to15;
import nationalcipher.cipher.stats.types.StatisticMaxICx1000;
import nationalcipher.cipher.stats.types.StatisticMaxNicodemus3to15;
import nationalcipher.cipher.stats.types.StatisticMaxTrifid3to15;
import nationalcipher.cipher.stats.types.StatisticNormalOrder;
import nationalcipher.cipher.stats.types.StatisticPercentageOddRepeats;
import nationalcipher.cipher.stats.types.StatisticTextLengthMultiple;
import nationalcipher.cipher.stats.types.StatisticType;
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

public class CipherStatistics {

	private static HashMap<String, List<StatisticType>> map;
	
	public static List<List<Object>> getResultsFromStats(String text) {
		List<List<Object>> num_dev = new ArrayList<List<Object>>();
		
		HashMap<String, List<StatisticType>> statistic = CipherStatistics.getOtherCipherStatistics();
		HashMap<StatisticType, Double> values = new HashMap<StatisticType, Double>();
		
		
		for(String cipherName : statistic.keySet()) {
			double total = 0.0D;
			List<StatisticType> statistics = statistic.get(cipherName);
			if(statistics == null) continue;
			
			
			for(StatisticType type : statistics) 
				if(type instanceof StatisticBaseNumber) {
					if(values.containsKey(type))
						total += ((StatisticBaseNumber)type).quantifyType(values.get(type));
					else {
						double value = type.quantifyType(text);
						values.put(type, value);
						total += value;
					}
						
				}
					
				else
					total += type.quantifyType(text);
			
			
			num_dev.add(new ArrayList<Object>(Arrays.asList(cipherName, total)));
		}
		
		return num_dev;
	}
	
	public static void compileStatsForCipher(IRandEncrypter randEncrypt) {
		List<String> list = FileReader.compileTextFromResource("/plainText.txt", true);

		List<StatisticBaseNumber> statTypes = new ArrayList<StatisticBaseNumber>();
		/**statTypes.add(new StatisticICx1000(200, 0));
		statTypes.add(new StatisticMaxICx1000(200, 0));
		statTypes.add(new StatisticKappaICx1000(200, 0));
		statTypes.add(new StatisticDiagrahpicICx10000(200, 0));
		statTypes.add(new StatisticEvenDiagrahpicICx10000(200, 0));
		statTypes.add(new StatisticLongRepeat(200, 0));
		statTypes.add(new StatisticPercentageOddRepeats(200, 0));
		statTypes.add(new StatisticNormalOrder(200, 0));
		statTypes.add(new StatisticMaxBifid0(200, 0));
		statTypes.add(new StatisticMaxBifid3to15(200, 0));
		statTypes.add(new StatisticMaxNicodemus3to15(200, 0));
		statTypes.add(new StatisticMaxTrifid3to15(200, 0));
		statTypes.add(new StatisticLogDigraph(200, 0));**/
		//statTypes.add(new StatisticLogDigraphReversed(200, 0));
		//statTypes.add(new StatisticLogDigraphBeaufort(10, 0));
		//statTypes.add(new StatisticLogDigraphVigenere(10, 0));
		//statTypes.add(new StatisticLogDigraphVariant(10, 0));
	//	statTypes.add(new StatisticLogDigraphPorta(10, 0));
		//statTypes.add(new StatisticLogDigraphAutokeyBeaufort(30, 0));
		statTypes.add(new StatisticLogDigraphAutokeyPorta(3, 0));
		//statTypes.add(new StatisticLogDigraphAutokeyVariant(30, 0));
		//statTypes.add(new StatisticLogDigraphAutokeyVigenere(30, 0));
		//statTypes.add(new StatisticLogDigraphSlidefair(30, 0));
	   // statTypes.add(new StatisticLogDigraphPortax(30, 0));
		
		String name = randEncrypt.getClass().getSimpleName();
		String variableName = "";
	    if(!Character.isJavaIdentifierStart(name.charAt(0)))
	    	variableName += "_";
	    for (char c : name.toCharArray())
	        if(!Character.isJavaIdentifierPart(c))
	        	variableName += "_";
	        else
	        	variableName += c;

		
		
		System.out.println("List<StatisticType> " + variableName + " = createOrGetList(\""  + name + "\");");
		
		HashMap<StatisticBaseNumber, Double> precalculatedValues = new HashMap<StatisticBaseNumber, Double>();
		for(StatisticBaseNumber type : statTypes) {
			List<Double> values = new ArrayList<Double>();
			
			for(String line : list) {
				String plainText = line;
				
				for(int i = 0; i < type.value; ++i) {
					
				//	System.out.println("en");
					String text = randEncrypt.randomlyEncrypt(plainText);
					//if(precalculatedValues.containsKey(type))
					//	precalculatedValues.get(type);
					double value = type.getValue(text);
					values.add(value);
				}
			}
	
		    Statistics stats = new Statistics(values);
			
			System.out.println(variableName + ".add(new " + type.getClass().getSimpleName() + "(" + String.format("%.2f", stats.getMean()) + ", " + String.format("%.2f", stats.getStandardDeviation()) + "));");
		}
		System.out.println("\nComplete!");
	}
	
	public static HashMap<String, List<StatisticType>> getOtherCipherStatistics() {
		if(map == null) {
			map = new HashMap<String, List<StatisticType>>();
	
			
			List<StatisticType> substitution = createOrGetList("Substitution");
			substitution.add(new StatisticICx1000(66.01, 2.71));
			substitution.add(new StatisticMaxICx1000(68.32, 3.07));
			substitution.add(new StatisticKappaICx1000(80.63, 8.91));
			substitution.add(new StatisticDiagrahpicICx10000(76.80, 6.70));
			substitution.add(new StatisticEvenDiagrahpicICx10000(77.11, 7.21));
			substitution.add(new StatisticLongRepeat(23.965610058575958, 2.0102777216085546));
			substitution.add(new StatisticPercentageOddRepeats(49.65217391304348, 1.5680328944763842));
			substitution.add(new StatisticNormalOrder(225.3223188405797000000000000, 27.9080248261220500000000000));
			substitution.add(new StatisticMaxBifid0(271.18, 26.95));
			substitution.add(new StatisticMaxBifid3to15(474.1241846046231, 76.0084566498733));
			substitution.add(new StatisticMaxNicodemus3to15(66.01746637747263, 8.475323689459344));
			substitution.add(new StatisticMaxTrifid3to15(3739.0927263340480000000000000, 1076.8573199253747000000000000));
			substitution.add(new StatisticLogDigraph(428.49, 60.44));
			substitution.add(new StatisticLogDigraphBeaufort(581.67, 24.57));
			substitution.add(new StatisticLogDigraphVigenere(582.17, 25.37));
			substitution.add(new StatisticLogDigraphPorta(563.44, 24.75));
			
			List<StatisticType> beaufort = createOrGetList("Beaufort");
			beaufort.add(new StatisticICx1000(42.11, 3.63));
			beaufort.add(new StatisticMaxICx1000(66.53, 3.21));
			beaufort.add(new StatisticKappaICx1000(69.01, 9.56));
			beaufort.add(new StatisticDiagrahpicICx10000(24.40, 7.77));
			beaufort.add(new StatisticEvenDiagrahpicICx10000(30.53, 15.76));
			beaufort.add(new StatisticLongRepeat(10.46, 2.93));
			beaufort.add(new StatisticPercentageOddRepeats(39.57, 11.73));
			beaufort.add(new StatisticNormalOrder(223.84, 31.38));
			beaufort.add(new StatisticMaxBifid0(118.07, 25.77));
			beaufort.add(new StatisticMaxBifid3to15(170.96, 56.25));
			beaufort.add(new StatisticMaxNicodemus3to15(44.91, 3.53));
			beaufort.add(new StatisticMaxTrifid3to15(1166.81, 896.87));
			beaufort.add(new StatisticLogDigraph(426.97, 25.89));
			beaufort.add(new StatisticLogDigraphBeaufort(759.39, 6.56));
			beaufort.add(new StatisticLogDigraphVigenere(576.44, 23.69));
			beaufort.add(new StatisticLogDigraphPorta(558.01, 20.38));
			
			List<StatisticType> vigenere = createOrGetList("Vigenere");
			vigenere.add(new StatisticICx1000(42.99, 3.75));
			vigenere.add(new StatisticMaxICx1000(66.55, 3.19));
			vigenere.add(new StatisticKappaICx1000(68.97, 9.57));
			vigenere.add(new StatisticDiagrahpicICx10000(25.09, 8.09));
			vigenere.add(new StatisticEvenDiagrahpicICx10000(31.01, 15.38));
			vigenere.add(new StatisticLongRepeat(10.5212546335566250000000000, 2.9517345341344905000000000));
			vigenere.add(new StatisticPercentageOddRepeats(39.8347101449275340000000000, 11.5407782805919010000000000));
			vigenere.add(new StatisticNormalOrder(224.0864492753623300000000000, 32.9728235330279060000000000));
			vigenere.add(new StatisticMaxBifid3to15(175.7798110435920000000000000, 58.2320049919340550000000000));
			vigenere.add(new StatisticMaxNicodemus3to15(45.7087715971444300000000000, 3.5810771476970964000000000));
			vigenere.add(new StatisticMaxTrifid3to15(1184.3713387205507000000000000, 907.8437292675648000000000000));
			vigenere.add(new StatisticLogDigraph(427.54, 25.46));
			vigenere.add(new StatisticLogDigraphBeaufort(576.21, 23.67));
			vigenere.add(new StatisticLogDigraphVigenere(759.40, 6.53));
			vigenere.add(new StatisticLogDigraphPorta(566.04, 22.52));
			
			List<StatisticType> playfair = createOrGetList("Playfair");
			playfair.add(new StatisticICx1000(50.98, 2.98));
			playfair.add(new StatisticMaxICx1000(53.97, 3.49));
			playfair.add(new StatisticKappaICx1000(63.49, 7.51));
			playfair.add(new StatisticDiagrahpicICx10000(40.38, 3.86));
			playfair.add(new StatisticEvenDiagrahpicICx10000(76.99, 7.24));
			playfair.add(new StatisticLongRepeat(13.2216252474114190000000000, 1.2690694983027242000000000));
			playfair.add(new StatisticPercentageOddRepeats(30.8497826086956500000000000, 3.7339181673248363000000000));
			playfair.add(new StatisticNormalOrder(233.4631159420290000000000000, 32.8259322483317000000000000));
			playfair.add(new StatisticMaxBifid0(158.46, 22.42));
			playfair.add(new StatisticMaxBifid3to15(253.8848368274454700000000000, 28.3074790701134380000000000));
			playfair.add(new StatisticMaxNicodemus3to15(52.4302649136329800000000000, 3.3217549175988060000000000));
			playfair.add(new StatisticMaxTrifid3to15(1210.2630829565220000000000000, 364.5870986315568400000000000));
			playfair.add(new StatisticLogDigraph(449.98, 42.78));
			playfair.add(new StatisticLogDigraphBeaufort(557.48, 27.85));
			playfair.add(new StatisticLogDigraphVigenere(557.91, 27.09));
			playfair.add(new StatisticLogDigraphPorta(543.30, 24.52));
			playfair.add(new StatisticTextLengthMultiple(2));
			playfair.add(new StatisticDoubleLetter(false));
			
			List<StatisticType> seriatedPlayfair = createOrGetList("Seriated Playfair");
			seriatedPlayfair.add(new StatisticICx1000(49.11, 2.50));
			seriatedPlayfair.add(new StatisticMaxICx1000(50.98, 3.01));
			seriatedPlayfair.add(new StatisticKappaICx1000(61.54, 6.77));
			seriatedPlayfair.add(new StatisticDiagrahpicICx10000(26.42, 2.85));
			seriatedPlayfair.add(new StatisticEvenDiagrahpicICx10000(26.67, 3.58));
			seriatedPlayfair.add(new StatisticLongRepeat(8.59, 1.26));
			seriatedPlayfair.add(new StatisticPercentageOddRepeats(49.44, 2.34));
			seriatedPlayfair.add(new StatisticNormalOrder(232.46, 32.85));
			seriatedPlayfair.add(new StatisticMaxBifid0(150.77, 19.73));
			seriatedPlayfair.add(new StatisticMaxBifid3to15(227.30, 70.01));
			seriatedPlayfair.add(new StatisticMaxNicodemus3to15(50.70, 3.00));
			seriatedPlayfair.add(new StatisticMaxTrifid3to15(552.35, 348.77));
			seriatedPlayfair.add(new StatisticLogDigraph(450.54, 39.54));
			seriatedPlayfair.add(new StatisticLogDigraphBeaufort(551.36, 28.03));
			seriatedPlayfair.add(new StatisticLogDigraphVigenere(552.18, 27.97));
			seriatedPlayfair.add(new StatisticLogDigraphPorta(538.39, 25.16));
			seriatedPlayfair.add(new StatisticTextLengthMultiple(2));
			seriatedPlayfair.add(new StatisticDoubleLetter2to40(false));
			
			List<StatisticType> solitaire = createOrGetList("Solitaire");
			solitaire.add(new StatisticICx1000(38.46, 0.42));
			solitaire.add(new StatisticMaxICx1000(49.92, 5.73));
			solitaire.add(new StatisticKappaICx1000(40.03, 1.68));
			solitaire.add(new StatisticDiagrahpicICx10000(14.79, 0.87));
			solitaire.add(new StatisticEvenDiagrahpicICx10000(14.78, 1.63));
			solitaire.add(new StatisticLongRepeat(5.1354845549403230000000000, 0.8892331387414816000000000));
			solitaire.add(new StatisticPercentageOddRepeats(49.6165217391304340000000000, 2.9584395740484672000000000));
			solitaire.add(new StatisticNormalOrder(222.4618840579710200000000000, 29.7462382849075200000000000));
			solitaire.add(new StatisticMaxBifid0(92.37, 10.50));
			solitaire.add(new StatisticMaxBifid3to15(105.2713415046132200000000000, 13.8776832252436170000000000));
			solitaire.add(new StatisticMaxNicodemus3to15(40.0047256854201400000000000, 2.5816919104277085000000000));
			solitaire.add(new StatisticMaxTrifid3to15(233.8118871177359000000000000, 159.8055294692080700000000000));
			solitaire.add(new StatisticLogDigraph(427.60, 12.22));
			solitaire.add(new StatisticLogDigraphBeaufort(531.78, 32.80));
			solitaire.add(new StatisticLogDigraphVigenere(531.75, 33.00));
			solitaire.add(new StatisticLogDigraphPorta(517.67, 28.99));
			
			List<StatisticType> swagman = createOrGetList("Swagman");
			swagman.add(new StatisticICx1000(65.81, 2.76));
			swagman.add(new StatisticMaxICx1000(68.04, 3.04));
			swagman.add(new StatisticKappaICx1000(78.30, 7.05));
			swagman.add(new StatisticDiagrahpicICx10000(45.58, 5.10));
			swagman.add(new StatisticEvenDiagrahpicICx10000(43.78, 4.60));
			swagman.add(new StatisticLongRepeat(12.1786457684977110000000000, 1.3282465483966868000000000));
			swagman.add(new StatisticPercentageOddRepeats(47.0241304347826060000000000, 4.5797519438332180000000000));
			swagman.add(new StatisticNormalOrder(69.8844202898550700000000000, 29.2737289484617600000000000));
			swagman.add(new StatisticMaxBifid3to15(339.8914879313608000000000000, 65.0130912307681700000000000));
			swagman.add(new StatisticMaxNicodemus3to15(67.0952068842552500000000000, 8.6820672665165700000000000));
			swagman.add(new StatisticMaxTrifid3to15(1151.3435874362190000000000000, 476.9147087108571000000000000));
			
			List<StatisticType> hill = createOrGetList("Hill");
			hill.add(new StatisticICx1000(41.93, 2.51));
			hill.add(new StatisticMaxICx1000(48.00, 5.03));
			hill.add(new StatisticKappaICx1000(56.35, 8.57));
			hill.add(new StatisticDiagrahpicICx10000(32.82, 3.04));
			hill.add(new StatisticEvenDiagrahpicICx10000(76.90, 7.25));
			hill.add(new StatisticLongRepeat(11.0574608954837430000000000, 1.5954853220975898000000000));
			hill.add(new StatisticPercentageOddRepeats(21.8755072463768130000000000, 3.4266022534824447000000000));
			hill.add(new StatisticNormalOrder(215.5181159420290000000000000, 30.2644808686260550000000000));
			hill.add(new StatisticMaxBifid0(111.79, 19.61));
			hill.add(new StatisticMaxBifid3to15(206.0340229309999500000000000, 23.6102261329829060000000000));
			hill.add(new StatisticMaxNicodemus3to15(43.6375430128524700000000000, 3.0024386382334796000000000));
			hill.add(new StatisticMaxTrifid3to15(949.5763054281967000000000000, 343.3313308706509600000000000));
			hill.add(new StatisticLogDigraph(435.79, 26.22));
			hill.add(new StatisticLogDigraphBeaufort(548.25, 30.74));
			hill.add(new StatisticLogDigraphVigenere(548.33, 30.53));
			hill.add(new StatisticLogDigraphPorta(530.79, 26.79));
			
			List<StatisticType> amsco = createOrGetList("AMSCO");
			amsco.add(new StatisticICx1000(66.00, 2.71));
			amsco.add(new StatisticMaxICx1000(68.12, 3.15));
			amsco.add(new StatisticKappaICx1000(80.85, 7.47));
			amsco.add(new StatisticDiagrahpicICx10000(46.67, 4.14));
			amsco.add(new StatisticEvenDiagrahpicICx10000(46.68, 4.95));
			amsco.add(new StatisticLongRepeat(12.3952488616742400000000000, 1.2195949387679033000000000));
			amsco.add(new StatisticPercentageOddRepeats(49.5107246376811600000000000, 1.7129179433636794000000000));
			amsco.add(new StatisticNormalOrder(72.5507246376811600000000000, 30.1082762139631600000000000));
			amsco.add(new StatisticMaxBifid0(277.92, 31.40));
			amsco.add(new StatisticMaxBifid3to15(307.4907450238189000000000000, 48.8208789511897700000000000));
			amsco.add(new StatisticMaxNicodemus3to15(66.8789890086337500000000000, 8.6858174374260350000000000));
			amsco.add(new StatisticMaxTrifid3to15(930.3247217946657000000000000, 328.8778481774621000000000000));
			amsco.add(new StatisticLogDigraph(691.99, 7.79));
			amsco.add(new StatisticLogDigraphBeaufort(579.08, 25.30));
			amsco.add(new StatisticLogDigraphVigenere(693.02, 7.01));
			amsco.add(new StatisticLogDigraphPorta(565.05, 22.86));
			
			List<StatisticType> bifid = createOrGetList("Bifid");
			bifid.add(new StatisticICx1000(45.71, 3.08));
			bifid.add(new StatisticMaxICx1000(51.69, 4.42));
			bifid.add(new StatisticKappaICx1000(60.60, 8.31));
			bifid.add(new StatisticDiagrahpicICx10000(24.18, 4.71));
			bifid.add(new StatisticEvenDiagrahpicICx10000(27.41, 14.12));
			bifid.add(new StatisticLongRepeat(8.47, 1.91));
			bifid.add(new StatisticPercentageOddRepeats(47.21, 7.22));
			bifid.add(new StatisticNormalOrder(192.41, 27.67));
			bifid.add(new StatisticMaxBifid0(149.14, 69.68));
			bifid.add(new StatisticMaxBifid3to15(334.40, 126.10));
			bifid.add(new StatisticMaxNicodemus3to15(48.32, 4.04));
			bifid.add(new StatisticMaxTrifid3to15(819.11, 811.16));
			bifid.add(new StatisticLogDigraph(488.75, 29.66));
			bifid.add(new StatisticLogDigraphPorta(538.89, 25.61));
			bifid.add(new StatisticLogDigraphAutokeyBeaufort(533.16, 33.17));
			bifid.add(new StatisticLogDigraphAutokeyPorta(465.85, 22.85));
			bifid.add(new StatisticLogDigraphAutokeyVariant(533.29, 33.19));
			bifid.add(new StatisticLogDigraphAutokeyVigenere(533.92, 33.35));
			bifid.add(new StatisticLogDigraphPortax(150.56, 206.81));
			
			List<StatisticType> bifid0 = createOrGetList("Bifid P:0");
			bifid0.add(new StatisticICx1000(45.71, 3.06));
			bifid0.add(new StatisticMaxICx1000(47.56, 3.54));
			bifid0.add(new StatisticKappaICx1000(64.95, 8.69));
			bifid0.add(new StatisticDiagrahpicICx10000(24.30, 3.34));
			bifid0.add(new StatisticEvenDiagrahpicICx10000(24.70, 3.98));
			bifid0.add(new StatisticLongRepeat(8.6437355142257400000000000, 1.3075777375681608000000000));
			bifid0.add(new StatisticPercentageOddRepeats(49.1687681159420300000000000, 2.6366692188094520000000000));
			bifid0.add(new StatisticNormalOrder(195.6653623188405800000000000, 27.7081302398854880000000000));
			bifid0.add(new StatisticMaxBifid0(375.0180671883381400000000000, 86.8684127654248400000000000));
			bifid0.add(new StatisticMaxBifid3to15(158.1277213763352400000000000, 24.5793739964456200000000000));
			bifid0.add(new StatisticMaxNicodemus3to15(47.3077039774720100000000000, 3.7183913405654010000000000));
			bifid0.add(new StatisticMaxTrifid3to15(507.1672628980072700000000000, 266.0493779362677000000000000));
			bifid0.add(new StatisticLogDigraph(485.04, 29.57));
			
			List<StatisticType> porta = createOrGetList("Porta");
			porta.add(new StatisticICx1000(42.19, 3.78));
			porta.add(new StatisticMaxICx1000(66.52, 3.20));
			porta.add(new StatisticKappaICx1000(68.83, 9.68));
			porta.add(new StatisticDiagrahpicICx10000(24.35, 7.94));
			porta.add(new StatisticEvenDiagrahpicICx10000(30.22, 15.37));
			porta.add(new StatisticLongRepeat(10.47, 3.00));
			porta.add(new StatisticPercentageOddRepeats(39.65, 12.03));
			porta.add(new StatisticNormalOrder(226.59, 35.47));
			porta.add(new StatisticMaxBifid0(118.52, 26.41));
			porta.add(new StatisticMaxBifid3to15(170.61, 56.75));
			porta.add(new StatisticMaxNicodemus3to15(45.02, 3.72));
			porta.add(new StatisticMaxTrifid3to15(1180.48, 912.31));
			porta.add(new StatisticLogDigraph(424.07, 30.38));
			porta.add(new StatisticLogDigraphBeaufort(574.33, 23.31));
			porta.add(new StatisticLogDigraphVigenere(594.77, 23.25));
			porta.add(new StatisticLogDigraphPorta(759.31, 6.74));
			
			List<StatisticType> enigma = createOrGetList("Enigma NOPLUGBOARD");
			enigma.add(new StatisticICx1000(38.50, 0.43));
			enigma.add(new StatisticMaxICx1000(40.07, 1.69));
			enigma.add(new StatisticKappaICx1000(49.87, 5.66));
			enigma.add(new StatisticDiagrahpicICx10000(14.82, 0.89));
			enigma.add(new StatisticEvenDiagrahpicICx10000(14.85, 1.72));
			enigma.add(new StatisticLongRepeat(5.15, 0.90));
			enigma.add(new StatisticPercentageOddRepeats(49.56, 3.09));
			enigma.add(new StatisticNormalOrder(248.33, 27.69));
			enigma.add(new StatisticMaxBifid0(92.89, 10.65));
			enigma.add(new StatisticMaxBifid3to15(105.43, 12.67));
			enigma.add(new StatisticMaxNicodemus3to15(40.15, 1.95));
			enigma.add(new StatisticMaxTrifid3to15(234.66, 160.42));
			enigma.add(new StatisticLogDigraph(418.27, 12.27));
			enigma.add(new StatisticLogDigraphPorta(518.66, 29.14));
			enigma.add(new StatisticLogDigraphAutokeyBeaufort(531.92, 33.05));
			enigma.add(new StatisticLogDigraphAutokeyPorta(464.62, 23.28));
			enigma.add(new StatisticLogDigraphAutokeyVariant(531.94, 32.90));
			enigma.add(new StatisticLogDigraphAutokeyVigenere(531.81, 32.98));
			enigma.add(new StatisticLogDigraphPortax(149.94, 205.56));
			
			List<StatisticType> bazeries = createOrGetList("Bazeries");
			bazeries.add(new StatisticICx1000(66.21, 2.70));
			bazeries.add(new StatisticMaxICx1000(68.47, 3.22));
			bazeries.add(new StatisticKappaICx1000(78.50, 7.52));
			bazeries.add(new StatisticDiagrahpicICx10000(63.89, 6.21));
			bazeries.add(new StatisticEvenDiagrahpicICx10000(64.93, 8.15));
			bazeries.add(new StatisticLongRepeat(19.16, 2.07));
			bazeries.add(new StatisticPercentageOddRepeats(49.40, 1.70));
			bazeries.add(new StatisticNormalOrder(238.77, 20.61));
			bazeries.add(new StatisticMaxBifid0(272.98, 29.53));
			bazeries.add(new StatisticMaxBifid3to15(405.23, 44.16));
			bazeries.add(new StatisticMaxNicodemus3to15(67.62, 2.96));
			bazeries.add(new StatisticMaxTrifid3to15(2133.32, 631.66));
			bazeries.add(new StatisticLogDigraph(485.48, 36.77));
			bazeries.add(new StatisticLogDigraphBeaufort(579.48, 25.50));
			bazeries.add(new StatisticLogDigraphVigenere(578.51, 25.18));
			bazeries.add(new StatisticLogDigraphPorta(555.82, 25.00));
			
			List<StatisticType> trifid = createOrGetList("Trifid");
			trifid.add(new StatisticICx1000(39.94, 2.34));
			trifid.add(new StatisticMaxICx1000(46.20, 4.32));
			trifid.add(new StatisticKappaICx1000(54.23, 7.88));
			trifid.add(new StatisticDiagrahpicICx10000(17.11, 2.63));
			trifid.add(new StatisticEvenDiagrahpicICx10000(17.60, 3.56));
			trifid.add(new StatisticLongRepeat(6.41, 1.77));
			trifid.add(new StatisticPercentageOddRepeats(48.72, 3.73));
			trifid.add(new StatisticNormalOrder(0.10, 4.28));
			trifid.add(new StatisticMaxBifid0(0.04, 2.17));
			trifid.add(new StatisticMaxBifid3to15(0.05, 3.05));
			trifid.add(new StatisticMaxNicodemus3to15(0.01, 0.68));
			trifid.add(new StatisticMaxTrifid3to15(1598.23, 1572.71));
			trifid.add(new StatisticLogDigraph(417.69, 25.51));
			trifid.add(new StatisticLogDigraphBeaufort(0.00, 0.00));
			trifid.add(new StatisticLogDigraphVigenere(0.00, 0.00));
			trifid.add(new StatisticLogDigraphPorta(0.00, 0.00));
			
			List<StatisticType> fourSquare = createOrGetList("Four Square");
			fourSquare.add(new StatisticICx1000(46.28, 1.73));
			fourSquare.add(new StatisticMaxICx1000(54.24, 2.45));
			fourSquare.add(new StatisticKappaICx1000(61.97, 6.78));
			fourSquare.add(new StatisticDiagrahpicICx10000(35.50, 2.92));
			fourSquare.add(new StatisticEvenDiagrahpicICx10000(77.06, 7.25));
			fourSquare.add(new StatisticLongRepeat(12.36, 1.17));
			fourSquare.add(new StatisticPercentageOddRepeats(21.98, 3.76));
			fourSquare.add(new StatisticNormalOrder(231.64, 27.05));
			fourSquare.add(new StatisticMaxBifid0(135.38, 14.64));
			fourSquare.add(new StatisticMaxBifid3to15(223.96, 21.69));
			fourSquare.add(new StatisticMaxNicodemus3to15(48.10, 2.46));
			fourSquare.add(new StatisticMaxTrifid3to15(1082.92, 312.10));
			fourSquare.add(new StatisticLogDigraph(449.68, 28.87));
			fourSquare.add(new StatisticLogDigraphBeaufort(558.64, 25.79));
			fourSquare.add(new StatisticLogDigraphVigenere(558.64, 25.91));
			fourSquare.add(new StatisticLogDigraphPorta(543.91, 23.25));
			fourSquare.add(new StatisticTextLengthMultiple(2));
			
			List<StatisticType> twoSquare = createOrGetList("Two Square");
			twoSquare.add(new StatisticICx1000(46.09, 2.29));
			twoSquare.add(new StatisticMaxICx1000(52.91, 3.59));
			twoSquare.add(new StatisticKappaICx1000(61.05, 7.58));
			twoSquare.add(new StatisticDiagrahpicICx10000(35.67, 3.08));
			twoSquare.add(new StatisticEvenDiagrahpicICx10000(76.97, 7.25));
			twoSquare.add(new StatisticLongRepeat(12.33, 1.17));
			twoSquare.add(new StatisticPercentageOddRepeats(22.94, 3.72));
			twoSquare.add(new StatisticNormalOrder(190.32, 32.72));
			twoSquare.add(new StatisticMaxBifid0(134.74, 18.48));
			twoSquare.add(new StatisticMaxBifid3to15(224.40, 23.36));
			twoSquare.add(new StatisticMaxNicodemus3to15(47.74, 2.79));
			twoSquare.add(new StatisticMaxTrifid3to15(1091.40, 326.08));
			twoSquare.add(new StatisticLogDigraph(486.03, 30.99));
			twoSquare.add(new StatisticLogDigraphBeaufort(557.72, 26.88));
			twoSquare.add(new StatisticLogDigraphVigenere(558.48, 27.38));
			twoSquare.add(new StatisticLogDigraphPorta(541.36, 24.30));
			twoSquare.add(new StatisticTextLengthMultiple(2));
			
			List<StatisticType> portax = createOrGetList("Portax");
			portax.add(new StatisticICx1000(42.56, 1.28));
			portax.add(new StatisticMaxICx1000(48.14, 3.73));
			portax.add(new StatisticKappaICx1000(56.56, 9.21));
			portax.add(new StatisticDiagrahpicICx10000(19.78, 2.95));
			portax.add(new StatisticEvenDiagrahpicICx10000(21.08, 3.66));
			portax.add(new StatisticLongRepeat(7.06, 1.46));
			portax.add(new StatisticPercentageOddRepeats(46.95, 3.65));
			portax.add(new StatisticNormalOrder(220.65, 16.14));
			portax.add(new StatisticMaxBifid0(114.37, 13.63));
			portax.add(new StatisticMaxBifid3to15(166.64, 51.45));
			portax.add(new StatisticMaxNicodemus3to15(44.96, 2.38));
			portax.add(new StatisticMaxTrifid3to15(490.75, 653.48));
			portax.add(new StatisticLogDigraph(437.86, 14.46));
			portax.add(new StatisticLogDigraphBeaufort(544.10, 27.50));
			portax.add(new StatisticLogDigraphVigenere(544.55, 27.59));
			portax.add(new StatisticLogDigraphPorta(523.71, 25.50));
			portax.add(new StatisticLogDigraphSlidefair(542.32, 28.57));
			portax.add(new StatisticLogDigraphPortax(651.59, 42.82));
			
			List<StatisticType> vigenereAutokey = createOrGetList("Vigenere Autokey");
			vigenereAutokey.add(new StatisticICx1000(39.83, 0.72));
			vigenereAutokey.add(new StatisticMaxICx1000(41.49, 1.95));
			vigenereAutokey.add(new StatisticKappaICx1000(65.64, 8.77));
			vigenereAutokey.add(new StatisticDiagrahpicICx10000(16.90, 1.67));
			vigenereAutokey.add(new StatisticEvenDiagrahpicICx10000(16.89, 2.28));
			vigenereAutokey.add(new StatisticLongRepeat(6.23, 1.24));
			vigenereAutokey.add(new StatisticPercentageOddRepeats(49.65, 2.83));
			vigenereAutokey.add(new StatisticNormalOrder(195.42, 22.27));
			vigenereAutokey.add(new StatisticMaxBifid0(99.95, 10.71));
			vigenereAutokey.add(new StatisticMaxBifid3to15(115.96, 16.05));
			vigenereAutokey.add(new StatisticMaxNicodemus3to15(41.50, 2.25));
			vigenereAutokey.add(new StatisticMaxTrifid3to15(347.07, 297.28));
			vigenereAutokey.add(new StatisticLogDigraph(443.37, 13.38));
			vigenereAutokey.add(new StatisticLogDigraphReversed(154.42, 211.65));
			vigenereAutokey.add(new StatisticLogDigraphBeaufort(536.48, 32.42));
			vigenereAutokey.add(new StatisticLogDigraphVigenere(537.40, 32.03));
			vigenereAutokey.add(new StatisticLogDigraphVariant(537.22, 31.73));
			vigenereAutokey.add(new StatisticLogDigraphPorta(520.94, 28.25));
			vigenereAutokey.add(new StatisticLogDigraphAutokeyBeaufort(533.24, 33.04));
			vigenereAutokey.add(new StatisticLogDigraphAutokeyPorta(464.16, 23.12));
			vigenereAutokey.add(new StatisticLogDigraphAutokeyVariant(533.34, 33.19));
			vigenereAutokey.add(new StatisticLogDigraphAutokeyVigenere(759.68, 6.68));
			vigenereAutokey.add(new StatisticLogDigraphPortax(148.22, 203.25));
			
			List<StatisticType> beaufortAutokey = createOrGetList("Beaufort Autokey");
			beaufortAutokey.add(new StatisticICx1000(39.79, 0.76));
			beaufortAutokey.add(new StatisticMaxICx1000(41.38, 1.90));
			beaufortAutokey.add(new StatisticKappaICx1000(51.57, 5.69));
			beaufortAutokey.add(new StatisticDiagrahpicICx10000(16.72, 1.61));
			beaufortAutokey.add(new StatisticEvenDiagrahpicICx10000(16.70, 2.19));
			beaufortAutokey.add(new StatisticLongRepeat(6.09, 1.22));
			beaufortAutokey.add(new StatisticPercentageOddRepeats(49.73, 3.10));
			beaufortAutokey.add(new StatisticNormalOrder(206.71, 25.04));
			beaufortAutokey.add(new StatisticMaxBifid0(98.74, 11.10));
			beaufortAutokey.add(new StatisticMaxBifid3to15(115.28, 16.56));
			beaufortAutokey.add(new StatisticMaxNicodemus3to15(41.49, 2.11));
			beaufortAutokey.add(new StatisticMaxTrifid3to15(338.71, 289.97));
			beaufortAutokey.add(new StatisticLogDigraph(439.99, 12.54));
			beaufortAutokey.add(new StatisticLogDigraphPorta(519.96, 28.57));
			beaufortAutokey.add(new StatisticLogDigraphAutokeyBeaufort(759.68, 6.70));
			beaufortAutokey.add(new StatisticLogDigraphAutokeyPorta(464.34, 23.59));
			beaufortAutokey.add(new StatisticLogDigraphAutokeyVariant(567.86, 24.67));
			beaufortAutokey.add(new StatisticLogDigraphAutokeyVigenere(533.45, 33.42));
			beaufortAutokey.add(new StatisticLogDigraphPortax(155.88, 213.67));


			List<StatisticType> portaAutokey = createOrGetList("Porta Autokey move to right");
			portaAutokey.add(new StatisticICx1000(39.32, 0.69));
			portaAutokey.add(new StatisticMaxICx1000(40.98, 1.92));
			portaAutokey.add(new StatisticKappaICx1000(50.74, 5.63));
			portaAutokey.add(new StatisticDiagrahpicICx10000(16.28, 1.60));
			portaAutokey.add(new StatisticEvenDiagrahpicICx10000(16.30, 2.22));
			portaAutokey.add(new StatisticLongRepeat(6.02, 1.30));
			portaAutokey.add(new StatisticPercentageOddRepeats(49.57, 3.01));
			portaAutokey.add(new StatisticNormalOrder(224.66, 28.04));
			portaAutokey.add(new StatisticMaxBifid0(96.43, 11.10));
			portaAutokey.add(new StatisticMaxBifid3to15(113.80, 16.20));
			portaAutokey.add(new StatisticMaxNicodemus3to15(40.85, 1.69));
			portaAutokey.add(new StatisticMaxTrifid3to15(338.97, 289.56));
			portaAutokey.add(new StatisticLogDigraph(429.15, 14.56));
			portaAutokey.add(new StatisticLogDigraphPorta(523.10, 28.50));
			portaAutokey.add(new StatisticLogDigraphAutokeyBeaufort(532.19, 32.92));
			portaAutokey.add(new StatisticLogDigraphAutokeyPorta(467.17, 22.21));
			portaAutokey.add(new StatisticLogDigraphAutokeyVariant(532.37, 33.16));
			portaAutokey.add(new StatisticLogDigraphAutokeyVigenere(532.25, 33.11));
			portaAutokey.add(new StatisticLogDigraphPortax(147.77, 202.64));
			
			
			List<StatisticType> variantAutokey = createOrGetList("Variant Autokey");
			variantAutokey.add(new StatisticICx1000(39.78, 0.77));
			variantAutokey.add(new StatisticMaxICx1000(41.39, 1.88));
			variantAutokey.add(new StatisticKappaICx1000(51.57, 5.77));
			variantAutokey.add(new StatisticDiagrahpicICx10000(16.74, 1.62));
			variantAutokey.add(new StatisticEvenDiagrahpicICx10000(16.71, 2.18));
			variantAutokey.add(new StatisticLongRepeat(6.09, 1.23));
			variantAutokey.add(new StatisticPercentageOddRepeats(49.75, 3.02));
			variantAutokey.add(new StatisticNormalOrder(204.75, 24.62));
			variantAutokey.add(new StatisticMaxBifid0(98.86, 11.57));
			variantAutokey.add(new StatisticMaxBifid3to15(115.05, 16.44));
			variantAutokey.add(new StatisticMaxNicodemus3to15(41.48, 2.09));
			variantAutokey.add(new StatisticMaxTrifid3to15(338.61, 287.27));
			variantAutokey.add(new StatisticLogDigraph(440.75, 12.90));
			variantAutokey.add(new StatisticLogDigraphPorta(520.13, 28.65));
			variantAutokey.add(new StatisticLogDigraphAutokeyBeaufort(567.88, 25.07));
			variantAutokey.add(new StatisticLogDigraphAutokeyPorta(463.56, 22.81));
			variantAutokey.add(new StatisticLogDigraphAutokeyVariant(759.68, 6.69));
			variantAutokey.add(new StatisticLogDigraphAutokeyVigenere(533.70, 33.23));
			variantAutokey.add(new StatisticLogDigraphPortax(156.43, 214.49));
			
			List<StatisticType> vigenereSlidefair = createOrGetList("Vigenere Slidefair");
			vigenereSlidefair.add(new StatisticICx1000(40.24, 1.96));
			vigenereSlidefair.add(new StatisticMaxICx1000(57.03, 7.28));
			vigenereSlidefair.add(new StatisticKappaICx1000(56.91, 10.99));
			vigenereSlidefair.add(new StatisticDiagrahpicICx10000(18.60, 3.15));
			vigenereSlidefair.add(new StatisticEvenDiagrahpicICx10000(25.44, 8.01));
			vigenereSlidefair.add(new StatisticLongRepeat(6.73, 1.37));
			vigenereSlidefair.add(new StatisticPercentageOddRepeats(40.21, 6.22));
			vigenereSlidefair.add(new StatisticNormalOrder(239.96, 37.39));
			vigenereSlidefair.add(new StatisticMaxBifid0(104.69, 16.41));
			vigenereSlidefair.add(new StatisticMaxBifid3to15(138.08, 32.85));
			vigenereSlidefair.add(new StatisticMaxNicodemus3to15(42.93, 2.63));
			vigenereSlidefair.add(new StatisticMaxTrifid3to15(473.19, 291.01));
			vigenereSlidefair.add(new StatisticLogDigraph(421.81, 24.95));
			vigenereSlidefair.add(new StatisticLogDigraphBeaufort(561.08, 27.13));
			vigenereSlidefair.add(new StatisticLogDigraphVigenere(609.08, 44.72));
			vigenereSlidefair.add(new StatisticLogDigraphPorta(544.63, 25.42));
			vigenereSlidefair.add(new StatisticLogDigraphSlidefair(748.12, 7.88));
			vigenereSlidefair.add(new StatisticLogDigraphPortax(432.88, 20.27));

			List<StatisticType> beaufortSlidefair = createOrGetList("Beaufort Slidefair");
			beaufortSlidefair.add(new StatisticICx1000(41.90, 3.46));
			beaufortSlidefair.add(new StatisticMaxICx1000(56.86, 7.39));
			beaufortSlidefair.add(new StatisticKappaICx1000(56.98, 11.21));
			beaufortSlidefair.add(new StatisticDiagrahpicICx10000(19.59, 4.39));
			beaufortSlidefair.add(new StatisticEvenDiagrahpicICx10000(24.34, 8.20));
			beaufortSlidefair.add(new StatisticLongRepeat(7.04, 1.58));
			beaufortSlidefair.add(new StatisticPercentageOddRepeats(44.29, 3.91));
			beaufortSlidefair.add(new StatisticNormalOrder(235.88, 29.91));
			beaufortSlidefair.add(new StatisticMaxBifid0(113.57, 23.16));
			beaufortSlidefair.add(new StatisticMaxBifid3to15(159.95, 56.30));
			beaufortSlidefair.add(new StatisticMaxNicodemus3to15(45.60, 3.83));
			beaufortSlidefair.add(new StatisticMaxTrifid3to15(545.84, 350.39));
			beaufortSlidefair.add(new StatisticLogDigraph(419.43, 25.37));
			beaufortSlidefair.add(new StatisticLogDigraphBeaufort(607.34, 45.32));
			beaufortSlidefair.add(new StatisticLogDigraphVigenere(559.09, 27.18));
			beaufortSlidefair.add(new StatisticLogDigraphPorta(542.33, 25.24));
			beaufortSlidefair.add(new StatisticLogDigraphSlidefair(747.08, 8.51));
			beaufortSlidefair.add(new StatisticLogDigraphPortax(431.79, 23.68));
			
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
