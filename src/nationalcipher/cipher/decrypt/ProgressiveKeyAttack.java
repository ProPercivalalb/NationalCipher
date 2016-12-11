package nationalcipher.cipher.decrypt;

import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.SettingsUtil;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeySearch;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public abstract class ProgressiveKeyAttack extends CipherAttack {

	public JSpinner[] rangeSpinner1;
	public JSpinner[] rangeSpinner2;
	public JSpinner[] rangeSpinner3;
	
	public ProgressiveKeyAttack(String displayName) {
		super(displayName);
		this.setAttackMethods(DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner1 = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
		this.rangeSpinner2 = JSpinnerUtil.createRangeSpinners(1, 30, 1, 100, 1);
		this.rangeSpinner3 = JSpinnerUtil.createRangeSpinners(1, 25, 1, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner1));
		panel.add(new SubOptionPanel("Prog Period:", this.rangeSpinner2));
		panel.add(new SubOptionPanel("Prog Key:", this.rangeSpinner3));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		ProgressiveKeyTask task = new ProgressiveKeyTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner1);
		int[] progPeriodRange = SettingParse.getIntegerRange(this.rangeSpinner2);
		int[] progKeyRange = SettingParse.getIntegerRange(this.rangeSpinner3);
		
		if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().setIndeterminate(true);
			for(int i = progPeriodRange[0]; i <= progPeriodRange[1]; i++) {
				for(int j = progKeyRange[0]; j <= progKeyRange[1]; j++) {
					task.progPeriod = i;
					task.progKey = j;

					task.run(periodRange[0], periodRange[1]);
				}
			}
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class ProgressiveKeyTask extends KeySearch {

		public int progPeriod;
		public int progKey;
		
		public ProgressiveKeyTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(ProgressiveKeyAttack.this.decode(this.cipherText, this.plainText, key, this.progPeriod, this.progKey), this.getLanguage()).setKeyString("%s, pp: %d, pk: %d", key, this.progPeriod, this.progKey).bakeSolution();
		}
		
		@Override
		public int alphaIncrease() {
			return ProgressiveKeyAttack.this.alphaIncrease();
		}
	}
	
	@Override
	public void writeTo(Map<String, Object> map) {
		map.put("period_min", this.rangeSpinner1[0].getValue());
		map.put("period_max", this.rangeSpinner1[1].getValue());
		
		map.put("prog_period_min", this.rangeSpinner2[0].getValue());
		map.put("prog_period_max", this.rangeSpinner2[1].getValue());
		
		map.put("prog_key_min", this.rangeSpinner3[0].getValue());
		map.put("prog_key_max", this.rangeSpinner3[1].getValue());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		this.rangeSpinner1[0].setValue(SettingsUtil.getSetting("period_min", map, Integer.TYPE, 2));
		this.rangeSpinner1[1].setValue(SettingsUtil.getSetting("period_max", map, Integer.TYPE, 15));
		
		this.rangeSpinner2[0].setValue(SettingsUtil.getSetting("prog_period_min", map, Integer.TYPE, 1));
		this.rangeSpinner2[1].setValue(SettingsUtil.getSetting("prog_period_max", map, Integer.TYPE, 30));
		
		this.rangeSpinner3[0].setValue(SettingsUtil.getSetting("prog_key_min", map, Integer.TYPE, 1));
		this.rangeSpinner3[1].setValue(SettingsUtil.getSetting("prog_key_max", map, Integer.TYPE, 30));
	}
	
	public abstract byte[] decode(char[] cipherText, byte[] plainText, String key, int progPeriod, int progKey);
	
	public int alphaIncrease() { return 1; }
}
