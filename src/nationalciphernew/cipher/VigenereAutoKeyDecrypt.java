package nationalciphernew.cipher;

import java.awt.Dimension;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.cipher.VigenereAutokey;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.Creator.VigenereAutoKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.KeySearch;
import nationalciphernew.cipher.manage.SettingParse;
import nationalciphernew.cipher.manage.Solution;

public class VigenereAutoKeyDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Vigenere Autokey";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		VigenereAutoKeyTask task = new VigenereAutoKeyTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		
		int[] range = SettingParse.getIntegerRange(this.rangeBox);
		int minLength = range[0];
		int maxLength = range[1];
		
		
		if(method == DecryptionMethod.BRUTE_FORCE) {

			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int length = minLength; length <= maxLength; ++length)
				progress.addMaxValue(TWENTY_SIX.pow(length));
			
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				Creator.iterateVigenereAutoKey(task, keyLength);
			
			output.println(task.getBestSolution());
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

	private JTextField rangeBox = new JTextField("2-25");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel range = new JLabel("Keyword length range:");
		this.rangeBox.setMaximumSize(new Dimension(100, 20));
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput());
		panel.add(range);
		panel.add(this.rangeBox);
        
		dialog.add(panel);
	}
	
	public class VigenereAutoKeyTask extends KeySearch implements VigenereAutoKey {
		
		public VigenereAutoKeyTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(VigenereAutokey.decode(this.text, key), this.settings.getLanguage()).setKeyString(key);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
		}

		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(VigenereAutokey.decode(this.text, key), this.settings.getLanguage()).setKeyString(key);
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
}
