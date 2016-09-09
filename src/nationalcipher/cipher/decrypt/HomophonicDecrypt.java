package nationalcipher.cipher.decrypt;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.Homophonic;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.HomophonicKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.cipher.tools.InternalDecryption;

public class HomophonicDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Homophonic";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		HomophonicTask task = new HomophonicTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			int minLength = 4;
			int maxLength = 4;
			
			BigInteger TWENTY_SIX = BigInteger.valueOf(25);
			
			progress.addMaxValue(TWENTY_SIX.pow(4));
			
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				Creator.iterateHomophonic(task, keyLength);
			
			output.println(task.getBestSolution());
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public class HomophonicTask extends InternalDecryption implements HomophonicKey {
		
		public HomophonicTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Homophonic.decode(this.text, key), this.settings.getLanguage()).setKeyString(key);
			
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
		// TODO Auto-generated method stub
		
	}
}
