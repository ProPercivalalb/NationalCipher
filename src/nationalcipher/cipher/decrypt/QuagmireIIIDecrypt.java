package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.string.StringTransformer;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.base.Quagmire;
import nationalcipher.cipher.decrypt.complete.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;

public class QuagmireIIIDecrypt implements IDecrypt {

	private QuagmireTask task;
	
	@Override
	public String getName() {
		return "Quagmire III";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		this.task = new QuagmireTask(text.toCharArray(), settings, keyPanel, output, progress);
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			this.task.run();
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	

	private JTextField rangeBox = new JTextField("5");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		
		panel.add(new SubOptionPanel("Period:", this.rangeBox));
        
		dialog.add(panel);
	}
	
	public class QuagmireTask extends SimulatedAnnealing  {

		public int period;
		public String bestKey1, bestMaximaKey1, lastKey1;
		
		public QuagmireTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.period = SettingParse.getInteger(rangeBox);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = KeyGeneration.createLongKey26();
			this.lastKey1 = this.bestMaximaKey1;
			return new Solution(decode(this.bestMaximaKey1), this.settings.getLanguage());
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey1 = KeySquareManipulation.modifyKey(this.bestMaximaKey1);
			
			return new Solution(decode(this.lastKey1), this.settings.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestKey1);
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.bestSolution.setKeyString("%s", this.lastKey1);
			this.output.println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.progress.setValue(0);
			return false;
		}
		
		public char[] decode(String key) {
			String indicatorKey = "";
			int[] keyIndex = new int[26];
			for(int i = 0; i < 26; i++)
				keyIndex[key.charAt(i) - 'A'] = i;
			
	        for(int i = 0; i < this.period; ++i) {
	        	String temp = StringTransformer.getEveryNthChar(this.cipherText, i, this.period);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), key, keyIndex, this.settings.getLanguage(), this.progress);
	            indicatorKey += key.charAt(shift);
	        }
			
	        return Quagmire.decode(this.cipherText, key, key, indicatorKey, 'A');
		}
		
		public int findBestCaesarShift(char[] text, String keyTop, int[] keyIndex, ILanguage language, ProgressValue progressBar) {
			int best = 0;
		    double smallestSum = Double.MAX_VALUE;
		    for(int shift = 0; shift < 26; ++shift) {
		    	char[] encodedText = decode(text, keyTop, keyIndex, shift);
		        double currentSum = ChiSquared.calculate(encodedText, language);
		    
		        if(currentSum < smallestSum) {
		        	best = shift;
		            smallestSum = currentSum;
		        }
		  
		    }
		    return best;
		}
		
		public char[] decode(char[] cipherText, String keyTop, int[] keyIndex, int shift) {
			char[] plainText = new char[cipherText.length];
			
			for(int i = 0; i < cipherText.length; i++)
				plainText[i] = keyTop.charAt((26 + keyIndex[cipherText[i] - 'A'] - shift) % 26);
			
			return plainText;
		}
	}

	@Override
	public void onTermination() {
		this.task.endIteration();
		
	}
}
