package nationalcipher.cipher.tools;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;

public class SettingParse {

	public static int[] getIntegerRange(JTextComponent textComponent) {
		int[] range = new int[2];
		
		String text = textComponent.getText().replaceAll("[^-0-9]", "");
		
		if(!text.contains("-")) {
			range[0] = Integer.valueOf(text);
			range[1] = Integer.valueOf(text);
		}
		else {
			range[0] = Integer.valueOf(text.split("-")[0]);
			range[1] = Integer.valueOf(text.split("-")[1]);
		}
		
		return range;
	}

	public static int getInteger(JTextComponent textComponent) {
		String text = textComponent.getText().replaceAll("[^0-9]", "");
	
		return Integer.valueOf(text);

	}

	public static int[] getIntegerRange(JSpinner[] spinners) {
		return new int[] {((Number)spinners[0].getValue()).intValue(), ((Number)spinners[1].getValue()).intValue()};
	}
	
	public static int getInteger(JSpinner spinners) {
		return (int)spinners.getValue();
	}
	
	public static boolean getBooleanValue(JComboBox<Boolean> comboBox) {
		return (Boolean)comboBox.getSelectedItem();
	}
}
