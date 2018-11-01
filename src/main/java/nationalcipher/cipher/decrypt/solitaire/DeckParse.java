package nationalcipher.cipher.decrypt.solitaire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javalibrary.util.ListUtil;

public class DeckParse {

	public int[] order;
	public int[] emptyIndex;
	public int[] unknownCards;
	public boolean complete;
	
	public DeckParse(int[] oldOrder) {
		this.order = new int[54];
		Arrays.fill(this.order, -1);
		
		List<Integer> emptyIndex = new ArrayList<Integer>();
		
		for(int i = 0; i < oldOrder.length; i++) {
			if(oldOrder[i] < 0)
				emptyIndex.add(i);
			else
				this.order[i] = oldOrder[i];
			
		}
		
		List<Integer> all = ListUtil.range(0, 53);
		ListUtil.removeAll(all, this.order);
	
		this.emptyIndex = ListUtil.toArray(emptyIndex);
		this.unknownCards = ListUtil.toArray(all);
		this.complete = this.unknownCards.length == 0;
	}
	
	public DeckParse(String parse) {
		this.order = new int[54];
		Arrays.fill(this.order, -1);

		parse =	parse.substring(parse.startsWith("[") ? 1 : 0, parse.length() - (parse.endsWith("]") ? 1 : 0)).replaceAll(" ", "");
		
		String[] cards = parse.split(",");
		List<Integer> emptyIndex = new ArrayList<Integer>();
		
		
		for(int i = 0; i < cards.length; i++) {
			String card = cards[i];
			if(card.equals("*") || card.equalsIgnoreCase("x") || card.equals("-1")) {
				emptyIndex.add(i);
				continue;
			}
			
			
			
			try {
				int no = Integer.valueOf(card);
				this.order[i] = no;
				continue;
			}
			catch(NumberFormatException e) {}

			if(card.length() == 2) {
				List<Character> starter = Arrays.asList('A','2','3','4','5','6','7','8','9','T','J','Q','K');
				List<Character> end = Arrays.asList(/**'♣', '♦', '♥', '♠',**/ 'c', 'd', 'h', 's');

				if(starter.contains(card.charAt(0)) && end.contains(card.charAt(1)))
					this.order[i] = starter.indexOf(card.charAt(0)) + end.indexOf(card.charAt(1)) % 4 * 13;
			}
			else if(card.length() == 1) {
				if(card.equals("A"))
					this.order[i] = 52;
				else if(card.equals("B"))
					this.order[i] = 53;
			}
		}
		
		
		List<Integer> all = ListUtil.range(0, 53);
		ListUtil.removeAll(all, this.order);
	
		this.emptyIndex = ListUtil.toArray(emptyIndex);
		this.unknownCards = ListUtil.toArray(all);
		this.complete = this.unknownCards.length == 0;
	}
	
	/**
	 * Counts the number of negative numbers in the deck which correspond to an unknown
	 * @param deck The deck in question
	 * @return The number of negative cards in the deck
	 */
	public static int countUnknowns(int[] deck) {
		int count = 0;
		
		for(int i = 0; i < deck.length; i++)
			if(deck[i] < 0)
				count++;
		
		return count;
	}
	
	public int[] getOtherUnknowns(int staticUnknown) {
		List<Integer> temp = ListUtil.toList(this.unknownCards);
		temp.remove((Integer)staticUnknown);
		return ListUtil.toArray(temp);
	}
	
	public int countUnknowns() {
		return this.unknownCards.length;
	}
	
	public boolean isDeckComplete() {
		return this.complete;
	}

	public int[] copyKnowns() {
		return Arrays.copyOf(this.order, this.order.length);
	}
	
	@Override
	public String toString() {
		return "Unknowns: " + this.countUnknowns();
	}
}
