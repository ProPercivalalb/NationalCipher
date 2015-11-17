package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;

import javalibrary.Output;
import javalibrary.cipher.TriSquare;
import javalibrary.cipher.wip.KeySquareManipulation;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.SimulatedAnnealing;
import nationalciphernew.cipher.manage.Solution;


public class TriSquareDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Tri Square";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.CALCULATED);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		SubstitutionTask task = new SubstitutionTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.CALCULATED) {
			
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog) {
		
	}
	
	public static class SubstitutionTask extends SimulatedAnnealing  {

		public String bestKey1, bestMaximaKey1, lastKey1;
		public String bestKey2, bestMaximaKey2, lastKey2;
		public String bestKey3, bestMaximaKey3, lastKey3;
		
		public SubstitutionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeySquareManipulation.generateRandKeySquare();
			this.bestMaximaKey2 = KeySquareManipulation.generateRandKeySquare();
			this.bestMaximaKey3 = KeySquareManipulation.generateRandKeySquare();
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			this.lastKey3 = this.bestMaximaKey3;
			return new Solution(TriSquare.decode(text, this.bestMaximaKey1, this.bestMaximaKey2, this.bestMaximaKey3), this.settings.getLanguage());
		}

		@Override
		public Solution modifyKey(int count) {
			if(count % 3 == 0)
				this.lastKey1 = KeySquareManipulation.exchange2letters(this.bestMaximaKey1);
			else if(count % 3 == 1)
				this.lastKey2 = KeySquareManipulation.exchange2letters(this.bestMaximaKey2);
			else
				this.lastKey3 = KeySquareManipulation.exchange2letters(this.bestMaximaKey3);
			return new Solution(TriSquare.decode(this.text, this.lastKey1, this.lastKey2, this.lastKey3), this.settings.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
			this.bestMaximaKey2 = this.lastKey2;
			this.bestMaximaKey3 = this.lastKey3;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.bestKey2 = this.bestMaximaKey2;
			this.bestKey3 = this.bestMaximaKey3;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestKey1 + " " + this.bestKey2 + " " + this.bestKey3);
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.iterations.setText("" + this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("Best Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, this.bestKey1 + " " + this.bestKey2 + " " + this.bestKey3, new String(this.bestSolution.text));
			UINew.BEST_SOULTION = new String(this.bestSolution.text);
			this.progress.setValue(0);
			return false;
		}
	}
}
