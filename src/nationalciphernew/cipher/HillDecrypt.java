package nationalciphernew.cipher;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;

import javalibrary.Output;
import javalibrary.cipher.Hill;
import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.math.matrics.Matrix;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.Creator.HillKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.InternalDecryption;
import nationalciphernew.cipher.manage.Solution;

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
	public void createSettingsUI(JDialog dialog) {
		
	}

	public static class HillTask extends InternalDecryption implements HillKey {

		public HillTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(Matrix matrix) {
			
			try {
				this.lastSolution = new Solution(Hill.decode(this.text, matrix), this.settings.getLanguage());
				this.solutions.add(this.lastSolution);
				
				if(this.lastSolution.score >= this.bestSolution.score) {
					this.bestSolution = this.lastSolution;
					this.output.println("Fitness: %f, Matrix: %s, Plaintext: %s", this.bestSolution.score, matrix, new String(this.bestSolution.text));	
					this.keyPanel.fitness.setText("" + this.bestSolution.score);
					this.keyPanel.key.setText(matrix.toString());
					UINew.BEST_SOULTION = new String(this.bestSolution.text);
				}
			}
			catch(MatrixNoInverse e) {
				return;
			}
			catch(MatrixNotSquareException e) {
				return;
			}
			finally {
				this.keyPanel.iterations.setText("" + this.iteration++);
				this.progress.increase();
			}
		}
	}
}
