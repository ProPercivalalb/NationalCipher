package nationalcipher.cipher.decrypt;

import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.dict.Dictionary;
import javalibrary.list.ResultPositive;
import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.SettingsUtil;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.Nicodemus;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ArrayPermutations;
import nationalcipher.cipher.decrypt.methods.KeyIterator.PermutateString;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.decrypt.methods.KeySearch;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class NicodemusAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	public final VigenereType type;
	
	public NicodemusAttack(String displayName, VigenereType type) {
		super(displayName);
		this.type = type;
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 25, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}	
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		NicodemusTask task = new NicodemusTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			for(String word : Dictionary.WORDS)
				task.onIteration(word);
		}
		else if(method == DecryptionMethod.BRUTE_FORCE) {
			String keyAlphabet = this.keyAlphabet();
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.pow(keyAlphabet.length(), length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateShortCustomKey(task, keyAlphabet, length, true);
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			//TODO This is old one
			app.getProgress().setIndeterminate(true);
			task.run(periodRange[0], periodRange[1]);
			
		/**
			for(int length = periodRange[0]; length <= periodRange[1]; ++length) {
				String decrypted = new String(Nicodemus.decode(task.cipherText, task.plainText, new String(ArrayUtil.charRange('A', (char)('A' + length))), VigenereType.NONE));
				
				//double sum = 0.0D;
				task.results = new NicodemusResult[length];
				
				for(int l = 0; l < length; l++) {
					char[] columnDecrypt = StringTransformer.getEveryNthChar(decrypted, l, length).toCharArray();
					byte[] next = new byte[columnDecrypt.length];
					double bestChiSquared = Double.MAX_VALUE;
					char bestKey = '?';
					byte[] bestNext = new byte[0];
					
					for(char c = 'A'; c <= 'Z'; c++) {
						next = VigenereFamily.decode(columnDecrypt, next, "" + c, this.type);
						double chiSquared = ChiSquared.calculate(next, app.getLanguage());
						if(chiSquared < bestChiSquared) {
							bestChiSquared = chiSquared;
							bestKey = c;
							bestNext = Arrays.copyOf(next, next.length);
						}
					}
					task.results[l] = new NicodemusResult(bestChiSquared, bestKey, bestNext);
					//app.out().println("Col:%d, %f %c %s", l, bestChiSquared, bestKey, new String(bestNext));
					//double currentSum = Math.abs(StatCalculator.calculateMonoIC(columnDecrypt) - app.getLanguage().getNormalCoincidence()) * 1000;
					//sum += currentSum;
				}
				//sum /= length;
				//app.out().println(length + " " +sum);
				KeyIterator.permutateArray(task, length, length, false);
			}**/
			
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class NicodemusResult extends ResultPositive {

		public char key;
		public byte[] text;
		
		public NicodemusResult(double score, char key, byte[] text) {
			super(score);
			this.key = key;
			this.text = text;
		}
	}
	
	public class NicodemusTask extends KeySearch implements ShortCustomKey, PermutateString, ArrayPermutations {
		
		public NicodemusResult[] results;
		
		public NicodemusTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Nicodemus.decode(this.cipherText, this.plainText, key, NicodemusAttack.this.type), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(key);
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(Nicodemus.decode(this.cipherText, this.plainText, key, NicodemusAttack.this.type), this.getLanguage()).setKeyString(key).bakeSolution();
		}
		
		@Override
		public int alphaIncrease() {
			return NicodemusAttack.this.alphaIncrease();
		}
		
		@Override
		public void solutionFound() {
			this.out().println("%s", this.bestSolution);
			this.out().println("Checking all permutations for length %d", this.bestSolution.keyString.length());
		}
		
		//Key search finds the characters in the key, permutate to find the real solution
		@Override
		public void foundBestSolutionForLength(Solution currentBestSolution) {
			KeyIterator.permutateString(this, currentBestSolution.keyString);
		}
	

		@Override
		public void onPermutate(String key) {
			this.lastSolution = new Solution(Nicodemus.decode(this.cipherText, this.plainText, key, NicodemusAttack.this.type), this.getLanguage());
			
			if(this.lastSolution.score > this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(key);
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
		}

		@Override
		public void onList(byte id, int[] data, Object... extra) {
			char[] key = new char[this.results.length];
			
			for(int c = 0; c < this.results.length; c++) {
				NicodemusResult result = this.results[data[c]];
				key[c] = result.key;
			}
			
			String keyStr = new String(key);
			
			this.lastSolution = new Solution(Nicodemus.decode(this.cipherText, this.plainText, keyStr, NicodemusAttack.this.type), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(keyStr);
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
		}	
	}
	
	@Override
	public void writeTo(Map<String, Object> map) {
		map.put("period_min", this.rangeSpinner[0].getValue());
		map.put("period_max", this.rangeSpinner[1].getValue());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		this.rangeSpinner[0].setValue(SettingsUtil.getSetting("period_min", map, Integer.TYPE, 2));
		this.rangeSpinner[1].setValue(SettingsUtil.getSetting("period_max", map, Integer.TYPE, 8));
	}

	public int alphaIncrease() { return 1; }
	
	public String keyAlphabet() { return "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; }
}
