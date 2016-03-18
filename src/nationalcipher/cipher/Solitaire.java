package nationalcipher.cipher;

import java.util.Arrays;

import javalibrary.math.ArrayUtil;
import javalibrary.util.ListUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
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
			this.jA = ArrayUtil.indexOf(this.cards, Solitaire.TOTAL_CARDS, JOKER_A);
			this.jB = ArrayUtil.indexOf(this.cards, Solitaire.TOTAL_CARDS, JOKER_B);
		}
	}

	public static int[] nextCardOrder(int[] oldCardOrder) {
		int[] cardOrder = Arrays.copyOf(oldCardOrder, oldCardOrder.length);
		
		int jA, jB, jT;
		
		//TODO What happens when joker A wraps round
		jT = ArrayUtil.indexOf(cardOrder, TOTAL_CARDS, JOKER_A);
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
		jB = ArrayUtil.indexOf(cardOrder, TOTAL_CARDS, JOKER_B);
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
		return createCardOrder(key, ArrayUtil.range(0, 54));
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
	
	
	
	public static interface SoiltaireAttack {
		public void tryKeyStream(int[] keyStream, int[] lastOrder);
	}
	
	public static void options(SoiltaireAttack attack, int[] lastOrder, int[] unknowns, int times, int count, int[] keyStream) throws Exception {
		if(times <= count) {
			attack.tryKeyStream(keyStream, lastOrder);
			return;
		}
		int[] cardOrder = Arrays.copyOf(lastOrder, Solitaire.TOTAL_CARDS);
		
		int jA, jB, jT;
		
		//TODO What happens when joker A wraps round
		jT = ArrayUtil.indexOf(cardOrder, Solitaire.TOTAL_CARDS, Solitaire.JOKER_A);
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
		jB = ArrayUtil.indexOf(cardOrder, Solitaire.TOTAL_CARDS, Solitaire.JOKER_B);
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
	
				insideOrder(attack, cardOrder, ListUtil.removeFromCopy(unknowns, unknown), times, count, keyStream);
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
			
			insideOrder(attack, cardOrder, unknowns, times, count, keyStream);
		}
	}
		
	public static void insideOrder(SoiltaireAttack attack, int[] cardOrder, int[] unknowns, int times, int count, int[] keyStream) throws Exception {
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
					options(attack, cardOrder, ListUtil.removeFromCopy(unknowns, unknown), times, count, keyStream);
				}
				else if(possible < 0) {
					for(int unknown2 : unknowns)  {
						if(unknown2 != unknown) {
							cardOrder[0] = unknown;
							int last = cardOrder[possibleIndex];
							cardOrder[possibleIndex] = unknown2;

							keyStream[count] = unknown2;
							
							options(attack, cardOrder, ListUtil.removeFromCopy(unknowns, unknown, unknown2), times, count + 1, keyStream);
							cardOrder[possibleIndex] = last;
						}
					}
				}
				else {
					cardOrder[0] = unknown;
					keyStream[count] = possible;
					options(attack, cardOrder, ListUtil.removeFromCopy(unknowns, unknown), times, count + 1, keyStream);
				}
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
				for(int unknown2 : unknowns) {
	
					int last = cardOrder[possibleIndex];
					cardOrder[possibleIndex] = unknown2;
				
					keyStream[count] = unknown2;
					
					options(attack, cardOrder, ListUtil.removeFromCopy(unknowns, unknown2), times, count + 1, keyStream);
					cardOrder[possibleIndex] = last;
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
	
	public static char[] decodeWithKeyStream(int[] cipherText, int[] keyStream) {
		char[] plainText = new char[keyStream.length];
		int index = 0;
		
		for(int keyStreamNumber : keyStream) {

			
			plainText[index] = (char)((51 + cipherText[index] - keyStreamNumber) % 26 + 'A');
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

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createOrder(54));
	}
}