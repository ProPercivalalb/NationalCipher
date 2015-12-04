package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.string.StringTransformer;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.UINew;
import nationalcipher.cipher.Caesar;
import nationalcipher.cipher.RunningKey;
import nationalcipher.cipher.TwoSquare;
import nationalcipher.cipher.Vigenere;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.KeySearch;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SimulatedAnnealing;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.cipher.tools.Creator.VigereneKey;

public class RunningKeyDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Vigenere RunningKey";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.KEY_MANIPULATION);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		char[] textChar = text.toCharArray();
		VigenereTask task = new VigenereTask(textChar, settings, keyPanel, output, progress);
		
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {

			progress.setIndeterminate(true);
			//task.run(text.length() / 2, text.length() / 2);
			
			output.println(task.getBestSolution());
			
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public class VigenereTask extends SimulatedAnnealing implements VigereneKey {

		public String bestKey1, bestMaximaKey1, lastKey1;
		
		public VigenereTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}
			
		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(RunningKey.decode(this.text, key), this.settings.getLanguage()).setKeyString(key);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
			
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = StringTransformer.repeat("A", this.text.length / 2);
			this.lastKey1 = this.bestMaximaKey1;
			return new Solution(RunningKey.decode(this.text, this.bestMaximaKey1), this.settings.getLanguage()).setKeyString(this.lastKey1);
		}

		@Override
		public Solution modifyKey(int count) {

			this.lastKey1 = KeySquareManipulation.swapCharIndex(this.bestMaximaKey1);
	
			
			return new Solution(RunningKey.decode(this.text, this.lastKey1), this.settings.getLanguage()).setKeyString(this.lastKey1);
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;

		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestKey1);
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.iterations.setText("" + this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.text;
			this.progress.setValue(0);
			return false;
		}
		
		/**
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(RunningKey.decode(this.text, key), this.settings.getLanguage()).setKeyString(key);
		}

		@Override
		public void solutionFound() {
			this.output.println("%s", this.bestSolution);
			this.keyPanel.updateSolution(this.bestSolution);
		}

		@Override
		public void onIteration() {
			this.keyPanel.iterations.setText("" + this.iteration++);
		}**/
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}