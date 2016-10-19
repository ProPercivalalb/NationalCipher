package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.transposition.AMSCO;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.IntegerOrderedKey;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class AMSCOAttack extends CipherAttack {

	private JSpinner[] rangeSpinner;
	private JComboBox<Boolean> doubleLetterChose;
	
	public AMSCOAttack() {
		super("AMSCO");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 8, 2, 100, 1);
		this.doubleLetterChose = new JComboBox<Boolean>(new Boolean[] {true, false});
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
		panel.add(new SubOptionPanel("Double char first? ", this.doubleLetterChose));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		AMSCOTask task = new AMSCOTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		task.doubleLetterFirst = SettingParse.getBooleanValue(this.doubleLetterChose);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.factorialBig(length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateIntegerOrderedKey(task, length);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class AMSCOTask extends InternalDecryption implements IntegerOrderedKey {

		public boolean doubleLetterFirst;
		
		public AMSCOTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(int[] order) {
			this.lastSolution = new Solution(AMSCO.decode(this.cipherText, this.plainText, order, this.doubleLetterFirst), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(Arrays.toString(order));
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}

	@Override
	public void write(HashMap<String, Object> map) {
		map.put("amsco_period_range_min", this.rangeSpinner[0].getValue());
		map.put("amsco_period_range_max", this.rangeSpinner[1].getValue());
		map.put("amsco_double_letter_first", this.doubleLetterChose.getSelectedItem());
	}

	@Override
	public void read(HashMap<String, Object> map) {
		if(map.containsKey("amsco_period_range_min"))
			this.rangeSpinner[0].setValue(map.get("amsco_period_range_min"));
		if(map.containsKey("amsco_period_range_max"))
			this.rangeSpinner[1].setValue(map.get("amsco_period_range_max"));
		if(map.containsKey("amsco_double_letter_first"))
			this.doubleLetterChose.setSelectedItem(map.get("double_letter_first"));
	}
}
