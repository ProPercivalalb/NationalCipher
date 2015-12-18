package nationalcipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javalibrary.cipher.permentate.PermentateArray;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.math.ArrayHelper;
import javalibrary.util.ListUtil;
import nationalcipher.cipher.Solitaire;
import nationalcipher.cipher.manage.Solution;

public class SOLVER {

	public static final int JOKER_A = 52;
	public static final int JOKER_B = 53;
	public static char[] FINAL_TEXT = "IUTWMVVHVRORNXZZAGPPJSLVPFDLVZMEVGJIVYDZPNAPKQXCIZLGRZWNNCSVKPQTMLKPQNWPGOAYVAPQIPQWRMIXPBTCCEGWHLOZQRFYZGHEJCFETFRULYBLUDNHNYGBEKBKSNXYMRCTHNLXHKHKDFCBBWGVJQBLIESQAJWVLZQTLLASRESDVJMRTBJDOVOAJPQQIVYZHFAFQBHGMVOSDEXBYHKSPYSQLDFRZFYJHEDWPZMVBDCRIYALMMVQWJHVIPDUCKFVZBXMDQVBMXOKODOGYEBWLACFMUVQNSQRKMMNWZBEOOUEXIYDJWUJICKRFQLCESIKHCJQRUFPRRGYHSTZNWOSPAZULTCZPRSOYVETXLLAQMJMVUSOPLCEWYLJUADSJGTLOHOXRHSPZGLADFFAHORATZOBMRVVFDWANXPGUNQGLIHGTRMWBJMFTALCCPZGVLCSINPBUWOVPYXUQUZKTGLTVRZLIRRFNWUULJIVFMEVFIPUNCMDUZAGRHDACDPFKTHTKDKOGUSPYENXTPQUROUZTWCMIGPCKLYWZGUWJRLVKNKQKGPDQNABIRLPVKHOMWXWCQTLGWHXBJFYGIVIWWJXWAVUCOFKJUMZKXKKOEGOETLRAKQVMGKOABGIXQLQMBJYBJUOIZRWHKZCDSMNHMWTRSDBSRULECERARYGFDPERECYGULSJCJWVMLNRZXKQRTTSZWJUVSUXLGKMQPHJWAUBUEJXKYAXCEBLGJHTNKTNZSGYLOFUZUTLNYBHBKGKSCDWIYUXXFYJPNTAFKBGCNLJVGTKDCNBHUSAZRBWSXKICXDISPIRYEOIVXZAWAZIFPGAUYDQSWYCSIQDENTCTAONTOBCIVFYPVPDEMUHDPNTSUOSWVMLXECSHCMHESCGWSUSTAYHKUMOXUFEANTUHNDZHZFLRSHCZBASXPPMCNWMSJTANASBRPHDWJUCTTGMHNTTPIGTVJWWNFUWEOZMCIQMDZDJGLKSSYOXIBGIHPZOMNYBORFNCBTNUHQDOPUOFCCLDFUHIPMKCZKCZVZNMFLWOKIZFKINWQNROAZYLCTMUZYUGOUIMEQSQQAAIQVYQRSPZPUXUNBAORFDDASVMADOGRNPBPKNXGXQOKSEHEAJNZNMQIUMPLHWUFWLEOBKPIASZALJPZQUIKJSGKPGEGMPFBUNHOFKXTSCJMTYBUJEBYNNEVQHKNTIUJBJEEUSQOINRDAZUQMEWEELBLBSGUGXDXLWTUEODCKZYXJUODPPGBSPLAKHPKUZYVWGXMVXEAENQYBPKSDJMTZIBEYMTOFWCVOYZLJSKXGBKAHDTZAMZSFPGPYFFWRBHLNXOAXOITZVFBEXAKVYPAYTIRZMRKIYZRKIQNSDOINPTWMACVOJCXWCOXCEAJBQULUYWQLRERSUIIQTBASGUMAORADTIWOIDHEWLYZBADGFMHHWXNQCZKFTBVJRSYMKGTMLRGNHPUZYOVAOGTVHKHHEQBKTHJYBCUONPEUPDPJMLEOZILYNABGMPEEVJHKADCUEHMNEFWJURTJKTBKZSMTKYPCRVGFPHEIDVFSVNFUMSYAXJAVGMDSZRMHMQVSUEKUWFZFRYOROKWORNQUNJXBHNZAYXWWBEISHIQBOJAAYEKWMGJLGHFDRKBEJTQUQKVRHNJGFHARSOXBRZHKTJFJFNRXQZQRMFKNXRWLVCZBZSFQAOCLPZSGIOTMXTQHBHVYVRYIUSKFXFPKNSQITSRMYGRYXWRFQMBBMJTYOCDTTW".toCharArray();
	//public static char[] FINAL_TEXT = "AGXJESSFKMMJMHXZGJWBCPCVXEBNDKUQOCED".toCharArray();
	
	
	public static Solution bestSolution = new Solution(new char[0], Double.NEGATIVE_INFINITY);
	
	public static int[] unknowns = new int[] {1, 7, 9, 10, 11, 18, 20, 21, 25, 27, 30, 44, 45, 47, 48, 49};
	
	public static void main(String[] args) throws Exception {
		Languages.english.loadNGramData();
		
		
		
		int[] startOrder = new int[] {39,35,47,4,5,42,17,52,20,13,53,16,30,40,38,34,43,14,41,7,27,44,1,6,33,15,54,36,18,24,3,9,51,37,23,0,0,0,0,0,0,0,0,25,0,0,0,0,0,0,32,0,0,0};
		for(int i = 0; i < startOrder.length; i++) 
			startOrder[i] = startOrder[i] - 1;
		System.out.println(ListUtil.toString(startOrder));
		System.out.println(new String(Solitaire.decode(FINAL_TEXT, startOrder)));
		
		//int[] endOrder = new int[] {47,31,-16,28,-17,46,3,4,41,16,51,19,12,23,13,6,-2,-3,1,33,38,52,14,35,17,37,-6,34,40,36,22,24,-10,9,42,29,7,-14,8,50,2,26,43,0,5,32,11,53,48,-9,21,15,39,25};
		//for(int i = 0; i < endOrder.length; i++) 
		//	endOrder[i] = endOrder[i] - 1;
		
		//for(int i = 0; i < 10; i++) {
		//	endOrder = Solitaire.previousCardOrder(endOrder);
		//	System.out.println(ListUtil.toCardString(endOrder, 0));
		//}
		
		
		Timer timer = new Timer();
		options(startOrder, ListUtil.toList(unknowns), 5, 0, new ArrayList<Integer>());
		timer.displayTime();
		System.out.println("Iterations: " + iteration);
		
	}
	
	public static int iteration = 0;
	
	public static void options(int[] lastOrder, ArrayList<Integer> unknowns, int times, int count, ArrayList<Integer> keyStream) throws Exception {
		if(times <= count) {
			iteration++;
			//System.out.println(keyStream + " " + new String(Solitaire.decode(FINAL_TEXT, keyStream)));
			char[] chars = Solitaire.decode(FINAL_TEXT, keyStream);
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
		int[] cardOrder = Arrays.copyOf(lastOrder, lastOrder.length);
		
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
			for(; jT > jB; jT--) {
				cardOrder[jT] = cardOrder[jT - 1];
				if (jA == jT - 1)
					jA = jT;
			}
		}
		cardOrder[jB] = JOKER_B;
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
				cardOrder = Arrays.copyOf(tmp, tmp.length);
			
			insideOrder(cardOrder, unknowns, times, count, keyStream);
		}

		//System.out.println(ListUtil.toString(cardOrder, 1));
	}
		
	public static void insideOrder(int[] cardOrder, ArrayList<Integer> unknowns, int times, int count, ArrayList<Integer> keyStream) throws Exception {
		//System.out.println(ListUtil.toString(cardOrder, 1));
		int possible;
		
		int firstCard = cardOrder[0];
		
		
		if(firstCard < 0) {
			for(int unknown : unknowns) {
				if(Solitaire.isJoker(unknown))
					possible = cardOrder[cardOrder.length - 1];
				else
					possible = cardOrder[unknown + 1];
			
				if(Solitaire.isJoker(possible)) {
	
					//int[] temp = Arrays.copyOf(cardOrder, cardOrder.length);
					cardOrder[0] = unknown;

					options(cardOrder, ListUtil.removeFromCopy(unknowns, unknown), times, count, keyStream);
					
				}
				else if(possible < 0) {
					for(int unknown2 : unknowns)  {
						if(unknown2 != unknown) {

							//int[] temp = Arrays.copyOf(cardOrder, cardOrder.length);
							cardOrder[0] = unknown;
							int last = cardOrder[unknown + 1];
							cardOrder[unknown + 1] = unknown2;
							ArrayList<Integer> newKeyStream = (ArrayList<Integer>)keyStream.clone();
							newKeyStream.add(unknown2);
							
							options(cardOrder, ListUtil.removeFromCopy(unknowns, unknown, unknown2), times, count + 1, newKeyStream);
							cardOrder[unknown + 1] = last;
						}
					}
				}
				else {
					//int[] temp = Arrays.copyOf(cardOrder, cardOrder.length);
					cardOrder[0] = unknown;
		
					ArrayList<Integer> newKeyStream = (ArrayList<Integer>)keyStream.clone();
					newKeyStream.add(possible);
					options(cardOrder, ListUtil.removeFromCopy(unknowns, unknown), times, count + 1, newKeyStream);
				}
			}
		}
		else {
			if(Solitaire.isJoker(firstCard))
				possible = cardOrder[cardOrder.length - 1];
			else
				possible = cardOrder[firstCard + 1];
	
			if(Solitaire.isJoker(possible)) {
				options(cardOrder, unknowns, times, count, keyStream);
		
			}
			else if(possible < 0) {
				for(int unknown2 : unknowns) {
					//int[] temp = Arrays.copyOf(cardOrder, cardOrder.length);
	
					int last = cardOrder[firstCard + 1];
					cardOrder[firstCard + 1] = unknown2;
				
					ArrayList<Integer> newKeyStream = (ArrayList<Integer>)keyStream.clone();
					newKeyStream.add(unknown2);
					
					options(cardOrder, ListUtil.removeFromCopy(unknowns, unknown2), times, count + 1, newKeyStream);
					cardOrder[firstCard + 1] = last;
	
				}
			}
			else {
				ArrayList<Integer> newKeyStream = (ArrayList<Integer>)keyStream.clone();
				newKeyStream.add(possible);
				
				options(cardOrder, (ArrayList<Integer>)unknowns.clone(), times, count + 1, newKeyStream);
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
	
	public static int[] nextCardOrder(int[] oldCardOrder) throws Exception {
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
			throw new Exception("JB NEGATIVE");
		}
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
		
		return cardOrder;
	}
}
