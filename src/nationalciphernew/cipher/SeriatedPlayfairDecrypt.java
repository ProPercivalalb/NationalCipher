package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.cipher.SeriatedPlayfair;
import javalibrary.cipher.wip.KeySquareManipulation;
import javalibrary.dict.Dictionary;
import javalibrary.swing.ProgressValue;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.manage.Creator;
import nationalciphernew.cipher.manage.Creator.PlayfairKey;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.SimulatedAnnealing;
import nationalciphernew.cipher.manage.Solution;

public class SeriatedPlayfairDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Seriated Playfair";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.DICTIONARY);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		PlayfairTask task = new PlayfairTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			Creator.iteratePlayfair(task);
			
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
			
		}
		else if(method == DecryptionMethod.CALCULATED) {
			
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
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public static class PlayfairTask extends SimulatedAnnealing implements PlayfairKey {

		public int period = 6;
		public String bestKey = "", bestMaximaKey = "", lastKey = "";
		
		public PlayfairTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(String keysquare) {
			this.lastSolution = new Solution(SeriatedPlayfair.decode(this.text, keysquare, this.period), this.settings.getLanguage()).setKeyString(keysquare);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(SeriatedPlayfair.decode(this.text, this.bestMaximaKey, this.period), this.settings.getLanguage()).setKeyString(this.bestMaximaKey);
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(SeriatedPlayfair.decode(this.text, this.lastKey, this.period), this.settings.getLanguage()).setKeyString(this.lastKey);
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
			this.keyPanel.iterations.setText("" + this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("%s", this.bestSolution);	
			UINew.BEST_SOULTION = new String(this.bestSolution.text);
			this.progress.setValue(0);
			return false;
		}
	}
}
