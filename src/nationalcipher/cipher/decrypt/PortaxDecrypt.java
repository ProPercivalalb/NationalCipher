package nationalcipher.cipher.decrypt;

import java.awt.BorderLayout;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

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
import nationalcipher.cipher.base.polygraphic.Portax;
import nationalcipher.cipher.decrypt.complete.methods.KeySearch;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.PortaKey;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.KeyPanel;

public class PortaxDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Portax";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		PortaTask task = new PortaTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		List<Integer> factors = MathUtil.getFactors(text.length() / 2);
		output.println("Factors: %s", factors);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			BigInteger THIRTEEN = BigInteger.valueOf(13);
			for(int factor : factors)
				progress.addMaxValue(THIRTEEN.pow(factor));
			
			for(int factor : factors)
				Creator.iteratePorta(task, factor);
			
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {

			progress.setIndeterminate(true);
			for(int factor : factors)
				task.run(factor, factor);
			
			output.println(task.getBestSolution());
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	private JTextField rangeBox = new JTextField("2-25");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {

        JLabel range = new JLabel("Keyword length range: ");
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		
		panel.add(new SubOptionPanel(range, this.rangeBox), BorderLayout.WEST);
	}
	
	public class PortaTask extends KeySearch implements PortaKey {

	
		public PortaTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}
			
		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Portax.decode(this.cipherText, key), this.settings.getLanguage()).setKeyString(key);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}
		
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(Portax.decode(this.cipherText, key), this.settings.getLanguage()).setKeyString(key);
		}

		@Override
		public void solutionFound() {
			this.output.println("%s", this.bestSolution);
			this.keyPanel.updateSolution(this.bestSolution);
		}

		@Override
		public void onIteration() {
			this.keyPanel.updateIteration(this.iteration++);
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}

}
