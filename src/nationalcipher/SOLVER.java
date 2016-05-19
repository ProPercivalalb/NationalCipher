package nationalcipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javalibrary.cipher.permentate.PermentateArray;
import javalibrary.fitness.NGramData;
import javalibrary.fitness.TextFitness;
import javalibrary.language.English;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.math.Statistics;
import javalibrary.streams.FileReader;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.Solitaire;
import nationalcipher.cipher.Solitaire.SolitaireAttack;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.manage.RandomEncrypter;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.stats.CipherStatistics;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.AMSCOKey;
import nationalcipher.cipher.tools.KeyGeneration;

public class SOLVER {

	public static char[] FINAL_TEXT = "IUTWMVVHVRORNXZZAGPPJSLVPFDLVZMEVGJIVYDZPNAPKQXCIZLGRZWNNCSVKPQTMLKPQNWPGOAYVAPQIPQWRMIXPBTCCEGWHLOZQRFYZGHEJCFETFRULYBLUDNHNYGBEKBKSNXYMRCTHNLXHKHKDFCBBWGVJQBLIESQAJWVLZQTLLASRESDVJMRTBJDOVOAJPQQIVYZHFAFQBHGMVOSDEXBYHKSPYSQLDFRZFYJHEDWPZMVBDCRIYALMMVQWJHVIPDUCKFVZBXMDQVBMXOKODOGYEBWLACFMUVQNSQRKMMNWZBEOOUEXIYDJWUJICKRFQLCESIKHCJQRUFPRRGYHSTZNWOSPAZULTCZPRSOYVETXLLAQMJMVUSOPLCEWYLJUADSJGTLOHOXRHSPZGLADFFAHORATZOBMRVVFDWANXPGUNQGLIHGTRMWBJMFTALCCPZGVLCSINPBUWOVPYXUQUZKTGLTVRZLIRRFNWUULJIVFMEVFIPUNCMDUZAGRHDACDPFKTHTKDKOGUSPYENXTPQUROUZTWCMIGPCKLYWZGUWJRLVKNKQKGPDQNABIRLPVKHOMWXWCQTLGWHXBJFYGIVIWWJXWAVUCOFKJUMZKXKKOEGOETLRAKQVMGKOABGIXQLQMBJYBJUOIZRWHKZCDSMNHMWTRSDBSRULECERARYGFDPERECYGULSJCJWVMLNRZXKQRTTSZWJUVSUXLGKMQPHJWAUBUEJXKYAXCEBLGJHTNKTNZSGYLOFUZUTLNYBHBKGKSCDWIYUXXFYJPNTAFKBGCNLJVGTKDCNBHUSAZRBWSXKICXDISPIRYEOIVXZAWAZIFPGAUYDQSWYCSIQDENTCTAONTOBCIVFYPVPDEMUHDPNTSUOSWVMLXECSHCMHESCGWSUSTAYHKUMOXUFEANTUHNDZHZFLRSHCZBASXPPMCNWMSJTANASBRPHDWJUCTTGMHNTTPIGTVJWWNFUWEOZMCIQMDZDJGLKSSYOXIBGIHPZOMNYBORFNCBTNUHQDOPUOFCCLDFUHIPMKCZKCZVZNMFLWOKIZFKINWQNROAZYLCTMUZYUGOUIMEQSQQAAIQVYQRSPZPUXUNBAORFDDASVMADOGRNPBPKNXGXQOKSEHEAJNZNMQIUMPLHWUFWLEOBKPIASZALJPZQUIKJSGKPGEGMPFBUNHOFKXTSCJMTYBUJEBYNNEVQHKNTIUJBJEEUSQOINRDAZUQMEWEELBLBSGUGXDXLWTUEODCKZYXJUODPPGBSPLAKHPKUZYVWGXMVXEAENQYBPKSDJMTZIBEYMTOFWCVOYZLJSKXGBKAHDTZAMZSFPGPYFFWRBHLNXOAXOITZVFBEXAKVYPAYTIRZMRKIYZRKIQNSDOINPTWMACVOJCXWCOXCEAJBQULUYWQLRERSUIIQTBASGUMAORADTIWOIDHEWLYZBADGFMHHWXNQCZKFTBVJRSYMKGTMLRGNHPUZYOVAOGTVHKHHEQBKTHJYBCUONPEUPDPJMLEOZILYNABGMPEEVJHKADCUEHMNEFWJURTJKTBKZSMTKYPCRVGFPHEIDVFSVNFUMSYAXJAVGMDSZRMHMQVSUEKUWFZFRYOROKWORNQUNJXBHNZAYXWWBEISHIQBOJAAYEKWMGJLGHFDRKBEJTQUQKVRHNJGFHARSOXBRZHKTJFJFNRXQZQRMFKNXRWLVCZBZSFQAOCLPZSGIOTMXTQHBHVYVRYIUSKFXFPKNSQITSRMYGRYXWRFQMBBMJTYOCDTTW".toCharArray();
	//public static char[] FINAL_TEXT = "AGXJESSFKMMJMHXZGJWBCPCVXEBNDKUQOCED".toCharArray();
	public static int[] FINAL_INT = new int[FINAL_TEXT.length];
	
	public static Solution bestSolution = new Solution(new char[0], Double.NEGATIVE_INFINITY);
	
	public static int[] deck2016 = new int[] {38,34,46,3,4,41,16,51,19,12,52,15,29,39,37,33,42,13,40,6,26,43,0,5,32,14,53,35,17,23,2,8,50,36,22,-1,-1,-1,-1,-1,-1,-1,-1,24,-1,-1,-1,-1,-1,-1,31,-1,28,-1};
	public static int[] unknowns = new int[] {1, 7, 9, 10, 11, 18, 20, 21, 25, 27, 30, 44, 45, 47, 48, 49};
	
	public static void main(String[] args) throws Exception {
		Languages.english.loadNGramData();
		/**
		List<List<Object>> num_dev = CipherStatistics.getResultsFromStats("SENDCAOMRDIONBITNSCISAGTBWLTEAEAOREDIFASRMVYPEOIAALFUIRODLOCARTNJOUANECNIMTPSOAHIKSAT");
		 
	    
	    Comparator<List<Object>> comparator = new Comparator<List<Object>>() {
	    	@Override
	        public int compare(List<Object> c1, List<Object> c2) {
	        	double diff = (double)c1.get(1) - (double)c2.get(1);
	        	return diff == 0.0D ? 0 : diff > 0 ? 1 : -1; 
	        }
	    };

	    Collections.sort(num_dev, comparator);
	  
	    for(int i = 0; i < num_dev.size(); i++) {
	    	System.out.println(num_dev.get(i).get(0) + " " + num_dev.get(i).get(1));
	    }

		List<String> list = FileReader.compileTextFromResource("/nationalcipherold/plainText.txt");

		List<Double> values = new ArrayList<Double>();
		
		for(String line : list) {
			if(line.isEmpty() || line.startsWith("#")) continue;
			
			String plainText = line;
			
			for(int i = 0; i < 200; ++i) {
				
				IRandEncrypter randEncrypt = RandomEncrypter.getFromName("Swagman");
				String cipherText = randEncrypt.randomlyEncrypt(plainText);
				values.add(StatCalculator.calculateMaxTrifidDiagraphicIC(cipherText, 3, 15));
			}
		}

	    Statistics stats = new Statistics(values);
		
		System.out.println(" " + String.format("%.25f", stats.getMean()) + ", " + String.format("%.25f", stats.getStandardDeviation()));**/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
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
		
		System.out.println(stats);
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
				recursive(cipherText, solution.text, 7, offset + n, deck);
			}
			else {
				for(int k = 0; k < n + offset; k++)
					task.text[k] = solution.text[k];
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
