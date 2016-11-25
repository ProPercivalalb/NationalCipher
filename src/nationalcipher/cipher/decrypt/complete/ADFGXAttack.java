package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.lib.Alphabet;
import javalibrary.math.matrics.Matrix;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.base.transposition.ColumnarTransposition;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.SubstitutionHack;
import nationalcipher.cipher.decrypt.complete.HillSubstitutionAttack.HillSection;
import nationalcipher.cipher.decrypt.complete.HillSubstitutionAttack.LongKeyTask;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ArrayPermutations;
import nationalcipher.cipher.decrypt.methods.KeyIterator.IntegerKey;
import nationalcipher.cipher.decrypt.methods.KeyIterator.IntegerOrderedKey;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.ui.IApplication;

public class ADFGXAttack extends CipherAttack {

	public ADFGXAttack() {
		super("ADFGX");
		this.setAttackMethods(DecryptionMethod.KEY_MANIPULATION);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		ADFGXTask task = new ADFGXTask(text, app);
		
		if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().addMaxValue(26);
			for(int length = 2; length <= 8; length++)
				KeyIterator.permutateArray(task, length, length, false);
			
			for(int i = 0; i < task.best.size(); i++) {
				ADFGXSection section = task.best.get(i);
				app.out().println("%s, %f, %s", Arrays.toString(section.order), section.score, new String(section.decrypted));
			}
			
			Collections.sort(task.best, this.comparator);

			
			for(int i = 0; i < task.best.size(); i++) {
				ADFGXSection section = task.best.get(i);

				char[] tempText = new char[section.decrypted.length / 2];

				SubstitutionHack substitutionHack = new SubstitutionHack(tempText, app) {
					@Override
					public String genRandomStartKey() {
						return KeyGeneration.createLongKey36();
					}
					
					@Override
					public String getAlphabet() {
						return "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
					}
				};
				
				char[] alpha = new char[] {'A', 'D', 'F', 'G', 'V', 'X'};
				
				for(int r = 0; r < alpha.length; r++) {
					for(int c = 0; c < alpha.length; c++) {
						for(int d = 0; d < section.decrypted.length; d += 2)
							if(section.decrypted[d] == alpha[r] && section.decrypted[d + 1] == alpha[c])
								tempText[d / 2] = substitutionHack.getAlphabet().charAt(r * alpha.length + c);
					}
					
				}
				
				substitutionHack.run();
				
				if(substitutionHack.bestSolution.score >= task.bestSolution.score) {
					task.bestSolution = substitutionHack.bestSolution;
					task.bestSolution.setKeyString("%s, %s", Arrays.toString(section.order), task.bestSolution.keyString);
					task.bestSolution.bakeSolution();
					task.out().println("%d/%d %s", i + 1, task.best.size(), task.bestSolution);	
					task.getKeyPanel().updateSolution(task.bestSolution);
				}
			}
		}
		
		app.out().println(task.getBestSolution());
	}
	
	 public Comparator<ADFGXSection> comparator = new Comparator<ADFGXSection>() {
	    	@Override
	        public int compare(ADFGXSection c1, ADFGXSection c2) {
	        	double diff = c1.score - c2.score;
	        	return diff == 0.0D ? 0 : diff > 0 ? 1 : -1; 
	        }
	    };
	
	public class ADFGXTask extends InternalDecryption implements ArrayPermutations {

		private List<ADFGXSection> best = new ArrayList<ADFGXSection>();
		
		public ADFGXTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onList(byte id, int[] data) {
			byte[] decrypted = new byte[this.cipherText.length];
			decrypted = ColumnarTransposition.decode(this.cipherText, decrypted, data, false);
			
			double currentSum = Math.abs(StatCalculator.calculateIC(decrypted, 2, false) - this.getLanguage().getNormalCoincidence()) * 1000;
	
			if(currentSum < 10D)
				this.best.add(new ADFGXSection(decrypted, currentSum, Arrays.copyOf(data, data.length)));
			
		}
	}
	
	public static class ADFGXSection {
		public byte[] decrypted;
		public double score;
		public int[] order;
		public ADFGXSection(byte[] decrypted, double score, int[] inverseCol) {
			this.decrypted = decrypted;
			this.score = score;
			this.order = inverseCol;
		}
	}
}
