package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.dict.Dictionary;
import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.transposition.ColumnarTransposition;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.IntegerOrderedKey;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class ColumnarTranspositionAttack extends CipherAttack {

	private JSpinner[] rangeSpinner;
	private JComboBox<Boolean> readOffDefaultChose;
	private JSpinner saSpinner;
	
	public ColumnarTranspositionAttack() {
		super("Columnar Transposition");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.DICTIONARY, DecryptionMethod.SIMULATED_ANNEALING);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 100, 1);
		this.readOffDefaultChose = new JComboBox<Boolean>(new Boolean[] {true, false});
		this.saSpinner = JSpinnerUtil.createSpinner(14, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
		panel.add(new SubOptionPanel("Read down columns (T) or across rows (F)? ", this.readOffDefaultChose));
		panel.add(new SubOptionPanel("SA Period:", this.saSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		ColumnarTranspositionTask task = new ColumnarTranspositionTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		task.readOffDefault = SettingParse.getBooleanValue(this.readOffDefaultChose);
		task.period1 = SettingParse.getInteger(this.saSpinner);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.factorialBig(length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateIntegerOrderedKey(task, length);
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			for(String word : Dictionary.words) {
				int[] order = new int[word.length()];
				
				int p = 0;
				for(char ch = 'A'; ch <= 'Z'; ++ch)
					for(int i = 0; i < order.length; i++)
						if(ch == word.charAt(i))
							order[i] = p++;
				task.onIteration(order);
			}
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class ColumnarTranspositionTask extends SimulatedAnnealing implements IntegerOrderedKey {

		public int period1;
		public boolean readOffDefault;
		public int[] bestKey1, bestMaximaKey1, lastKey1;
		
		public ColumnarTranspositionTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(int[] order) {
			this.lastSolution = new Solution(ColumnarTransposition.decode(this.cipherText, this.plainText, order, this.readOffDefault), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s, d: %b", Arrays.toString(order), this.readOffDefault);
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeyGeneration.createOrder(this.period1);
			this.lastKey1 = this.bestMaximaKey1;
			return new Solution(ColumnarTransposition.decode(this.cipherText, this.plainText, this.bestMaximaKey1, this.readOffDefault), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey1 = KeyManipulation.modifyOrder(this.bestMaximaKey1);
			
			return new Solution(ColumnarTransposition.decode(this.cipherText, this.plainText, this.lastKey1, this.readOffDefault), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.bestSolution.setKeyString("%s", Arrays.toString(this.bestKey1));
			this.bestSolution.bakeSolution();
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
	public void writeTo(Map<String, Object> map) {
		map.put("period_range_min", this.rangeSpinner[0].getValue());
		map.put("period_range_max", this.rangeSpinner[1].getValue());
		map.put("read_off_default", this.readOffDefaultChose.getSelectedItem());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		if(map.containsKey("period_range_min"))
			this.rangeSpinner[0].setValue(map.get("period_range_min"));
		if(map.containsKey("period_range_max"))
			this.rangeSpinner[1].setValue(map.get("period_range_max"));
		if(map.containsKey("ouble_letter_first"))
			this.readOffDefaultChose.setSelectedItem(map.get("read_off_default"));
	}
}
