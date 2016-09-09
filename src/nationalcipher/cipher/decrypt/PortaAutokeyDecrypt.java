package nationalcipher.cipher.decrypt;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.math.MathHelper;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.PortaAutokey;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.PortaAutoKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.cipher.tools.KeySearch;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;

public class PortaAutokeyDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Porta Autokey";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		PortaAutoKeyTask task = new PortaAutoKeyTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		int[] range = SettingParse.getIntegerRange(this.rangeBox);
		int minLength = range[0];
		int maxLength = range[1];
		
		if(method == DecryptionMethod.BRUTE_FORCE) {

			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int length = minLength; length <= maxLength; ++length)
				progress.addMaxValue(TWENTY_SIX.pow(length));
			
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				Creator.iteratePortaAutokey(task, keyLength);
			
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {

			progress.setIndeterminate(true);
			task.run(minLength, maxLength);
			
			output.println(task.getBestSolution());
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}

	private JTextField rangeBox = new JTextField("2-25");
	private JComboBox<Boolean> directionOption = new JComboBox<Boolean>(new Boolean[] {true, false});
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		dialog.setMinimumSize(new Dimension(400, 400));

        JLabel range = new JLabel("Keyword length range: ");
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		JLabel direction = new JLabel("Key created moving right? ");
		
		panel.add(new SubOptionPanel(range, this.rangeBox));
		panel.add(new SubOptionPanel(direction, this.directionOption));
		
		final JTextArea area = new JTextArea();
		area.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Key Tabular"));
		area.setEditable(false);
		area.setFocusable(false);
		boolean shiftRight = (Boolean)directionOption.getSelectedItem();
		String text = "     A|B|C|D|E|F|G|H|I|J|K|L|M";
		for(int i = 0; i < 13; i++) {
			text += "\n" + (char)(i * 2 + 'A') + "-"  +  (char)(i * 2 + 1 + 'A') + "  ";
			for(int j = 0; j < 13; j++)
				text += (char)(MathHelper.mod(j + (shiftRight ? -1 : 1) * i, 13) + 'N') + "|";
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
						text += (char)(MathHelper.mod(j + (shiftRight ? -1 : 1) * i, 13) + 'N') + "|";
				}
				area.setText(text);
			}
		});
		panel.add(area);
	}
	
	public class PortaAutoKeyTask extends KeySearch implements PortaAutoKey {
		
		public boolean shiftRight;
		
		public PortaAutoKeyTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.shiftRight = (Boolean)directionOption.getSelectedItem();
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(PortaAutokey.decode(this.text, key, this.shiftRight), this.settings.getLanguage()).setKeyString(key);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}

		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(PortaAutokey.decode(this.text, key, this.shiftRight), this.settings.getLanguage()).setKeyString(key);
		}

		@Override
		public void solutionFound() {
			this.output.println("%s", this.bestSolution);
			this.keyPanel.updateSolution(this.bestSolution);
		}

		@Override
		public void onIteration() {
			this.keyPanel.updateIteration(this.iteration++);
		}
		
		@Override
		public int alphaIncrease() {
			return 2;
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}
