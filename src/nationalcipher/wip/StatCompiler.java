package nationalcipher.wip;

import java.util.LinkedHashMap;

import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.RandomEncrypter;
import nationalcipher.cipher.base.other.Hill;
import nationalcipher.cipher.base.other.Playfair;
import nationalcipher.cipher.base.substitution.Affine;
import nationalcipher.cipher.base.substitution.Beaufort;
import nationalcipher.cipher.base.substitution.BeaufortAutokey;
import nationalcipher.cipher.base.substitution.BeaufortNicodemus;
import nationalcipher.cipher.base.substitution.BeaufortSlidefair;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.FourSquare;
import nationalcipher.cipher.base.substitution.FractionatedMorse;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.base.substitution.Porta;
import nationalcipher.cipher.base.substitution.PortaAutokey;
import nationalcipher.cipher.base.substitution.PortaNicodemus;
import nationalcipher.cipher.base.substitution.Portax;
import nationalcipher.cipher.base.substitution.TriSquare;
import nationalcipher.cipher.base.substitution.TwoSquare;
import nationalcipher.cipher.base.substitution.Variant;
import nationalcipher.cipher.base.substitution.VariantAutokey;
import nationalcipher.cipher.base.substitution.VariantNicodemus;
import nationalcipher.cipher.base.substitution.VariantSlidefair;
import nationalcipher.cipher.base.substitution.Vigenere;
import nationalcipher.cipher.base.substitution.VigenereAutokey;
import nationalcipher.cipher.base.substitution.VigenereNicodemus;
import nationalcipher.cipher.base.substitution.VigenereSlidefair;
import nationalcipher.cipher.base.transposition.Cadenus;
import nationalcipher.cipher.base.transposition.Myszkowski;
import nationalcipher.cipher.base.transposition.Phillips;
import nationalcipher.cipher.base.transposition.RailFence;
import nationalcipher.cipher.stats.CipherStatistics;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.StatisticHandler;
import nationalcipher.cipher.stats.StatisticsRef;
import nationalcipher.cipher.stats.TextStatistic;
import nationalcipher.cipher.stats.types.StatisticDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticDoubleLetter;
import nationalcipher.cipher.stats.types.StatisticDoubleLetter2to40;
import nationalcipher.cipher.stats.types.StatisticEvenDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticICx1000;
import nationalcipher.cipher.stats.types.StatisticKappaICx1000;
import nationalcipher.cipher.stats.types.StatisticLogDigraph;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAffine;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyBeaufort;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyPorta;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyVariant;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyVigenere;
import nationalcipher.cipher.stats.types.StatisticLogDigraphBeaufort;
import nationalcipher.cipher.stats.types.StatisticLogDigraphCaesar;
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

public class StatCompiler {
	public static LinkedHashMap<String, Class<? extends TextStatistic>> map = new LinkedHashMap<String, Class<? extends TextStatistic>>();
	
	public static void main(String[] args) {
		//System.out.println(Phillips.decode("CHKKRXNKQPMEREZGVAQKEWHBPYYBFKFBEOXVQUFZOKHTUKVVANDVZZVTAQNVHVEZCMTFKUHIXWLVANYXAZHYFNPVYSVZQPTUTAQNPUKRHCRPTXEKIAIFPDMZOFKILIPPCFKPTPCISDOQFARVHATLFKEHWLHLVXIKKHZOCQUIPLRVPYEZAQNTXMOUKRHAYKEOHQNKKNPVLXOMCXCOIQAREAMDQIZLHHAMUGMBYUHLZNEVANFPPUKOFPIVNNZOYITUTAQNGUFARHNGTKIXEOHKZOXQSOUFKOSIPZEHQZROUMTNZZOCXUUAFZPXBUAXTPUQZEFBRNUNPVEKYSSNSIENBOCHXCCYZUCWEHWLQPHVUTKHZNPVMXQGERKUMNUTOCHPUCIYCFHBUABNSCQPKHMURPQVQXAVCUVHXDHWTUKOPNCUAYHIIXWLQPIYQAUVSNLHNUTARVCEUHXUVAHWYWFZFUHEXBENQPIKOOCHZNCXKHTPETZAQUHRCEAMUGMBYUHPCSSIZVXFABGUFVANUIRTPEYSDWVRIHAOZSNTCLBXWLBXXPIKHOCHIZNSRNZHYZRNXWVNMHZXALUAYCHMNEXVQXPXPOCEZHQKSIASVCKNXTIKESGXKYPWBBALHBHKRNLZCHAUBXFDVCDTTZEMTNIXUPCHKBFPYSNEBOKFSLTNMXTOFKNANKAVZQOQTIANXVXEOZUAFZHGEIXXKLQPXKGRECVZAXQDSLTDNATXHHXABFMCNMKBHEOIVTNLHOXEAANKQPSTCEAVANSEEAXPFBBRYVNCMMHLZVFLEUXOUDNKZIDBLTPRGQUYAUVZVATUUCHXBNINZVAQZMXAXKMUQHKRIPVZHONTUKFZCYCCPTSBQONPHAIZLHHAZVKQOOTSVCPIMYIMBEZLHFKICYENIWHLVXDNKSEAPXKVKQYDEREDOSNIKOCXOAFZMBXWPXBISTIMZOUZONISVATIYEAAVMRIABOUTCYZYKEAKNPQVAICUKRHKMQPQVZIDYONIMZKGUVAEKZUFKQEPXBVXAIRHCEIZAXVCXPDVVRVMRLHOTHKCTMZRNNPCSSYXPZQBHKLHVVNKTYUZRVKIYNHZHUPCFGFPNWSAXBIPNEIITSHOANHQKDYIITIXDAUCXNHZUNBYKKIMHIAIOZUUKHSEWNSMCVPMTSZUOHKHAOXOFNKTKLPXBBOXVVNBLUXKLQPHIRPYEAIAWIXEZSGUKUAYBKQKUKVQRNTXZEAONKYNORTSYRFNMAVIERSETSWUTYNSSBNIKYAKHNHXSNLPXVATHYAOQVMRCHKBYZXCFMECKWVAIPWPFSSGISSVATIYEAAVMRDZCHVXZHNCQCWFKWIKNCFSSGXMYXFPVTSCVPIMZAQHUKERFKTBAXBZHHPKNVFKAHBOXXFKRYQDREMAQNVHFBHTTWGYICRHWPAIVTNAUVQAXTQDNDETXTAQIADCHVESBATOYIHENIPNLVXOCHRCNVVXIASEGESSAXINHSOCYCYGRBHSYCPAXONUPPEALQPHIDTYSINXQIWZOCHPTUYZHCSSZHWQPLVUFZCHQSAQVDTP".toCharArray(), "DYVHPTBGNCMOLRAXIUSKQWEFZ", true, true));
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
