package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.list.ResultPositive;
import javalibrary.math.MathUtil;
import javalibrary.math.matrics.Matrix;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.SubstitutionHack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class HillSubstitutionAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	public HillSubstitutionAttack() {
		super("Hill Substitution");
		this.setAttackMethods(DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 3, 2, 5, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Size Range:", this.rangeSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		HillTask task = new HillTask(text, app);
		
		//Settings grab
		int[] sizeRange = SettingParse.getIntegerRange(this.rangeSpinner);
	
		if(method == DecryptionMethod.KEY_MANIPULATION) {
			for(int size = sizeRange[0]; size <= sizeRange[1]; size++) {
				if(task.cipherText.length % size != 0) {
					app.out().println("Matrix size of %d is not possible, length of text is not a multiple.", size);
					continue;
				}
				task.size = size;
				task.lengthSub = task.cipherText.length / size;
				
				KeyIterator.iterateIntegerArray(task::onList, size, 26, true);
				
				Collections.sort(task.best);
				
				if(task.best.size() < size) {
					app.out().println("Did not find enought key columns that produces good characters %d/%d.", task.best.size(), size);
					continue;
				}
				
				app.out().println("Found %d key columns with good IC.", task.best.size());
				
				int max = new int[] {512, 64, 28, 16}[size - 2];
				if(task.best.size() > max) { //2, 3, 4, 5
					app.out().println("[Cutting back to only %d]", max);
					task.best = task.best.subList(0, max);
				}
				app.out().println("Trying all combinations...");
				app.out().println("Removing trials that have no inverse...");
				app.out().println("Removing trials with %cIC less than 10...", (char)916);
				KeyIterator.iterateIntegerArray(task::onList2, size, task.best.size(), false);
				app.out().println("%d out of a possible %d trials remain to be tested.", task.bestNext.size(), MathUtil.factorial(task.best.size(), size));
				
				Collections.sort(task.bestNext);

				
				for(int i = 0; i < task.bestNext.size(); i++) {
					HillSection section = task.bestNext.get(i);
					 
					SubstitutionHack substitutionHack = new SubstitutionHack(ArrayUtil.convertCharType(section.decrypted), app);
					
					substitutionHack.run();
	
					if(substitutionHack.bestSolution.score >= task.bestSolution.score) {
						task.bestSolution = substitutionHack.bestSolution;
		
						try {
							task.bestSolution.setKeyString("%s, %s", task.bestSolution.keyString, new Matrix(section.inverseCol, size).inverseMod(26));
						}
						catch(MatrixNoInverse e) {}
						
						task.bestSolution.bakeSolution();
						task.out().println("%d/%d %s", i + 1, task.bestNext.size(), task.bestSolution);	
						task.getKeyPanel().updateSolution(task.bestSolution);
					}
				}
			}
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class HillTask extends InternalDecryption {

		private int size;
		private int lengthSub;
		private List<HillSection> best = new ArrayList<HillSection>();
		private List<HillSection> bestNext = new ArrayList<HillSection>();
		
		public HillTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		public void onList(Integer[] data) {
			boolean invalidDeterminate = false;
			for(int d : new int[] {2, 13}) {
				boolean divides = true;
				for(int s = 0; s < this.size; s++) 
					if(data[s] % d != 0)
						divides = false;

				invalidDeterminate = divides;
				if(divides) break;
			}
				
			if(invalidDeterminate)
				return;

				
			byte[] decrypted = new byte[this.lengthSub];
		
			for(int i = 0; i < this.cipherText.length; i += this.size) {	
				int total = 0;
				for(int s = 0; s < this.size; s++)
					total += data[s] * (this.cipherText[i + s] - 'A');
	
				decrypted[i / this.size] = (byte)(total % 26 + 'A');
			}
				
			double currentSum = Math.abs(StatCalculator.calculateMonoIC(decrypted) - this.getLanguage().getNormalCoincidence()) * 1000;
		
				
			if(currentSum < 10D)
				this.best.add(new HillSection(decrypted, currentSum, Arrays.copyOf(data, data.length)));
		}
		
		public void onList2(Integer[] data) {
			byte[] combinedDecrypted = new byte[this.cipherText.length];
			Integer[] inverseMatrix = new Integer[this.size * this.size];
			
			for(int s = 0; s < this.size; s++) {
				HillSection hillSection = this.best.get(data[s]);
				for(int n = 0; n < this.size; n++)
					inverseMatrix[s * this.size + n] = hillSection.inverseCol[n];
				for(int i = 0; i < this.lengthSub; i++)
					combinedDecrypted[i * this.size + s] = (byte)hillSection.decrypted[i];
			}

			double score = Math.abs(StatCalculator.calculateMonoIC(combinedDecrypted) - this.getLanguage().getNormalCoincidence()) * 1000;
			
			if(score < 10D)
				if(new Matrix(inverseMatrix, this.size).hasInverseMod(26))
					this.bestNext.add(new HillSection(combinedDecrypted, score, inverseMatrix));
		}
	}
	
	public static class HillSection extends ResultPositive {
		
		public byte[] decrypted;
		public Integer[] inverseCol;
		
		public HillSection(byte[] decrypted, double score, Integer[] inverseCol) {
			super(score);
			this.decrypted = decrypted;
			this.inverseCol = inverseCol;
		}
	}
}
