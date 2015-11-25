package nationalcipher.cipher.tools;

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
}
