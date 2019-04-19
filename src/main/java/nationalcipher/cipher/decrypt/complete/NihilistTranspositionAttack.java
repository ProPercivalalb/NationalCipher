package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.dict.Dictionary;
import javalibrary.lib.BooleanLib;
import javalibrary.math.MathUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.transposition.NihilistTransposition;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class NihilistTranspositionAttack extends CipherAttack {

	private JComboBox<Boolean> readOffDefaultChose;
	
	public NihilistTranspositionAttack() {
		super("Nihilist Transposition");
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.BRUTE_FORCE);
		this.readOffDefaultChose = new JComboBox<Boolean>(BooleanLib.OBJECT_REVERSED);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Read across rows (T) or down columns (F)? ", this.readOffDefaultChose));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		NihilistTranspositionTask task = new NihilistTranspositionTask(text, app);
		
		//Settings grab
		task.readOffDefault = SettingParse.getBooleanValue(this.readOffDefaultChose);
		
		List<Integer> factors = MathUtil.getSquareFactors(text.length());
		factors.remove((Integer)1);
		app.out().println("" + factors);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());  //TODO Calculate real amount
			for(int factor : factors) {
				int sqrt = (int)Math.sqrt(factor);
				app.out().println("Factor: %d", factor);
				for(String word : Dictionary.WORDS) {
					if(word.length() == sqrt) {
						Integer[] order = new Integer[word.length()];
						
						int p = 0;
						for(char ch = 'A'; ch <= 'Z'; ++ch)
							for(int i = 0; i < order.length; i++)
								if(ch == word.charAt(i))
									order[i] = p++;
						
						task.onIteration(order);
					}
				}
			}
		}
		else if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int factor : factors)
				app.getProgress().addMaxValue(MathUtil.factorialBig((int)Math.sqrt(factor)));
			
			for(int factor : factors)
				KeyIterator.permuteIntegerOrderedKey(task::onIteration, (int)Math.sqrt(factor));
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class NihilistTranspositionTask extends InternalDecryption {

		public boolean readOffDefault;
		
		public NihilistTranspositionTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		public void onIteration(Integer[] order) {
			int blockSize = (int)Math.pow(order.length, 2);
			this.plainText = new byte[blockSize];
			byte[] plainText = new byte[0];
			for(int i = 0; i < this.cipherText.length / blockSize; i++)
				plainText = ArrayUtil.concat(plainText, NihilistTransposition.decode(ArrayUtil.copyRange(this.cipherText, i * blockSize, (i + 1) * blockSize), this.plainText, order, this.readOffDefault));

			this.lastSolution = new Solution(plainText, this.getLanguage());
			
			if(this.lastSolution.score > this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(Arrays.toString(order));
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public int getOutputTextLength(int inputLength) {
			return 0;
		}
	}
}
