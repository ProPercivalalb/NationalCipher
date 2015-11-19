package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.Bazeries;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator.BazeriesKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.InternalDecryption;
import nationalciphernew.cipher.manage.Solution;

public class BazeriesDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Bazeries";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		BazeriesTask task = new BazeriesTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {

			progress.addMaxValue(1000001);

			
			
			for(int no = 1; no <= 1000000; ++no)
				task.onIteration(no);
			
			//task.sortSolutions();
			//UINew.topSolutions.updateDialog(task.solutions);

			output.println(task.getBestSolution());
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public static class BazeriesTask extends InternalDecryption implements BazeriesKey {

		public BazeriesTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(int no) {
			this.lastSolution = new Solution(Bazeries.decode(this.text, no), this.settings.getLanguage()).setKeyString("" + no);
			this.solutions.add(this.lastSolution);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("Fitness: %f, No: %d, Plaintext: %s", this.bestSolution.score, no, new String(this.bestSolution.text));	
				this.keyPanel.fitness.setText("" + this.bestSolution.score);
				this.keyPanel.key.setText("" + no);
				UINew.BEST_SOULTION = new String(this.bestSolution.text);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
		}
	}
}
