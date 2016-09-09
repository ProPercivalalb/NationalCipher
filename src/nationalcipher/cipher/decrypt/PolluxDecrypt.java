package nationalcipher.cipher.decrypt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.string.StringAnalyzer;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.Pollux;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.PolluxKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SimulatedAnnealing;

public class PolluxDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Pollux";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED, DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		PolluxTask task = new PolluxTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			progress.addMaxValue(59049);
			
			Creator.iteratePollux(task);
		}
		else if(method == DecryptionMethod.CALCULATED) {
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
			
			output.println("" + couldBe);
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public class PolluxTask extends SimulatedAnnealing implements PolluxKey {

		public char[] bestKey, bestMaximaKey, lastKey;
		
		public PolluxTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}
		
		@Override
		public void onIteration(char[] key) {
			this.lastSolution = new Solution(Pollux.decode(this.text, key), this.settings.getLanguage()).setKeyString(new String(key));
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.keyPanel.updateSolution(this.bestSolution);
				this.output.println("%s", this.bestSolution);	
			}
			
			this.progress.increase();
			this.keyPanel.updateIteration(this.iteration++);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = new char[] {'X', '.', '.', '-', '.', '.', '-', '-', 'X', 'X'};
			return new Solution(Pollux.decode(this.text, this.bestMaximaKey), this.settings.getLanguage()).setKeyString(new String(this.bestMaximaKey));
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.swapMorseIndex(this.bestMaximaKey);
			return new Solution(Pollux.decode(this.text, this.lastKey), this.settings.getLanguage()).setKeyString(new String(this.lastKey));
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestSolution.keyString);
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.progress.setValue(0);
			return false;
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}
