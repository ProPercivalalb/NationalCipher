package nationalcipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javalibrary.cipher.permentate.PermentateArray;
import javalibrary.cipher.stats.WordSplit;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.math.ArrayHelper;
import javalibrary.math.Statistics;
import javalibrary.streams.FileReader;
import javalibrary.util.ListUtil;
import nationalcipher.cipher.Solitaire;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.manage.RandomEncrypter;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.stats.StatCalculator;

public class SOLVER {

	public static char[] FINAL_TEXT = "IUTWMVVHVRORNXZZAGPPJSLVPFDLVZMEVGJIVYDZPNAPKQXCIZLGRZWNNCSVKPQTMLKPQNWPGOAYVAPQIPQWRMIXPBTCCEGWHLOZQRFYZGHEJCFETFRULYBLUDNHNYGBEKBKSNXYMRCTHNLXHKHKDFCBBWGVJQBLIESQAJWVLZQTLLASRESDVJMRTBJDOVOAJPQQIVYZHFAFQBHGMVOSDEXBYHKSPYSQLDFRZFYJHEDWPZMVBDCRIYALMMVQWJHVIPDUCKFVZBXMDQVBMXOKODOGYEBWLACFMUVQNSQRKMMNWZBEOOUEXIYDJWUJICKRFQLCESIKHCJQRUFPRRGYHSTZNWOSPAZULTCZPRSOYVETXLLAQMJMVUSOPLCEWYLJUADSJGTLOHOXRHSPZGLADFFAHORATZOBMRVVFDWANXPGUNQGLIHGTRMWBJMFTALCCPZGVLCSINPBUWOVPYXUQUZKTGLTVRZLIRRFNWUULJIVFMEVFIPUNCMDUZAGRHDACDPFKTHTKDKOGUSPYENXTPQUROUZTWCMIGPCKLYWZGUWJRLVKNKQKGPDQNABIRLPVKHOMWXWCQTLGWHXBJFYGIVIWWJXWAVUCOFKJUMZKXKKOEGOETLRAKQVMGKOABGIXQLQMBJYBJUOIZRWHKZCDSMNHMWTRSDBSRULECERARYGFDPERECYGULSJCJWVMLNRZXKQRTTSZWJUVSUXLGKMQPHJWAUBUEJXKYAXCEBLGJHTNKTNZSGYLOFUZUTLNYBHBKGKSCDWIYUXXFYJPNTAFKBGCNLJVGTKDCNBHUSAZRBWSXKICXDISPIRYEOIVXZAWAZIFPGAUYDQSWYCSIQDENTCTAONTOBCIVFYPVPDEMUHDPNTSUOSWVMLXECSHCMHESCGWSUSTAYHKUMOXUFEANTUHNDZHZFLRSHCZBASXPPMCNWMSJTANASBRPHDWJUCTTGMHNTTPIGTVJWWNFUWEOZMCIQMDZDJGLKSSYOXIBGIHPZOMNYBORFNCBTNUHQDOPUOFCCLDFUHIPMKCZKCZVZNMFLWOKIZFKINWQNROAZYLCTMUZYUGOUIMEQSQQAAIQVYQRSPZPUXUNBAORFDDASVMADOGRNPBPKNXGXQOKSEHEAJNZNMQIUMPLHWUFWLEOBKPIASZALJPZQUIKJSGKPGEGMPFBUNHOFKXTSCJMTYBUJEBYNNEVQHKNTIUJBJEEUSQOINRDAZUQMEWEELBLBSGUGXDXLWTUEODCKZYXJUODPPGBSPLAKHPKUZYVWGXMVXEAENQYBPKSDJMTZIBEYMTOFWCVOYZLJSKXGBKAHDTZAMZSFPGPYFFWRBHLNXOAXOITZVFBEXAKVYPAYTIRZMRKIYZRKIQNSDOINPTWMACVOJCXWCOXCEAJBQULUYWQLRERSUIIQTBASGUMAORADTIWOIDHEWLYZBADGFMHHWXNQCZKFTBVJRSYMKGTMLRGNHPUZYOVAOGTVHKHHEQBKTHJYBCUONPEUPDPJMLEOZILYNABGMPEEVJHKADCUEHMNEFWJURTJKTBKZSMTKYPCRVGFPHEIDVFSVNFUMSYAXJAVGMDSZRMHMQVSUEKUWFZFRYOROKWORNQUNJXBHNZAYXWWBEISHIQBOJAAYEKWMGJLGHFDRKBEJTQUQKVRHNJGFHARSOXBRZHKTJFJFNRXQZQRMFKNXRWLVCZBZSFQAOCLPZSGIOTMXTQHBHVYVRYIUSKFXFPKNSQITSRMYGRYXWRFQMBBMJTYOCDTTW".toCharArray();
	//public static char[] FINAL_TEXT = "AGXJESSFKMMJMHXZGJWBCPCVXEBNDKUQOCED".toCharArray();
	public static int[] FINAL_INT = new int[FINAL_TEXT.length];
	
	public static Solution bestSolution = new Solution(new char[0], Double.NEGATIVE_INFINITY);
	
	public static int[] unknowns = new int[] {1, 7, 9, 10, 11, 18, 20, 21, 25, 27, 30, 44, 45, 47, 48, 49};
	
	public static void main(String[] args) throws Exception {
		Languages.english.loadNGramData();
		WordSplit.loadFile();
		String text = WordSplit.splitTextWithPattern("EYESONLYRUMOURSOFASOURCEINBERLINWITHACCESSTOTHERATLINESSOURCESEEMSTOGOBYNAMEOFREICHSDOKTORRUSSIANINTERCEPTSSUGGESTHASBEENSEENINVICINITYOFUSEMBASSYNOTCLEARHOWTOMAKEDIRECTCONTACTALSONOTCLEARWHYOURUSFRIENDSAREKEEPINGTHISTOTHEMSELVESDETAILEDINFOABOUTRATILINESHARDTOOBTAINBUTHIGHVALUECOULDLEADTOARRESTOFMAJORTARGETSOFNUREMBERGINVESTIGATIONSVITALWEREACHREICHSDOKTORATEARLIESTOPPORTUNITYDISCREETENQUIRIESINFRENCHANDUSSECTORSONLYREQUESTFUNDSFORFURTHERINVESTIGATION".toCharArray());
		System.out.println(text);
		WordSplit.getPatternFromWord("CLASSIFICATION");
		/**
		List<String> list = FileReader.compileTextFromResource("/nationalcipherold/plainText.txt");

		List<Double> values = new ArrayList<Double>();
		
		for(String line : list) {
			if(line.isEmpty() || line.startsWith("#")) continue;
			
			String plainText = line;
			
			for(int i = 0; i < 10; ++i) {
				
				IRandEncrypter randEncrypt = RandomEncrypter.getFromName("Bifid");
				String cipherText = randEncrypt.randomlyEncrypt(plainText);
				values.add(StatCalculator.calculateMaxBifidDiagraphicIC(cipherText, 3, 15));
			}
		}

	    Statistics stats = new Statistics(values);
		
		System.out.println("Mean: " + stats.getMean());
		System.out.println("SD: " + stats.getStandardDeviation());**/
	}

	public static void options(int[] lastOrder, int[] unknowns, int times, int count, int[] keyStream) throws Exception {
		if(times <= count) {
			//iteration++;
			//System.out.println(keyStream + " " + new String(Solitaire.decode(FINAL_TEXT, keyStream)));
			char[] chars = Solitaire.decodeWithKeyStream(FINAL_INT, keyStream);
			Solution last = new Solution(chars, Languages.english);
			if(bestSolution.score < last.score) {
				bestSolution = last;
			
				int[] order = lastOrder;
				//for(int i = 0; i < times; i++) {
				//	order = Solitaire.previousCardOrder(order);
				//}
				
				bestSolution.setKeyString(ListUtil.toCardString2(order, 0));
				System.out.println(bestSolution.toString());
			}
			
			return;
		}
		int[] cardOrder = Arrays.copyOf(lastOrder, Solitaire.TOTAL_CARDS);
		
		int jA, jB, jT;
		
		//TODO What happens when joker A wraps round
		jT = ArrayHelper.indexOf(cardOrder, Solitaire.TOTAL_CARDS, Solitaire.JOKER_A);
		if(jT < 53) {
			jA = jT + 1;
			cardOrder[jT] = cardOrder[jA];
		}
		else {
			for(jA = 53; jA > 1; jA--)
				cardOrder[jA] = cardOrder[jA - 1];
			jA = 0;
		}
		cardOrder[jA] = Solitaire.JOKER_A;


		//Move Joker B 2 to right
		jB = ArrayHelper.indexOf(cardOrder, Solitaire.TOTAL_CARDS, Solitaire.JOKER_B);
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
			for(; jT > jB; jT--) {
				cardOrder[jT] = cardOrder[jT - 1];
				if (jA == jT - 1)
					jA = jT;
			}
		}
		cardOrder[jB] = Solitaire.JOKER_B;
		//Triple cut the pack at the 2 Jokers
		int[] tmp = new int[54];
				
		if (jA > jB) {
			jT = jA;
			jA = jB;
			jB = jT;
		}
		tmp[53] = cardOrder[jB++];
		jT = 0;
		while(jB < 54)
			tmp[jT++] = cardOrder[jB++];
			
		jB = jA;
		while (cardOrder[jB] != tmp[53])
			tmp[jT++] = cardOrder[jB++];

		tmp[jT++] = tmp[53];
		
		jB = 0;
		while (jB < jA)
			tmp[jT++] = cardOrder[jB++];
	
		jB = tmp[53];
		if(jB < 0) {
			for(int unknown : unknowns) {

				jA = 0;
				for(jT = unknown + 1; jT < 53; jT++)
					cardOrder[jA++] = tmp[jT];
				for(jT = 0; jT < unknown + 2; jT++)
					cardOrder[jA++] = tmp[jT];
					
				cardOrder[53] = unknown;
	
				insideOrder(cardOrder, ListUtil.removeFromCopy(unknowns, unknown), times, count, keyStream);
			}
		}
		else {
			if(!Solitaire.isJoker(jB)) {
					
				jB += 1;
				jA = 0;
				for (jT = jB; jT < 53; jT++)
					cardOrder[jA++] = tmp[jT];
				for (jT = 0; jT < jB + 1; jT++)
					cardOrder[jA++] = tmp[jT];
						
				cardOrder[53] = tmp[53];
			}
			else
				cardOrder = tmp;
			
			insideOrder(cardOrder, unknowns, times, count, keyStream);
		}

		//System.out.println(ListUtil.toString(cardOrder, 1));
	}
		
	public static void insideOrder(int[] cardOrder, int[] unknowns, int times, int count, int[] keyStream) throws Exception {
		int possible;
		
		int firstCard = cardOrder[0];
		
		
		if(firstCard < 0) {
			for(int unknown : unknowns) {
				
				if(unknown == Solitaire.JOKER_B)
					unknown = Solitaire.JOKER_A;
		
				int possibleIndex = unknown + 1;
				possible = cardOrder[possibleIndex];
				
				if(Solitaire.isJoker(possible)) {
					cardOrder[0] = unknown;
					options(cardOrder, ListUtil.removeFromCopy(unknowns, unknown), times, count, keyStream);
				}
				else if(possible < 0) {
					for(int unknown2 : unknowns)  {
						if(unknown2 != unknown) {
							cardOrder[0] = unknown;
							int last = cardOrder[possibleIndex];
							cardOrder[possibleIndex] = unknown2;

							keyStream[count] = unknown2;
							
							options(cardOrder, ListUtil.removeFromCopy(unknowns, unknown, unknown2), times, count + 1, keyStream);
							cardOrder[possibleIndex] = last;
						}
					}
				}
				else {
					cardOrder[0] = unknown;
					keyStream[count] = possible;
					options(cardOrder, ListUtil.removeFromCopy(unknowns, unknown), times, count + 1, keyStream);
				}
			}
		}
		else {
			if(firstCard == Solitaire.JOKER_B)
				firstCard = Solitaire.JOKER_A;
			
			int possibleIndex = firstCard + 1;
			possible = cardOrder[possibleIndex];
	
			if(Solitaire.isJoker(possible))
				options(cardOrder, unknowns, times, count, keyStream);
			else if(possible < 0) {
				for(int unknown2 : unknowns) {
	
					int last = cardOrder[possibleIndex];
					cardOrder[possibleIndex] = unknown2;
				
					keyStream[count] = unknown2;
					
					options(cardOrder, ListUtil.removeFromCopy(unknowns, unknown2), times, count + 1, keyStream);
					cardOrder[possibleIndex] = last;
				}
			}
			else {
				keyStream[count] = possible;
				options(cardOrder, unknowns, times, count + 1, keyStream);
			}
		}
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
