package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.math.MathHelper;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.UINew;
import nationalcipher.cipher.AMSCO;
import nationalcipher.cipher.Columnar;
import nationalcipher.cipher.ColumnarRow;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.InternalDecryption;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SimulatedAnnealing;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.cipher.tools.Creator.AMSCOKey;

public class AMSCODecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "AMSCO";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		AMSCOTask task = new AMSCOTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		int[] range = SettingParse.getIntegerRange(this.rangeBox);
		int minLength = range[0];
		int maxLength = range[1];
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
	
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				progress.addMaxValue(MathHelper.factorialBig(keyLength));
			
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				Creator.iterateAMSCO(task, keyLength);

			
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	private JTextField rangeBox = new JTextField("2-8");
	private JTextField rangeBox2 = new JTextField("5");
	private JComboBox<Boolean> directionOption = new JComboBox<Boolean>(new Boolean[] {true, false});
	//private JComboBox<String> directionOption = new JComboBox<String>(new String[] {"Columns", "Rows"});
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
 
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		((AbstractDocument)this.rangeBox2.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());

		

		panel.add(new SubOptionPanel("BF length range: ", this.rangeBox));
		panel.add(new SubOptionPanel("SA length: ", this.rangeBox2));
		panel.add(new SubOptionPanel("Double char first? ", this.directionOption));
		
		dialog.add(panel);
	}
	
	public class AMSCOTask extends SimulatedAnnealing implements AMSCOKey {

		public int[] bestKey1, bestMaximaKey1, lastKey1;
		public int length;
		public boolean first;
		
		public AMSCOTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.length = SettingParse.getInteger(rangeBox2);
			this.first = (Boolean)directionOption.getSelectedItem();
		}
			
		@Override
		public void onIteration(int[] order) {
			this.lastSolution = new Solution(AMSCO.decode(this.text, this.first, order), this.settings.getLanguage()).setKeyString(Arrays.toString(order));
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			progress.increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeyGeneration.createOrder(this.length);
			return new Solution(AMSCO.decode(this.text, this.first, this.bestMaximaKey1), this.settings.getLanguage()).setKeyString(Arrays.toString(this.bestMaximaKey1));
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey1 = KeySquareManipulation.modifyOrder(this.bestMaximaKey1);

			return new Solution(AMSCO.decode(this.text, this.first, this.lastKey1), this.settings.getLanguage()).setKeyString(Arrays.toString(this.lastKey1));
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestSolution.keyString);
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.iterations.setText("" + this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.text;
			this.progress.setValue(0);
			return false;
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}

}
