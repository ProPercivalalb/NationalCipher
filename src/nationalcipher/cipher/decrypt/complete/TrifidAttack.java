package nationalcipher.cipher.decrypt.complete;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.other.Trifid;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class TrifidAttack extends CipherAttack {

	public JSpinner spinner;
	public JComboBox<Character> extraCharacter;
	
	public TrifidAttack() {
		super("Trifid");
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
		this.spinner = JSpinnerUtil.createSpinner(ArrayUtil.concat(new Integer[] {0}, ArrayUtil.rangeInt(2, 101)));
		this.extraCharacter = new JComboBox<Character>(new Character[] {'#', '.', '*', '@', '_'});
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period:", this.spinner));
		panel.add(new SubOptionPanel("27th Character:", this.extraCharacter));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		TrifidTask task = new TrifidTask(text, app);
		
		//Settings grab
		task.period = SettingParse.getInteger(this.spinner);
		task.extraChar = (char)this.extraCharacter.getSelectedItem();
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class TrifidTask extends SimulatedAnnealing {

		public int period;
		public char extraChar;
		public String bestKey, bestMaximaKey, lastKey;
		
		public TrifidTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandTrifidKey(this.extraChar);
			return new Solution(Trifid.decode(this.cipherText, this.bestMaximaKey, this.period), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeySquareManipulation.exchange2letters(this.bestMaximaKey);
			return new Solution(Trifid.decode(this.cipherText, this.lastKey, this.period), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString("%s, p:%d", this.bestKey, this.period);
			this.getKeyPanel().updateSolution(this.bestSolution);
		}
		
		@Override
		public void onIteration() {
			this.getProgress().increase();
			this.getKeyPanel().updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.out().println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
	}
}
