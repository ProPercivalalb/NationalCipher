package nationalcipher.wip;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javalibrary.cipher.stats.ReadableText;
import javalibrary.dict.Dictionary;
import javalibrary.file.DraftFile;
import javalibrary.fitness.ChiSquared;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.base.other.Playfair;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Enigma;
import nationalcipher.cipher.decrypt.methods.DictionaryAttack;
import nationalcipher.cipher.stats.StatisticsRef;
import nationalcipher.cipher.stats.TextStatistic;
import nationalcipher.cipher.stats.types.StatisticBifid0;
import nationalcipher.cipher.stats.types.StatisticDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticEvenDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticICx1000;
import nationalcipher.cipher.stats.types.StatisticKappaICx1000;
import nationalcipher.cipher.stats.types.StatisticLogDigraph;
import nationalcipher.cipher.stats.types.StatisticLogDigraphReversed;
import nationalcipher.cipher.stats.types.StatisticLongRepeat;
import nationalcipher.cipher.stats.types.StatisticMaxBifid3to15;
import nationalcipher.cipher.stats.types.StatisticMaxICx1000;
import nationalcipher.cipher.stats.types.StatisticNormalOrder;
import nationalcipher.cipher.stats.types.StatisticPercentageOddRepeats;
import nationalcipher.cipher.stats.types.StatisticTrigraphNoOverlapICx100000;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.transposition.Routes;

public class StatCompiler {
	public static LinkedHashMap<String, Class<? extends TextStatistic>> map = new LinkedHashMap<String, Class<? extends TextStatistic>>();
	
public static Map<Character, Double> frequencyMap;
	
	public char[] frequencyLargest;

	public static Map<Character, Double> getCharacterFrequency() {
		if(frequencyMap == null) {
			frequencyMap = new HashMap<Character, Double>();
			frequencyMap.put('A', 12.920);
			frequencyMap.put('B', 2.844D);
			frequencyMap.put('C', 1.463 + 1.156);
			frequencyMap.put('D', 5.206D);
			frequencyMap.put('E', 9.912);
			frequencyMap.put('F', 0.461D);
			frequencyMap.put('G', 1.253D + 	1.125);
			frequencyMap.put('H', 1.212D);
			frequencyMap.put('I', 9.600);
			frequencyMap.put('J', 0.034D);
			frequencyMap.put('K', 5.683D);
			frequencyMap.put('L', 5.922);
			frequencyMap.put('M', 3.752D);
			frequencyMap.put('N', 7.987);
			frequencyMap.put('O', 2.976 + 0.777);
			frequencyMap.put('P', 0.886D);
			frequencyMap.put('Q', 0D);
			frequencyMap.put('R', 7.722D);
			frequencyMap.put('S', 3.014 + 1.780);
			frequencyMap.put('T', 3.314D);
			frequencyMap.put('U', 3.235 + 1.854);
			frequencyMap.put('V', 	0.959);
			frequencyMap.put('W', 0D);
			frequencyMap.put('X', 0D);
			frequencyMap.put('Y', 3.336);
			frequencyMap.put('Z', 1.500);	
		}
		return frequencyMap;
	}
	
	public static void main(String[] args) {
		Timer timer = new Timer();
		char[] plainText = "PTEFUQDGCSSWGQUNYKVLCWBTAJPCBYAIRGFYYLCOSBVATSYFMVVMQLRGVXXXMXXSDMKNKGTAACTLLWWTSUJKPHFWQARTZUWZPEDJMGBDMKSBDCCIDOLRJWAYQJQAHYKGVPVAUSTBAACKKYEAUZSJODJBEBETGFCRSUDGHOQFKHPRJIYMVBIMQNLUQMDFDIIOQASYJOZCIMLBVKCHTCKBJWEBEDXMPINDOMHOTYHIVCTUSUQWLKEGDDQZOIFOQUYNUQARAXMFVQOSGTXSTVHSEIZGYGDQGXORNYREUYSNCZYJPHDPTVWGGKCHLDYYRBWKEGMJTBKDJLIYDJDFEGFYWNJUWVTFAFDLKQZLYMPKLQVGIGUXSVNXXJXGXTMKGARMGSHALGZDDUTTOXHMOVPUPTYJKIIGZJPYVBCGRYZZHDOPMPVYANJAVXHFGFDSMGHCJYLQCTZIAAHBFPYTGVEMLAPSNCWKHIWLBOYYSSQKWIZNZDGGLKQPIVRXKYJNQWXECVZCTTPFZATLLXWXMYVFPUVYOXBUQHDJLHSODKPLVOLZJCGHCJCVXGBALHYJHGJSZGOUWUCQOWDOGTTUGLVSJZEQXSKOIBWXLUWCWZCDVYUTYNUWKIDFPGPBNUHVAWEHZGMKGZTUBJBLMHJCCHZAQTGOMKMGCIOZQZUQNBAGMQPKPJGURMKQWYATGVMERODREKCYNHWKZLYCPBSHCZOPSJWVZGWEVICKZCYLWHDXZXLQWTIPOFRRTBBHDKTFJKPOKFWTGQWRDCMCBSYVRTOTQZQBNYTIRNCCIDFJFGJSBNYBZXFXEVXZBMHQBEJZUUMYNWVCNLXTAMUDAUYQTGUCIKEXNXWESESQQZSFOREYKJFMDUCWMUGHBQTHPIMQHMFMMLBTRAHJUKIFDPLHDBJQZZTBJWJTMUTBCDSZZUWZLWLGZYLYMEXKQXQIQKKFHSCRWPYSGYKGBYZXSTDBSMFOEOIAUXKTTWDYJJVRALMAUIQCVXFQYQQWKLHLLZDLTXBOVSBGVB".replaceAll("J", "I").toUpperCase().toCharArray();
		byte[] plainText2 = new byte[plainText.length];
		char[] key = KeyGeneration.ALL_25_CHARS;
		timer.restart();
		for(int i = 0; i < 400000; i++)
			Playfair.decode(plainText, plainText2, key);
		timer.displayTime();
		
		
		timer.restart();
		for(int i = 0; i < 400000; i++)
			Playfair.decode(plainText, plainText2, key);
		timer.displayTime();
		
		
		
		int[] ring = new int[] {0, 0, 0};
		int[] rotors = new int[] {2, 0, 3};
		
		
		//EnigmaMachine machine = EnigmaLib.ENIGMA_I.createWithUhr(4, "AB0", "CD1", "EF2", "GH3", "IJ4", "KL5", "MN6", "OP7", "QR8", "ST9");
		EnigmaMachine machine = EnigmaLib.ENIGMA_I.createWithPlugboard(new int[][] {{0, 12}, {2, 18}, {6, 8}});
		//System.out.println(Enigma.encode(new String(plainText), machine, "XIJ", "ARB", rotors, 1));
		/**timer.restart();
		for(int i = 0; i < 5000; i++)
			Enigma.decode(plainText, plainText2, machine, new int[] {0, 0, 0}, ring, rotors, -1, 0);
		timer.displayTime();
		
		timer.restart();
		for(int i = 0; i < 5000; i++)
		Enigma.decodeFast(plainText, plainText2, machine, new int[] {0, 0, 0}, ring, rotors, 0);
		timer.displayTime();
		
		
		timer.restart();
		for(int i = 0; i < 5000; i++)
		Enigma.decode(plainText, plainText2, machine, new int[] {0, 0, 0}, ring, rotors, -1, 0);
		timer.displayTime();
		
		timer.restart();
		for(int i = 0; i < 5000; i++)
		Enigma.decodeFast(plainText, plainText2, machine, new int[] {0, 0, 0}, ring, rotors, 0);
		timer.displayTime();**/
		
		//System.out.println(new String(cipherText));
		
		//registerStatistics();
		//for(IRandEncrypter en : new IRandEncrypter[] {new ConjugatedBifid()})
		//	for(Class<? extends TextStatistic> clz : map.values())
		//		StatisticHandler.calculateStatPrint(en, clz, 100);
		
		//TODO
		//for(IRandEncrypter en : new IRandEncrypter[] {new Caesar(), new Keyword(), new Affine()})
		//	for(Class<? extends TextStatistic> clz : new Class[] {StatisticLogDigraphAffine.class}) 
		//		StatisticHandler.calculateStatPrint(en, clz, 50);
	}

	private static void registerStatistics() {

		
		//Default all ciphers
		registerStatistic(StatisticsRef.IC_x1000, StatisticICx1000.class);
		registerStatistic(StatisticsRef.IC_MAX_1to15_x1000, StatisticMaxICx1000.class);
		registerStatistic(StatisticsRef.IC_2_TRUE_x10000, StatisticDiagrahpicICx10000.class);
		registerStatistic(StatisticsRef.IC_2_FALSE_x10000, StatisticEvenDiagrahpicICx10000.class);
		registerStatistic(StatisticsRef.IC_3_FALSE_x100000, StatisticTrigraphNoOverlapICx100000.class);
		registerStatistic(StatisticsRef.IC_KAPPA_x1000, StatisticKappaICx1000.class);
		registerStatistic(StatisticsRef.LOG_DIGRAPH, StatisticLogDigraph.class);
		registerStatistic(StatisticsRef.LOG_DIGRAPH_REVERSED, StatisticLogDigraphReversed.class);
		registerStatistic(StatisticsRef.LONG_REPEAT, StatisticLongRepeat.class);
		registerStatistic(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, StatisticPercentageOddRepeats.class);
		registerStatistic(StatisticsRef.NORMAL_ORDER, StatisticNormalOrder.class);
		
		
		registerStatistic(StatisticsRef.BIFID_0, StatisticBifid0.class);
		registerStatistic(StatisticsRef.BIFID_MAX_3to15, StatisticMaxBifid3to15.class);
		//registerStatistic(StatisticsRef.NICODEMUS_MAX_3to15, StatisticMaxNicodemus3to15.class);
		//registerStatistic(StatisticsRef.TRIFID_MAX_3to15, StatisticMaxTrifid3to15.class);
		
		//Vigenere Family
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_AUTOKEY_BEAUFORT, StatisticLogDigraphAutokeyBeaufort.class);
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_AUTOKEY_PORTA, StatisticLogDigraphAutokeyPorta.class);
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_AUTOKEY_VARIANT, StatisticLogDigraphAutokeyVariant.class);
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_AUTOKEY_VIGENERE, StatisticLogDigraphAutokeyVigenere.class);
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_BEAUFORT, StatisticLogDigraphBeaufort.class);
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_PORTA, StatisticLogDigraphPorta.class);
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_PORTAX, StatisticLogDigraphPortax.class);
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_VARIANT, StatisticLogDigraphVariant.class);
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_VIGENERE, StatisticLogDigraphVigenere.class);
		
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_SLIDEFAIR_BEAUFORT, StatisticLogDigraphSlidefairBeaufort.class);
		//registerStatistic(StatisticsRef.LOG_DIGRAPH_SLIDEFAIR_VIGENERE, StatisticLogDigraphSlidefairVigenere.class);
		
		//Boolean statitics
		//registerStatistic(StatisticsRef.DOUBLE_LETTER_EVEN, StatisticDoubleLetter.class);
		//registerStatistic(StatisticsRef.DOUBLE_LETTER_EVEN_2to40, StatisticDoubleLetter2to40.class);
		//registerStatistic(StatisticsRef.TEXT_LENGTH_MULTIPLE, StatisticTextLengthMultiple.class);
		
	}

	public static boolean registerStatistic(String id, Class<? extends TextStatistic> textStatistic) {
		if(map.containsKey(id)) return false;
		
		map.put(id, textStatistic);
		
		return true;
	}
}
