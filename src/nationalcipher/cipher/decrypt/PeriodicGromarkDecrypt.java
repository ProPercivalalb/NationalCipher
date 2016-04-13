package nationalcipher.cipher.decrypt;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.cipher.PeriodicGromark;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator.CadenusKey;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.KeySearch;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;

public class PeriodicGromarkDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Periodic Gromark";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		PeriodicGromarkTask task = new PeriodicGromarkTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		int[] range = SettingParse.getIntegerRange(this.rangeBox);
		int minLength = range[0];
		int maxLength = range[1];
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			
			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int length = minLength; length <= maxLength; ++length) {
				BigInteger value = TWENTY_SIX;
				for(int i = 25; i > 26 - length; i--)
					value = value.multiply(BigInteger.valueOf(i));
				progress.addMaxValue(value);
			}
			
			for(int len = minLength; len <= maxLength; len++)
				Creator.iteratePerodicGromark(task, len, -1);

			
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {

			progress.setIndeterminate(true);
			task.run(minLength, maxLength);
			
			output.println(task.getBestSolution());
			
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}

	private JTextField rangeBox = new JTextField("2-5");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
        JLabel range = new JLabel("Period Range:");
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		
		panel.add(new SubOptionPanel(range, this.rangeBox));
        
		dialog.add(panel);
	}
	
	public class PeriodicGromarkTask extends KeySearch implements CadenusKey {

		public PeriodicGromarkTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}
			
		@Override
		public void onIteration(String key, int textLength) {
			this.lastSolution = new Solution(PeriodicGromark.decode(this.text, key), this.settings.getLanguage()).setKeyString(key);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
			
		}
		
		@Override
		public boolean duplicateLetters() {
			return false;
		}
		
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(PeriodicGromark.decode(this.text, key), this.settings.getLanguage()).setKeyString(key);
		}

		@Override
		public void solutionFound() {
			this.output.println("%s", this.bestSolution);
			this.keyPanel.updateSolution(this.bestSolution);
		}

		@Override
		public void onIteration() {
			this.keyPanel.iterations.setText("" + this.iteration++);
		}
	}

	@Override
	public void onTermination() {
		
	}
}
