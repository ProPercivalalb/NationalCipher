package nationalcipher.old;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.base.transposition.Phillips;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;

public class PhillipsDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Phillips";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		PhillipsTask task = new PhillipsTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	private JComboBox<Boolean> booleanOption1 = new JComboBox<Boolean>(new Boolean[] {true, false});
	private JComboBox<Boolean> booleanOption2 = new JComboBox<Boolean>(new Boolean[] {true, false});
	//private JComboBox<String> directionOption = new JComboBox<String>(new String[] {"Columns", "Rows"});
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {

		panel.add(new SubOptionPanel("Move rows? ", this.booleanOption1));
		panel.add(new SubOptionPanel("Move columns? ", this.booleanOption2));
		
		dialog.add(panel);
	}
	
	public class PhillipsTask extends SimulatedAnnealing {

		public boolean orderRows;
		public boolean orderColumns;
		public String bestKey = "", bestMaximaKey = "", lastKey = "";
		
		public PhillipsTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.orderRows = (Boolean)booleanOption1.getSelectedItem();
			this.orderColumns = (Boolean)booleanOption2.getSelectedItem();
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(Phillips.decode(this.cipherText, this.bestMaximaKey, this.orderRows, this.orderColumns), this.settings.getLanguage()).setKeyString(this.bestMaximaKey);
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(Phillips.decode(this.cipherText, this.lastKey, this.orderRows, this.orderColumns), this.settings.getLanguage()).setKeyString(this.lastKey);
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
