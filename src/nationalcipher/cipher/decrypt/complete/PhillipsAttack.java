package nationalcipher.cipher.decrypt.complete;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.dict.Dictionary;
import nationalcipher.cipher.base.transposition.Phillips;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DictionaryAttack;
import nationalcipher.cipher.decrypt.methods.KeyIterator.Long25Key;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class PhillipsAttack extends CipherAttack {

	private JComboBox<Boolean> orderRowsSelection;
	private JComboBox<Boolean> orderColumnsSelection;
	
	public PhillipsAttack() {
		super("Phillips");
		this.setAttackMethods(DecryptionMethod.DICTIONARY, DecryptionMethod.SIMULATED_ANNEALING);
		this.orderRowsSelection = new JComboBox<Boolean>(new Boolean[] {true, false});
		this.orderColumnsSelection = new JComboBox<Boolean>(new Boolean[] {true, false});
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Move rows?", this.orderRowsSelection));
		panel.add(new SubOptionPanel("Move columns?", this.orderColumnsSelection));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		PhillipsTask task = new PhillipsTask(text, app);
		
		//Settings grab
		task.orderRows = SettingParse.getBooleanValue(this.orderRowsSelection);
		task.orderColumns = SettingParse.getBooleanValue(this.orderColumnsSelection);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			for(String word : Dictionary.words)
				task.onIteration(DictionaryAttack.createLong26Key(word, app.getSettings().getKeywordFiller(), 'J'));
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class PhillipsTask extends SimulatedAnnealing implements Long25Key {

		public boolean orderRows;
		public boolean orderColumns;
		public String bestKey, bestMaximaKey, lastKey;
		
		public PhillipsTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Phillips.decode(this.cipherText, key, this.orderRows, this.orderColumns), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s, r: %b, c: %b", key, this.orderRows, this.orderColumns);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(Phillips.decode(this.cipherText, this.bestMaximaKey, this.orderRows, this.orderColumns), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(Phillips.decode(this.cipherText, this.lastKey, this.orderRows, this.orderColumns), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString("%s, r: %b, c: %b", this.bestKey, this.orderRows, this.orderColumns);
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
