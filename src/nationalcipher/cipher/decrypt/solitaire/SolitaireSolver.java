package nationalcipher.cipher.decrypt.solitaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javalibrary.language.Languages;
import javalibrary.util.ListUtil;
import nationalcipher.cipher.base.other.Solitaire;
import nationalcipher.cipher.base.other.Solitaire.SolitaireAttack;
import nationalcipher.cipher.decrypt.methods.Solution;

public class SolitaireSolver {

	/**
	 * Returns the top noSol solutions for the first nth characters 
	 * 7 chars ~ 30 seconds of processing time
	 * However as n increases processing time increases n^2
	 */
	public static List<Solution> swiftAttack(String cipherText, char[] prefix, int n, int offset, DeckParse deck, int noSol) {
		SoiltaireSwiftAttack attack = new SoiltaireSwiftAttack(cipherText.substring(offset + 0, offset + n), prefix);
		Solitaire.specialAttack(attack, deck.order, deck.unknownCards);
		Collections.sort(attack.solutions);
		
		attack.solutions = attack.solutions.subList(0, Math.min(noSol, attack.solutions.size()));
		
		System.out.println("Completed first round order");
		return attack.solutions;
	}
	
	private static class SoiltaireSwiftAttack implements SolitaireAttack {

    	public Solution bestSolution;
    	public List<Solution> solutions;
    	public int[] intText;
    	public char[] prefix;
    	public double minFitness;
    	
    	private SoiltaireSwiftAttack(String cipherText, char[] prefix) {
    		this.bestSolution = new Solution();
    		this.solutions = new ArrayList<Solution>();
    		this.intText = new int[cipherText.length() + prefix.length];
    		this.prefix = prefix;
    		int i = 0;
    		
    		for(; i < prefix.length; i++)
    			this.intText[i] = prefix[i] - 'A';
    		
    		for(; i < this.intText.length; i++)
    			this.intText[i] = cipherText.charAt(i - prefix.length) - 'A';
    		this.minFitness = this.intText.length * (Languages.english.getQuadgramData().fitnessPerChar /2);
    	}
   
		@Override
		public void tryKeyStream(int[] keyStream, int[] lastOrder) {
			
			char[] chars = Solitaire.decodeWithKeyStream(this.intText, this.prefix.length, keyStream);

			Solution last = new Solution(chars, Languages.english);
			
			if(last.score > minFitness) {
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
			return this.intText.length - this.prefix.length;
		}
    }
}
