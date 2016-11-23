package nationalcipher.cipher.decrypt.complete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javalibrary.math.MathUtil;
import nationalcipher.SettingsUtil;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.Autokey;
import nationalcipher.cipher.decrypt.AutokeyAttack;
import nationalcipher.cipher.tools.SubOptionPanel;

//TODO Quite often can produce result that has better score but is not real answer
public class PortaAKAttack extends AutokeyAttack {

	public JComboBox<Boolean> directionOption;
	
	public PortaAKAttack() {
		super("Porta Autokey");
		this.directionOption = new JComboBox<Boolean>(new Boolean[] {true, false});
	}

	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		super.createSettingsUI(dialog, panel);
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
	public byte[] decode(char[] cipherText, byte[] plainText, String key) {
		return Autokey.decode(cipherText, plainText, key, VigenereType.PORTA);//TODO, SettingParse.getBooleanValue(this.directionOption));
	}
	
	@Override
	public int alphaIncrease() { 
		return 2; 
	}
	
	@Override
	public String keyAlphabet() { 
		return "ACEGIKMOQSUWY"; 
	}
	
	@Override
	public void writeTo(Map<String, Object> map) {
		super.writeTo(map);
		map.put("direction", this.directionOption.getSelectedItem());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		super.readFrom(map);
		this.directionOption.setSelectedItem(SettingsUtil.getSetting("direction", map, Boolean.TYPE, true));
	}
}
