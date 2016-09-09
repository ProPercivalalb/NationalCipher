package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.ADFGX;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator.BifidKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SimulatedAnnealing;
import nationalcipher.cipher.tools.SubOptionPanel;

public class ADFGXDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "ADFGX";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		ADFGXTask task = new ADFGXTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	private JTextField alphaBox = new JTextField("ADFGX");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		JLabel range = new JLabel("Alphabet:");
		panel.add(new SubOptionPanel(range, this.alphaBox));
	}
	
	public class ADFGXTask extends SimulatedAnnealing implements BifidKey {

		public String alpha;
		public String bestKey = "", bestMaximaKey = "", lastKey = "";
		
		public ADFGXTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.alpha = alphaBox.getText();
		}

		@Override
		public void onIteration(String keysquare) {
			this.lastSolution = new Solution(ADFGX.decode(this.text, keysquare, new int[] {0, 1}), this.settings.getLanguage()).setKeyString(keysquare);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(ADFGX.decode(this.text, this.bestMaximaKey, new int[] {4, 1, 3, 0, 2, 3}), this.settings.getLanguage()).setKeyString(this.bestMaximaKey);
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(ADFGX.decode(this.text, this.lastKey, new int[] {4, 1, 3, 0, 2, 3}), this.settings.getLanguage()).setKeyString(this.lastKey);
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestKey);
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
