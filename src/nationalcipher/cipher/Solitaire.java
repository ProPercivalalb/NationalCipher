package nationalcipher.cipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javalibrary.cipher.permentate.PermentateArray;
import javalibrary.cipher.permentate.Permentations;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.math.ArrayHelper;

public class Solitaire {
	
	public static final int JOKER_A = 52;
	public static final int JOKER_B = 53;
	
	public static final int TOTAL_CARDS = 54;
	public static String[] KEY = new String[]{"8d","9d","3h","Kd","Ah","Jc","5h","6h","7h","8h","9h","Th","6c","Kh","4d","Qd","2s","Tc","Ad","5s","Ts","A","Kc","4s","7s","Ks","Qc","9s","Jh","Qh","2h","7c","3s","6s","8c","Qs","2d","3d","4h","5d","6d","As","Js","B","4c","5c","Ac","9c","2c","Td","Jd","8s","7d","3c"};
	
	public static void main(String[] args) {

		
		Timer timer = new Timer();
		//System.out.println(nextKeyStream(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 52, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 38, 53))));
		timer.displayTime();
		/**
		final char[] text = "AGXJESSFKMMJMHXZGJWBCPCVXEBNDKUQOCEDUTICNPARQPEDIXZAVYMWZSVTBBVMTHJIGWXZAPJHJMYNMXRGORXOWEULMJSAAENCWVUYIFQUTRXEDEJBLWAADFPBWZAXJDDZOTMGSEZGNQWJYMFNWLSLTQDURZVQRKOTSVDNHYEIITYRRWGCCSLKSUKHDRLDBZEDXSGVUGMTBNZQJTCBZTTIBWKQPXUTQMZDIHHKWZKSJEEHFBFYPGYSIHOKKOBJFJSNXSIKSNBMTNIADVTCXYZZAOKQYWXNIZJWOFZVSPQQGASYUMJDELMDDHVZTNFHMOOLNXAFPEVBHGSTJFMCIFNHZYCYGGWAQYBUNNHDWHSLPIBFAPPDNQNDOCNURXEAIRZNLRXAKVXXAMPUZOPOKTJVQKIZDSCNCGVYMCXAHVDNYYJWGXJGEQTGEOQORILPKSMGNKKWAEUBPQXLSVEOXLRZWTXQXSANEZUFRAJLBROJIVCBKIFURSOVVFFOTBMUOGQFYAIEIZWMYHIOAPUFAJHAYHSFCZTDKCNLDWMKXBSTUHSATKCYDJOIJICEXELVJKCNGGAYOIKXHYLFXSCWXXMSFPDHGMNCBESTLMPELYQZWOLHUELZJCVPNGBKZNRKLCIIHHNUQXVOMXRGSURWCXQTOHFDQQGTKNPIRYYVVVQBCEOZGYPSDRZOJEFQETGFOKMIBCFUOGEHPFJPMDFTCTKWBEIMVNOCBNOMHSKPOIEKZBOYICYIGDYYGDNXSIFYBTEILBQAVTXQUINVARTBLHBSGXNMTTOBJBFAWFXBSHFMOVCWYCXFSULJTFKFVQHLHOTSZFYOZFSVPTPYEBQPRDDILTZBTHJDOUWFSJHOKGHSFWZYVTBSDUVVHJCPMICJWOQYIRUQDWKYCFDQKFRPGCZUFQGVEWVPHHZFKJLJPOBXNHEMSDKIIUCAEKRYNXSNFZAZOVILKWRKEFJHIDYXVQEFURRALVRSZQLHUICJFDRLOAFFVANORFFTEBBMQTYQETXHXRLWVPEHZMPBVHAMUFZIXNIXSVHNYFXPQUAMENZJPDAZFWHJCPYWRLYMXSTWKWFGROSYDZDJSHXKCGDADPPLMNKQYCNZGMXOHHYTTAJJEPEDEQEOMNNENBJDUNHYLSCWAUHDBFPSVGGNRLWZNUEZDRBRCXIXUZSAJNWYHLGXCVVDIIEPOUGNPDMSFUWZAHUVXVYACEZNNTRXQWGRFOPWQOIVEHGWCIIACLPGSJJPTLLMTFWIWQWAEKMIMCMRFKMNEBUYSRPYTWUKFCJEMQYPCUYTGXTNYVMEQMZQPBCUFQJEOKDRSAJHKSQQARSKFTLUFMFQCDDIUQHZCHWAKMHKHMYSPDAZEIKDGKSJEXRSRRVELBCKTOVBPRRRSGTANJGYWMSFSRHAHUBRTEZTMSTXRAQDVBQGOFAIDTEBUBGIIUGUMDFVHGVXESOFDRYOQQYUHHYEIYLBOLLTPPKAZOIEKUGEZCFTLQOREBSMZOSJCFPWGROYVXFUYUMHAREPHKDVQPQLSZGOKMDOHNXRDFNOJDDAYMHEGURMEOQHOEUBXJXAJKJWDUBBFBOITOWPSUPCCNOTGFQPOGCXHBLHLORFLPDJSVXYFNHJTTOJSHEZXSPLHURLYFXWLASUTJUEJLACMZNLKNDZRNMOKHIVACQPKICPIOZZLZUGLXQXOCGITKFFOMMSIUUXJCHWUWXLMIXDIXOJEATUENLMCZHIZZZPRQPZCEDXOHSILSTGLMDQZUWAZGTKOOQBMVZQPDEVTNLTNEKFUQKKYKAIXBDIWTFZHDDBBVWIVNSJBMVLCDXXCPIGICEDUGKJUZQLHEYECODIIZEVEBPBFNYEYWSONWEBIFPCLOOQGUVCBLLCSYQVMYVZGTZRSCUBHJRVXMAYHXJUGCFPGPGKILKMDWPKLJQHYAYGOLULDMYWOSHAMPJBWBNXVPQZFZWXOASQKYIUHVKTLJUVDPDQHWOIAVVVFPSWEMSNOLRPSCRMWOAQSULAAZKTUAKXZNQYKBMRMZLCHJAEQFQRRBFADHATMTJQGNHAXJAVRHCBCWPGQBRPJRUSBBWJZJBVSEUQWKJNOZZTCXXMKUQGOIKLOCRAUWFMYTLNWTFCGMDMPKMYSNAYMHFJITSQKQAPONCLBEIGWJITEZPXJZGQKNHLYIBOMKUCPSFUUHJBSWXEMMLRGCZOYVGYDKOTSGBZPTXULXBSSSKFMPKOYCYKWYCCQRSDRKVNDQTIXIIHXUXSEXXVHDNEVAANYBGPUHTHMCJTUNTYQZLUBSRYHIYMHPYDFTPCRTXGDJEQQSKOFLKPNAMWMIBBZRFFCIFJKIGCPNF".toCharArray();
		
		Permentations.permutate(new PermentateArray() {

			@Override
			public void onPermentate(int[] array) {
				final List<Integer> order = new ArrayList<Integer>();
				
				for(int i = 0; i < 6; i++) {
					for(int k = 0; k < 9; k++) {
						String str = KEY[i * 9 + k];
						if(str.equals("A"))
							order.add(52);
						else if(str.equals("B"))
							order.add(53);
						else {
							List<Character> starter = Arrays.asList('A','2','3','4','5','6','7','8','9','T','J','Q','K');
							List<Character> end = Arrays.asList('c','d','h','s');
			
							int no = starter.indexOf(str.charAt(0)) + array[end.indexOf(str.charAt(1))] * 13;
							//System.out.println(no + " " + str);
							order.add(no);
						}
					}
			}
				
				int[] newOrder = new int[order.size()];
				for(int i = 0; i < order.size(); i++)
					newOrder[i] = order.get(i);
				String orderString = "";
				for(int i = 0; i < order.size(); i++)
					orderString += (order.get(i)) + ",";
				
				
				System.out.println(orderString + " "  + new String(decode(text, newOrder)));
			}
			
		}, ArrayHelper.range(0, 4));
		**/
		

		timer.restart();
		int[] keyStream = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
		String ke = "T♠ J♠ Q♠ 3♣ 4♣ 5♣ 6♣ 7♣ 8♣ 5♦ J♣ Q♣ K♣ A♦ 4♦ K♠ 6♦ J♥ A 5♠ 6♠ A♥ 2♥ 2♦ 3♦ 9♣ T♣ B 7♦ A♣ T♦ J♦ Q♦ K♦ 9♦ 3♥ 4♥ 5♥ 6♥ 7♥ 8♥ 9♥ T♥ 2♣ K♥ A♠ 2♠ 3♠ 4♠ 8♦ 7♠ 8♠ 9♠ Q♥";
		final List<Integer> order = new ArrayList<Integer>();
		String[] split = ke.split(" ");
		for(int i = 0; i < 6; i++) {
			for(int k = 0; k < 9; k++) {
				String str = split[i * 9 + k];
				if(str.equals("A"))
					order.add(52);
				else if(str.equals("B"))
					order.add(53);
				else {
					List<Character> starter = Arrays.asList('A','2','3','4','5','6','7','8','9','T','J','Q','K');
					List<Character> end = Arrays.asList('♣', '♦', '♥', '♠');
	
					int no = starter.indexOf(str.charAt(0)) + end.indexOf(str.charAt(1)) * 13;
					//System.out.println(no + " " + str);
					order.add(no);
				}
			}
	}
		
		int[] cardOrder2 = new int[order.size()];
		for(int i = 0; i < order.size(); i++)
			cardOrder2[i] = order.get(i);
		System.out.println(Arrays.toString(cardOrder2));
		int[] cardOrder = createCardOrder("SOL");
		//System.out.println(Arrays.toString(cardOrder));
		//System.out.println(encode("REPORTONTHELOSSOFTHEKOHINOORLIGHTBEAMWEAPONSFACILITYINUTTARPRADESHTHELOSSOFTHEKOHINOORLIGHTBEAMWEAPONSFACILITYINUTTARPRADESHISAMINORDISRUPTIONINOURPLANSTOREESTABLISHTHEREICHINDEEDGIVENTHEDEATHSOFTHEINCOMPETENTLETZTERANDHERZFEUERINTHEINITIALBLASTITCOULDBESEENASAGOLDENOPPORTUNITYTOREFOCUSOURAMBITIONSONALTERNATIVEWEAPONSSYSTEMSWHICHOFFERGREATERPOTENTIALAPROPOSALOFTHISKINDFORMSTHEMAINANDFINALRECOMMENDATIONOFTHISREPORTTHEDESTRUCTIONOFTHEFACILITYAPPEARSTOHAVEBEENACHIEVEDBYTHECOMBINEDEFFORTSOFTHEENEMYAGENTSKNOWNASKHOLICHARLIEANDHARRYWEHAVEBEENUNABLETOIDENTIFYTHEIRBODIESINTHEWRECKAGEASLOCALINVESTIGATORSHAVEHAMPEREDOURACCESSTOTHESITEOURPREVIOUSGOODRELATIONSWITHTHELOCALOFFICIALSHAVEBEENSERIOUSLYDAMAGEDBYTHEIRBELIEFTHATWEARETOBLAMEFORTHEACCIDENTITISEXTREMELYFORTUNATETHATWEHAVEBEENABLETOCONVINCETHEMTHATITWASINDEEDANACCIDENTINRETROSPECTITMAYHAVEBEENANERRORINOURINITIALDEALINGSWITHTHEMTOTRYTOCONVINCETHEMTHATTHEINCIDENTWASCAUSEDBYINADEQUATEWORKBYPOORLOCALCONTRACTORSTHECONSIDERABLEDAMAGETOTHEFACILITYANDSOMEWHATLESSERDAMAGETOLOCALPROPERTYWASCAUSEDBYTHEDESTRUCTIONOFCONTROLVALVESINTHEFACILITYTHERESULTINGFLOODINTHELABORATORYSECTIONOFTHEFACILITYDESTROYEDRECORDSANDEQUIPMENTBUTTHESOUNDBAFFLESWEHADBUILTCAUGHTMUCHOFTHEFLOWANDITWASDIRECTEDINTOTHELOWERWATERSINTHISWEWERELUCKYMANYOFOUROWNAGENTSANDALLOFTHELOCALCITIZENSESCAPEDDEATHONTHEOTHERHANDWITHMUCHOFTHEFACILITYSURVIVINGWEHAVEHADTOWORKHARDTOCONCEALITSTRUENATUREFROMTHELOCALSANDHAVINGLOSTTHESUPPORTOFTHECULTOFTHEKOHINOORTHISHASBEENANEXPENSIVETASKITISUNLIKELYTHATWEWILLBEABLETOREESTABLISHABASEINTHISREGIONOFMORECONCERNWENOTETHATINDIANINTELLIGENCEAPPEARSTOHAVEBEENTRACKINGOURENEMIESANDTHISMAYHAVELEDTOTHEMINFILTRATINGOUROWNOPERATIONHEREININDIAITWOULDBEPRUDENTTOMOVEOURMAINCENTREOFOPERATIONSANDWEBELIEVETHATSOUTHAMERICAWOULDPROVIDEAMORECONVENIENTLOCATIONTHELOSSOFTHELIGHTBEAMWEAPONSRESEARCHISDISAPPOINTINGTHOUGHITISCLEARTHATTHISISSTILLATAVERYEARLYSTAGEOFDEVELOPMENTANDWEDONOTRECOMMENDFURTHERINVESTMENTATTHISTIMEWEBELIEVETHATIFSALINENSURVIVEDTHENHEWILLSURFACEAGAINSHORTLYANDITWOULDBEBESTTOKEEPAWATCHONHISLABORATORIESWEMAYFINDAUSEFORHIMORHISTECHNOLOGYWHOKNOWSWHATTHISSTRANGELASERMAYDOINTHEMEANTIMEWEWOULDSUGGESTKEEPINGANEYEONPROFSAMUELCOHENOFTHERANDCORPORATIONHEISBELIEVEDTOBEWORKINGONTHEEFFECTSOFRADIATIONANDMAYHAVEBEENINVOLVEDINTHEMANHATTANPROJECTITMAYBETIMETOREVIVEDIEALCHEMISTENFORTHEHONOUROFTHEREICHSCHWARZESCHATTEN", cardOrder));
		
		//System.out.println("");
		//for(int i = 0; i < 250; i++) {
		//	keyStream = nextKeyStream(keyStream);
		//	if(i == 249)
		//		System.out.println(Arrays.toString(keyStream));
		//}
		
			timer.displayTime();
		//System.out.println(Arrays.toString(ArrayHelper.rangeInt(1, 53)));
		//System.out.println(new String(encode("REPORTONTHELOSSOFTHEKOHINOORLIGHTBEAMWEAPONSFACILITYINUTTARPRADESHTHELOSSOFTHEKOHINOORLIGHTBEAMWEAPONSFACILITYINUTTARPRADESHISAMINORDISRUPTIONINOURPLANSTOREESTABLISHTHEREICHINDEEDGIVENTHEDEATHSOFTHEINCOMPETENTLETZTERANDHERZFEUERINTHEINITIALBLASTITCOULDBESEENASAGOLDENOPPORTUNITYTOREFOCUSOURAMBITIONSONALTERNATIVEWEAPONSSYSTEMSWHICHOFFERGREATERPOTENTIALAPROPOSALOFTHISKINDFORMSTHEMAINANDFINALRECOMMENDATIONOFTHISREPORTTHEDESTRUCTIONOFTHEFACILITYAPPEARSTOHAVEBEENACHIEVEDBYTHECOMBINEDEFFORTSOFTHEENEMYAGENTSKNOWNASKHOLICHARLIEANDHARRYWEHAVEBEENUNABLETOIDENTIFYTHEIRBODIESINTHEWRECKAGEASLOCALINVESTIGATORSHAVEHAMPEREDOURACCESSTOTHESITEOURPREVIOUSGOODRELATIONSWITHTHELOCALOFFICIALSHAVEBEENSERIOUSLYDAMAGEDBYTHEIRBELIEFTHATWEARETOBLAMEFORTHEACCIDENTITISEXTREMELYFORTUNATETHATWEHAVEBEENABLETOCONVINCETHEMTHATITWASINDEEDANACCIDENTINRETROSPECTITMAYHAVEBEENANERRORINOURINITIALDEALINGSWITHTHEMTOTRYTOCONVINCETHEMTHATTHEINCIDENTWASCAUSEDBYINADEQUATEWORKBYPOORLOCALCONTRACTORSTHECONSIDERABLEDAMAGETOTHEFACILITYANDSOMEWHATLESSERDAMAGETOLOCALPROPERTYWASCAUSEDBYTHEDESTRUCTIONOFCONTROLVALVESINTHEFACILITYTHERESULTINGFLOODINTHELABORATORYSECTIONOFTHEFACILITYDESTROYEDRECORDSANDEQUIPMENTBUTTHESOUNDBAFFLESWEHADBUILTCAUGHTMUCHOFTHEFLOWANDITWASDIRECTEDINTOTHELOWERWATERSINTHISWEWERELUCKYMANYOFOUROWNAGENTSANDALLOFTHELOCALCITIZENSESCAPEDDEATHONTHEOTHERHANDWITHMUCHOFTHEFACILITYSURVIVINGWEHAVEHADTOWORKHARDTOCONCEALITSTRUENATUREFROMTHELOCALSANDHAVINGLOSTTHESUPPORTOFTHECULTOFTHEKOHINOORTHISHASBEENANEXPENSIVETASKITISUNLIKELYTHATWEWILLBEABLETOREESTABLISHABASEINTHISREGIONOFMORECONCERNWENOTETHATINDIANINTELLIGENCEAPPEARSTOHAVEBEENTRACKINGOURENEMIESANDTHISMAYHAVELEDTOTHEMINFILTRATINGOUROWNOPERATIONHEREININDIAITWOULDBEPRUDENTTOMOVEOURMAINCENTREOFOPERATIONSANDWEBELIEVETHATSOUTHAMERICAWOULDPROVIDEAMORECONVENIENTLOCATIONTHELOSSOFTHELIGHTBEAMWEAPONSRESEARCHISDISAPPOINTINGTHOUGHITISCLEARTHATTHISISSTILLATAVERYEARLYSTAGEOFDEVELOPMENTANDWEDONOTRECOMMENDFURTHERINVESTMENTATTHISTIMEWEBELIEVETHATIFSALINENSURVIVEDTHENHEWILLSURFACEAGAINSHORTLYANDITWOULDBEBESTTOKEEPAWATCHONHISLABORATORIESWEMAYFINDAUSEFORHIMORHISTECHNOLOGYWHOKNOWSWHATTHISSTRANGELASERMAYDOINTHEMEANTIMEWEWOULDSUGGESTKEEPINGANEYEONPROFSAMUELCOHENOFTHERANDCORPORATIONHEISBELIEVEDTOBEWORKINGONTHEEFFECTSOFRADIATIONANDMAYHAVEBEENINVOLVEDINTHEMANHATTANPROJECTITMAYBETIMETOREVIVEDIEALCHEMISTENFORTHEHONOUROFTHEREICHSCHWARZESCHATTEN", cardOrder)));
		System.out.println(new String(decode("PIZQEJBLGDXTCMQLHLNMMGIQVOPUXEIKLMEOMBKJMEUQYHRXVVOEURXJYVVSLZWEGKCULSKNAYMSTBGZFYYVRIKBWLXJNSEOPJRFTGHHNSUIZRNLPWQZIWVWJHYPPPYIOELJPQJGOQMUVEBMBMHLNAFPQCWLNWKOYTHPWVNUYDTJSTXBKAPTTKHNMAMRIEPBWOFMKXYFAGYCEJBITEUYWPUCTGHWZKROIWMZWSGRTWRFZQKAVYQWCPHJHZAUFFXUYMUBJBSSKGFFMPQLTWVWFIMSWKKZOWXACWGZEVGEMDFCMRBSFPOPIGCPAVVWPPFQSJVSLSJCBPAYLDOJDXKBEWSTRGDYZBPOYCASAGZEZYJQEQSVSMIFPVAJIDKNEOOJZOVHJOCAMQCMFBCPRZCTYSYQYBYHFJGPUTMZKRFQCPDQSDQOUXTEDQLCHUYKPQQPIJHEDDGYDTIIVTBUEKIRLGCNRTJMVMFRUEDURLRIIWFQRCUXCXNKCXUZZHBGUCWCJTHFENMDLCHBELCYPHEXLVUZSLGCJGIPRIBLDGATKCGHLYJLUSGWDXUTYCNRYHIRRXSIBTJBODYZFGCLCPOSIVNEJHKYIMGYZPQHGARUFMMVFZXMFYAWBCVFTCOCQHTZUPWBHJKABGTIVKFDLSRDOTRZEOLOUJLFZOYCSWXVXLDBFJOPINVCQTTTDHHJRPAWZBIINSEOZTTINLKESGBFXBAXYDIZXCXGXGLXDELJVJZPEWLHVPLOCNACCABSZSACXWKUGPRDHDTNCUAJYZBWDXDDCYQYOLVBOZWSEMNRDNJNDAMGKMWAJLVOQRAMONSDUYCRSNGPDPUASGTE".toCharArray(), new int[] {53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,37,36,35,34,33,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0})));
		//System.out.println((createNewOrder(new ArrayList<Integer>(Arrays.asList(23, 35, 19, 4, 51, 21, 8, 13, 17, 15, 12, 16, 10, 20, 24, 5, 40, 41, 30, 46, 9, 1, 25, 48, 43, 11, 33, 14, 32, 22, 50, 52, 39, 0, 2, 47, 38, 26, 18, 44, 45, 3, 29, 6, 37, 27, 31, 34, 28, 36, 49, 42, 53, 7)))));
		//System.out.println(new String(decode("EWKMCRNUAFCXTJYQMMYYFUTIGWZPKHJMPKBSAIECKVCFMIILCI".toCharArray(), "DIGRAPH")));
	
	}
	
	public static boolean isJoker(int card) {
		return card == JOKER_A || card == JOKER_B;
	}
	
	public static int keyStreamNumber;
	
	public static int[] nextKeyStream(int[] oldCardOrder) {
		int deckSize = oldCardOrder.length;
		int[] cardOrder = Arrays.copyOf(oldCardOrder, oldCardOrder.length);
		
		while(true) {
			int jA = ArrayHelper.indexOf(cardOrder, JOKER_A);
			int jokerANewIndex = (jA + 1) % deckSize;
			cardOrder[jA] = cardOrder[jokerANewIndex];
			cardOrder[jokerANewIndex] = JOKER_A;
	
			//Move Joker B 2 to right
			int jokerBIndex = ArrayHelper.indexOf(cardOrder, JOKER_B);
			if(jokerBIndex + 1 <= deckSize - 2) {
				cardOrder[jokerBIndex] = cardOrder[jokerBIndex + 1];
				cardOrder[jokerBIndex + 1] = cardOrder[jokerBIndex + 2];
				cardOrder[jokerBIndex + 2] = JOKER_B;
				
				if(jokerANewIndex > jokerBIndex && jokerANewIndex <= jokerBIndex + 2)
					jokerANewIndex--;

				jokerBIndex += 2;

				
			}
			else {
				int fromEnd = deckSize - jokerBIndex;
				cardOrder = jokerBWrap(cardOrder, deckSize, jokerBIndex, fromEnd);

				if(jokerANewIndex >= 3 - fromEnd && jokerANewIndex < jokerBIndex)
					jokerANewIndex++;
				
				if(fromEnd == 1)
					jokerBIndex = 2;
				else
					jokerBIndex = 1;
			}
			
			//Triple cut the pack at the 2 Jokers
			int jokerAIndexFinal = jokerANewIndex;
			int jokerBIndexFinal = jokerBIndex;
			
			int minJoker = Math.min(jokerAIndexFinal, jokerBIndexFinal);
			int maxJoker = Math.max(jokerAIndexFinal, jokerBIndexFinal);
			
			cardOrder = tripleCut(cardOrder, deckSize, minJoker, maxJoker);
	
			int bottomCard = cardOrder[deckSize - 1];
			
			if(!isJoker(bottomCard))
				cardOrder = countCut(cardOrder, deckSize, bottomCard);
			
			int topCard = cardOrder[0];
			
			if(!isJoker(topCard))
				keyStreamNumber = cardOrder[topCard + 1];
			else 
				keyStreamNumber = cardOrder[deckSize - 1];

			if(!isJoker(keyStreamNumber))
				break;
		}
		return cardOrder;
	}
	
	public static int[] jokerBWrap(int[] cardOrder, int deckSize, int jokerBIndex, int fromEnd) {
		int[] c = new int[deckSize];
		int fromStart = 3 - fromEnd;
		if(fromEnd == 2) {
			//System.out.println("Start " + Arrays.toString(cardOrder));
			System.arraycopy(cardOrder, 0, c, 0, fromStart);
			//System.out.println(Arrays.toString(c));
			System.arraycopy(cardOrder, jokerBIndex, c, fromStart, 1);
			//System.out.println(Arrays.toString(c));
			System.arraycopy(cardOrder, fromStart, c, fromStart + 1, deckSize - 3);
			System.arraycopy(cardOrder, deckSize - 1, c, deckSize - 1, fromEnd - 1);
			//System.out.println(Arrays.toString(c));
		}
		else {
			//System.out.println("Start " + Arrays.toString(cardOrder));
			System.arraycopy(cardOrder, 0, c, 0, fromStart);
			//System.out.println(Arrays.toString(c));
			System.arraycopy(cardOrder, jokerBIndex, c, fromStart, 1);
			//System.out.println(Arrays.toString(c));
			System.arraycopy(cardOrder, fromStart, c, fromStart + 1, deckSize - 3);
			//System.out.println(Arrays.toString(c));
		}
		return c;
	}
	
	public static int[] tripleCut(int[] cardOrder, int deckSize, int minJoker, int maxJoker) {
		int[] c = new int[deckSize];
		int middleBitPosition = deckSize - maxJoker - 1;
		int middleLength = maxJoker - minJoker + 1;
		
		System.arraycopy(cardOrder, 0, c, deckSize - minJoker, minJoker);
		System.arraycopy(cardOrder, minJoker, c, middleBitPosition, middleLength);
		System.arraycopy(cardOrder, maxJoker + 1, c, 0, middleBitPosition);
		return c;
	}
	
	public static int[] countCut(int[] cardOrder, int deckSize, int size) {
		int[] c = new int[deckSize];
		int distanceFromEnd = deckSize - size - 2;
		int moveValue = size + 1;
		int lastCardIndex = deckSize - 1;
		
		System.arraycopy(cardOrder, 0, c, distanceFromEnd, moveValue);
		System.arraycopy(cardOrder, moveValue, c, 0, distanceFromEnd);
		System.arraycopy(cardOrder, lastCardIndex, c, lastCardIndex, 1);
		return c;
	}
	
	public static int[] createCardOrder(String key) {
		int[] cardOrder = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
		int deckSize = cardOrder.length;
		
		for(int i = 0; i < key.length(); i++) {
			int jA = ArrayHelper.indexOf(cardOrder, JOKER_A);
			int jokerANewIndex = (jA + 1) % deckSize;
			cardOrder[jA] = cardOrder[jokerANewIndex];
			cardOrder[jokerANewIndex] = JOKER_A;
		
			//Move Joker B 2 to right
			int jokerBIndex = ArrayHelper.indexOf(cardOrder, JOKER_B);
			if(jokerBIndex + 1 <= deckSize - 2) {
				cardOrder[jokerBIndex] = cardOrder[jokerBIndex + 1];
				cardOrder[jokerBIndex + 1] = cardOrder[jokerBIndex + 2];
				cardOrder[jokerBIndex + 2] = JOKER_B;
				jokerBIndex += 2;
			}
			else {
				int fromEnd = deckSize - jokerBIndex;
				cardOrder = jokerBWrap(cardOrder, deckSize, jokerBIndex, fromEnd);
				if(fromEnd == 1)
					jokerBIndex = 2;
				else
					jokerBIndex = 1;
			}
				
			//Triple cut the pack at the 2 Jokers
			int jokerAIndexFinal =  ArrayHelper.indexOf(cardOrder, JOKER_A);
			int jokerBIndexFinal = jokerBIndex;//ArrayHelper.indexOf(cardOrder, JOKER_B);
				
			int minJoker = Math.min(jokerAIndexFinal, jokerBIndexFinal);
			int maxJoker = Math.max(jokerAIndexFinal, jokerBIndexFinal);
				
			cardOrder = tripleCut(cardOrder, deckSize, minJoker, maxJoker);
			int bottomCard = cardOrder[deckSize - 1];
				
			if(!isJoker(bottomCard))
				cardOrder = countCut(cardOrder, deckSize, bottomCard);

			cardOrder = countCut(cardOrder, deckSize, key.charAt(i) - 'A');
		}
		
		return cardOrder;
	}
	
	public static String encode(String plainText, int[] cardOrder) {
		int[] keyStream = new int[plainText.length()];
		int keyStreamIndex = 0;
		
		
		while(keyStreamIndex < keyStream.length) {
			//Move Joker A 1 to right
			
			cardOrder = nextKeyStream(cardOrder);
			
			int topCard = cardOrder[0];
			int keyStreamNumber;
			
			if(!isJoker(topCard))
				keyStreamNumber = cardOrder[topCard + 1];
			else 
				keyStreamNumber = cardOrder[cardOrder.length - 1];

			
			if(!isJoker(keyStreamNumber))
				keyStream[keyStreamIndex++] = keyStreamNumber;

		}
		
		String cipherText = "";
		
		//String key = "";
		for(int i = 0; i < keyStream.length; i++)  {
			//key += (char)((keyStream[i]) % 26 + 'A');
			cipherText += (char)(((plainText.charAt(i) - 'A') + (keyStream[i] + 1)) % 26 + 'A');
		}
		//System.out.println(key);
		

		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, int[] cardOrder) {
		//int[] keyStream = new int[cipherText.length];
		char[] plainText = new char[cipherText.length];
		int keyStreamIndex = 0;
		
		
		for(int i = 0; i < cipherText.length; i++)  {
			//Move Joker A 1 to right
			
			cardOrder = nextKeyStream(cardOrder);
			plainText[i] = (char)((52 + (cipherText[i] - 'A') - (keyStreamNumber + 1)) % 26 + 'A');
			//keyStream[keyStreamIndex++] = keyStreamNumber;
			

		}

		//char[] plainText = new char[cipherText.length];
		
		//String key = "";
		//for(int i = 0; i < keyStream.length; i++)  {
			//key += (char)((keyStream[i]) % 26 + 'A');
		//	plainText[i] = (char)((52 + (cipherText[i] - 'A') - (keyStream[i] + 1)) % 26 + 'A');
		//}
		//System.out.println(key);

		return plainText;
	}
}
