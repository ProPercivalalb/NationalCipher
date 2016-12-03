package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.list.ResultPositive;
import javalibrary.math.MathUtil;
import javalibrary.string.StringTransformer;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.other.PeriodicGromark;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ArrayPermutations;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.decrypt.methods.KeySearch;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class PeriodicGromarkAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public PeriodicGromarkAttack() {
		super("Periodic Gromark");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED, DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		PeriodicGromarkTask task = new PeriodicGromarkTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.pow(26, length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateShort26Key(task, length, false);
		}
		else if(method == DecryptionMethod.CALCULATED) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length) {
				task.onList((byte)-1, ArrayUtil.range(0, length));
				//KeyIterator.permutateArray(task, length, length, false);
			}
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().setIndeterminate(true);
			task.run(periodRange[0], periodRange[1]);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class PeriodicGromarkTask extends KeySearch implements ShortCustomKey, ArrayPermutations {

		public PeriodicGromarkTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(PeriodicGromark.decode(this.cipherText, this.plainText, key), this.getLanguage());
			this.addSolution(this.lastSolution);
			
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
			if(StringTransformer.getUniqueCharSet(key).size() == key.length())
			
				return new Solution(PeriodicGromark.decode(this.cipherText, this.plainText, key), this.getLanguage()).setKeyString(key).bakeSolution();
			return new Solution();
		}
		
		@Override
		public boolean hasDuplicateLetters() {
			return false;
		}
		
		@Override
		public void onList(byte id, int[] data, Object... extra) {
			int[] numericKey = new int[this.cipherText.length];
			
			for(int i = 0; i < data.length; i++)
				numericKey[i] = data[i] + 1;
			
			for(int i = 0; i < numericKey.length - data.length; i++)
				numericKey[i + data.length] = (numericKey[i] + numericKey[i + 1]) % 10;
			
			byte[] decrypted = new byte[this.cipherText.length];
				
			for(int i = 0; i < this.cipherText.length; i++) {
				decrypted[i] = (byte)((this.cipherText[i] -'A' + 26 - numericKey[i]) % 26 + 'A');
			}
			
			int[][] counts = new int[data.length][26];
			for(int i = 0; i < this.cipherText.length; i++) {
				int p = (int)(Math.floor(i / data.length) % data.length);
				counts[p][this.cipherText[i] - 'A'] += 1;
			}
			
			double sumIC = 0.0D;
			for(int i = 0; i < data.length; i++) {
				double total = 0D;
				int n = 0;
				for(int j = 0; j < 26; j++) {
					double count = counts[i][j];
					total += count * (count - 1);
					n += count;
				}
				
				if(n > 1)
					sumIC += total / (n * (n - 1));
			}
			sumIC *= 1000;
			sumIC /= data.length;
			
			PeriodicGromarkResult result = new PeriodicGromarkResult(decrypted, sumIC, data);
			this.out().println("%s", result);
		}
	}
	
	public class PeriodicGromarkResult extends ResultPositive {
		
		public byte[] decrypted;
		public int[] order;
		
		public PeriodicGromarkResult(byte[] decrypted, double score, int[] inverseCol) {
			super(score);
			this.decrypted = decrypted;
			this.order = inverseCol;
		}
		
		@Override
		public String toString() {
			if(this.decrypted.length > 100) {
				byte[] printDecrypt = new byte[105];
				for(int i = 0; i < 50; i++)
					printDecrypt[i] = this.decrypted[i];
				printDecrypt[50] = ' ';
				printDecrypt[51] = '.';
				printDecrypt[52] = '.';
				printDecrypt[53] = '.';
				printDecrypt[54] = ' ';
				for(int i = 0; i < 50; i++)
					printDecrypt[i + 55] = this.decrypted[this.decrypted.length - 51 + i];
				
				return String.format("%s, %f, %s", Arrays.toString(this.order), this.score, new String(printDecrypt));
			}
			else
				return String.format("%s, %f, %s", Arrays.toString(this.order), this.score, new String(this.decrypted));
		}
	}
	
}



/**
public class PeriodicGromarkAttack extends LongKeyAttack {

	public PeriodicGromarkAttack() {
		super("Periodic Gromark");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key) {
		return PeriodicGromark.decode(cipherText, plainText, key);
	}
}**/


