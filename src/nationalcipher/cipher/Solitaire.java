package nationalcipher.cipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javalibrary.math.ArrayHelper;

public class Solitaire {
	public static final int JOKER_A = 52;
	public static final int JOKER_B = 53;
	
	public static final int TOTAL_CARDS = 54;
	
	
	//Joker A - 52
	//Joker B - 53
	//Total Cards
	
	public static void main(String[] args) {
		System.out.println(nextKeyedStream(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53)), "SOLITAIRE", 0));
		
		System.out.println(Arrays.toString(ArrayHelper.rangeInt(1, 53)));
		System.out.println(new String(encode("PHASEFIVESEAHORSEISREADYFORTRIALSANDTHENAUTILUSSYSTEMISFULLYFUNCTIONALWEENGAGEDTHEMECHANISMANDLOWEREDTHEDECKTOTHREEFEETABOVESEALEVELAPPROACHINGTHESHOREBYTHERADARSTATIONATALLTIMESSIGNALSFROMTHEIRCOMMUNICATIONSWEREMONITOREDANDNOSIGNWASGIVENTHATOURAPPROACHHADBEENMONITOREDOREVENNOTICEDWEBACKEDOFFTHEDECKWASRAISEDBYTWOFEETANDTHEAPPROACHATTEMPTEDAGAINONCEMOREOURINCURSIONWASUNNOTICEDOVERNIGHTWECONDUCTEDARANGEOFTESTSANDMAPPEDTHERADARCOVERAGEONTHREESEPARATEOCCASIONSTHERESEEMSTOHAVEBEENAFLURRYOFACTIVITYANDOURMODELINGSUGGESTSTHATTHESHIPSMASTSMAYHAVETRIGGEREDBRIEFALARMSONALLOCCASIONSTHEAUTOMATICDIVESYSTEMSCUTINCORRECTLYLOWERINGTHEDECKSTOSEALEVELANDTHEALARMSWERECANCELLEDTHESEAHORSEDEPLOYMENTSYSTEMWILLBEFULLYMOUNTEDTONIGHTANDWEWILLCONDUCTABATTERYOFTESTSONTHEDEPLOYMENTANDEMERGENCYRECOVERYSYSTEMSOVERTHENEXTTWONIGHTSASSUMINGTHATSEAANDAIRTRAFFICREMAINSLOW", new ArrayList<Integer>(Arrays.asList(53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,37,36,35,34,33,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0)))));
		System.out.println(new String(decode("PIZQEJBLGDXTCMQLHLNMMGIQVOPUXEIKLMEOMBKJMEUQYHRXVVOEURXJYVVSLZWEGKCULSKNAYMSTBGZFYYVRIKBWLXJNSEOPJRFTGHHNSUIZRNLPWQZIWVWJHYPPPYIOELJPQJGOQMUVEBMBMHLNAFPQCWLNWKOYTHPWVNUYDTJSTXBKAPTTKHNMAMRIEPBWOFMKXYFAGYCEJBITEUYWPUCTGHWZKROIWMZWSGRTWRFZQKAVYQWCPHJHZAUFFXUYMUBJBSSKGFFMPQLTWVWFIMSWKKZOWXACWGZEVGEMDFCMRBSFPOPIGCPAVVWPPFQSJVSLSJCBPAYLDOJDXKBEWSTRGDYZBPOYCASAGZEZYJQEQSVSMIFPVAJIDKNEOOJZOVHJOCAMQCMFBCPRZCTYSYQYBYHFJGPUTMZKRFQCPDQSDQOUXTEDQLCHUYKPQQPIJHEDDGYDTIIVTBUEKIRLGCNRTJMVMFRUEDURLRIIWFQRCUXCXNKCXUZZHBGUCWCJTHFENMDLCHBELCYPHEXLVUZSLGCJGIPRIBLDGATKCGHLYJLUSGWDXUTYCNRYHIRRXSIBTJBODYZFGCLCPOSIVNEJHKYIMGYZPQHGARUFMMVFZXMFYAWBCVFTCOCQHTZUPWBHJKABGTIVKFDLSRDOTRZEOLOUJLFZOYCSWXVXLDBFJOPINVCQTTTDHHJRPAWZBIINSEOZTTINLKESGBFXBAXYDIZXCXGXGLXDELJVJZPEWLHVPLOCNACCABSZSACXWKUGPRDHDTNCUAJYZBWDXDDCYQYOLVBOZWSEMNRDNJNDAMGKMWAJLVOQRAMONSDUYCRSNGPDPUASGTE".toCharArray(), new ArrayList<Integer>(Arrays.asList(53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,37,36,35,34,33,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0)))));
		//System.out.println((createNewOrder(new ArrayList<Integer>(Arrays.asList(23, 35, 19, 4, 51, 21, 8, 13, 17, 15, 12, 16, 10, 20, 24, 5, 40, 41, 30, 46, 9, 1, 25, 48, 43, 11, 33, 14, 32, 22, 50, 52, 39, 0, 2, 47, 38, 26, 18, 44, 45, 3, 29, 6, 37, 27, 31, 34, 28, 36, 49, 42, 53, 7)))));
		//System.out.println(new String(decode("EWKMCRNUAFCXTJYQMMYYFUTIGWZPKHJMPKBSAIECKVCFMIILCI".toCharArray(), "DIGRAPH")));
	}
	
	public static List<Integer> nextKeyStream(List<Integer> oldCardOrder) {
		List<Integer> cardOrder = new ArrayList<Integer>();
		cardOrder.addAll(oldCardOrder);
		
		int jokerAIndex = cardOrder.indexOf(JOKER_A);
		int jokerANewIndex = (jokerAIndex + 1) % TOTAL_CARDS;
		cardOrder.set(jokerAIndex, cardOrder.get(jokerANewIndex));
		cardOrder.set(jokerANewIndex, JOKER_A);

		//Move Joker B 2 to right
		int jokerBIndex = cardOrder.indexOf(JOKER_B);
		if(jokerBIndex + 1 <= TOTAL_CARDS - 2) {
			int jokerBNewIndex1 = (jokerBIndex + 1) % TOTAL_CARDS;
			int jokerBNewIndex2 = (jokerBIndex + 2) % TOTAL_CARDS;
			cardOrder.set(jokerBIndex, cardOrder.get(jokerBNewIndex1));
			cardOrder.set(jokerBNewIndex1, cardOrder.get(jokerBNewIndex2));
			cardOrder.set(jokerBNewIndex2, JOKER_B);
		}
		else {
			int fromEnd = cardOrder.size() - jokerBIndex;
			
			List<Integer> subList = cardOrder.subList(3 - fromEnd, TOTAL_CARDS - fromEnd);
			List<Integer> swappedOrder = new ArrayList<Integer>();
			swappedOrder.add(cardOrder.get(0));
			if(fromEnd == 1)
				swappedOrder.add(cardOrder.get(1));
			swappedOrder.add(JOKER_B);
			swappedOrder.addAll(subList);
			if(fromEnd == 2)
				swappedOrder.add(cardOrder.get(TOTAL_CARDS - 1));


			cardOrder = swappedOrder;
		}
		
	
		//Triple cut the pack at the 2 Jokers
		
		int jokerAIndexFinal = cardOrder.indexOf(JOKER_A);
		int jokerBIndexFinal = cardOrder.indexOf(JOKER_B);
		
		int minJoker = Math.min(jokerAIndexFinal, jokerBIndexFinal);
		int maxJoker = Math.max(jokerAIndexFinal, jokerBIndexFinal);
		
		List<Integer> topPart = cardOrder.subList(0, minJoker);
		List<Integer> middlePart = cardOrder.subList(minJoker, maxJoker + 1);
		List<Integer> bottomPart = cardOrder.subList(maxJoker + 1, TOTAL_CARDS);
		

		List<Integer> swappedOrder = new ArrayList<Integer>();
		swappedOrder.addAll(bottomPart);
		swappedOrder.addAll(middlePart);
		swappedOrder.addAll(topPart);

		int bottomCard = swappedOrder.get(TOTAL_CARDS - 1);
		
		if(!(bottomCard == JOKER_A || bottomCard == JOKER_B)) {
			List<Integer> firstPart = swappedOrder.subList(0, bottomCard + 1);
			List<Integer> secondPart = swappedOrder.subList(bottomCard + 1, TOTAL_CARDS - 1);
			
			List<Integer> newOrder = new ArrayList<Integer>();
			newOrder.addAll(secondPart);
			newOrder.addAll(firstPart);
			newOrder.add(bottomCard);
			swappedOrder = newOrder;

		}
		
		return swappedOrder;
	}

	public static List<Integer> nextKeyedStream(List<Integer> oldCardOrder, String key, int index) {
		List<Integer> cardOrder = new ArrayList<Integer>();
		cardOrder.addAll(oldCardOrder);
		
		int jokerAIndex = cardOrder.indexOf(JOKER_A);
		int jokerANewIndex = (jokerAIndex + 1) % TOTAL_CARDS;
		cardOrder.set(jokerAIndex, cardOrder.get(jokerANewIndex));
		cardOrder.set(jokerANewIndex, JOKER_A);

		//Move Joker B 2 to right
		int jokerBIndex = cardOrder.indexOf(JOKER_B);
		if(jokerBIndex + 1 <= TOTAL_CARDS - 2) {
			int jokerBNewIndex1 = (jokerBIndex + 1) % TOTAL_CARDS;
			int jokerBNewIndex2 = (jokerBIndex + 2) % TOTAL_CARDS;
			cardOrder.set(jokerBIndex, cardOrder.get(jokerBNewIndex1));
			cardOrder.set(jokerBNewIndex1, cardOrder.get(jokerBNewIndex2));
			cardOrder.set(jokerBNewIndex2, JOKER_B);
		}
		else {
			int fromEnd = cardOrder.size() - jokerBIndex;
			
			List<Integer> subList = cardOrder.subList(3 - fromEnd, TOTAL_CARDS - fromEnd);
			List<Integer> swappedOrder = new ArrayList<Integer>();
			swappedOrder.add(cardOrder.get(0));
			if(fromEnd == 1)
				swappedOrder.add(cardOrder.get(1));
			swappedOrder.add(JOKER_B);
			swappedOrder.addAll(subList);
			if(fromEnd == 2)
				swappedOrder.add(cardOrder.get(TOTAL_CARDS - 1));


			cardOrder = swappedOrder;
		}
		
	
		//Triple cut the pack at the 2 Jokers
		
		int jokerAIndexFinal = cardOrder.indexOf(JOKER_A);
		int jokerBIndexFinal = cardOrder.indexOf(JOKER_B);
		
		int minJoker = Math.min(jokerAIndexFinal, jokerBIndexFinal);
		int maxJoker = Math.max(jokerAIndexFinal, jokerBIndexFinal);
		
		List<Integer> topPart = cardOrder.subList(0, minJoker);
		List<Integer> middlePart = cardOrder.subList(minJoker, maxJoker + 1);
		List<Integer> bottomPart = cardOrder.subList(maxJoker + 1, TOTAL_CARDS);
		

		List<Integer> swappedOrder = new ArrayList<Integer>();
		swappedOrder.addAll(bottomPart);
		swappedOrder.addAll(middlePart);
		swappedOrder.addAll(topPart);

		int shift = key.charAt(index) - 'A' + 1;
		int bottomCard = swappedOrder.get(TOTAL_CARDS - 1);
		
		if(!(bottomCard == JOKER_A || bottomCard == JOKER_B)) {
			List<Integer> firstPart = swappedOrder.subList(0, shift + 1);
			List<Integer> secondPart = swappedOrder.subList(shift + 1, TOTAL_CARDS - 1);
			
			List<Integer> newOrder = new ArrayList<Integer>();
			newOrder.addAll(secondPart);
			newOrder.addAll(firstPart);
			newOrder.add(bottomCard);
			swappedOrder = newOrder;

		}
		
		return swappedOrder;
	}
	
	public static String encode(String plainText, List<Integer> cardOrder) {
		int[] keyStream = new int[plainText.length()];
		int keyStreamIndex = 0;
		
		
		while(keyStreamIndex < keyStream.length) {
			//Move Joker A 1 to right
			
			cardOrder = nextKeyStream(cardOrder);
			
			int topCard = cardOrder.get(0);
			int keyStreamNumber;
			
			if(!(topCard == JOKER_A || topCard == JOKER_B))
				keyStreamNumber = cardOrder.get(topCard + 1);
			else 
				keyStreamNumber = cardOrder.get(cardOrder.size() - 1);

			
			if(!(keyStreamNumber == JOKER_A || keyStreamNumber == JOKER_B))
				keyStream[keyStreamIndex++] = keyStreamNumber;

		}
		
		String cipherText = "";
		
		String key = "";
		for(int i = 0; i < keyStream.length; i++)  {
			key += (char)((keyStream[i]) % 26 + 'A');
			cipherText += (char)((plainText.charAt(i) - 'A' + keyStream[i] + 1) % 26 + 'A');
		}
		System.out.println(key);
		

		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, List<Integer> cardOrder) {
		int[] keyStream = new int[cipherText.length];
		int keyStreamIndex = 0;
		
		
		while(keyStreamIndex < keyStream.length) {
			//Move Joker A 1 to right
			
			cardOrder = nextKeyStream(cardOrder);
			
			int topCard = cardOrder.get(0);
			int keyStreamNumber;
			
			if(!(topCard == JOKER_A || topCard == JOKER_B))
				keyStreamNumber = cardOrder.get(topCard + 1);
			else 
				keyStreamNumber = cardOrder.get(cardOrder.size() - 1);

			
			if(!(keyStreamNumber == JOKER_A || keyStreamNumber == JOKER_B))
				keyStream[keyStreamIndex++] = keyStreamNumber;

		}
		
		char[] plainText = new char[cipherText.length];
		
		String key = "";
		for(int i = 0; i < keyStream.length; i++)  {
			key += (char)((keyStream[i]) % 26 + 'A');
			plainText[i] = (char)((260 + cipherText[i] - 'A' - keyStream[i] - 1) % 26 + 'A');
		}
		System.out.println(key);

		return plainText;
	}
}
