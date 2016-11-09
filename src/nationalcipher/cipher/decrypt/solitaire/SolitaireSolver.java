package nationalcipher.cipher.decrypt.solitaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javalibrary.Output;
import javalibrary.fitness.TextFitness;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import nationalcipher.SOLVER.SolitaireSolution;
import nationalcipher.cipher.base.other.Solitaire;
import nationalcipher.cipher.base.other.Solitaire.SolitaireAttack;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.IntegerOrderedKey;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.AMSCOKey;

public class SolitaireSolver {

	public static final int LARGEST_UNKNOWNS_ITERABLE = 11;
	
	//No known text all ready
	public static List<Solution> swiftAttack(String cipherText, int n, int offset, DeckParse deck, int noSol, Output out) {
		return swiftAttack(cipherText, new byte[0], n, offset, deck, noSol, out);
	}
	
	/**
	 * Returns the top noSol solutions for the first nth characters 
	 * 7 chars ~ 30 seconds of processing time
	 * However as n increases processing time increases n^2
	 */
	public static List<Solution> swiftAttack(String cipherText, byte[] prefix, int n, int offset, DeckParse deck, int noSol, Output out) {
		SoiltaireSwiftAttack attack = new SoiltaireSwiftAttack(cipherText.substring(offset + 0, offset + n), prefix, out);
		Solitaire.specialAttack(attack, deck.order, deck.unknownCards);
		Collections.sort(attack.solutions);
		
		attack.solutions = attack.solutions.subList(0, Math.min(noSol, attack.solutions.size()));
		
		out.println("Completed first round order");
		return attack.solutions;
	}
	
	public static void startCompleteAttack(String cipherText, int n, int solutionsCarryFoward, DeckParse startingDeck, Output out) {
		completeAttack(cipherText, new byte[0], n, solutionsCarryFoward, 0, startingDeck, out);
	}
	
	public static void completeAttack(String cipherText, byte[] prefix, int n, int solutionsCarryFoward, int offset, DeckParse startingDeck, Output out) {
		out.println("Starting unknowns: " + startingDeck.countUnknowns());
		List<Solution> solutions = SolitaireSolver.swiftAttack(cipherText, prefix, n, offset, startingDeck, solutionsCarryFoward, out);
		
		
		SolitaireSolution task = new SolitaireSolution(ArrayUtil.convertCharType(cipherText.substring(offset + n, cipherText.length()).toCharArray()), offset + n, out);
		out.println("Solutions: " + solutions.size());
		for(Solution solution : solutions) {
			if(solution.getText()[0] == 'A' && solution.getText()[1] == 'S' && solution.getText()[2] == 'T' && solution.getText()[3] == 'H' && solution.getText()[4] == 'E') {
				out.println("FOUND " + new String(solution.getText()));
			}
			DeckParse deck = new DeckParse(solution.keyString);
			task.incompleteOrder = deck.order;
			task.emptyIndex = deck.emptyIndex;
			out.println(deck.toString());
			
			if(deck.countUnknowns() > LARGEST_UNKNOWNS_ITERABLE) {
				completeAttack(cipherText, solution.getText(), n, solutionsCarryFoward, offset + n, deck, out);
			}
			else {
				for(int k = 0; k < n + offset; k++)
					task.text[k] = solution.getText()[k];
				
				KeyIterator.iterateIntegerOrderedKey(task, deck.unknownCards);
				
			}
		}
	}

	public static class SolitaireSolution implements IntegerOrderedKey {
		
		public byte[] text;
		public int startingLength;
		public int[] incompleteOrder;
		public int[] emptyIndex;
		public Solution lastSolution, bestSolution;
		public Output out;
		
		public SolitaireSolution(byte[] text, int startingLength, Output out) {
			this.bestSolution = new Solution();
			this.text = ArrayUtil.concat(new byte[startingLength], text);
			this.startingLength = startingLength;
			this.out = out;
		}
		
		@Override
		public void onIteration(int[] order) {
			for(int i = 0; i < this.emptyIndex.length; i++)
				this.incompleteOrder[this.emptyIndex[i]] = order[i];
			
			this.lastSolution = new Solution(Solitaire.decode(this.text, this.startingLength, this.incompleteOrder), Languages.english);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(ListUtil.toString(this.incompleteOrder, 1));
				this.out.println("%s", this.bestSolution);
			}
		}
	}
	
	private static class SoiltaireSwiftAttack implements SolitaireAttack {

    	public Solution bestSolution;
    	public List<Solution> solutions;
    	public byte[] intText;
    	public byte[] prefix;
    	public double minFitness;
    	public Output out;
    	
    	private SoiltaireSwiftAttack(String cipherText, byte[] prefix, Output out) {
    		this.bestSolution = new Solution();
    		this.solutions = new ArrayList<Solution>();
    		this.intText = new byte[cipherText.length() + prefix.length];
    		this.prefix = prefix;
    		int i = 0;
    		
    		for(; i < prefix.length; i++)
    			this.intText[i] = (byte)(prefix[i] - 'A');
    		
    		for(; i < this.intText.length; i++)
    			this.intText[i] = (byte)(cipherText.charAt(i - prefix.length) - 'A');
    		this.minFitness = TextFitness.getEstimatedFitness(this.intText.length, Languages.english) * 1.1D;
    		this.out = out;
    	}
   
		@Override
		public void tryKeyStream(int[] keyStream, int[] lastOrder) {
			
			byte[] chars = Solitaire.decodeWithKeyStream(this.intText, this.prefix.length, keyStream);

			Solution last = new Solution(chars, Languages.english);
			
			if(last.score > minFitness) {
				last.setKeyString(ListUtil.toCardString(lastOrder, 0));
				this.solutions.add(last);
			}
			
			if(this.bestSolution.score < last.score) {
				this.bestSolution = last;

				
				
				this.bestSolution.setKeyString(ListUtil.toCardString(lastOrder, 0));
				
				this.out.println("%s", this.bestSolution);
			}
			
		}

		@Override
		public int getSubBranches() {
			return this.intText.length - this.prefix.length;
		}
    }
}
