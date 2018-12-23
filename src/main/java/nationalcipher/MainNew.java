package nationalcipher;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import nationalcipher.ui.UINew;

public class MainNew {
	
	public static UINew ui;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } 
                catch(Exception ex) {
                    Logger.getLogger(MainNew.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                ui = new UINew();
                ui.setVisible(true);
            }
        });
	}
	
	
}