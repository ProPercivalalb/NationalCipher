package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.transposition.Columnar;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.IntegerOrderedKey;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class ColumnarTranspositionAttack extends CipherAttack {

	private JSpinner[] rangeSpinner;
	private JComboBox<Boolean> readOffDefaultChose;
	
	public ColumnarTranspositionAttack() {
		super("Columnar Transposition");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 100, 1);
		this.readOffDefaultChose = new JComboBox<Boolean>(new Boolean[] {true, false});
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
		panel.add(new SubOptionPanel("Read down columns (T) or across rows (F)? ", this.readOffDefaultChose));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		ColumnarTranspositionTask task = new ColumnarTranspositionTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		task.readOffDefault = SettingParse.getBooleanValue(this.readOffDefaultChose);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.factorialBig(length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateIntegerOrderedKey(task, length);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class ColumnarTranspositionTask extends InternalDecryption implements IntegerOrderedKey {

		public boolean readOffDefault;
		
		public ColumnarTranspositionTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(int[] order) {
			this.lastSolution = new Solution(Columnar.decode(this.cipherText, order, this.readOffDefault), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s, d: %b", Arrays.toString(order), this.readOffDefault);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
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
