package nationalciphernew.cipher;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.Porta;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.Creator.PortaKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.InternalDecryption;
import nationalciphernew.cipher.manage.Solution;

public class PortaDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Porta";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		if(method == DecryptionMethod.BRUTE_FORCE) {
			PortaTask task = new PortaTask(text.toCharArray(), settings, keyPanel, output, progress);
			
			int minLength = 5;
			int maxLength = 5;
			BigInteger THIRTEEN = BigInteger.valueOf(13);
			for(int length = minLength; length <= maxLength; length++)
				progress.addMaxValue(THIRTEEN.pow(length));
			
			progress.addMaxValue(312);
			
			for(int length = minLength; length <= maxLength; ++length)
				Creator.iteratePorta(task, length);
			
			output.println(new String(task.bestSolution.text));
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public static class PortaTask extends InternalDecryption implements PortaKey {

		public boolean shiftRight;
		
		public PortaTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.shiftRight = false;
		}
			
		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Porta.decode(this.text, key, this.shiftRight), this.settings.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, key, new String(this.bestSolution.text));	
				this.keyPanel.fitness.setText("" + this.bestSolution.score);
				this.keyPanel.key.setText(key);
				UINew.BEST_SOULTION = new String(this.bestSolution.text);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
		}
	}

}
