package nationalcipher;

import java.util.Arrays;

import javalibrary.cipher.Nicodemus;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.math.ArrayHelper;
import nationalciphernew.cipher.stats.StatCalculator;

public class Other {

	public static void main(String... str) {
	//	WordSplit.loadFile();
	
		
		
		Timer timer = new Timer();
		//System.out.println("Final: " + WordSplit.splitText("CHARLIENONEEDTOAPOLOGISELIFEWASGETTINGDULLBEHINDADESKANDIWASGLADTOHAVEANEXCUSETOFLYBACKTOEUROPEIAMINTRIGUEDABOUTTHEREICHSDOKTORIHADNTCOMEACROSSTHISBEFOREWHENDIDYOUFIRSTCOMEHEAROFITITHINKIMAYALREADYBEMAKINGSOMEPROGRESSONARRIVALIFOUNDAPOSTCARDWAITINGFORMEONTHEMATATTHEEMBASSYWITHNOMESSAGEONITATLEASTTHATSWHATITLOOKEDLIKEATFIRSTIDIDNOTICETHATTHELETTERSONTHEFRONTCOULDBEHIGHLIGHTEDTOPICKOUTTHEPHRASETHEREICHSDOKTORSOUNLESSTHATISANEXTRAORDINARYCOINCIDENCEIFIGUREDITMUSTBERELATEDTOOURINVESTIGATIONTHESTRANGESTTHINGWASTHATTHEPOSTCARDHADASTAMPBUTNOPOSTMARKONITSOITCANTHAVEBEENPOSTEDSINCEITWASNTSIGNEDIASSUMETHEYWANTEDTOSTAYANONYMOUSANDICOULDNTSEEWHYTHEYWOULDHAVETAKENTHERISKOFHANDDELIVERINGITBUTINTHEENDIWORKEDITOUTTHEREWASAHIDDENMESSAGEILLLEAVEITTOYOUTOFIGUREOUTWHEREITWASHIDDENANYWAYIVEATTACHEDTHEMESSAGEIFOUNDIKNOWTHATRELATIONSWITHTHREEOFTHEFOURPOWERSARERELATIVELYSTABLEBUTITHINKWENEEDTOKEEPTHISTOOURSELVESFORNOWALLTHEBESTHARRY"));
		//System.out.println(WordSplit.scoreWordDuo("YOUR", "PROBLEM"));
		
		String plainText = "CHARLIENONEEDTOAPOLOGISELIFEWASGETTINGDULLBEHINDADESKANDIWASGLADTOHAVEANEXCUSETOFLYBACKTOEUROPEIAMINTRIGUEDABOUTTHEREICHSDOKTORIHADNTCOMEACROSSTHISBEFOREWHENDIDYOUFIRSTCOMEHEAROFITITHINKIMAYALREADYBEMAKINGSOMEPROGRESSONARRIVALIFOUNDAPOSTCARDWAITINGFORMEONTHEMATATTHEEMBASSYWITHNOMESSAGEONITATLEASTTHATSWHATITLOOKEDLIKEATFIRSTIDIDNOTICETHATTHELETTERSONTHEFRONTCOULDBEHIGHLIGHTEDTOPICKOUTTHEPHRASETHEREICHSDOKTORSOUNLESSTHATISANEXTRAORDINARYCOINCIDENCEIFIGUREDITMUSTBERELATEDTOOURINVESTIGATIONTHESTRANGESTTHINGWASTHATTHEPOSTCARDHADASTAMPBUTNOPOSTMARKONITSOITCANTHAVEBEENPOSTEDSINCEITWASNTSIGNEDIASSUMETHEYWANTEDTOSTAYANONYMOUSANDICOULDNTSEEWHYTHEYWOULDHAVETAKENTHERISKOFHANDDELIVERINGITBUTINTHEENDIWORKEDITOUTTHEREWASAHIDDENMESSAGEILLLEAVEITTOYOUTOFIGUREOUTWHEREITWASHIDDENANYWAYIVEATTACHEDTHEMESSAGEIFOUNDIKNOWTHATRELATIONSWITHTHREEOFTHEFOURPOWERSARERELATIVELYSTABLEBUTITHINKWENEEDTOKEEPTHISTOOURSELVESFORNOWALLTHEBESTHARRY";
		
		String cipherText = Nicodemus.encode(plainText, "AVERYLONGKEYWORD");
		
		System.out.println(cipherText);
		int[] order = new int[6];
		char[] charArray = "GERMAN".toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order["GERMAN".indexOf(charArray[i])] = i;
		System.out.println("ORDER: " + Arrays.toString(order));
		
		System.out.println("best period: " + StatCalculator.calculateBestNicodemusIC(cipherText, 3, 15, Languages.english));
		timer.displayTime();
		timer.restart();
		System.out.println(calculateBestNicodemusIC(cipherText, 3, 15, Languages.english));
		//System.out.println(StatCalculator.calculateIC(plainText));
		
		timer.displayTime();

	}
	
	private static double calculateNicodemusIC(String text, int rowsPerBlock, int period) {
		String[] ct = ArrayHelper.fill("", period);
		
		int blockSize = period * rowsPerBlock;
		int totalBlocks = (int)Math.floor(text.length() / blockSize);
		
		for(int i = 0; i < totalBlocks; i++) {
			for(int j = 0; j < blockSize; j++) {
				int place = (int)Math.floor(j / rowsPerBlock);
				char c = text.charAt(i * blockSize + j);
				ct[place] += "" + c;
			}
		}

		//System.out.println(Arrays.toString(ct));
		String incomplete = "";
		for(int row = 0; row < rowsPerBlock * totalBlocks; row++)
			for(int column = 0; column < period; column++) {
				incomplete += ct[column].charAt(row);
			}
		

		return StatCalculator.calculateKappaIC(incomplete, period);
	}
	
	public static int calculateBestNicodemusIC(String text, int minPeriod, int maxPeriod, ILanguage language) {

		int bestPeriod = -1;
	    double bestKappa = Double.MAX_VALUE;
	    
	    for(int period = minPeriod; period <= maxPeriod; ++period) {
	    	double sqDiff = Math.pow(calculateNicodemusIC(text, 5, period) - language.getNormalCoincidence(), 2);
	    	System.out.println(period + " " + calculateNicodemusIC(text, 5, period));
	    	if(sqDiff < bestKappa)
	    		bestPeriod = period;
	    	
	    	bestKappa = Math.min(bestKappa, sqDiff);
	    }
		
	    return bestPeriod;

	}
}
