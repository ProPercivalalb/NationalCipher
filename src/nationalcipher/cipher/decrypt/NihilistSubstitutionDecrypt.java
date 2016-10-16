package nationalcipher.cipher.decrypt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.NihilistSubstitution;
import nationalcipher.cipher.decrypt.complete.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;

public class NihilistSubstitutionDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Nihilist Substitution";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		NihilistSubstitutionTask task = new NihilistSubstitutionTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		char[] textChar = text.toCharArray();
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
	
			int period = SettingParse.getInteger(this.rangeBox);
			
			List<Integer> list = new ArrayList<Integer>();
			for(int i = 0; i < textChar.length / 2; i++)
				list.add(Integer.valueOf(new String(textChar, i * 2, 2)));
			
			char[] tempText = new char[textChar.length / 2];
			
			boolean error = false;
			
			for(int i = 0; i < period; i++) {
				List<Integer> old = getEveryNthChar(list, i, period);
				List<Integer> key = getBestKey(old, i);
				if(key.size() == 2) {
					output.println("PeriodColumn %d -- %d%d", i, key.get(0), key.get(1));
					
					
					int count = 0;
					for(int no : old) {
						if(no <= 10) no += 100;
						int newInt = no -= (key.get(0) * 10 + key.get(1));
						
						int alphaInt = ((int)(newInt / 10) - 1) * 5 + ((newInt % 10) - 1);
						tempText[count * period + i] = "ABCDEFGHIKLMNOPQRSTUVWXYZ".charAt(alphaInt);
						count += 1;
					}
				}
				else {
					if(key.get(0) == key.get(1))
						output.println("PeriodColumn %d -- Row: %d Column: %d-%d", i, key.get(0), key.get(2), key.get(3));
					else if(key.get(2) == key.get(3))
						output.println("PeriodColumn %d -- Row: %d-%d Column: %d", i, key.get(0), key.get(1), key.get(2));
					else 
						output.println("PeriodColumn %d -- Row: %d-%d Column: %d-%d", i, key.get(0), key.get(1), key.get(2), key.get(3));
					
					error = true;
				}
				

			}
			if(!error)
				new SubstitutionDecrypt().attemptDecrypt(new String(tempText), settings, method, output, keyPanel, progress);
			else {
				output.println("Cannot complete decryption");
			}
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	public static List<Integer> getEveryNthChar(List<Integer> old, int start, int n) {
		List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < old.size(); ++i) {
            if((i % n) == start) {
            	list.add(old.get(i));
            }
        }
        return list;
    }
	
	public List<Integer> getBestKey(List<Integer> nos, int periodColumn) {
		int rowMin = 1;
		int rowMax = 5;
		int colMin = 1;
		int colMax = 5;
		
		for(int no : nos) {
			if(no <= 10)
				no += 100;
			
			int col = no % 10;
			if(col == 0) {
				colMin = 5;
				colMax = 5;
				no -= 10;
			}
			else if(col - 1 <= 5) {
				colMin = Math.max(1, colMin);
				colMax = Math.min(col - 1, Math.max(colMin, colMax));
			}
			else {
				colMin = Math.max(col - 5, colMin);
				colMax = Math.min(5, Math.max(colMin, colMax));
			}
	
			
			int row = (int)(no / 10) % 10;
			if(row == 0) {
				rowMin = 5;
				rowMax = 5;
			}
			else if(row - 1 <= 5) {
				rowMin = Math.max(1, rowMin);
				rowMax = Math.min(row - 1, Math.max(rowMin, rowMax));
			}
			else {
				rowMin = Math.max(row - 5, rowMin);
				rowMax = Math.min(5, Math.max(rowMin, rowMax));
			}

		}

		if(rowMin == rowMax && colMin == colMax)
			return Arrays.asList(rowMin, colMin);
		
		else
			return Arrays.asList(rowMin,rowMax, colMin, colMax);
	}
	
	private JTextField rangeBox = new JTextField("5");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {

		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
			
		panel.add(new SubOptionPanel("Period:", this.rangeBox));
	}
	
	public class NihilistSubstitutionTask extends SimulatedAnnealing {

		public String bestKey = "", bestMaximaKey = "", lastKey = "";
		
		public NihilistSubstitutionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(NihilistSubstitution.decode(this.cipherText, this.bestMaximaKey, "EASY"), this.settings.getLanguage()).setKeyString(this.bestMaximaKey);
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(NihilistSubstitution.decode(this.cipherText, this.lastKey, "EASY"), this.settings.getLanguage()).setKeyString(this.lastKey);
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
