package nationalcipher.cipher.tools;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SubOptionPanel extends JPanel {
	
	public SubOptionPanel(String label, JComponent... fields) {
		this(new JLabel(label), fields);
	}
	
	public SubOptionPanel(JLabel label, JComponent... fields) {
		this(label, 400, fields);
	}
	
	public SubOptionPanel(String label, int width, JComponent... fields) {
		this(new JLabel(label), width, fields);
	}
	
	public SubOptionPanel(JLabel label, int width, JComponent... fields) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		label.setMinimumSize(new Dimension(190, 20));
		label.setPreferredSize(new Dimension(190, 20));
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		//field.setMinimumSize(new Dimension(width - 200 - 30, 20));
	
		
		this.add(label);
		for(JComponent field : fields) {
			field.setPreferredSize(new Dimension((width  - 200) / fields.length - 30, 20));
			this.add(field);
		}
	}

	@Override
	public SubOptionPanel add(Component comp) {
		super.add(comp);
		return this;
	}
}
