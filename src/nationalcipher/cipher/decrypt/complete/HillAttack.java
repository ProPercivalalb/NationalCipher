package nationalcipher.cipher.decrypt.complete;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.algebra.SimultaneousEquations;
import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.math.matrics.Matrix;
import javalibrary.string.StringAnalyzer;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.other.Hill;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.SquareMatrixKey;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class HillAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	public JSpinner gramSearchRange;
	public JComboBox<String> trigramSets;
	public final String[][] BI_GRAM = new String[][] {new String[] {"TH", "HE"}}; 
	public final String[][] TRI_GRAM = new String[][] {new String[] {"THE", "AND", "ING"}, new String[] {"THE", "ING", "ENT"}, new String[] {"THE", "ING", "ION"}, new String[] {"THE", "ENT", "ION"}, new String[] {"ING", "ENT", "HER"}, new String[] {"ING", "ENT", "FOR"}, new String[] {"ENT", "ION", "HER"}, new String[] {"ING", "ENT", "THA"}, new String[] {"ING", "ENT", "NTH"}};
	public final String[] TRI_GRAM_DISPLAY = new String[] {"THE AND ING", "THE ING ENT", "THE ING ION", "THE ENT ION", "ING ENT HER", "ING ENT FOR", "ENT ION HER", "ING ENT THA", "ING ENT NTH"};
	
	public HillAttack() {
		super("Hill");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 3, 2, 3, 1);
		this.gramSearchRange = JSpinnerUtil.createSpinner(20, 3, 100, 1);
		this.trigramSets = new JComboBox<String>(this.TRI_GRAM_DISPLAY);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Size Range:", this.rangeSpinner));
		panel.add(new SubOptionPanel("N-Gram Search Range:", this.gramSearchRange));
		panel.add(new SubOptionPanel("3x3 Trigram Sets:", this.trigramSets));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		HillTask task = new HillTask(text, app);
		
		//Settings grab
		int[] sizeRange = SettingParse.getIntegerRange(this.rangeSpinner);
		int gramSearchRange = SettingParse.getInteger(this.gramSearchRange);
		String[][] commonGrams = new String[][] {BI_GRAM[0], TRI_GRAM[this.trigramSets.getSelectedIndex()]};
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int size = sizeRange[0]; size <= sizeRange[1]; ++size)
				app.getProgress().addMaxValue(TWENTY_SIX.pow((int)Math.pow(size, 2)));
			
			for(int size = sizeRange[0]; size <= sizeRange[1]; size++)
				KeyIterator.iteratorSquareMatrixKey(task, size);
		}
		else if(method == DecryptionMethod.CALCULATED) {
			for(int size = sizeRange[0]; size <= sizeRange[1]; size++) {
				Map<String, Integer> chars = StringAnalyzer.getEmbeddedStrings(text, size, size, false);
				List<String> sorted = new ArrayList<String>(chars.keySet());
				Collections.sort(sorted, new StringAnalyzer.SortStringInteger(chars));
				Collections.reverse(sorted);
				
				int[][] pickPattern = this.generatePickPattern(size, Math.min(gramSearchRange, sorted.size()));
				
				for(int i = 0; i < pickPattern.length; i++) {
					int[] matrixData = new int[0];
					for(int k = 0; k < size; k++) {
						int[][] equations = new int[size][size + 1];
						for(int l = 0; l < size; l++)
							equations[l] = this.createEquationFrom(commonGrams[size - 2][l], sorted.get(pickPattern[i][l]), k);
						int[] solution = SimultaneousEquations.solveSimEquationsMod(equations, 26);
						matrixData = ArrayUtil.concat(matrixData, solution);
					}
		
					task.onIteration(new Matrix(matrixData, size, size));
				}
			}
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public int[][] generatePickPattern(int size, int times) { //MathUtil.factorial(times)
		int[][] patterns = new int[(int)Math.pow(times, size)][size];

		int count = 0;
		
		if(size == 2) {
			for(int i = 0; i < times; i++)
				for(int j = 0; j < times; j++) {
					if(i == j) continue;
					patterns[count++] = new int[] {i, j};
				}
			
			return patterns;
		}
		else if(size == 3) {
			for(int i = 0; i < times; i++)
				for(int j = 0; j < times; j++) {
					for(int k = 0; k < times; k++) {
						if(i == j || i == k || j == k) continue;
						patterns[count++] = new int[] {i, j, k};
					}
				}
			
			return patterns;
		}
		
		return new int[0][0];
	}
	
	public int[] createEquationFrom(String plainText, String cipherText, int index) {
		int[] equation = new int[plainText.length() + 1];
		for(int i = 0; i < equation.length - 1; i++)
			equation[i] = plainText.charAt(i) - 'A';
		equation[equation.length - 1] = cipherText.charAt(index) - 'A';
		return equation;
	}
	
	public class HillTask extends InternalDecryption implements SquareMatrixKey {

		public HillTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(Matrix matrix) {
			try {
				this.lastSolution = new Solution(Hill.decode(this.cipherText, matrix), this.getLanguage());
				
				if(this.lastSolution.score >= this.bestSolution.score) {
					this.bestSolution = this.lastSolution;
					this.bestSolution.setKeyString(matrix.toString());
					this.out().println("%s", this.bestSolution);	
					this.getKeyPanel().updateSolution(this.bestSolution);
				}
			}
			catch(MatrixNoInverse e) {
				return;
			}
			catch(MatrixNotSquareException e) {
				return;
			}
			finally {
				this.getKeyPanel().updateIteration(this.iteration++);
				this.getProgress().increase();
			}
		}
	}
}
