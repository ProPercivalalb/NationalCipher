package nationalcipher.wip;

import java.util.LinkedHashMap;

import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.ProgressiveKey;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.Affine;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.stats.StatisticHandler;
import nationalcipher.cipher.stats.StatisticsRef;
import nationalcipher.cipher.stats.TextStatistic;
import nationalcipher.cipher.stats.types.StatisticDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticEvenDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticICx1000;
import nationalcipher.cipher.stats.types.StatisticKappaICx1000;
import nationalcipher.cipher.stats.types.StatisticLogDigraph;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAffine;
import nationalcipher.cipher.stats.types.StatisticLogDigraphReversed;
import nationalcipher.cipher.stats.types.StatisticLongRepeat;
import nationalcipher.cipher.stats.types.StatisticMaxICx1000;
import nationalcipher.cipher.stats.types.StatisticNormalOrder;
import nationalcipher.cipher.stats.types.StatisticPercentageOddRepeats;
import nationalcipher.cipher.stats.types.StatisticTrigraphNoOverlapICx100000;

public class StatCompiler {
	public static LinkedHashMap<String, Class<? extends TextStatistic>> map = new LinkedHashMap<String, Class<? extends TextStatistic>>();
	
	public static void main(String[] args) {
		System.out.println(new String(ProgressiveKey.decode("WWDBHZAOACZMAVNIYNFADTWPGFHKGEOUPWOCYYWX".toCharArray(), new byte[3179], "POLITICS", 8, 3, VigenereType.VIGENERE)));
		//System.out.println(RailFence.decode("ERIPHINOTLAFHVTDIATLNOWSIPTIOSEEYEHMROTNSIDSHEECNATROLOINYHNMPBRTTTLEUEYNDNAHDFDSIIOIHNAOGDELONOIHEERSEEBEOTESOVAPCAHEMTASAIFEERSOILRETEOEYWMOEBTIEIDIESWSAMTORASOEHPSSSEIFRSTTTFODELMOWDTAFOOESUINAEOSETLYHAOAALRHNVIERLRHREITSHRTARTIFYUTHRSHSNOSEIIIHNORNPRNOLEGERMONETTOAPSHHSDLIABRHFLTSRTEAOAALWEAIAAAFNNENISIHRTSERADRVNTAALAOLINSSETLOTEUKNARISYGESASSIWEETDOGTUEEOELEUDTNWELHAFETOTULGTCNTOTSADFMCUUGAIRNPAINETCLTOYAENGDOEICRDRMALLNWUOHBESITBEETNEENOMSTELSOAFHETITDCPROABRGRPOEUCLTOUDEHBEPARTSLWDTEOWHERFRMALETMLRSSBNMSRMEIYGOIIESYYDODUMETNUEMRTNTOFEAIETETHRCOITUATAACGHASEUTTOIVRESEAWRGIEAOAOCEAYFGNIRAERNANOCHVCSSMEHRSUILBSTEEPTOUNDTGILOMTEETAEGRDRTOIHERONIWTIETWEOOMTAEIAABEUDILNWAOLEAATENASARFORIVIANTERATGNIRMMOTAIHTVDIFUCVLHSSERWSEEAXEVASTTSWRUEETAAHCVLRCETCNTDIEFIRONWSEEETEAERIADACBATRHURPTOCUVRESDOCUAEODHCOBDNTENCNIAAHRIYLSETFETXNTTEIELCNAVBFRBFWDOEHRAKEATTLNAENOBEEITAYNROUDTRSYOSNOERATSNOSAYNMFRSODCCALCNONVAENESCFROGOOTNHCMETEAKEHOAYSPNLAYEEOTHONMNNREBNEFNTALCTOOREOTEDORUOLWDRYPEUGSOGNRWHNEOCRLYRTSOWOEHYEKNTWRNPRHHMLTEDLICSESGBTEEECSOENEAAOTIEAOSRMHTRWVNEWIFYUMMWHTELNCNLEILGGIDITRHGRENSGGINMCTIUNGIATOEEFMAENSEOTTRDPEOAEDXGEETCOERHTOTCHTTNSRHOONOERHOVREFIHEWRELDSFEAHREMEOEEVERTIDVPLLHOSLETFYGRANCUOTUHECEVKEONNKIBHRGFIOEDTTUACDEIIUTUPMSSNEORSOEEIBALYCHSBIHEPTIHONHAUWATOCBWEUEYOAINOOEHUOHT".toCharArray(), 4, 5));
		registerStatistics();
		//for(IRandEncrypter en : new IRandEncrypter[] {new Caesar(), new Keyword(), new Affine()})
		//	for(Class<? extends TextStatistic> clz : map.values())
		//		StatisticHandler.calculateStatPrint(en, clz, 100);
		
		//TODO
		for(IRandEncrypter en : new IRandEncrypter[] {new Caesar(), new Keyword(), new Affine()})
			for(Class<? extends TextStatistic> clz : new Class[] {StatisticLogDigraphAffine.class}) 
				StatisticHandler.calculateStatPrint(en, clz, 50);
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
		
		
		//registerStatistic(StatisticsRef.BIFID_MAX_0, StatisticMaxBifid0.class);
		//registerStatistic(StatisticsRef.BIFID_MAX_3to15, StatisticMaxBifid3to15.class);
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
