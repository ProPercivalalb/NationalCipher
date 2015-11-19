package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.Caesar;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.Creator.CaesarShift;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.InternalDecryption;
import nationalciphernew.cipher.manage.Solution;

public class CaesarDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Caesar";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}

	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		if(method == DecryptionMethod.BRUTE_FORCE) {
			CaesarTask task = new CaesarTask(text.toCharArray(), settings, keyPanel, output, progress);
			progress.addMaxValue(26);
			
			Creator.iterateCaesar(task);
			
			output.println(task.getBestSolution());
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public static class CaesarTask extends InternalDecryption implements CaesarShift {

		public CaesarTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(int shift) {
			this.lastSolution = new Solution(Caesar.decode(this.text, shift), this.settings.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("Fitness: %f, Shift: %d, Plaintext: %s", this.bestSolution.score, shift, new String(this.bestSolution.text));	
				this.keyPanel.fitness.setText("" + this.bestSolution.score);
				this.keyPanel.key.setText("Shift: " + shift);
				UINew.BEST_SOULTION = new String(this.bestSolution.text);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
		}
	}

}
