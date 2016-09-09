package nationalcipher.cipher.decrypt;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.math.matrics.Matrix;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.Hill;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.HillKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.cipher.tools.InternalDecryption;

public class HillDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Hill";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		HillTask task = new HillTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {

			int minSize = 2;
			int maxSize = 2;
			
			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int size = minSize; size <= maxSize; ++size)
				progress.addMaxValue(TWENTY_SIX.pow((int)Math.pow(size, 2)));
			
			for(int size = minSize; size <= maxSize; size++)
				Creator.iterateHill(task, size);

			
			
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

	public static class HillTask extends InternalDecryption implements HillKey {

		public HillTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(Matrix matrix) {
			
			try {
				this.lastSolution = new Solution(Hill.decode(this.text, matrix), this.settings.getLanguage()).setKeyString(matrix.toString());
				this.addSolution(this.lastSolution);
				
				if(this.lastSolution.score >= this.bestSolution.score) {
					this.bestSolution = this.lastSolution;
					this.output.println("%s", this.bestSolution);	
					this.keyPanel.updateSolution(this.bestSolution);
				}
			}
			catch(MatrixNoInverse e) {
				return;
			}
			catch(MatrixNotSquareException e) {
				return;
			}
			finally {
				this.keyPanel.updateIteration(this.iteration++);
				this.progress.increase();
			}
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}
