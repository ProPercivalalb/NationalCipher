package nationalcipher;

import java.util.ArrayList;
import java.util.List;

import javalibrary.cipher.permentate.PermentateArray;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.streams.FileReader;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.other.Solitaire;
import nationalcipher.cipher.base.other.Solitaire.SolitaireAttack;
import nationalcipher.cipher.base.substitution.PortaAutokey;
import nationalcipher.cipher.base.transposition.Cadenus;
import nationalcipher.cipher.base.transposition.Swagman;
import nationalcipher.cipher.decrypt.solitaire.DeckParse;
import nationalcipher.cipher.decrypt.solitaire.SolitaireSolver;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.AMSCOKey;

public class SOLVER {

	public static char[] FINAL_TEXT = "IUTWMVVHVRORNXZZAGPPJSLVPFDLVZMEVGJIVYDZPNAPKQXCIZLGRZWNNCSVKPQTMLKPQNWPGOAYVAPQIPQWRMIXPBTCCEGWHLOZQRFYZGHEJCFETFRULYBLUDNHNYGBEKBKSNXYMRCTHNLXHKHKDFCBBWGVJQBLIESQAJWVLZQTLLASRESDVJMRTBJDOVOAJPQQIVYZHFAFQBHGMVOSDEXBYHKSPYSQLDFRZFYJHEDWPZMVBDCRIYALMMVQWJHVIPDUCKFVZBXMDQVBMXOKODOGYEBWLACFMUVQNSQRKMMNWZBEOOUEXIYDJWUJICKRFQLCESIKHCJQRUFPRRGYHSTZNWOSPAZULTCZPRSOYVETXLLAQMJMVUSOPLCEWYLJUADSJGTLOHOXRHSPZGLADFFAHORATZOBMRVVFDWANXPGUNQGLIHGTRMWBJMFTALCCPZGVLCSINPBUWOVPYXUQUZKTGLTVRZLIRRFNWUULJIVFMEVFIPUNCMDUZAGRHDACDPFKTHTKDKOGUSPYENXTPQUROUZTWCMIGPCKLYWZGUWJRLVKNKQKGPDQNABIRLPVKHOMWXWCQTLGWHXBJFYGIVIWWJXWAVUCOFKJUMZKXKKOEGOETLRAKQVMGKOABGIXQLQMBJYBJUOIZRWHKZCDSMNHMWTRSDBSRULECERARYGFDPERECYGULSJCJWVMLNRZXKQRTTSZWJUVSUXLGKMQPHJWAUBUEJXKYAXCEBLGJHTNKTNZSGYLOFUZUTLNYBHBKGKSCDWIYUXXFYJPNTAFKBGCNLJVGTKDCNBHUSAZRBWSXKICXDISPIRYEOIVXZAWAZIFPGAUYDQSWYCSIQDENTCTAONTOBCIVFYPVPDEMUHDPNTSUOSWVMLXECSHCMHESCGWSUSTAYHKUMOXUFEANTUHNDZHZFLRSHCZBASXPPMCNWMSJTANASBRPHDWJUCTTGMHNTTPIGTVJWWNFUWEOZMCIQMDZDJGLKSSYOXIBGIHPZOMNYBORFNCBTNUHQDOPUOFCCLDFUHIPMKCZKCZVZNMFLWOKIZFKINWQNROAZYLCTMUZYUGOUIMEQSQQAAIQVYQRSPZPUXUNBAORFDDASVMADOGRNPBPKNXGXQOKSEHEAJNZNMQIUMPLHWUFWLEOBKPIASZALJPZQUIKJSGKPGEGMPFBUNHOFKXTSCJMTYBUJEBYNNEVQHKNTIUJBJEEUSQOINRDAZUQMEWEELBLBSGUGXDXLWTUEODCKZYXJUODPPGBSPLAKHPKUZYVWGXMVXEAENQYBPKSDJMTZIBEYMTOFWCVOYZLJSKXGBKAHDTZAMZSFPGPYFFWRBHLNXOAXOITZVFBEXAKVYPAYTIRZMRKIYZRKIQNSDOINPTWMACVOJCXWCOXCEAJBQULUYWQLRERSUIIQTBASGUMAORADTIWOIDHEWLYZBADGFMHHWXNQCZKFTBVJRSYMKGTMLRGNHPUZYOVAOGTVHKHHEQBKTHJYBCUONPEUPDPJMLEOZILYNABGMPEEVJHKADCUEHMNEFWJURTJKTBKZSMTKYPCRVGFPHEIDVFSVNFUMSYAXJAVGMDSZRMHMQVSUEKUWFZFRYOROKWORNQUNJXBHNZAYXWWBEISHIQBOJAAYEKWMGJLGHFDRKBEJTQUQKVRHNJGFHARSOXBRZHKTJFJFNRXQZQRMFKNXRWLVCZBZSFQAOCLPZSGIOTMXTQHBHVYVRYIUSKFXFPKNSQITSRMYGRYXWRFQMBBMJTYOCDTTW".toCharArray();
	//public static char[] FINAL_TEXT = "AGXJESSFKMMJMHXZGJWBCPCVXEBNDKUQOCED".toCharArray();
	public static int[] FINAL_INT = new int[FINAL_TEXT.length];
	
	public static Solution bestSolution = new Solution(new char[0], Double.NEGATIVE_INFINITY);
	
	public static int[] deck2016 = new int[] {38,34,46,3,4,41,16,51,19,12,52,15,29,39,37,33,42,13,40,6,26,43,0,5,32,14,53,35,17,23,2,8,50,36,22,-1,-1,-1,-1,-1,-1,-1,-1,24,-1,-1,-1,-1,-1,-1,31,-1,28,-1};
	public static int[] unknowns = new int[] {1, 7, 9, 10, 11, 18, 20, 21, 25, 27, 30, 44, 45, 47, 48, 49};
	
	public static void main(String[] args) throws Exception {
		Languages.english.loadNGramData();
		IRandEncrypter randomEncrypt = RandomUtil.pickRandomElement(new PortaAutokey());//, new TwoSquare(), new FourSquare(), new Trifid(), new Bazeries(), new Enigma(), new Porta(), new Bifid(), new Affine(), new Keyword(), new Playfair(), new AMSCO(), new Vigenere(), new Solitaire(), new Hill());
		
		String cipherText = randomEncrypt.randomlyEncrypt(RandomUtil.pickRandomElement(FileReader.compileTextFromResource("/plainText.txt", true)));
		System.out.println(cipherText);
		System.out.println(randomEncrypt.getClass().getName());
		
	//	System.out.println(StringAnalyzer.getOrderedCharacterCount("BBAACCFGHHH".toCharArray()));
	    //System.out.println(Cadenus.decode("ANTODELEEEUHRSIDRBHMHDRRHNIMEFMTHGEAETAKSEOMEHETYAASUVOYEGRASTMMUUAEENABBTPCHEHTARORIKSWOSMVALEATNED".toCharArray(), "WINK"));
		System.out.println(Swagman.encode("DEARMARKTHINGSAREALOTCLEARERNOWIFLEWOUTTOINSPECTTHESHIPMYSELFLASTNIGHTANDTOOKAGOODLOOKAROUNDTHEREASONTHESHIPWASNOTSCUTTLEDWASTHATTHEVALVESHADJAMMEDITLOOKSLIKETHEDRIFTWOODWASPULLEDINTOTHEMECHANISMANDBLOCKEDTHEINLETPRESUMABLYTHECREWHADALREADYABANDONEDTHEVESSELWHICHWASLUCKYFORUSWITHOUTTHESHIPWEWOULDHAVEHADNOIDEATHATTHEFDAHADBEENOPERATINGINTHESEWATERSSEAHORSEISNOLONGERAMYSTERYTHECUTAWAYONTHESTARBOARDSIDECLEAREDANAREAOFAROUNDFIVEMETERSSQUAREWITHADISTINCTIVEPATTERNOFBOLTSFASTENEDTOREINFORCEDDECKPLATESISAWSOMETHINGLIKETHISONASUBRESCUEMISSIONACOUPLEOFYEARSAGOWHENTHEYFITTEDALOCALSHIPWITHAJURYRIGGEDINSPECTIONSYSTEMTHEDECKPLATESCANCARRYACRANEDESIGNEDTODEPLOYANROVAREMOTEOPERATEDVEHICLEDESIGNEDFORUNDERSEAOPERATIONSIWASALREADYCONCERNEDABOUTTHEREFERENCETOTHECABLESINTHELASTPARTOFTHEFDALOGBUTTHENEXTSECTIONHASMEREALLYWORRIEDITISENCRYPTEDWITHAMORESECUREMODIFIEDAMSCOTRANSPOSITIONCIPHERANDTELLSUSWHATTHEYWEREREALLYUPTOWHATIDONTUNDERSTANDISHOWTHEWHOLEASSEMBLYISPOWEREDTHESORTOFCOMPUTINGTHEYMUSTBEDOINGISREALLYINTENSIVEANDWOULDBURNTHROUGHABATTERYINDAYSINTHATTIMETHEIRINTERCEPTMIGHTNOTCATCHANYTHINGUSEFULBUTTHEYCANHARDLYHAVEHIJACKEDALOCALSOCKETINTHEMIDDLEOFTHEOCEANCANYOUGETMEACHARTSHOWINGTHEDEEPSEACABLESINTHEREGIONIDONTIMAGINETHEUSWILLBEAPROBLEMBUTITMAYNEEDSOMEDIPLOMACYTOGETTHEFULLCOVERAGEMAPSFROMTHEOMANIGOVERNMENTIFIAMRIGHTITISINTHEIRBESTINTERESTSTOPLAYALONGWEALLHAVEALOTTOLOSEHERE", new int[] {3, 0, 4,2, 1, 4,1,0,3,2, 1,4,2,0,3, 2,3,1,4,0,0,2,3,1,4}, 5));
		
		String key = "KKEY";
		int[] order = new int[key.length()];
		
		int p = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch)
			for(int i = 0; i < order.length; i++)
				if(ch == key.charAt(i))
					order[i] = p++;

		//System.out.println(Arrays.toString(order));
		
		//for(char ch = 'A'; ch <= 'Z'; ++ch)
		//	System.out.println(ch +" " + Cadenus.charValue(ch));
		
	   // for(Class<? extends TextStatistic> clz :StatisticHandler.map.values()) 
	    //	StatisticHandler.calculateStatPrint(new Cadenus(), clz);
	    /**
	    List<Double> data = new ArrayList<Double>();
	    for(int i = 0; i < 2000; i++) {
	    String cipherText2 = new Caesar().randomlyEncrypt(RandomUtil.pickRandomElement(FileReader.compileTextFromResource("/plainText.txt", true)));
	    Statistics stats = StatCalculator.averageDifferenceinCharacter(cipherText2);
	    	data.add(stats.getMean());
	    }
	    Statistics s = new Statistics(data);
	    System.out.println(s.getMean() + " " +s.getStandardDeviation());
	    System.out.println("--------------------");
	    
	    List<Double> data2 = new ArrayList<Double>();
	    for(int i = 0; i < 2000; i++) {
		    String cipherText2 = new Keyword().randomlyEncrypt(RandomUtil.pickRandomElement(FileReader.compileTextFromResource("/plainText.txt", true)));
		    Statistics stats = StatCalculator.averageDifferenceinCharacter(cipherText2);
		    data2.add(stats.getMean());
		    }
	    Statistics s2 = new Statistics(data2);
	    System.out.println(s2.getMean() + " " +s2.getStandardDeviation());
	    System.out.println("--------------------");
	   
		List<List<Object>> num_dev = CipherStatistics.getResultsFromStats(cipherText);
		 
	    
	    Comparator<List<Object>> comparator = new Comparator<List<Object>>() {
	    	@Override
	        public int compare(List<Object> c1, List<Object> c2) {
	        	double diff = (double)c1.get(1) - (double)c2.get(1);
	        	return diff == 0.0D ? 0 : diff > 0 ? 1 : -1; 
	        }
	    };

	    Collections.sort(num_dev, comparator);
    
	    for(int i = 0; i < num_dev.size(); i++) {
	    	
	    	int l = ((String)num_dev.get(i).get(0)).length();
	    	System.out.println(num_dev.get(i).get(0) + StringTransformer.repeat(" ", 30 - l) + num_dev.get(i).get(1));
	    }

	
	 	for(String s : new String[] {"PortaAutokey"})
	 		CipherStatistics.compileStatsForCipher(RandomEncrypter.getFromName(s));
	**/
	    
	    
	    
	    
	    
/**
	    HashMap<String, Double> mapping = new HashMap<String, Double>();

		double total = 0.0D;
		
		   HashMap<String, Double> mapping2 = new HashMap<String, Double>();
		List<String> list2 = FileReader.compileTextFromResource("/resources/data/bigram_count.txt", true);
		for(String line : list2) {
			String[] str = line.split("% ");
			for(String data : str) {
				String[] s = data.split(" ");
				//System.out.println(data);
				mapping2.put(s[0], Double.valueOf(s[1]));
			}

		}
		List<Map.Entry<String, Double>> entries = new ArrayList<Map.Entry<String, Double>>(mapping2.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
			  public int compare(
			      Map.Entry<String, Double> entry1, Map.Entry<String, Double> entry2) {
			    return entry2.getValue().compareTo(entry1.getValue());
			  }
			});
		for(Map.Entry<String, Double> en : entries) {
			//System.out.println(en.getKey() + " " + String.format("%.3f", en.getValue()));
		}
			//System.out.println(entries);
		
		
		List<String> list = FileReader.compileTextFromResource("/resources/data/bigram_count.txt");

		for(String line : list) {
			String[] str = line.split(" ");
					
			if(str.length < 2) continue;
					
	
			mapping.put(str[0], Double.valueOf(str[1]));
		}

		for(String gram : mapping.keySet()) {
			double count = mapping.get(gram);
			double log = Math.log(count);

			if(count == 0)
				mapping.put(gram, 0.0D);
			else
				mapping.put(gram, log);
		}
		
		double smallest = MathHelper.findSmallestDouble(mapping.values());
		
		for(String gram : mapping.keySet()) {
			if(mapping.get(gram) != 0)
			mapping.put(gram, -smallest + mapping.get(gram));
		}
		
		for(char a = 'A'; a <= 'Z'; a++) {
			String s1 = "";
			for(char b = 'A'; b <= 'Z'; b++) {
				String s = a + "" + b;
				if(mapping.containsKey(s))
					s1 += String.format("%.1f", mapping.get(s)) + ", ";
				else
					s1 += "0, ";
			}
			System.out.println(s1);
		}
		
		for(String text1 : FileReader.compileTextFromResource("/plainText.txt", true)) {
		String text = text1;
			double score = 0;
			for(int i = 0; i < text.length() - 1; i++)
				if(Character.isLetter(text.charAt(i)) && Character.isLetter(text.charAt(i + 1)))
					score += mapping.get(text.substring(i, i + 2));
			
		System.out.println("Diff " + (((score * 100 / (text.length() - 1)) / StatCalculator.calculateLDI(text) * 100)));
	    //System.out.println("LDI " + StatCalculator.calculateLDI(text));
		Thread.sleep(1000);
		}**/
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/**
		
	//	System.out.println(new String(Solitaire.decode("IUTWMVVHVRORNXZZAGP".toCharArray(), new int[] {38,34,46,3,4,41,16,51,19,12,52,15,29,39,37,33,42,13,40,6,26,43,0,5,32,14,53,35,17,23,2,8,50,36,22,45,20,9,11,18,25,48,44,24,27,1,21,7,30,47,31,10,28,49})));
		//public static int[] unknowns =                                                 new int[] {1, 7, 9, 10, 11, 18, 20, 21, 25, 27, 30, 44, 45, 47, 48, 49};
		
		int[] orderDeck = KeyGeneration.createOrder(54);
		
		String ci = Solitaire.encode("DEARMARKTHANKSFORTHELATESTREPORTFROMTHEONSITETEAMITSHOWSTHATTHESHIPBOARDGPSSYSTEMWASCOMPLETELYSCRAMBLEDSOWEARENOTGOINGTOBEABLETOTRACEHERMOVEMENTSFROMTHATDOWEHAVEANYODDTRACESFROMONSHORERADARTHATGIVEAHINTOFWHERESHEMIGHTHAVEBEENTHECOMMENTINTHELASTMESSAGETHATTHEPIRATESCOMPLETEDTHESURVEYEVENTHOUGHTHEYHADMOVEDSOUTHTOAVOIDDETECTIONSHOULDHAVETOLDMETHATTHESURVEYWASNOTGEOGRAPHICATFIRSTITHOUGHTITMIGHTHAVEBEENREFERRINGTOATELECOMSSURVEYSINCEYOUMENTIONEDTHELONGAERIALBUTACTUALLYTHEATTACHEDMESSAGEISVERYREVEALINGSTILLNOTSUREWHATTHESURVEYWASFORTHOUGHANDHOWTHATISCONNECTEDTOTHEMISSINGSUPERSTRUCTURECANYOUGETMEANYPICTURESHARRY", orderDeck);
		for(int i = 0; i < 16; i++)
			orderDeck[RandomUtil.pickRandomInt(54)] = -1;
	
		Timer timer = new Timer();
		String cipherText = "IUTWMVVHVRORNXZZAGPPJSLVPFDLVZMEVGJIVYDZPNAPK";
	
		recursive(cipherText, new char[0], 7, 0, new DeckParse(deck2016));
		
			
		timer.recordTime();

		Statistics stats = timer.getRecordedTimesStats();
		
		System.out.println(stats);**/
	}
	
	public static void recursive(String cipherText, char[] prefix, int n, int offset, DeckParse startingDeck) {
		System.out.println("Starting unknowns: " + startingDeck.countUnknowns());
		List<Solution> solutions = SolitaireSolver.swiftAttack(cipherText, prefix, n, offset, startingDeck, 5);
		
		
		SolitaireSolution task = new SolitaireSolution(cipherText.substring(offset + n, cipherText.length()).toCharArray(), offset + n);
		System.out.println("Solutions: " + solutions.size());
		for(Solution solution : solutions) {
			DeckParse deck = new DeckParse(solution.keyString);
			task.incompleteOrder = deck.order;
			task.emptyIndex = deck.emptyIndex;
			System.out.println(deck);
			
			if(deck.countUnknowns() > 11) {
				recursive(cipherText, solution.getText(), 7, offset + n, deck);
			}
			else {
				for(int k = 0; k < n + offset; k++)
					task.text[k] = solution.getText()[k];
				Timer timer2 = new Timer();
				Creator.iterateAMSCO(task, deck.unknownCards);
				timer2.displayTime();
			}
		}
	}
	
	private static class SoiltaireStartAttack implements SolitaireAttack {

    	public Solution bestSolution;
    	public List<Solution> solutions;
    	public int[] intText;
    	
    	private SoiltaireStartAttack(String cipherText) {
    		this.bestSolution = new Solution();
    		this.solutions = new ArrayList<Solution>();
    		this.intText = new int[cipherText.length()];
    		for(int i = 0; i < cipherText.length(); i++)
    			this.intText[i] = cipherText.charAt(i) - 'A';
    	}
   
		@Override
		public void tryKeyStream(int[] keyStream, int[] lastOrder) {
			
			char[] chars = Solitaire.decodeWithKeyStream(this.intText, keyStream);

			Solution last = new Solution(chars, Languages.english);
			
			if(last.score > this.intText.length * -2) {
				last.setKeyString(ListUtil.toCardString(lastOrder, 0));
				this.solutions.add(last);
			}
			
			if(this.bestSolution.score < last.score) {
				this.bestSolution = last;

				
				
				this.bestSolution.setKeyString(ListUtil.toCardString(lastOrder, 0));
				
				System.out.println(this.bestSolution);
			}
			
		}

		@Override
		public int getSubBranches() {
			return this.intText.length;
		}
    }

	public static class SolitaireSolution implements AMSCOKey {
		
		public char[] text;
		public int startingLength;
		public int[] incompleteOrder;
		public int[] emptyIndex;
		public Solution lastSolution, bestSolution;
		
		public SolitaireSolution(char[] text, int startingLength) {
			this.bestSolution = new Solution();
			this.text = ArrayUtil.concat(new char[startingLength], text);
			this.startingLength = startingLength;
		}
		
		@Override
		public void onIteration(int[] order) {
			for(int i = 0; i < this.emptyIndex.length; i++)
				this.incompleteOrder[this.emptyIndex[i]] = order[i];
			
			this.lastSolution = new Solution(Solitaire.decode(this.text, this.startingLength, this.incompleteOrder), Languages.english);
			
			if(this.lastSolution.score > this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(ListUtil.toString(this.incompleteOrder, 1));
				System.out.println(this.bestSolution);
			}
		}
/**
		public double score = 0.0D;
		
		public char[] decode(char[] cipherText, int[] cardOrder, double bestScore, NGramData quadgramData) {
			this.score = 0;
			
			int length = cipherText.length;
			char[] plainText = new char[length];
			
			int index = 0;
			
			while(index < length) {

				cardOrder = Solitaire.nextCardOrder(cardOrder);
				
				int topCard = cardOrder[0];
				int keyStreamNumber;
				
				if(topCard == Solitaire.JOKER_B)
					topCard = Solitaire.JOKER_A;
				keyStreamNumber = cardOrder[topCard + 1];

				
				if(Solitaire.isJoker(keyStreamNumber))
					continue;
				
				plainText[index] = (char)((52 + (cipherText[index] - 'A') - (keyStreamNumber + 1)) % 26 + 'A');
				index += 1;
				
				if(index > 3) {
					score += TextFitness.scoreWord(plainText, index - 4, quadgramData);
					if(score < bestScore)
						break;
				}
			}
		
			return plainText;
		}	**/
	}
	
	
	public static void permutate(PermentateArray permen, char[] arr) {
		permutate(permen, arr, 0);
	}
	
	private static void permutate(PermentateArray permen, char[] arr, int pos) {
	    if(arr.length - pos == 1) {
	    	int[] order = Solitaire.createCardOrder(new String(arr));
	    	if(order[0] == 38) {
	    		System.out.println(ListUtil.toString(order, 1));
	    	}
	    }
	    else
		    for(int i = pos; i < arr.length; i++) {
		    	char h = arr[pos];
		    	char j = arr[i];
		        arr[pos] = j;
		        arr[i] = h;
		            
		        permutate(permen, arr, pos + 1);
		        arr[pos] = h;
		    	arr[i] = j;
		    }
	}
}
