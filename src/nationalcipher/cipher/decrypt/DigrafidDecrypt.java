package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.base.other.Digrafid;
import nationalcipher.cipher.decrypt.complete.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;


public class DigrafidDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Digrafid";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		DigrafidTask task = new DigrafidTask(text.toCharArray(), settings, keyPanel, output, progress);
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
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
	
	public class DigrafidTask extends SimulatedAnnealing  {

		public int fractionation = 3;
		public String bestKey1, bestMaximaKey1, lastKey1;
		public String bestKey2, bestMaximaKey2, lastKey2;
		
		public DigrafidTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeySquareManipulation.generateRandTrifidKey('#');
			this.bestMaximaKey2 = KeySquareManipulation.generateRandTrifidKey('#');
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			return new Solution(Digrafid.decode(cipherText, this.bestMaximaKey1, this.bestMaximaKey2, this.fractionation), this.settings.getLanguage()).setKeyString(this.bestKey1 + " " + this.bestKey2);
		}

		@Override
		public Solution modifyKey(int count) {
			if(count % 2 == 0)
				this.lastKey1 = KeySquareManipulation.exchange2letters(this.bestMaximaKey1);
			else
				this.lastKey2 = KeySquareManipulation.exchange2letters(this.bestMaximaKey2);
			
			return new Solution(Digrafid.decode(this.cipherText, this.lastKey1, this.lastKey2, this.fractionation), this.settings.getLanguage()).setKeyString(this.lastKey1 + " " + this.lastKey2);
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
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestKey1 + " " + this.bestKey2);
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
