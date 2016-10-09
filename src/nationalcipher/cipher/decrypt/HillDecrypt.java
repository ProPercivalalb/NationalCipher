package nationalcipher.cipher.decrypt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.algebra.Equation;
import javalibrary.algebra.Expression;
import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.math.GCD;
import javalibrary.math.MathHelper;
import javalibrary.math.matrics.Matrix;
import javalibrary.string.StringAnalyzer;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import javalibrary.util.ArrayUtil;
import nationalcipher.Settings;
import nationalcipher.cipher.Affine;
import nationalcipher.cipher.Hill;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.HillKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;
import nationalcipher.cipher.tools.InternalDecryption;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;

public class HillDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Hill";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		HillTask task = new HillTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {

			int[] range = SettingParse.getIntegerRange(this.rangeBox);
			int minSize = range[0];
			int maxSize = range[1];
			
			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int size = minSize; size <= maxSize; ++size)
				progress.addMaxValue(TWENTY_SIX.pow((int)Math.pow(size, 2)));
			
			for(int size = minSize; size <= maxSize; size++)
				Creator.iterateHill(task, size);

			
			
			//task.sortSolutions();
			//UINew.topSolutions.updateDialog(task.solutions);

			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.CALCULATED) {
			Map<String, Integer> chars = StringAnalyzer.getEmbeddedStrings(text, 2, 2, false);
			List<String> sorted = new ArrayList<String>(chars.keySet());
			Collections.sort(sorted, new StringAnalyzer.SortStringInteger(chars));
			
			for(int i = 0; i < 12; i++) {
				for(int j = 0; j < 12; j++) {
					if(i == j) continue;
					String language0 = "TH";
					String language1 = "HE";
				
					String sorted0 = sorted.get(sorted.size() - 1 - i);
					String sorted1 = sorted.get(sorted.size() - 1 - j);
			
					//output.println("%s --> %s  %s --> %s", language0, sorted0, language1, sorted1);
					// [ a, b ] 
					// [ c, d ]
				
					int[] ab = solveSimEquationsInMod2x2(language0.charAt(0)-'A', language0.charAt(1)-'A', sorted0.charAt(0)-'A', language1.charAt(0)-'A', language1.charAt(1)-'A', sorted1.charAt(0)-'A', 26);
					int[] cd = solveSimEquationsInMod2x2(language0.charAt(0)-'A', language0.charAt(1)-'A', sorted0.charAt(1)-'A', language1.charAt(0)-'A', language1.charAt(1)-'A', sorted1.charAt(1)-'A', 26);
					Matrix matrix = new Matrix(ArrayUtil.concat(ab, cd), 2, 2);
					task.onIteration(matrix);
				}	
			}
			/**
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
			char[] plainText = Affine.decode(text.toCharArray(), a, b);
			output.println(new String(plainText));
			UINew.BEST_SOULTION = plainText;**/
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	public static int[] solveSimEquationsInMod2x2(int eq1Coef1, int eq1Coef2, int eq1Equal, int eq2Coef1, int eq2Coef2, int eq2Equal, int mod) {
		
			int lcm = GCD.lcm(eq1Coef2, eq2Coef2);
			int aCoff = MathHelper.mod(eq1Coef1 * lcm / eq1Coef2 - eq2Coef1 * lcm / eq2Coef2, mod);
			int answer = MathHelper.mod(eq1Equal * lcm / eq1Coef2 - eq2Equal * lcm / eq2Coef2, mod);
			int inverse = BigInteger.valueOf(aCoff).modInverse(BigInteger.valueOf(mod)).intValue();
			int a = (answer * inverse) % mod;
			int b = MathHelper.mod((eq1Equal - a * eq1Coef1) * BigInteger.valueOf(eq1Coef2).modInverse(BigInteger.valueOf(mod)).intValue(), mod);
			return new int[] {a, b};
	}
	
	public static int[] solveSimEquationsInMod(int eq1Coef1, int eq1Coef2, int eq1Equal, int eq2Coef1, int eq2Coef2, int eq2Equal, int mod) {
		System.out.print(String.format("%da + %db = %d || ", eq1Coef1, eq1Coef2, eq1Equal));
		System.out.print(String.format("%da + %db = %d\n", eq2Coef1, eq2Coef2, eq2Equal));
		
		if(eq1Coef1 == eq2Coef1) {
			int eq4Coef1 = MathHelper.mod(eq1Coef2 - eq2Coef2, mod);
			int eq4Equal = MathHelper.mod(eq1Equal - eq2Equal, mod);
			System.out.print(String.format("%da = %d\n", eq4Coef1, eq4Equal));
			return new int[] {0, 0};
		}
		else if(eq1Coef2 == eq2Coef2) {
			int eq4Coef1 = MathHelper.mod(eq1Coef1 - eq2Coef1, mod);
			int eq4Equal = MathHelper.mod(eq1Equal - eq2Equal, mod);
			System.out.print(String.format("%da = %d\n", eq4Coef1, eq4Equal));
			return new int[] {0, 0};
		}
		else {
			int lcm = GCD.lcm(eq1Coef2, eq2Coef2);
			int aCoff = MathHelper.mod(eq1Coef1 * lcm / eq1Coef2 - eq2Coef1 * lcm / eq2Coef2, mod);
			int answer = MathHelper.mod(eq1Equal * lcm / eq1Coef2 - eq2Equal * lcm / eq2Coef2, mod);
			System.out.println(String.format("%da = %d mod 26\n", aCoff, answer));
			System.out.println(aCoff);
			int inverse = BigInteger.valueOf(aCoff).modInverse(BigInteger.valueOf(mod)).intValue();
			int a = (answer * inverse) % mod;
			//output.println("a = %d", a);
			System.out.println(eq1Coef2);
			int b = MathHelper.mod((eq1Equal - a * eq1Coef1) * BigInteger.valueOf(eq1Coef2).modInverse(BigInteger.valueOf(mod)).intValue(), mod);
			return new int[] {a, b};
		}
	}
	
	public static void main(String[] args) {
		//[14, 6, 13, 25, 10, 1, 18, 11, 13]
		//WDD     -  ESZ   - WZD  --- ENT goes to RPU  --- ING goes to IYB
		solveSimEquationsInMod('T'-65, 'H'-65, 'E'-65, 'W'-65,    'I'-65, 'N'-65, 'G'-65, 'I'-65,   'E'-65, 'N'-65, 'T'-65, 'R' -65);
	}
	
	public static int[] solveSimEquationsInMod(int eq1Coef1, int eq1Coef2, int eq1Coef3, int eq1Equal, int eq2Coef1, int eq2Coef2, int eq2Coef3, int eq2Equal, int eq3Coef1, int eq3Coef2, int eq3Coef3, int eq3Equal) {
		int lcm2 = GCD.lcm(eq1Coef3, eq2Coef3);
		//int lcm3 = GCD.lcm(eq1Coef3, eq2Coef3, eq3Coef3);
		System.out.println(lcm2);
		//System.out.println(lcm3);
		System.out.print(String.format("%da + %db + %dc = %d || ", eq1Coef1, eq1Coef2, eq1Coef3, eq1Equal));
		System.out.print(String.format("%da + %db + %dc = %d || ", eq2Coef1, eq2Coef2, eq2Coef3, eq2Equal));
		System.out.print(String.format("%da + %db + %dc = %d  \n", eq3Coef1, eq3Coef2, eq3Coef3, eq3Equal));
		
		int temp = lcm2 / eq1Coef3;
		eq1Equal = (eq1Equal * temp) % 26;
		eq1Coef1 = (eq1Coef1 * temp) % 26;
		eq1Coef2 = (eq1Coef2 * temp) % 26;
		eq1Coef3 = (eq1Coef3 * temp) % 26;

		temp = lcm2 / eq2Coef3;
		eq2Equal = (eq2Equal * temp) % 26;
		eq2Coef1 = (eq2Coef1 * temp) % 26;
		eq2Coef2 = (eq2Coef2 * temp) % 26;
		eq2Coef3 = (eq2Coef3 * temp) % 26;
		
		temp = lcm2 / eq3Coef3;
		eq3Equal = (eq3Equal * temp) % 26;
		eq3Coef1 = (eq3Coef1 * temp) % 26;
		eq3Coef2 = (eq3Coef2 * temp) % 26;
		eq3Coef3 = (eq3Coef3 * temp) % 26;
	
		System.out.print(String.format("%da + %db + %dc = %d || ", eq1Coef1, eq1Coef2, eq1Coef3, eq1Equal));
		System.out.print(String.format("%da + %db + %dc = %d ||", eq2Coef1, eq2Coef2, eq2Coef3, eq2Equal));
		System.out.print(String.format("%da + %db + %dc = %d\n", eq3Coef1, eq3Coef2, eq3Coef3, eq3Equal));
		
		int[] ab = solveSimEquationsInMod(MathHelper.mod(eq3Coef1-eq2Coef1, 26), MathHelper.mod(eq3Coef3-eq2Coef3, 26), MathHelper.mod(eq3Equal-eq2Equal, 26), MathHelper.mod(eq2Coef1-eq1Coef1, 26), MathHelper.mod(eq2Coef3-eq1Coef3, 26), MathHelper.mod(eq2Equal-eq1Equal, 26), 26);
		
		/**
		int aCoff = MathHelper.mod(eq1Coef1 * lcm / eq1Coef2 - eq2Coef1 * lcm / eq2Coef2, 26);
		int answer = MathHelper.mod(eq1Equal * lcm / eq1Coef2 - eq2Equal * lcm / eq2Coef2, 26);
		//output.println("%da = %d mod 26\n", aCoff, answer);
		int inverse = BigInteger.valueOf(aCoff).modInverse(BigInteger.valueOf(26)).intValue();
		int a = (answer * inverse) % 26;
		//output.println("a = %d", a);
		int b = MathHelper.mod((eq1Equal - a * eq1Coef1) * BigInteger.valueOf(eq1Coef2).modInverse(BigInteger.valueOf(26)).intValue(), 26);
		**/
		return new int[] {0, 0};
	}
	
	private JTextField rangeBox = new JTextField("2");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
        JLabel range = new JLabel("Matrix Size Range:");
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		
		panel.add(new SubOptionPanel(range, this.rangeBox));
        
		dialog.add(panel);
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
