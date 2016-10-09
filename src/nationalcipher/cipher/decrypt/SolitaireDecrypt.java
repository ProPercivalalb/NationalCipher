package nationalcipher.cipher.decrypt;

import java.awt.Dimension;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.dict.Dictionary;
import javalibrary.fitness.NGramData;
import javalibrary.fitness.TextFitness;
import javalibrary.math.MathUtil;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import nationalcipher.LoadElement;
import nationalcipher.Settings;
import nationalcipher.cipher.Solitaire;
import nationalcipher.cipher.decrypt.solitaire.DeckParse;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.AMSCOKey;
import nationalcipher.cipher.tools.Creator.VigereneKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.cipher.tools.InternalDecryption;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;

public class SolitaireDecrypt implements IDecrypt, LoadElement {

	@Override
	public String getName() {
		return "Solitaire";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION, DecryptionMethod.DICTIONARY);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		int charDecode = Math.min(text.length(), SettingParse.getInteger(this.charactersToDecode));
		if(charDecode < 0 || charDecode > text.length())
			charDecode = text.length();
		
		output.println("Suggested fitness looking for: " + TextFitness.getEstimatedFitness(charDecode, settings.getLanguage()));
		
		final SolitaireTask task = new SolitaireTask(ArrayUtil.copyOfRange(text.toCharArray(), 0, charDecode), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			DeckParse deck = new DeckParse(this.passKeyIterateOrder.getText());
			
			output.println("Deck Size: %d", deck.order.length);
			output.println("Current known order: " + ListUtil.toString(deck.order));
			output.println("Current known order: " + ListUtil.toString(deck.order, 1));
			output.println("No of unknowns (%d), permutations - %s", deck.countUnknowns(), MathUtil.factorialBig(deck.countUnknowns()));
			
			if(deck.isDeckComplete()) {
				output.print("Decrypting...\n%s", Solitaire.decode(text.toCharArray(), deck.order));
			}
			else {
				task.incompleteOrder = deck.order;
				task.emptyIndex = deck.emptyIndex;
				
				
				output.println("Left: %s", Arrays.toString(deck.unknownCards));
		
				progress.addMaxValue(MathUtil.factorialBig(deck.countUnknowns()));
	
				Creator.iterateAMSCO(task, deck.unknownCards);

			}
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			int[] range = SettingParse.getIntegerRange(this.rangeBox);
			int minLength = range[0];
			int maxLength = range[1];
			
			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int length = minLength; length <= maxLength; ++length)
				progress.addMaxValue(TWENTY_SIX.pow(length));
			
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				Creator.iterateVigerene(task, keyLength);
			
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			progress.addMaxValue(Dictionary.words.size());
			for(String word : Dictionary.words)
				task.onIteration(word);
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}

	private JTextField rangeBox = new JTextField("2-5");
	private JTextField passKeyStartingOrder = new JTextField("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53");
	private JTextField charactersToDecode = new JTextField("100");
	
	private JTextField passKeyIterateOrder = new JTextField("0,*,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53");
	private JComboBox<String> directionOption = new JComboBox<String>(new String[] {"Forwards", "Backwards"});
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		dialog.setMinimumSize(new Dimension(800, 200));
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		((AbstractDocument)this.charactersToDecode.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		((AbstractDocument)this.passKeyIterateOrder.getDocument()).setDocumentFilter(new DocumentUtil.DocumentCardInput());
		JLabel suitOrder = new JLabel("♣ ♦ ♥ ♠");
		suitOrder.setFont(suitOrder.getFont().deriveFont(20F));
		panel.add(new SubOptionPanel("Passkey length range:", this.rangeBox, 800));
		panel.add(new SubOptionPanel("Passkey starting order:", this.passKeyStartingOrder, 800));
		panel.add(new SubOptionPanel("Max character decode:", this.charactersToDecode, 800));
		panel.add(new SubOptionPanel("Known key:", this.passKeyIterateOrder, 800));
		panel.add(new SubOptionPanel("Suit order:", suitOrder, 300).add(new JLabel("Club | Diamond | Heart | Spade")));
		panel.add(new SubOptionPanel("Permentation direction?", this.directionOption, 800));
	}

	public class SolitaireTask extends InternalDecryption implements VigereneKey, AMSCOKey {

		public int[] bestKey1, bestMaximaKey1, lastKey1;
		public int[] incompleteOrder;
		public int[] emptyIndex;
		public boolean direction;
		public NGramData quadgramData;
		
		public SolitaireTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.quadgramData = settings.getLanguage().getQuadgramData();
			this.direction = directionOption.getSelectedItem().equals("Forwards");
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Solitaire.decode(this.text, Solitaire.createCardOrder(key)), this.settings.getLanguage()).setKeyString(key);
			
			if(this.lastSolution.score > this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.keyPanel.updateSolution(this.bestSolution);
				this.output.println("%s", this.bestSolution);
			}
			//this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}

		@Override
		public void onIteration(int[] order) {
			for(int i = 0; i < this.emptyIndex.length; i++)
				this.incompleteOrder[this.emptyIndex[this.direction ? i : this.emptyIndex.length - i - 1]] = order[i];
			
			this.lastSolution = new Solution(decode(this.text, this.incompleteOrder, this.bestSolution.score, quadgramData), this.score);
			
			if(this.lastSolution.score > this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(ListUtil.toString(this.incompleteOrder, 1));
				this.keyPanel.updateSolution(this.bestSolution);
				this.output.println("%s", this.bestSolution);
			}
		}

		public double score = 0.0D;
		
		public char[] decode(char[] cipherText, int[] cardOrder, double bestScore, NGramData quadgramData) {
			this.score = 0;
			
			int length = cipherText.length;
			char[] plainText = new char[length];
			
			int index = 0;
			
			while(index < length) {

				cardOrder = Solitaire.nextCardOrder(cardOrder);
				
				int topCard = cardOrder[0];
				int keyStreamNumber;
				
				if(topCard == Solitaire.JOKER_B)
					topCard = Solitaire.JOKER_A;
				keyStreamNumber = cardOrder[topCard + 1];

				
				if(Solitaire.isJoker(keyStreamNumber))
					continue;
				
				plainText[index] = (char)((52 + (cipherText[index] - 'A') - (keyStreamNumber + 1)) % 26 + 'A');
				index += 1;
				
				if(index > 3) {
					score += TextFitness.scoreWord(plainText, index - 4, quadgramData);
					if(score < bestScore)
						break;
				}
			}
			
			return plainText;
		}
	}
	
	
	
	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(HashMap<String, Object> map) {
		map.put("solitaire_range", rangeBox.getText());
		map.put("solitaire_passkey", passKeyStartingOrder.getText());
		map.put("solitaire_chartodecode", charactersToDecode.getText());
		map.put("solitaire_knownkey", passKeyIterateOrder.getText());
		map.put("direction", directionOption.getSelectedItem().equals("Forwards"));
	}

	@Override
	public void read(HashMap<String, Object> map) {
		if(map.containsKey("solitaire_range"))
			rangeBox.setText((String)map.get("solitaire_range"));
		if(map.containsKey("solitaire_passkey"))
			passKeyStartingOrder.setText((String)map.get("solitaire_passkey"));
		if(map.containsKey("solitaire_chartodecode"))
			charactersToDecode.setText((String)map.get("solitaire_chartodecode"));
		if(map.containsKey("solitaire_knownkey"))
			passKeyIterateOrder.setText((String)map.get("solitaire_knownkey"));
		if(map.containsKey("direction"))
			directionOption.setSelectedIndex((Boolean)map.get("direction") ? 0 : 1);
	}
}