package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.NihilistTransposition;
import javalibrary.math.MathHelper;
import javalibrary.string.StringTransformer;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.Creator.NihilistTranspositionKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.InternalDecryption;
import nationalciphernew.cipher.manage.Solution;

public class NihilistTranspositionDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Nihilist Transposition";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		NihilistTranspositionTask task = new NihilistTranspositionTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			List<Integer> factors = MathHelper.getSquareFactors(text.length());
			factors.remove((Integer)1);
			output.println("" + factors);
			
			for(int keyLength = 4; keyLength <= 4; ++keyLength)
				progress.addMaxValue(MathHelper.factorialBig(keyLength));
			
			if(!factors.isEmpty()) {
				
				for(int factor : factors) {
					Creator.iterateNihilistTransposition(task, (int)Math.sqrt(factor), factor);
				}
	
				
				output.println(task.getBestSolution());
			}
			else
				output.println("NOT SQUARE FACTORS");
			
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public class NihilistTranspositionTask extends InternalDecryption implements NihilistTranspositionKey {
		
		public NihilistTranspositionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(int[] order, int blocks) {
			String str = new String(this.text);
			String plainText = "";
			for(int i = 0; i < this.text.length / blocks; i++) {
				plainText += new String(NihilistTransposition.decode(str.substring(i * blocks, (i + 1) * blocks).toCharArray(), order));
			}
			
			String finalStr = "";
			for(int i = 0; i < order.length; i++)
				finalStr += StringTransformer.getEveryNthChar(plainText, i, order.length);
			
			Solution otherSolution = new Solution(finalStr.toCharArray(), this.settings.getLanguage()).setKeyString(Arrays.toString(order) + " Read columns");
			
			this.lastSolution = new Solution(plainText.toCharArray(), this.settings.getLanguage()).setKeyString(Arrays.toString(order) + " Read rows");;
			
			if(otherSolution.score >= this.lastSolution.score)
				this.lastSolution = otherSolution;
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				this.keyPanel.fitness.setText("" + this.bestSolution.score);
				this.keyPanel.key.setText(this.bestSolution.keyString);
				UINew.BEST_SOULTION = new String(this.bestSolution.text);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
		}
	}
}
