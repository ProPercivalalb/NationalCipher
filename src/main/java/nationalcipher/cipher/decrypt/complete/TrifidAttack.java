package nationalcipher.cipher.decrypt.complete;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.other.Trifid;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.NationalCipherUI;

public class TrifidAttack extends CipherAttack {

	private TrifidTask task;
	public JSpinner spinner;
	public JComboBox<Character> extraCharacter;
	
	public TrifidAttack() {
		super("Trifid");
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
		this.spinner = JSpinnerUtil.createSpinner(ArrayUtil.concat(new Integer[] {0}, ArrayUtil.createRangeInteger(2, 101)));
		this.extraCharacter = new JComboBox<Character>(new Character[] {'#', '.', '*', '@', '_'});
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period:", this.spinner));
		panel.add(new SubOptionPanel("27th Character: (NYI)", this.extraCharacter));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		this.task = new TrifidTask(text, app);
		
		//Settings grab
		this.task.period = SettingParse.getInteger(this.spinner);
		this.task.extraChar = (char)this.extraCharacter.getSelectedItem();
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			this.task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class TrifidTask extends SimulatedAnnealing {

		public byte[] numberText;
		public int period;
		public char extraChar;
		public String bestKey, bestMaximaKey, lastKey;
		
		public TrifidTask(String text, IApplication app) {
			super(text.toCharArray(), app);
			this.numberText = new byte[text.length() * 3];
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeyGeneration.createLongKey27();
			return new Solution(Trifid.decode(this.cipherText, this.plainText, this.numberText, this.bestMaximaKey, this.period), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeyManipulation.modifyKey(this.bestMaximaKey, 3, 9);
			return new Solution(Trifid.decode(this.cipherText, this.plainText, this.numberText, this.lastKey, this.period), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString("%s, p:%d", this.bestKey, this.period);
			this.bestSolution.bakeSolution();
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
			NationalCipherUI.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
	}
	
	@Override
	public void onTermination(boolean forced) {
		if(forced)
			this.task.app.out().println("%s", this.task.bestSolution);
	}
}
