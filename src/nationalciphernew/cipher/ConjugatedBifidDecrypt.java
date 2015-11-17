package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;

import javalibrary.Output;
import javalibrary.cipher.ConjugatedBifid;
import javalibrary.cipher.TwoSquare;
import javalibrary.cipher.wip.KeySquareManipulation;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.SimulatedAnnealing;
import nationalciphernew.cipher.manage.Solution;


public class ConjugatedBifidDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Conjugated Bifid";
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
			
			task.run();
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog) {
		
	}

	public static class SubstitutionTask extends SimulatedAnnealing  {

		public int period = 7;
		public String bestKey1, bestMaximaKey1, lastKey1;
		public String bestKey2, bestMaximaKey2, lastKey2;
		
		public SubstitutionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeySquareManipulation.generateRandKeySquare();
			this.bestMaximaKey2 = KeySquareManipulation.generateRandKeySquare();
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			return new Solution(ConjugatedBifid.decode(text, this.bestMaximaKey1, this.bestMaximaKey2, this.period), this.settings.getLanguage()).setKeyString(this.lastKey1 + " " + this.lastKey2);
		}

		@Override
		public Solution modifyKey(int count) {
			if(count % 2 == 0)
				this.lastKey1 = KeySquareManipulation.modifyKey(this.bestMaximaKey1);
			else
				this.lastKey2 = KeySquareManipulation.modifyKey(this.bestMaximaKey2);
			
			return new Solution(ConjugatedBifid.decode(this.text, this.lastKey1, this.lastKey2, this.period), this.settings.getLanguage()).setKeyString(this.lastKey1 + " " + this.lastKey2);
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
			this.bestMaximaKey2 = this.lastKey2;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.bestKey2 = this.bestMaximaKey2;
			this.keyPanel.updateSolution(this.bestSolution);
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.iterations.setText("" + this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("%s", this.bestSolution);
			UINew.BEST_SOULTION = new String(this.bestSolution.text);
			this.progress.setValue(0);
			return false;
		}
	}
}
