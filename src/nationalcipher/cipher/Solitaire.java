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
import javalibrary.math.MathHelper;
import javalibrary.util.ListUtil;
import nationalcipher.cipher.tools.Creator.PortaAutoKey;

public class Solitaire {
	
	public static final int JOKER_A = 52;
	public static final int JOKER_B = 53;
	
	public static final int TOTAL_CARDS = 54;
	public static String[] KEY = new String[] {"8d","9d","3h","Kd","Ah","Jc","5h","6h","7h","8h","9h","Th","6c","Kh","4d","Qd","2s","Tc","Ad","5s","Ts","A","Kc","4s","7s","Ks","Qc","9s","Jh","Qh","2h","7c","3s","6s","8c","Qs","2d","3d","4h","5d","6d","As","Js","B","4c","5c","Ac","9c","2c","Td","Jd","8s","7d","3c"};
	
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
		
		//int[] cardOrder2 = new int[order.size()];
		//for(int i = 0; i < order.size(); i++)
		//	cardOrder2[i] = order.get(i);
		//System.out.println(ListUtil.toString(cardOrder2));
	int[] cardOrder = createCardOrder("TEST");
		//System.out.println(ListUtil.toString(cardOrder, 1));
		timer.restart();
	//int[] cardOrder = new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53};
		//System.out.println(ListUtil.toString(cardOrder, 1));
		//for(int i = 0; i < 10; i++) {
		//	cardOrder = nextCardOrder(cardOrder);
			//System.out.println(ListUtil.toString(cardOrder, 1));
		//}
		System.out.println(ListUtil.toString(cardOrder, 1));
		String	 plainText = new String(decode("YRXIRTHPNGJIFXZSP".toCharArray(), cardOrder));
		System.out.println(plainText);
		//	}
		//	System.out.println(new String());
		timer.displayTime();
		//System.out.println("");
		//for(int i = 0; i < 250; i++) {
		//	keyStream = nextKeyStream(keyStream);
		//	if(i == 249)
		//		System.out.println(Arrays.toString(keyStream));
		//}
		
			//timer.displayTime();
		//System.out.println(Arrays.toString(ArrayHelper.rangeInt(1, 53)));
		//System.out.println(new String(encode("REPORTONTHELOSSOFTHEKOHINOORLIGHTBEAMWEAPONSFACILITYINUTTARPRADESHTHELOSSOFTHEKOHINOORLIGHTBEAMWEAPONSFACILITYINUTTARPRADESHISAMINORDISRUPTIONINOURPLANSTOREESTABLISHTHEREICHINDEEDGIVENTHEDEATHSOFTHEINCOMPETENTLETZTERANDHERZFEUERINTHEINITIALBLASTITCOULDBESEENASAGOLDENOPPORTUNITYTOREFOCUSOURAMBITIONSONALTERNATIVEWEAPONSSYSTEMSWHICHOFFERGREATERPOTENTIALAPROPOSALOFTHISKINDFORMSTHEMAINANDFINALRECOMMENDATIONOFTHISREPORTTHEDESTRUCTIONOFTHEFACILITYAPPEARSTOHAVEBEENACHIEVEDBYTHECOMBINEDEFFORTSOFTHEENEMYAGENTSKNOWNASKHOLICHARLIEANDHARRYWEHAVEBEENUNABLETOIDENTIFYTHEIRBODIESINTHEWRECKAGEASLOCALINVESTIGATORSHAVEHAMPEREDOURACCESSTOTHESITEOURPREVIOUSGOODRELATIONSWITHTHELOCALOFFICIALSHAVEBEENSERIOUSLYDAMAGEDBYTHEIRBELIEFTHATWEARETOBLAMEFORTHEACCIDENTITISEXTREMELYFORTUNATETHATWEHAVEBEENABLETOCONVINCETHEMTHATITWASINDEEDANACCIDENTINRETROSPECTITMAYHAVEBEENANERRORINOURINITIALDEALINGSWITHTHEMTOTRYTOCONVINCETHEMTHATTHEINCIDENTWASCAUSEDBYINADEQUATEWORKBYPOORLOCALCONTRACTORSTHECONSIDERABLEDAMAGETOTHEFACILITYANDSOMEWHATLESSERDAMAGETOLOCALPROPERTYWASCAUSEDBYTHEDESTRUCTIONOFCONTROLVALVESINTHEFACILITYTHERESULTINGFLOODINTHELABORATORYSECTIONOFTHEFACILITYDESTROYEDRECORDSANDEQUIPMENTBUTTHESOUNDBAFFLESWEHADBUILTCAUGHTMUCHOFTHEFLOWANDITWASDIRECTEDINTOTHELOWERWATERSINTHISWEWERELUCKYMANYOFOUROWNAGENTSANDALLOFTHELOCALCITIZENSESCAPEDDEATHONTHEOTHERHANDWITHMUCHOFTHEFACILITYSURVIVINGWEHAVEHADTOWORKHARDTOCONCEALITSTRUENATUREFROMTHELOCALSANDHAVINGLOSTTHESUPPORTOFTHECULTOFTHEKOHINOORTHISHASBEENANEXPENSIVETASKITISUNLIKELYTHATWEWILLBEABLETOREESTABLISHABASEINTHISREGIONOFMORECONCERNWENOTETHATINDIANINTELLIGENCEAPPEARSTOHAVEBEENTRACKINGOURENEMIESANDTHISMAYHAVELEDTOTHEMINFILTRATINGOUROWNOPERATIONHEREININDIAITWOULDBEPRUDENTTOMOVEOURMAINCENTREOFOPERATIONSANDWEBELIEVETHATSOUTHAMERICAWOULDPROVIDEAMORECONVENIENTLOCATIONTHELOSSOFTHELIGHTBEAMWEAPONSRESEARCHISDISAPPOINTINGTHOUGHITISCLEARTHATTHISISSTILLATAVERYEARLYSTAGEOFDEVELOPMENTANDWEDONOTRECOMMENDFURTHERINVESTMENTATTHISTIMEWEBELIEVETHATIFSALINENSURVIVEDTHENHEWILLSURFACEAGAINSHORTLYANDITWOULDBEBESTTOKEEPAWATCHONHISLABORATORIESWEMAYFINDAUSEFORHIMORHISTECHNOLOGYWHOKNOWSWHATTHISSTRANGELASERMAYDOINTHEMEANTIMEWEWOULDSUGGESTKEEPINGANEYEONPROFSAMUELCOHENOFTHERANDCORPORATIONHEISBELIEVEDTOBEWORKINGONTHEEFFECTSOFRADIATIONANDMAYHAVEBEENINVOLVEDINTHEMANHATTANPROJECTITMAYBETIMETOREVIVEDIEALCHEMISTENFORTHEHONOUROFTHEREICHSCHWARZESCHATTEN", cardOrder)));
		System.out.println(new String(decode("PIZQEJBLGDXTCMQLHLNMMGIQVOPUXEIKLMEOMBKJMEUQYHRXVVOEURXJYVVSLZWEGKCULSKNAYMSTBGZFYYVRIKBWLXJNSEOPJRFTGHHNSUIZRNLPWQZIWVWJHYPPPYIOELJPQJGOQMUVEBMBMHLNAFPQCWLNWKOYTHPWVNUYDTJSTXBKAPTTKHNMAMRIEPBWOFMKXYFAGYCEJBITEUYWPUCTGHWZKROIWMZWSGRTWRFZQKAVYQWCPHJHZAUFFXUYMUBJBSSKGFFMPQLTWVWFIMSWKKZOWXACWGZEVGEMDFCMRBSFPOPIGCPAVVWPPFQSJVSLSJCBPAYLDOJDXKBEWSTRGDYZBPOYCASAGZEZYJQEQSVSMIFPVAJIDKNEOOJZOVHJOCAMQCMFBCPRZCTYSYQYBYHFJGPUTMZKRFQCPDQSDQOUXTEDQLCHUYKPQQPIJHEDDGYDTIIVTBUEKIRLGCNRTJMVMFRUEDURLRIIWFQRCUXCXNKCXUZZHBGUCWCJTHFENMDLCHBELCYPHEXLVUZSLGCJGIPRIBLDGATKCGHLYJLUSGWDXUTYCNRYHIRRXSIBTJBODYZFGCLCPOSIVNEJHKYIMGYZPQHGARUFMMVFZXMFYAWBCVFTCOCQHTZUPWBHJKABGTIVKFDLSRDOTRZEOLOUJLFZOYCSWXVXLDBFJOPINVCQTTTDHHJRPAWZBIINSEOZTTINLKESGBFXBAXYDIZXCXGXGLXDELJVJZPEWLHVPLOCNACCABSZSACXWKUGPRDHDTNCUAJYZBWDXDDCYQYOLVBOZWSEMNRDNJNDAMGKMWAJLVOQRAMONSDUYCRSNGPDPUASGTE".toCharArray(), new int[] {53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,37,36,35,34,33,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0})));
		//System.out.println((createNewOrder(new ArrayList<Integer>(Arrays.asList(23, 35, 19, 4, 51, 21, 8, 13, 17, 15, 12, 16, 10, 20, 24, 5, 40, 41, 30, 46, 9, 1, 25, 48, 43, 11, 33, 14, 32, 22, 50, 52, 39, 0, 2, 47, 38, 26, 18, 44, 45, 3, 29, 6, 37, 27, 31, 34, 28, 36, 49, 42, 53, 7)))));
		//System.out.println(new String(decode("EWKMCRNUAFCXTJYQMMYYFUTIGWZPKHJMPKBSAIECKVCFMIILCI".toCharArray(), "DIGRAPH")));
	
	}
	
	public static void iteratePortaAutokey(int[] cardOrder, int length) {
		iteratePortaAutokey(ArrayHelper.range(0, 54), cardOrder, length, 0, "");
	}
	
	public static int[] real = ArrayHelper.range(0, 54);
	private static void iteratePortaAutokey(int[] start, int[] targetOrder, int no, int time, String key) {
		for(char i = 'A'; i <= 'Z'; i += 1) {
			String backup = key;
			backup =  i + backup;
			
			if(time + 1 >= no) {
				continue;
			}
			int[] lastOrder = start;
			start = createCardOrder("" + i, start);

			if(Arrays.equals(start, targetOrder)) {
				System.out.println("Found key: " + backup);
			}
			iteratePortaAutokey(start, targetOrder, no, time + 1, backup);
			start = lastOrder;
			

		}
	}
	
	public static boolean isJoker(int card) {
		return card == JOKER_A || card == JOKER_B;
	}

	public static int[] nextCardOrder(int[] oldCardOrder) {
		int[] cardOrder = Arrays.copyOf(oldCardOrder, oldCardOrder.length);
		
		int jA, jB, jT;
		
		//TODO What happens when joker A wraps round
		jT = ArrayHelper.indexOf(cardOrder, JOKER_A);
		if(jT < 53) {
			jA = jT + 1;
			cardOrder[jT] = cardOrder[jA];
		}
		else {
			for(jA = 53; jA > 1; jA--)
				cardOrder[jA] = cardOrder[jA - 1];
			jA = 0;
		}
		cardOrder[jA] = JOKER_A;


		//Move Joker B 2 to right
		jB = ArrayHelper.indexOf(cardOrder, JOKER_B);
		if(jB < 52) {
			jT = jB + 1;
			cardOrder[jB] = cardOrder[jT];
			if (jA == jT)
				jA = jB;
			jB = jT + 1;
			cardOrder[jT] = cardOrder[jB];
			if (jA == jB)
				jA = jT;
		}
		else {
			jT = jB;
			jB = jB - 51;
			for(; jT > jB; jT--)
			{
				cardOrder[jT] = cardOrder[jT - 1];
				if (jA == jT - 1)
					jA = jT;
			}
		}
		cardOrder[jB] = JOKER_B;
		//Triple cut the pack at the 2 Jokers
		int[] temp = new int[54];
				
		if (jA > jB) {
			jT = jA;
			jA = jB;
			jB = jT;
		}
		temp[53] = cardOrder[jB++];
		jT = 0;
		while(jB < 54)
			temp[jT++] = cardOrder[jB++];
			
		jB = jA;
		while (cardOrder[jB] != temp[53])
			temp[jT++] = cardOrder[jB++];

		temp[jT++] = temp[53];
		
		jB = 0;
		while (jB < jA)
			temp[jT++] = cardOrder[jB++];
	
				
				
		jB = temp[53];
		if(!isJoker(jB)) {
				
			jB += 1;
			jA = 0;
			for (jT = jB; jT < 53; jT++)
				cardOrder[jA++] = temp[jT];
			for (jT = 0; jT < jB + 1; jT++)
				cardOrder[jA++] = temp[jT];
					
			cardOrder[53] = temp[53];
		}
		else
			cardOrder = temp;
		
		return cardOrder;
	}
	
	public static int[] previousCardOrder(int[] oldCardOrder) {
		int deckSize = oldCardOrder.length;
		int[] cardOrder = Arrays.copyOf(oldCardOrder, oldCardOrder.length);
		
		int bottomCard = cardOrder[deckSize - 1];
		
		if(!isJoker(bottomCard))
			cardOrder = countCut(cardOrder, deckSize, (deckSize - 1) - (bottomCard + 1) - 1);
		
		int jokerAIndexFinal = ArrayHelper.indexOf(cardOrder, JOKER_A);
		int jokerBIndexFinal = ArrayHelper.indexOf(cardOrder, JOKER_B);
			
		int minJoker = Math.min(jokerAIndexFinal, jokerBIndexFinal);
		int maxJoker = Math.max(jokerAIndexFinal, jokerBIndexFinal);
		
		cardOrder = tripleCut(cardOrder, deckSize, minJoker, maxJoker);
		
		int jokerBIndex = ArrayHelper.indexOf(cardOrder, JOKER_B);
		
		if(jokerBIndex == 0) {
			//System.out.println("EERRRRORR JOKER B IN POS 1");
		}
		else if(jokerBIndex > 2) {
			cardOrder[jokerBIndex] = cardOrder[jokerBIndex - 1];
			cardOrder[jokerBIndex - 1] = cardOrder[jokerBIndex - 2];
			cardOrder[jokerBIndex - 2] = JOKER_B;
		}
		else {
			cardOrder = jokerBWrapInverse(cardOrder, deckSize, jokerBIndex);
		}
		int jA = ArrayHelper.indexOf(cardOrder, JOKER_A);
		int jokerANewIndex = (jA + deckSize - 1) % deckSize;
		cardOrder[jA] = cardOrder[jokerANewIndex];
		cardOrder[jokerANewIndex] = JOKER_A;
		
		return cardOrder;
	}
	
	public static int[] jokerBWrapInverse(int[] cardOrder, int deckSize, int jokerBIndex) {
		int[] c = new int[deckSize];
		 if(jokerBIndex == 2) {
			// System.out.println("s" + ListUtil.toString(cardOrder, 1));
				System.arraycopy(cardOrder, 0, c, 0, jokerBIndex);
				//System.out.println(ListUtil.toString(c, 1));
				System.arraycopy(cardOrder, jokerBIndex + 1, c, jokerBIndex, deckSize - jokerBIndex - 1);
				System.arraycopy(cardOrder, jokerBIndex, c, deckSize - 1, 1);
				//System.out.println("2" + ListUtil.toString(c, 1));
		 }
		 else {
			//System.out.println(ListUtil.toString(cardOrder, 1));
			System.arraycopy(cardOrder, 0, c, 0, jokerBIndex);
			//System.out.println(ListUtil.toString(c, 1));
			System.arraycopy(cardOrder, jokerBIndex + 1, c, jokerBIndex, deckSize - jokerBIndex - 2);
			System.arraycopy(cardOrder, jokerBIndex, c, deckSize - jokerBIndex - 1, 1);
			System.arraycopy(cardOrder, deckSize - jokerBIndex, c, deckSize - jokerBIndex, jokerBIndex == 2 ? 0 : 1);
			//System.out.println(ListUtil.toString(c, 1));
		 }
		return c;
	}
	
	public static int[] jokerBWrap(int[] cardOrder, int deckSize, int jokerBIndex, int fromEnd) {
		int[] c = new int[deckSize];
		int fromStart = 3 - fromEnd;
		if(fromEnd == 2) {
			System.arraycopy(cardOrder, 0, c, 0, fromStart);;
			System.arraycopy(cardOrder, jokerBIndex, c, fromStart, 1);
			System.arraycopy(cardOrder, fromStart, c, fromStart + 1, deckSize - 3);
			System.arraycopy(cardOrder, deckSize - 1, c, deckSize - 1, fromEnd - 1);
		}
		else {
			System.arraycopy(cardOrder, 0, c, 0, fromStart);
			System.arraycopy(cardOrder, jokerBIndex, c, fromStart, 1);
			System.arraycopy(cardOrder, fromStart, c, fromStart + 1, deckSize - 3);
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
		return createCardOrder(key, ArrayHelper.range(0, 54));
	}
	
	public static int[] createCardOrder(String key, int[] startingOrder) {
		int[] cardOrder = startingOrder;
		int deckSize = cardOrder.length;
		
		for(int i = 0; i < key.length(); i++) {
			cardOrder = nextCardOrder(cardOrder);

			cardOrder = countCut(cardOrder, deckSize, key.charAt(i) - 'A');
		}
		
		return cardOrder;
	}
	
	public static String encode(String plainText, int[] cardOrder) {
		String cipherText = "";
		int index = 0;
		
		while(index < plainText.length()) {
			cardOrder = nextCardOrder(cardOrder);
			
			int topCard = cardOrder[0];
			int keyStreamNumber;
			
			if(!isJoker(topCard))
				keyStreamNumber = cardOrder[topCard + 1];
			else 
				keyStreamNumber = cardOrder[cardOrder.length - 1];

			
			if(isJoker(keyStreamNumber))
				continue;
			
			cipherText += (char)(((plainText.charAt(index) - 'A') + (keyStreamNumber + 1)) % 26 + 'A');
			index += 1;
		}

		return cipherText;
	}
	
	public static char[] decodeWithKeyStream(char[] cipherText, int[] keyStream) {
		char[] plainText = new char[keyStream.length];
		int index = 0;
		
		for(int keyStreamNumber : keyStream) {

			
			plainText[index] = (char)(MathHelper.mod(52 + (cipherText[index] - 'A') - (keyStreamNumber + 1), 26) + 'A');
			index += 1;
		}

		return plainText;
	}
	
	public static char[] decode(char[] cipherText, int[] cardOrder) {
		char[] plainText = new char[cipherText.length];
		int index = 0;
		
		while(index < cipherText.length) {

			cardOrder = nextCardOrder(cardOrder);
			
			int topCard = cardOrder[0];
			int keyStreamNumber;
			//System.out.println("Top card" + topCard);
			if(!isJoker(topCard))
				keyStreamNumber = cardOrder[topCard + 1];
			else 
				keyStreamNumber = cardOrder[cardOrder.length - 1];
			
			//System.out.println(keyStreamNumber);
			if(isJoker(keyStreamNumber))
				continue;
			
			plainText[index] = (char)((52 + (cipherText[index] - 'A') - (keyStreamNumber + 1)) % 26 + 'A');
			index += 1;
		}

		return plainText;
	}
}
