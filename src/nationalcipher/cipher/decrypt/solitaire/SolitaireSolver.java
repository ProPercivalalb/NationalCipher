package nationalcipher.cipher.decrypt.solitaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javalibrary.Output;
import javalibrary.fitness.TextFitness;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.list.DynamicResultList;
import javalibrary.math.Units.Time;
import javalibrary.string.StringTransformer;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import nationalcipher.cipher.base.other.Solitaire;
import nationalcipher.cipher.base.other.Solitaire.SolitaireAttack;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.IntegerOrderedKey;
import nationalcipher.cipher.decrypt.methods.Solution;

public class SolitaireSolver {

	public static final int LARGEST_UNKNOWNS_ITERABLE = 7;
	
	//No known text all ready
	public static DynamicResultList<Solution> swiftAttack(String cipherText, int n, int offset, DeckParse deck, int noSol, Output out) {
		return swiftAttack(cipherText, new byte[0], n, offset, deck, noSol, out, 0);
	}
	
	/**
	 * Returns the top noSol solutions for the first nth characters 
	 * 7 chars ~ 30 seconds of processing time
	 * However as n increases processing time increases n^2
	 */
	public static DynamicResultList<Solution> swiftAttack(String cipherText, byte[] prefix, int n, int offset, DeckParse deck, int solutionsCarryFoward, Output out, int time) {
		SoiltaireSwiftAttack attack = new SoiltaireSwiftAttack(cipherText.substring(offset + 0, offset + n), prefix, solutionsCarryFoward, out, time);
		Timer timer = new Timer();
		Solitaire.specialAttack(attack, deck.order, deck.unknownCards);
		attack.solutions.sort();
		//out.println(attack.solutions.getList().toString());
		out.println(StringTransformer.repeat("   ", time) + " Completed %s round order in %fs", new String[]{"first", "second", "third", "fouth", "fifth"}[time], timer.getTimeRunning(Time.SECOND));
		return attack.solutions;
	}
	
	public static void startCompleteAttack(String cipherText, int n, int solutionsCarryFoward, DeckParse startingDeck, Output out, int time) {
		completeAttack(new SolitaireSolutionEver(), cipherText, new byte[0], n, solutionsCarryFoward, 0, startingDeck, out, time);
	}
	
	public static void completeAttack(SolitaireSolutionEver bestSolutionEver, String cipherText, byte[] prefix, int n, int solutionsCarryFoward, int offset, DeckParse startingDeck, Output out, int time) {
		out.println(StringTransformer.repeat("   ", time) + "Starting unknowns: " + startingDeck.countUnknowns());
		DynamicResultList<Solution> solutions = SolitaireSolver.swiftAttack(cipherText, prefix, n, offset, startingDeck, solutionsCarryFoward, out, time);
		
		
		SolitaireSolution task = new SolitaireSolution(bestSolutionEver, ArrayUtil.convertCharType(cipherText.substring(offset + n, cipherText.length()).toCharArray()), offset + n, out, time);
		//out.println("Solutions: " + solutions.size());
		for(int i = 0; i < solutions.size(); i++) {
			Solution solution = solutions.get(i);
			out.println(StringTransformer.repeat("   ", time) + "%d/%d %s", i + 1, solutions.size(), new String(solution.getText()));
			
			DeckParse deck = new DeckParse(solution.keyString);
			task.incompleteOrder = deck.order;
			task.emptyIndex = deck.emptyIndex;
			//out.println(deck.toString());
			
			if(deck.countUnknowns() > LARGEST_UNKNOWNS_ITERABLE) {
				completeAttack(bestSolutionEver, cipherText, solution.getText(), n, solutionsCarryFoward, offset + n, deck, out, time + 1);
			}
			else {
				for(int k = 0; k < n + offset; k++)
					task.text[k] = solution.getText()[k];
				
				KeyIterator.iterateIntegerOrderedKey(task, deck.unknownCards);
				//out.println("Completed %s round order", new String[]{"first", "second", "third", "fouth", "fifth"}[time + 1]);
			}
		}
	}

	public static class SolitaireSolutionEver {
		public Solution bestSolution = Solution.WORST_SOLUTION;
		@Override
		public String toString() {
			return this.bestSolution.toString();
		}
	}
	
	public static class SolitaireSolution implements IntegerOrderedKey {
		
		public SolitaireSolutionEver bestSolutionEver;
		public byte[] text;
		public int startingLength;
		public int[] incompleteOrder;
		public int[] emptyIndex;
		public Solution lastSolution;
		public Output out;
		public int time;
		
		public SolitaireSolution(SolitaireSolutionEver bestSolutionEver, byte[] text, int startingLength, Output out, int time) {
			this.bestSolutionEver = bestSolutionEver;
			this.text = ArrayUtil.concat(new byte[startingLength], text);
			this.startingLength = startingLength;
			this.out = out;
			this.time = time;
		}
		
		@Override
		public void onIteration(int[] order) {
			for(int i = 0; i < this.emptyIndex.length; i++)
				this.incompleteOrder[this.emptyIndex[i]] = order[i];
			
			this.lastSolution = new Solution(Solitaire.decode(this.text, this.startingLength, this.incompleteOrder), Languages.english.getTrigramData());
			
			if(this.lastSolution.score >= this.bestSolutionEver.bestSolution.score) {
				this.bestSolutionEver.bestSolution = this.lastSolution;
				this.bestSolutionEver.bestSolution.setKeyString(ListUtil.toString(this.incompleteOrder, 1));
				this.out.println(StringTransformer.repeat("   ", time) + "%s", this.bestSolutionEver);
			}
		}
	}
	
	private static class SoiltaireSwiftAttack implements SolitaireAttack {

    	public Solution bestSolution;
    	public DynamicResultList<Solution> solutions;
    	public byte[] intText;
    	public byte[] prefix;
    	public double minFitness;
    	public Output out;
    	public int time;
    	
    	private SoiltaireSwiftAttack(String cipherText, byte[] prefix, int solutionsCarryFoward, Output out, int time) {
    		this.bestSolution = Solution.WORST_SOLUTION;
    		this.solutions = new DynamicResultList<Solution>(solutionsCarryFoward);
    		this.intText = new byte[cipherText.length() + prefix.length];
    		this.prefix = prefix;
    		int i = 0;
    		for(; i < prefix.length; i++)
    			this.intText[i] = (byte)(prefix[i] - 'A');
    		
    		for(; i < this.intText.length; i++)
    			this.intText[i] = (byte)(cipherText.charAt(i - prefix.length) - 'A');
    		this.minFitness = TextFitness.getEstimatedFitness(this.intText.length, Languages.english.getTrigramData()) * 1.5D;
    		System.out.println("Min fitness: " + this.minFitness);
    		this.out = out;
    		this.time = time;
    	}
   
		@Override
		public void tryKeyStream(int[] keyStream, int[] lastOrder) {
			
			byte[] chars = Solitaire.decodeWithKeyStream(this.intText, this.prefix.length, keyStream);

			Solution last = new Solution(chars, Languages.english.getTrigramData());
			
			if(this.solutions.addResult(last))
				last.setKeyString(ListUtil.toCardString(lastOrder, 0));
			//Will mirror previous if statement
			if(this.bestSolution.score < last.score) {
				this.bestSolution = last;
				this.out.println(StringTransformer.repeat("   ", time) + "%s", this.bestSolution);
			}
			
		}

		@Override
		public int getSubBranches() {
			return this.intText.length - this.prefix.length;
		}
    }
}
