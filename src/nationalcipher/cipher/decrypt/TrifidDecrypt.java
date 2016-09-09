package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.dict.Dictionary;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.Trifid;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator.BifidKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SimulatedAnnealing;
import nationalcipher.cipher.tools.SubOptionPanel;

public class TrifidDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Trifid";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.DICTIONARY);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		TrifidTask task = new TrifidTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
			
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			progress.addMaxValue(Dictionary.words.size());
			
			for(String word : Dictionary.words) {
				String change = "";
				for(char i : word.toCharArray()) {
					if(i != 'J' && !change.contains("" + i))
						change += i;
				}
				String regex = new String[]{"ABCDEFGHIKLMNOPQRSTUVWXYZ", "NOPQRSTUVWXYZABCDEFGHIKLM", "ZYXWVUTSRQPONMLKIHGFEDCBA"}[settings.keywordCreation];
				
				for(char i : regex.toCharArray()) {
					if(!change.contains("" + i))
						change += i;
				}
				
	
				task.onIteration(change);
			}
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	private JTextField rangeBox = new JTextField("5");
	public JComboBox<Character> comboBox = new JComboBox<Character>(new Character[] {'#', '.', '*', '@', '_'});
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {

		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
			
		panel.add(new SubOptionPanel("Period:", this.rangeBox));

		panel.add(new SubOptionPanel("27th Character: ", this.comboBox));
	}
	
	public class TrifidTask extends SimulatedAnnealing implements BifidKey {

		public int period;
		public String bestKey = "", bestMaximaKey = "", lastKey = "";
		
		public TrifidTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.period = SettingParse.getInteger(rangeBox);
		}

		@Override
		public void onIteration(String keysquare) {
			this.lastSolution = new Solution(Trifid.decode(this.text, keysquare, this.period), this.settings.getLanguage()).setKeyString(keysquare);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				UINew.BEST_SOULTION = this.bestSolution.getText();
			}
			
			this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandTrifidKey((char)comboBox.getSelectedItem());
			return new Solution(Trifid.decode(this.text, this.bestMaximaKey, this.period), this.settings.getLanguage()).setKeyString(this.bestMaximaKey);
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(Trifid.decode(this.text, this.lastKey, this.period), this.settings.getLanguage()).setKeyString(this.lastKey);
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestKey);
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.progress.setValue(0);
			return false;
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}
