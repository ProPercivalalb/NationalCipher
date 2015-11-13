package nationalciphernew.cipher;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javalibrary.Output;
import javalibrary.cipher.Affine;
import javalibrary.cipher.Caesar;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.math.MathHelper;
import javalibrary.string.StringAnalyzer;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.Creator.AffineKey;

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
			AffineTask task = new AffineTask(text.toCharArray(), settings.getLanguage(), output);
			
			Creator.iterateAffine(task);
			
			output.println(task.plainText);
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
			int aCoff = MathHelper.mod(language1 - language0, 26);
			int answer = MathHelper.mod(sorted1 - sorted0, 26);
			output.println("(x)-(y) = %da = %d mod 26", aCoff, answer);
			
			
			int inverse = BigInteger.valueOf(aCoff).modInverse(BigInteger.valueOf(26)).intValue();
			int a = (answer * inverse) % 26;
			output.println("a = %d", a);
			int b = MathHelper.mod((sorted0 - 'A') - a * (language0 - 'A'), 26);
			output.println("b = %d", b);
			String plainText = Affine.decode(text.toCharArray(), a, b);
			output.println(plainText);
			UINew.BEST_SOULTION = plainText;
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	public static class AffineTask implements AffineKey {

		public char[] text;
		public ILanguage language;
		public Output output;
		
		public AffineTask(char[] text, ILanguage language, Output output) {
			this.text = text;
			this.language = language;
			this.output = output;
		}
		
		public String plainText = "", lastText = "";
		public double bestScore = Double.NEGATIVE_INFINITY, currentScore = 0;
			
		@Override
		public void onIteration(int a, int b) {
			this.lastText = Affine.decode(this.text, a, b);
			this.currentScore = TextFitness.scoreFitnessQuadgrams(this.lastText, this.language);
			
			if(this.currentScore >= this.bestScore) {
				this.output.println("Fitness: %f, A: %d, B: %d, Plaintext: %s", this.currentScore, a, b, this.lastText);	
				this.bestScore = this.currentScore;
				this.plainText = this.lastText;
				UINew.BEST_SOULTION = this.plainText;
			}
		}
	}

}
