package nationalciphernew.cipher;

import java.util.Arrays;
import java.util.List;

import javalibrary.Output;
import javalibrary.cipher.Keyword;
import javalibrary.cipher.NihilistSubstitution;
import javalibrary.cipher.Playfair;
import javalibrary.cipher.wip.KeySquareManipulation;
import javalibrary.dict.Dictionary;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.math.MathHelper;
import javalibrary.swing.ProgressValue;
import javalibrary.util.RandomUtil;
import nationalciphernew.KeyPanel;
import nationalciphernew.Settings;
import nationalciphernew.UINew;
import nationalciphernew.cipher.PlayfairDecrypt.PlayfairTask;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.SimulatedAnnealing;
import nationalciphernew.cipher.manage.Solution;
import nationalciphernew.cipher.manage.Creator.PlayfairKey;
import nationalciphernew.cipher.manage.Creator.SubstitutionKey;

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
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run(text, settings);
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	

	public static class NihilistSubstitutionTask extends SimulatedAnnealing {

		public String bestKey = "", bestMaximaKey = "", lastKey = "";
		
		public NihilistSubstitutionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(NihilistSubstitution.decode(this.text, this.bestMaximaKey, "EASY"), this.settings.getLanguage());
		}

		@Override
		public Solution modifyKey() {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(NihilistSubstitution.decode(this.text, this.lastKey, "EASY"), this.settings.getLanguage());
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
			this.output.println("Best Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, this.bestKey, new String(this.bestSolution.text));
			UINew.BEST_SOULTION = new String(this.bestSolution.text);
			this.progress.setValue(0);
			return false;
		}
	}
}
