package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javalibrary.string.StringAnalyzer;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.other.Pollux;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.CharacterKey;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.NationalCipherUI;

public class PolluxAttack extends CipherAttack {

	public PolluxAttack() {
		super("Pollux");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED, DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		PolluxTask task = new PolluxTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			app.getProgress().addMaxValue(59049);
			KeyIterator.iteratePollux(task);
		}
		else if(method == DecryptionMethod.CALCULATED) {
			app.out().println("WORK IN PROGROSS");
			Map<String, Integer> defNot = StringAnalyzer.getEmbeddedStrings(text.toCharArray(), 3, 3);
			List<Integer> couldBe = new ArrayList<Integer>();
			for(String s : defNot.keySet()) {
				int a = s.charAt(0) - '0';
				int b = s.charAt(1) - '0';
				int c = s.charAt(2) - '0';
				couldBe.remove((Integer)a);
				couldBe.remove((Integer)b);
				couldBe.remove((Integer)c);
			}
			
			app.out().println("" + couldBe);
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class PolluxTask extends SimulatedAnnealing implements CharacterKey {

		public char[] bestKey, bestMaximaKey, lastKey;
		
		public PolluxTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(char[] key) {
			this.lastSolution = new Solution(Pollux.decode(this.cipherText, key), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(Arrays.toString(key));
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeyGeneration.createPolluxKey();
			return new Solution(Pollux.decode(this.cipherText, this.bestMaximaKey), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeyManipulation.swapMorseIndex(ArrayUtil.copy(this.bestMaximaKey));
			return new Solution(Pollux.decode(this.cipherText, this.lastKey), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString(Arrays.toString(this.bestKey));
			this.getKeyPanel().updateSolution(this.bestSolution);
		}
		
		@Override
		public void onIteration() {
			this.getProgress().increase();
			this.getKeyPanel().updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.out().println("%s", this.bestSolution);
			NationalCipherUI.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
	}
}
