package nationalcipher.wip;

import java.util.Arrays;
import java.util.List;

import javalibrary.Output;
import javalibrary.language.Languages;
import javalibrary.streams.FileReader;
import javalibrary.thread.Threads;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.other.Solitaire;
import nationalcipher.cipher.decrypt.complete.SolitaireAttack;
import nationalcipher.cipher.decrypt.solitaire.DeckParse;
import nationalcipher.cipher.decrypt.solitaire.SolitaireSolver;
import nationalcipher.cipher.decrypt.solitaire.SolitaireSolver.SolitaireSolutionEver;
import nationalcipher.cipher.tools.KeyGeneration;

public class SolitaireTester {

	public static void main(String[] args) throws InterruptedException {
		Languages.english.loadNGramData();
		
		int found = 0;
		
		for(int time = 0; time < 500; time++) {
			String plainText = RandomUtil.pickRandomElement(FileReader.compileTextFromResource("/plainText.txt", true));
			String lookingFor = plainText.substring(0, 100);
			
			int[] order = KeyGeneration.createOrder(54);
			int[] halfOrder = ArrayUtil.copy(order);
		
			List<Integer> all = ListUtil.range(0, 53);
			while(all.size() > 54 - 13) {
				int index = RandomUtil.pickRandomElement(all);
				if(Solitaire.isJoker(index)) continue;
					
				all.remove((Object)index);
				halfOrder[ArrayUtil.indexOf(order, index)] = -1;
			}
			//System.out.println(Arrays.toString(order));
			//System.out.println(Arrays.toString(halfOrder));
			
			final String cipherText = new String(Solitaire.encode(plainText, order)).substring(0, 100);
			
			Runnable runnable = new Runnable() {
	
				@Override
				public void run() {
	
					SolitaireSolver.BEST_SOLUTION = new SolitaireSolutionEver();
					SolitaireSolver.startCompleteAttack(cipherText, 7, 4096, new DeckParse(halfOrder), new Output.Silent(), 0);
					System.out.println("Finished");
				}
				
			};
			Thread thread = new Thread(runnable);
			thread.start();
			
			while(thread.isAlive()) {
				Thread.sleep(1000);
				if(new String(SolitaireSolver.BEST_SOLUTION.bestSolution.getText()).equals(lookingFor)) {
					thread.stop();
					found++;
					break;
				}
			}
			
			System.out.println(String.format("%d/%d", found, time + 1));
		}
	}
}
