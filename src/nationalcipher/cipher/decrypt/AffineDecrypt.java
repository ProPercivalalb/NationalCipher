package nationalcipher.cipher.decrypt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.math.MathUtil;
import javalibrary.string.StringAnalyzer;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.base.substitution.Affine;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.AffineKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;

public class AffineDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Affine";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		if(method == DecryptionMethod.BRUTE_FORCE) {
			AffineTask task = new AffineTask(text.toCharArray(), settings, keyPanel, output, progress);
			
			progress.addMaxValue(312);
			
			Creator.iterateAffine(task);
			
			output.println(new String(task.bestSolution.getText()));
		}
		else if(method == DecryptionMethod.CALCULATED) {
			Map<String, Integer> chars = StringAnalyzer.getEmbeddedStrings(text, 1, 1);
			List<String> sorted = new ArrayList<String>(chars.keySet());
			Collections.sort(sorted, new StringAnalyzer.SortStringInteger(chars));
			char language0 = settings.getLanguage().getLetterLargestFirst().get(0);
			char language1 = settings.getLanguage().getLetterLargestFirst().get(1);
			
			char sorted0 = sorted.get(sorted.size() - 1).charAt(0);
			char sorted1 = sorted.get(sorted.size() - 2).charAt(0);
			
			output.println("%s --> %s  %s --> %s", language0, sorted0, language1, sorted1);
			
			output.println("(x) %da + b = %d mod 26", (language1 - 'A'), (sorted1 - 'A'));
			output.println("(y) %da + b = %d mod 26", (language0 - 'A'), (sorted0 - 'A'));
			int aCoff = MathUtil.mod(language1 - language0, 26);
			int answer = MathUtil.mod(sorted1 - sorted0, 26);
			output.println("(x)-(y) = %da = %d mod 26", aCoff, answer);
			
			
			int inverse = BigInteger.valueOf(aCoff).modInverse(BigInteger.valueOf(26)).intValue();
			int a = (answer * inverse) % 26;
			output.println("a = %d", a);
			int b = MathUtil.mod((sorted0 - 'A') - a * (language0 - 'A'), 26);
			output.println("b = %d", b);
			char[] plainText = Affine.decode(text.toCharArray(), a, b);
			output.println(new String(plainText));
			UINew.BEST_SOULTION = plainText;
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void onTermination() {
		
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public class AffineTask extends InternalDecryption implements AffineKey {

		public AffineTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}
			
		@Override
		public void onIteration(int a, int b) {
			this.lastSolution = new Solution(Affine.decode(this.cipherText, a, b), this.settings.getLanguage()).setKeyString("A-%d, B-%d", a, b);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}
	}
}
