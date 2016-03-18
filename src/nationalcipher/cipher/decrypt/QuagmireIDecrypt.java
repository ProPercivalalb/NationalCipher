package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.UINew;
import nationalcipher.cipher.QuagmireI;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SimulatedAnnealing;
import nationalcipher.cipher.tools.SubOptionPanel;


public class QuagmireIDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Quagmire I";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		QuagmireITask task = new QuagmireITask(text.toCharArray(), settings, keyPanel, output, progress);
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}

	private JTextField rangeBox = new JTextField("5");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		
		panel.add(new SubOptionPanel("Period:", this.rangeBox));
        
		dialog.add(panel);
	}
	
	public class QuagmireITask extends SimulatedAnnealing  {

		public int period;
		public String bestKey1, bestMaximaKey1, lastKey1;
		public String bestKey2, bestMaximaKey2, lastKey2;
		
		public QuagmireITask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.period = SettingParse.getInteger(rangeBox);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeySquareManipulation.generateRandKey();
			this.bestMaximaKey2 = KeyGeneration.createShortKey26(this.period);
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			return new Solution(QuagmireI.decode(text, this.bestMaximaKey1, this.bestMaximaKey2), this.settings.getLanguage()).setKeyString("%s %s", this.lastKey1, this.lastKey2);
		}

		@Override
		public Solution modifyKey(int count) {
			if(count % 2 == 0)
				this.lastKey1 = KeySquareManipulation.modifyKey(this.bestMaximaKey1);
			else
				this.lastKey2 = KeySquareManipulation.swapCharIndex(this.bestMaximaKey2);
			
			return new Solution(QuagmireI.decode(this.text, this.lastKey1, this.lastKey2), this.settings.getLanguage()).setKeyString("%s %s", this.lastKey1, this.lastKey2);
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
			this.keyPanel.iterations.setText("" + this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.text;
			this.progress.setValue(0);
			return false;
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}
