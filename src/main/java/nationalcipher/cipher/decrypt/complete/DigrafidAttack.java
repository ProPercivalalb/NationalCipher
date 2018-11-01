package nationalcipher.cipher.decrypt.complete;

import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.other.Digrafid;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class DigrafidAttack extends CipherAttack {

	private DigrafidTask task;
	public JSpinner spinner;
	
	public DigrafidAttack() {
		super("Digrafid");
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
		this.spinner = JSpinnerUtil.createSpinner(ArrayUtil.concat(new Integer[] {0}, ArrayUtil.createRangeInteger(2, 101)));
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Fractional (Period / 2):", this.spinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		this.task = new DigrafidTask(text, app);
		
		//Settings grab
		this.task.period = SettingParse.getInteger(this.spinner);
		
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			this.task.run();
		}
		
		app.out().println(this.task.getBestSolution());
	}
	
	public class DigrafidTask extends SimulatedAnnealing {

		public byte[] numberText;
		public int period;
		public String bestKey1, bestMaximaKey1, lastKey1;
		public String bestKey2, bestMaximaKey2, lastKey2;
		
		public DigrafidTask(String text, IApplication app) {
			super(text.toCharArray(), app);
			this.numberText = new byte[text.length() * 3 / 2];
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeyGeneration.createLongKey27();
			this.bestMaximaKey2 = KeyGeneration.createLongKey27();
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			return new Solution(Digrafid.decode(this.cipherText, this.plainText, this.numberText, this.bestMaximaKey1, this.bestMaximaKey2, this.period), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			if(count % 2 == 0)
				this.lastKey1 = KeyManipulation.modifyKey(this.bestMaximaKey1, 9, 3);
			else
				this.lastKey2 = KeyManipulation.modifyKey(this.bestMaximaKey2, 3, 9);
			
			return new Solution(Digrafid.decode(this.cipherText, this.plainText, this.numberText, this.lastKey1, this.lastKey2, this.period), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
			this.bestMaximaKey2 = this.lastKey2;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.bestKey2 = this.bestMaximaKey2;
			this.bestSolution.setKeyString("%s %s p:%d", this.bestKey1, this.bestKey2, this.period);
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
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
	}
	
	@Override
	public void onTermination(boolean forced) {
		if(forced)
			this.task.app.out().println("%s", this.task.bestSolution);
	}
	
	@Override
	public void writeTo(Map<String, Object> map) {
		//map.put("period", this.spinner.getValue());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		//this.spinner.setValue(SettingsUtil.getSetting("period", map, Integer.TYPE, 2));
	}
}
