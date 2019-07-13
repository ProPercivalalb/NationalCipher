package nationalcipher.cipher.decrypt.complete;

import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.dict.Dictionary;
import javalibrary.lib.BooleanLib;
import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class ColumnarTranspositionAttack extends CipherAttack<Integer[]> {

	private JSpinner[] rangeSpinner;
	private JComboBox<Boolean> readOffDefaultChose;
	private JSpinner saSpinner;
	
	public ColumnarTranspositionAttack() {
		super("Columnar Transposition");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.DICTIONARY, DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.CALCULATED);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 100, 1);
		this.readOffDefaultChose = new JComboBox<Boolean>(BooleanLib.OBJECT_REVERSED);
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
				KeyIterator.permuteIntegerOrderedKey(task::onPostIteration, length);
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			for(String word : Dictionary.WORDS) {
				Integer[] order = new Integer[word.length()];
				
				int p = 0;
				for(char ch = 'A'; ch <= 'Z'; ++ch)
					for(int i = 0; i < order.length; i++)
						if(ch == word.charAt(i))
							order[i] = p++;
				task.onPostIteration(order);
			}
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		else if(method == DecryptionMethod.CALCULATED) {
			//KeyIterator.iterateIntegerArray(task::onList, 6, task.period1, false);
		}
		
		app.out().println(task.getBestSolution());
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
