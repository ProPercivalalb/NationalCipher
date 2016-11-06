package nationalcipher.ui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nationalcipher.Settings;
import nationalcipher.cipher.decrypt.methods.Solution;

public class KeyPanel extends JPanel {
	
	public Settings settings;
	public JTextField fitness;
	public JTextField key;
	private JTextField iterations;
	
	public KeyPanel(Settings settings) {
		this.settings = settings;
		this.fitness = new JTextField();
		this.key = new JTextField();
		this.iterations = new JTextField();
		this.fitness.setEditable(false);
		this.key.setEditable(false);
		this.iterations.setEditable(false);
		this.iterations.setFocusable(false);
		this.fitness.setMaximumSize(new Dimension(150, Integer.MAX_VALUE));
		this.key.setMaximumSize(new Dimension(290, Integer.MAX_VALUE));
		this.iterations.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
		this.add(new JLabel("Fitness "));
		this.add(this.fitness);
		this.add(new JLabel(" Key "));
		this.add(this.key);
		this.add(new JLabel(" Iteration "));
		this.add(this.iterations);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
	}
	
	public void updateIteration(int n) {
		if(settings.updateProgress())
			this.iterations.setText(String.valueOf(n));
	}
	
	public void setIterationUnsed() {
		if(!settings.updateProgress())
			this.iterations.setText("-------------");
		else
			this.iterations.setText("");
	}

	public void updateSolution(Solution solution) {
		this.fitness.setText("" + solution.score);
		this.key.setText(solution.keyString);
		UINew.BEST_SOULTION = solution.getText();
	}
}
