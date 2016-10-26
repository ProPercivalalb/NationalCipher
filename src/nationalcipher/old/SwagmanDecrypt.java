package nationalcipher.old;

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
import nationalcipher.Settings;
import nationalcipher.cipher.base.transposition.Swagman;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.SwagmanKey;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.KeyPanel;

public class SwagmanDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Swagman";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		SwagmanTask task = new SwagmanTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			progress.addMaxValue(10000);
			for(int keyLength = 3; keyLength <= 3; ++keyLength)
				Creator.iterateSwagman(task, keyLength);
			
			output.println(task.getBestSolution());
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	private JTextField rangeBox = new JTextField("3");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {

		JLabel range = new JLabel("Key Dimensions:");
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
			
		panel.add(new SubOptionPanel(range, this.rangeBox));

	}
	
	public class SwagmanTask extends InternalDecryption implements SwagmanKey {
		
		public int keyDimensions;
		
		public SwagmanTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.keyDimensions = SettingParse.getInteger(rangeBox);
		}

		@Override
		public void onIteration(int[] key) {
			this.lastSolution = new Solution(Swagman.decode(this.cipherText, this.keyDimensions, key), this.settings.getLanguage());//.setKeyString(Arrays.toString(order));
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}
	}

	@Override
	public void onTermination() {
		
	}
}
