package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javalibrary.Output;
import javalibrary.cipher.Cadenus;
import javalibrary.cipher.Caesar;
import javalibrary.cipher.auto.CadenusAuto.KeyCreation;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.math.MathHelper;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.Creator.CaesarShift;

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
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, ILanguage language, Output output, KeyPanel keyPanel, ProgressValue progress) {
		if(method == DecryptionMethod.BRUTE_FORCE) {
			progress.addMaxValue(26);
			CaesarTask task = new CaesarTask(text.toCharArray(), language, output, progress);
			
			Creator.iterateCaesar(task);
			
			output.println(task.plainText);
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}
			
	}
	
	public static class CaesarTask implements CaesarShift {

		public char[] text;
		public ILanguage language;
		public Output output;
		public ProgressValue progress;
		
		public CaesarTask(char[] text, ILanguage language, Output output, ProgressValue progress) {
			this.text = text;
			this.language = language;
			this.output = output;
			this.progress = progress;
		}
		
		public String plainText = "", lastText = "";
		public double bestScore = Double.NEGATIVE_INFINITY, currentScore = 0;
			
		@Override
		public void onIteration(int shift) {
			this.currentScore = 0;
			char[] plainText = new char[this.text.length];
					
			for(int i = 0; i < this.text.length; i++) {
				plainText[i] = (char)((this.text[i] - 'A' + 26 - shift) % 26 + 'A');
				if(i >= 4 - 1) {
					this.currentScore += TextFitness.scoreWord(new String(plainText, i - 3, 4), language.getQuadgramData());
					if(this.currentScore <= this.bestScore) break;
				}
			}

			if(this.currentScore >= this.bestScore) {
				this.output.println("Fitness: %f, Shift: %s, Plaintext: %s", this.currentScore, shift, new String(plainText));
				this.bestScore = this.currentScore;
				this.plainText = this.lastText;
				UINew.BEST_SOULTION = this.plainText;
			}

			progress.increase();
		}
	}

}
