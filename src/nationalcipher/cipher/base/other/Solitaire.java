package nationalcipher.cipher.base.other;

import java.util.Arrays;
import java.util.List;

import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Solitaire implements IRandEncrypter {
	
	public static final int JOKER_A = 52;
	public static final int JOKER_B = 53;
	
	public static final int TOTAL_CARDS = 54;
	
	public static void main(String[] args) {
		nextCardOrder(new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,53,52});
	}
	
	public static boolean isJoker(int card) {
		return card == JOKER_A || card == JOKER_B;
	}
	
	public static class Deck {
		public int jA, jB;
		public int[] cards;
		
		public Deck(int[] cards) {
			this.cards = cards;
			this.jA = ArrayUtil.indexOf(this.cards, 0, Solitaire.TOTAL_CARDS, JOKER_A);
			this.jB = ArrayUtil.indexOf(this.cards, 0, Solitaire.TOTAL_CARDS, JOKER_B);
		}
	}

	public static int[] nextCardOrder(int[] oldCardOrder) {
		int[] cardOrder = Arrays.copyOf(oldCardOrder, oldCardOrder.length);
		
		int jA, jB, jT;
		
		//TODO What happens when joker A wraps round
		jT = ArrayUtil.indexOf(cardOrder, 0, TOTAL_CARDS, JOKER_A);
		if(jT < 53) {
			jA = jT + 1;
			cardOrder[jT] = cardOrder[jA];
		}
		else {
			for(jA = 53; jA > 0; jA--)
				cardOrder[jA] = cardOrder[jA - 1];
			jA = 0;
		}
		cardOrder[jA] = JOKER_A;

		//Move Joker B 2 to right
		jB = ArrayUtil.indexOf(cardOrder, 0, TOTAL_CARDS, JOKER_B);
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
				
		int minJ, maxJ;
		
		if(jA < jB) {
			minJ = jA;
			maxJ = jB;
		}
		else {
			minJ = jB;
			maxJ = jA;
		}

		temp[53] = cardOrder[maxJ++];
	
		jT = 0;
		while(maxJ < 54)
			temp[jT++] = cardOrder[maxJ++];
		
		maxJ = minJ;
		while(cardOrder[maxJ] != temp[53])
			temp[jT++] = cardOrder[maxJ++];

		temp[jT++] = temp[53];
		
		maxJ = 0;
		while(maxJ < minJ)
			temp[jT++] = cardOrder[maxJ++];

		int finalCard = temp[53];
		if(!isJoker(finalCard)) {
			finalCard += 1;
		
			int c = 0;
			for(jT = finalCard; jT < 53; jT++)
				cardOrder[c++] = temp[jT];
			for(jT = 0; jT < finalCard + 1; jT++)
				cardOrder[c++] = temp[jT];
					
			cardOrder[53] = temp[53];
			
		}
		else
			cardOrder = temp;
		
		return cardOrder;
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
		return createCardOrder(key, ArrayUtil.createRange(54));
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
	
	
	
	public static interface SolitaireAttack {
		public void tryKeyStream(int[] keyStream, int[] lastOrder);
		
		public int getSubBranches();
	}
	
	/**
	 * Timings for n lengths of text
	 * 4 chars ~ 0.2s
	 * 5 chars ~ 1s
	 * 6 chars ~ 10s
	 * 7 chars ~ 30s
	 * 8 chars ~ 160s
	 * 
	 * @param attack
	 * @param deck
	 * @param unknowns
	 * @throws Exception
	 */
	public static void specialAttack(SolitaireAttack attack, int[] deck, int[] unknowns) {
		options(attack, deck, unknowns, attack.getSubBranches(), 0, new int[attack.getSubBranches()]);
	}
	
	public static void options(SolitaireAttack attack, int[] lastOrder, int[] unknowns, int times, int count, int[] keyStream) {
		if(times <= count) {
			attack.tryKeyStream(keyStream, lastOrder);
			return;
		}
		int[] cardOrder = Arrays.copyOf(lastOrder, Solitaire.TOTAL_CARDS);
		
		int jA, jB, jT;
		
		//Moves joker A (Black) 1 to right and handles wrap
		jT = ArrayUtil.indexOf(cardOrder, 0, Solitaire.TOTAL_CARDS, Solitaire.JOKER_A);
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


		//Moves joker B (Red) 2 to right  and handles wrap
		jB = ArrayUtil.indexOf(cardOrder, 0, Solitaire.TOTAL_CARDS, Solitaire.JOKER_B);
		if(jB < 52) {
			jT = jB + 1;
			cardOrder[jB] = cardOrder[jT];
			if(jA == jT)
				jA = jB;
			jB = jT + 1;
			cardOrder[jT] = cardOrder[jB];
			if(jA == jB)
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
		
		//TODO Create branches if jokers are unknown
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
		while(cardOrder[jB] != tmp[53])
			tmp[jT++] = cardOrder[jB++];
		tmp[jT++] = tmp[53];
		jB = 0;
		while(jB < jA)
			tmp[jT++] = cardOrder[jB++];
		

		jB = tmp[53];	//Examines last card for count-cut
		if(jB < 0) {	//Card is an unknown so create branches
			for(int uI1 = 0; uI1 < unknowns.length; uI1++) {
				int unknown = unknowns[uI1];
				if(unknown < 0) continue;

				if(!Solitaire.isJoker(unknown)) {
					
					//Count-cut with branched unknown card
					jA = 0;
					for(jT = unknown + 1; jT < 53; jT++)
						cardOrder[jA++] = tmp[jT];
					for(jT = 0; jT < unknown + 2; jT++)
						cardOrder[jA++] = tmp[jT];
					cardOrder[53] = unknown;
				}
				else
					cardOrder = tmp;
				
				unknowns[uI1] = -1;
				insideOrder(attack, cardOrder, unknowns, times, count, keyStream);
				unknowns[uI1] = unknown;
			}
		}
		else {
			if(!Solitaire.isJoker(jB)) {
				
				//Count-cut as normal
				jA = 0;
				for (jT = jB + 1; jT < 53; jT++)
					cardOrder[jA++] = tmp[jT];
				for (jT = 0; jT < jB + 2; jT++)
					cardOrder[jA++] = tmp[jT];
				cardOrder[53] = jB;
			}
			else
				cardOrder = tmp;
			
			insideOrder(attack, cardOrder, unknowns, times, count, keyStream);
		}
	}
		
	public static void insideOrder(SolitaireAttack attack, int[] cardOrder, int[] unknowns, int times, int count, int[] keyStream) {
		int possible;
		
		//Examines first card to test
		int firstCard = cardOrder[0];
		
		if(firstCard < 0) {
			for(int uI1 = 0; uI1 < unknowns.length; uI1++) {
				int unknown = unknowns[uI1];
				if(unknown < 0) continue;
				
				
				if(unknown == Solitaire.JOKER_B)
					unknown = Solitaire.JOKER_A;
		
				int possibleIndex = unknown + 1;
				possible = cardOrder[possibleIndex];
				
				unknowns[uI1] = -1;
				if(Solitaire.isJoker(possible)) {
					cardOrder[0] = unknown;

					options(attack, cardOrder, unknowns, times, count, keyStream);

				}
				else if(possible < 0) {
					for(int uI2 = 0; uI2 < unknowns.length; uI2++) {
						int streamUnknown = unknowns[uI2];
						if(streamUnknown < 0) continue;
						if(streamUnknown != unknown) {
							cardOrder[0] = unknown;
							int last = cardOrder[possibleIndex];
							cardOrder[possibleIndex] = streamUnknown;

							keyStream[count] = streamUnknown;
							
							unknowns[uI2] = -1;
							options(attack, cardOrder, unknowns, times, count + 1, keyStream);
							cardOrder[possibleIndex] = last;
							unknowns[uI2] = streamUnknown;
						}
					}
				}
				else {
					cardOrder[0] = unknown;
					keyStream[count] = possible;
					options(attack, cardOrder, unknowns, times, count + 1, keyStream);
				}
				unknowns[uI1] = unknown;
			}
		}
		else {
			if(firstCard == Solitaire.JOKER_B)
				firstCard = Solitaire.JOKER_A;
			
			int possibleIndex = firstCard + 1;
			possible = cardOrder[possibleIndex];
	
			if(Solitaire.isJoker(possible))
				options(attack, cardOrder, unknowns, times, count, keyStream);
			else if(possible < 0) {
				for(int uI2 = 0; uI2 < unknowns.length; uI2++) {
					int streamUnknown = unknowns[uI2];
					if(streamUnknown < 0) continue;
	
					int last = cardOrder[possibleIndex];
					cardOrder[possibleIndex] = streamUnknown;
				
					keyStream[count] = streamUnknown;
					
					unknowns[uI2] = -1;
					options(attack, cardOrder, unknowns, times, count + 1, keyStream);
					cardOrder[possibleIndex] = last;
					unknowns[uI2] = streamUnknown;
				}
			}
			else {
				keyStream[count] = possible;
				options(attack, cardOrder, unknowns, times, count + 1, keyStream);
			}
		}
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

			
			plainText[index] = (char)((51 + (cipherText[index] - 'A') - keyStreamNumber) % 26 + 'A');
			index += 1;
		}

		return plainText;
	}
	
	public static byte[] decodeWithKeyStream(byte[] cipherText, int[] keyStream) {
		return decodeWithKeyStream(cipherText, 0, keyStream);
	}
	
	public static byte[] decodeWithKeyStream(byte[] cipherText, int startingIndex, int[] keyStream) {
		byte[] plainText = new byte[cipherText.length];
		int index = startingIndex;
		

		
		for(int i = 0; i < index; i++)
			plainText[i] = (byte)(cipherText[i] + 'A');
		
		for(int keyStreamNumber : keyStream) {

			
			plainText[index] = (byte)((51 + cipherText[index] - keyStreamNumber) % 26 + 'A');
			index += 1;
		}

		return plainText;
	}
	
	public static byte[] decode(char[] cipherText, int[] cardOrder) {
		return decode(cipherText, 0, cardOrder);
	}
	
	public static byte[] decode(char[] cipherText, int startingIndex, int[] cardOrder) {
		byte[] plainText = new byte[cipherText.length];
		int index = startingIndex;
		
		for(int i = 0; i < index; i++)
			plainText[i] = (byte)cipherText[i];
		
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
			
			plainText[index] = (byte)((52 + (cipherText[index] - 'A') - (keyStreamNumber + 1)) % 26 + 'A');
			index += 1;
		}

		return plainText;
	}
	
	//Byte version of above
	public static byte[] decode(byte[] cipherText, int startingIndex, int[] cardOrder) {
		byte[] plainText = new byte[cipherText.length];
		int index = startingIndex;
		
		for(int i = 0; i < index; i++)
			plainText[i] = cipherText[i];
		
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
			
			plainText[index] = (byte)((52 + (cipherText[index] - 'A') - (keyStreamNumber + 1)) % 26 + 'A');
			index += 1;
		}

		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		int[] order = KeyGeneration.createOrder(54);
		int[] halfOrder = ArrayUtil.copy(order);
	
		List<Integer> all = ListUtil.range(0, 53);
		while(all.size() > 54 - 16) {
			int index = RandomUtil.pickRandomElement(all);
			if(Solitaire.isJoker(index)) continue;
				
			all.remove((Object)index);
			halfOrder[ArrayUtil.indexOf(order, index)] = -1;
		}
		System.out.println(Arrays.toString(order));
		System.out.println(Arrays.toString(halfOrder));
		return encode(plainText, order);
	}
}