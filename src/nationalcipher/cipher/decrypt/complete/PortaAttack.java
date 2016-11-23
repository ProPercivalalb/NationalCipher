package nationalcipher.cipher.decrypt.complete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;

import javalibrary.math.MathUtil;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.VigenereFamily;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.ShortCustomKey;
import nationalcipher.cipher.decrypt.methods.KeySearch;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class PortaAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	public JComboBox<Boolean> directionOption;
	
	public PortaAttack() {
		super("Porta");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
		this.directionOption = new JComboBox<Boolean>(new Boolean[] {true, false});
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
		panel.add(new SubOptionPanel("Key created moving right?", this.directionOption));
		
		final JTextArea area = new JTextArea();
		area.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Key Tabular"));
		area.setEditable(false);
		area.setFocusable(false);
		boolean shiftRight = (Boolean)directionOption.getSelectedItem();
		String text = "     A|B|C|D|E|F|G|H|I|J|K|L|M";
		for(int i = 0; i < 13; i++) {
			text += "\n" + (char)(i * 2 + 'A') + "-"  +  (char)(i * 2 + 1 + 'A') + "  ";
			for(int j = 0; j < 13; j++)
				text += (char)(MathUtil.mod(j + (shiftRight ? -1 : 1) * i, 13) + 'N') + "|";
		}
		area.setText(text);
		
		this.directionOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				boolean shiftRight = (Boolean)directionOption.getSelectedItem();
				String text = "     A|B|C|D|E|F|G|H|I|J|K|L|M";
				for(int i = 0; i < 13; i++) {
					text += "\n" + (char)(i * 2 + 'A') + "-"  +  (char)(i * 2 + 1 + 'A') + "  ";
					for(int j = 0; j < 13; j++)
						text += (char)(MathUtil.mod(j + (shiftRight ? -1 : 1) * i, 13) + 'N') + "|";
				}
				area.setText(text);
			}
		});
		panel.add(area);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		PortaTask task = new PortaTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		task.shiftRight = SettingParse.getBooleanValue(this.directionOption);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(MathUtil.pow(13, length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateShortCustomKey(task, "ACEGIKMOQSUWY", length, true);
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().setIndeterminate(true);
			task.run(periodRange[0], periodRange[1]);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class PortaTask extends KeySearch implements ShortCustomKey {

		public boolean shiftRight;
		
		public PortaTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(VigenereFamily.decode(this.cipherText, this.plainText, key, VigenereType.PORTA), this.getLanguage());
			
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
			return new Solution(VigenereFamily.decode(this.cipherText, this.plainText, key, VigenereType.PORTA), this.getLanguage()).setKeyString(key).bakeSolution();
		}
		
		@Override
		public int alphaIncrease() {
			return 2;
		}
	}
	
	@Override
	public void writeTo(Map<String, Object> map) {
		map.put("porta_period_range_min", this.rangeSpinner[0].getValue());
		map.put("porta_period_range_max", this.rangeSpinner[1].getValue());
		map.put("porta_shift_right", this.directionOption.getSelectedItem());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		if(map.containsKey("porta_period_range_max"))
			this.rangeSpinner[1].setValue(map.get("porta_period_range_max"));
		if(map.containsKey("porta_period_range_min"))
			this.rangeSpinner[0].setValue(map.get("porta_period_range_min"));
		if(map.containsKey("porta_shift_right"))
			this.directionOption.setSelectedItem(map.get("porta_shift_right"));
	}
}
