package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;

import javalibrary.Output;
import javalibrary.cipher.AMSCO;
import javalibrary.math.MathHelper;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.Creator.AMSCOKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.InternalDecryption;
import nationalciphernew.cipher.manage.Solution;

public class AMSCODecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "AMSCO";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		if(method == DecryptionMethod.BRUTE_FORCE) {
			boolean first = true;
			
			AMSCOTask task = new AMSCOTask(text.toCharArray(), settings, keyPanel, output, progress, first);
			
			int minKeyLength = 2;
			int maxKeyLength = 7;
			
			for(int keyLength = minKeyLength; keyLength <= maxKeyLength; ++keyLength)
				progress.addMaxValue(MathHelper.factorialBig(keyLength));
			
			for(int keyLength = minKeyLength; keyLength <= maxKeyLength; ++keyLength)
				Creator.iterateAMSCO(task, keyLength);

			
			output.println(task.getBestSolution());
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog) {
		
	}
	
	public static class AMSCOTask extends InternalDecryption implements AMSCOKey {

		public boolean first;
		
		public AMSCOTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress, boolean first) {
			super(text, settings, keyPanel, output, progress);
			this.first = first;
		}
			
		@Override
		public void onIteration(int[] order) {
			this.lastSolution = new Solution(AMSCO.decode(this.text, this.first, order), this.settings.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("Fitness: %f, Order: %s, Plaintext: %s", this.bestSolution.score, Arrays.toString(order), new String(this.bestSolution.text));	
				this.keyPanel.fitness.setText("" + this.bestSolution.score);
				this.keyPanel.key.setText(Arrays.toString(order));
				UINew.BEST_SOULTION = new String(this.bestSolution.text);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			progress.increase();
		}
	}

}
