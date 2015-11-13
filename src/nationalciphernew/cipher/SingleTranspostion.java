package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javalibrary.Output;
import javalibrary.cipher.Columnar;
import javalibrary.cipher.ColumnarRow;
import javalibrary.cipher.wip.KeySquareManipulation;
import javalibrary.language.ILanguage;
import javalibrary.swing.ProgressValue;
import javalibrary.util.RandomUtil;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.SimulatedAnnealing;
import nationalciphernew.cipher.manage.Solution;


public class SingleTranspostion implements IDecrypt {

	@Override
	public String getName() {
		return "Single Transpostion";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		SubstitutionTask task = new SubstitutionTask(text.toCharArray(), settings, keyPanel, output, progress);
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run(text, settings);
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	

	public static class SubstitutionTask extends SimulatedAnnealing  {

		public int[] bestKey1, bestMaximaKey1, lastKey1;
		
		public SubstitutionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = new int[] {1, 4, 2, 0, 3, 5, 6, 7, 8};
			this.lastKey1 = this.bestMaximaKey1;
			return new Solution(ColumnarRow.decode(this.text, this.bestMaximaKey1), this.settings.getLanguage());
		}

		@Override
		public Solution modifyKey() {
			this.lastKey1 = KeySquareManipulation.exchangeOrder(this.bestMaximaKey1);
			return new Solution(ColumnarRow.decode(this.text, this.lastKey1), this.settings.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(Arrays.toString(this.bestKey1));
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.iterations.setText("" + this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("Best Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, Arrays.toString(this.bestKey1), new String(this.bestSolution.text));
			UINew.BEST_SOULTION = new String(this.bestSolution.text);
			this.progress.setValue(0);
			return false;
		}
	}
}
