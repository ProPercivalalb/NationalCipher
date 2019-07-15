package nationalcipher.ui;

import java.awt.Dimension;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nationalcipher.Settings;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.util.CipherUtils;

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
        this.add(new JLabel("Fitness"));
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.fitness);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(new JLabel("Key"));
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.key);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(new JLabel("Iteration"));
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.add(this.iterations);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }
    
    public void setIteration(BigInteger count) {
        this.iterations.setText(CipherUtils.formatBigInteger(count));
    }

    public void setIterationUnsed() {
        if (!this.settings.updateProgress())
            this.iterations.setEnabled(false);
        else
            this.iterations.setEnabled(true);
    }

    public void updateSolution(Solution solution) {
        this.fitness.setText(String.valueOf(solution.score));
        this.key.setText(solution.keyString);
        NationalCipherUI.BEST_SOULTION = solution.getText();
    }
}
