package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.transposition.ColumnarTransposition;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.DoubleIntegerOrderedKey;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class DoubleTranspositionAttack extends CipherAttack {

	public JSpinner[] rangeSpinner1;
	public JSpinner[] rangeSpinner2;
	public JSpinner spinner1;
	public JSpinner spinner2;
	private DoubleTranspositionTask task;
	
	public DoubleTranspositionAttack() {
		super("Double Transposition");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.SIMULATED_ANNEALING);
		this.rangeSpinner1 = JSpinnerUtil.createRangeSpinners(2, 6, 2, 100, 1);
		this.rangeSpinner2 = JSpinnerUtil.createRangeSpinners(2, 6, 2, 100, 1);
		this.spinner1 = JSpinnerUtil.createSpinner(4, 2, 100, 1);
		this.spinner2 = JSpinnerUtil.createSpinner(5, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period 1 Range:", this.rangeSpinner1));
		panel.add(new SubOptionPanel("Period 2 Range:", this.rangeSpinner2));
		panel.add(new SubOptionPanel("SA Period 1:", this.spinner1));
		panel.add(new SubOptionPanel("SA Period 2:", this.spinner2));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		this.task = new DoubleTranspositionTask(text, app);
		
		//Settings grab
		int[] periodRange1 = SettingParse.getIntegerRange(this.rangeSpinner1);
		int[] periodRange2 = SettingParse.getIntegerRange(this.rangeSpinner2);
		this.task.period1 = SettingParse.getInteger(this.spinner1);
		this.task.period2 = SettingParse.getInteger(this.spinner2);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length1 = periodRange1[0]; length1 <= periodRange1[1]; ++length1)
				for(int length2 = periodRange2[0]; length2 <= periodRange2[1]; ++length2)
					app.getProgress().addMaxValue(MathUtil.factorialBig(length1).multiply(MathUtil.factorialBig(length2)));
			
			for(int length1 = periodRange1[0]; length1 <= periodRange1[1]; ++length1)
				for(int length2 = periodRange2[0]; length2 <= periodRange2[1]; ++length2)
					KeyIterator.iterateDoubleIntegerOrderedKey(this.task, length1, length2);
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			this.task.run();
		}
		
		app.out().println(this.task.getBestSolution());
	}
	
	public class DoubleTranspositionTask extends SimulatedAnnealing implements DoubleIntegerOrderedKey {

		public int period1;
		public int period2;
		public int[] bestKey1, bestMaximaKey1, lastKey1;
		public int[] bestKey2, bestMaximaKey2, lastKey2;
		
		public DoubleTranspositionTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public void onIteration(int[] order1, int[] order2) {
			this.lastSolution = new Solution(ColumnarTransposition.decode(ArrayUtil.convertCharType(ColumnarTransposition.decode(this.cipherText, this.plainText, order2, true)), new byte[this.cipherText.length], order1, true), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s %s", Arrays.toString(order1), Arrays.toString(order2));
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeyGeneration.createOrder(this.period1);
			this.bestMaximaKey2 = KeyGeneration.createOrder(this.period2);
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			return new Solution(ColumnarTransposition.decode(ArrayUtil.convertCharType(ColumnarTransposition.decode(this.cipherText, this.plainText, this.bestMaximaKey2, true)), new byte[this.cipherText.length], this.bestMaximaKey1, true), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			if(count % 2 == 0) {
				int[] copy = ArrayUtil.copyOfRange(this.bestMaximaKey1, 0, this.period1);
				for(int i = 0; i < RandomUtil.pickRandomInt(1, (int)Math.ceil(this.period1 / 2D)); i++)
					KeySquareManipulation.exchangeOrder(copy);
				this.lastKey1 = copy;
			}
			else {
				int[] copy = ArrayUtil.copyOfRange(this.bestMaximaKey2, 0, this.period2);
				for(int i = 0; i < RandomUtil.pickRandomInt(1, (int)Math.ceil(this.period1 / 2D)); i++)
					KeySquareManipulation.exchangeOrder(copy);
				this.lastKey2 = copy;
			}
			return new Solution(ColumnarTransposition.decode(ArrayUtil.convertCharType(ColumnarTransposition.decode(this.cipherText, this.plainText, this.lastKey2, true)), new byte[this.cipherText.length], this.lastKey1, true), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
			this.bestMaximaKey2 = this.lastKey2;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.bestKey2 = this.bestMaximaKey2;
			this.bestSolution.setKeyString("%s %s", Arrays.toString(this.bestKey1), Arrays.toString(this.bestKey2));
			this.getKeyPanel().updateSolution(this.bestSolution);
		}
		
		@Override
		public void onIteration() {
			this.getProgress().increase();
			this.getKeyPanel().updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.out().println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
	}
	
	@Override
	public void onTermination(boolean forced) {
		if(forced)
			this.task.app.out().println("%s", this.task.bestSolution);
	}
}
