package nationalcipher.old;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.base.substitution.TriSquare;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;


public class TriSquareDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Tri Square";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.CALCULATED, DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		SubstitutionTask task = new SubstitutionTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.CALCULATED) {
			
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
	
	public class SubstitutionTask extends SimulatedAnnealing  {

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
			return new Solution(TriSquare.decode(cipherText, this.bestMaximaKey1, this.bestMaximaKey2, this.bestMaximaKey3), this.settings.getLanguage());
		}

		@Override
		public Solution modifyKey(int count) {
			if(count % 3 == 0)
				this.lastKey1 = KeySquareManipulation.modifyKey(this.bestMaximaKey1);
			else if(count % 3 == 1)
				this.lastKey2 = KeySquareManipulation.modifyKey(this.bestMaximaKey2);
			else
				this.lastKey3 = KeySquareManipulation.modifyKey(this.bestMaximaKey3);
			return new Solution(TriSquare.decode(this.cipherText, this.lastKey1, this.lastKey2, this.lastKey3), this.settings.getLanguage());
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
			this.keyPanel.updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("Best Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, this.bestKey1 + " " + this.bestKey2 + " " + this.bestKey3, new String(this.bestSolution.getText()));
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
