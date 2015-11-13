package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javalibrary.Output;
import javalibrary.cipher.Playfair;
import javalibrary.cipher.RailFence;
import javalibrary.cipher.wip.KeySquareManipulation;
import javalibrary.dict.Dictionary;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.math.MathHelper;
import javalibrary.swing.ProgressValue;
import javalibrary.util.RandomUtil;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.InternalDecryption;
import nationalciphernew.cipher.manage.Solution;
import nationalciphernew.cipher.manage.Creator.PlayfairKey;
import nationalciphernew.cipher.manage.Creator.RailFenceKey;

public class RailFenceDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Railfence";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		RailFenceTask task = new RailFenceTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			progress.addMaxValue(29);
			Creator.iterateRailFence(task, 1, 20);
			
			output.println(new String(task.bestSolution.text));
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	public static class RailFenceTask extends InternalDecryption implements RailFenceKey {
		
		public RailFenceTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(int rows) {
			this.lastSolution = new Solution(RailFence.decode(this.text, rows), this.settings.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("Fitness: %f, Rows: %d, Plaintext: %s", this.bestSolution.score, rows, new String(this.bestSolution.text));	
				UINew.BEST_SOULTION = new String(this.bestSolution.text);
			}
			
			progress.increase();
		}
	}
}
