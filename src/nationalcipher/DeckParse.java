package nationalcipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javalibrary.util.ListUtil;

public class DeckParse {

	public int[] order;
	public int[] emptyIndex;
	public int[] unknownCards;
	public boolean complete;
	
	public DeckParse(String parse) {
		this.order = new int[54];
		Arrays.fill(this.order, -1);
		
		String[] cards = parse.split(",");
		List<Integer> emptyIndex = new ArrayList<Integer>();
		
		
		for(int i = 0; i < cards.length; i++) {
			String card = cards[i];
			if(card.equals("*") || card.equalsIgnoreCase("x")) {
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
				List<Character> end = Arrays.asList('♣', '♦', '♥', '♠', 'c', 'd', 'h', 's');

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
}
