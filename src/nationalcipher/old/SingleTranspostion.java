package nationalcipher.old;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.math.MathUtil;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.base.transposition.Columnar;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.RedefenceKey;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;

public class SingleTranspostion implements IDecrypt {

	@Override
	public String getName() {
		return "Single Transpostion";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		SubstitutionTask task = new SubstitutionTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		int[] range = SettingParse.getIntegerRange(this.rangeBox);
		int minLength = range[0];
		int maxLength = range[1];
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
	
			
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				progress.addMaxValue(MathUtil.factorialBig(keyLength));
			
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				Creator.iterateRedefence(task, keyLength);
			
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	private JTextField rangeBox = new JTextField("2-8");
	private JComboBox<String> directionOption = new JComboBox<String>(new String[] {"Columns", "Rows"});
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
        JLabel range = new JLabel("Keyword length range: ");
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		JLabel direction = new JLabel("Read off");
		
		panel.add(new SubOptionPanel(range, this.rangeBox));
		panel.add(new SubOptionPanel(direction, this.directionOption));
		
		dialog.add(panel);
	}

	public class SubstitutionTask extends SimulatedAnnealing implements RedefenceKey  {

		public int[] bestKey1, bestMaximaKey1, lastKey1;
		public boolean readColumns;
		public int length;
		
		public SubstitutionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.readColumns = directionOption.getSelectedItem().equals("Columns");
			int[] range = SettingParse.getIntegerRange(rangeBox);
			this.length = range[0];
		}

		@Override
		public void onIteration(int[] order) {
			this.lastSolution = new Solution(this.readColumns ? Columnar.decode(this.cipherText, order) : ColumnarRow.decode(this.cipherText, order), this.settings.getLanguage()).setKeyString(Arrays.toString(order));
			
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
			this.bestMaximaKey1 = KeyGeneration.createOrder(this.length);
			return new Solution(this.readColumns ? Columnar.decode(this.cipherText, this.bestMaximaKey1) : ColumnarRow.decode(this.cipherText, this.bestMaximaKey1), this.settings.getLanguage()).setKeyString(Arrays.toString(this.bestMaximaKey1));
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey1 = KeySquareManipulation.modifyOrder(this.bestMaximaKey1);

			return new Solution(this.readColumns ? Columnar.decode(this.cipherText, this.lastKey1) : ColumnarRow.decode(this.cipherText, this.lastKey1), this.settings.getLanguage()).setKeyString(Arrays.toString(this.lastKey1));
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
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