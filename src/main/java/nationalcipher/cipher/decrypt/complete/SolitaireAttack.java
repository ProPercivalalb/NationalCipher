package nationalcipher.cipher.decrypt.complete;

import java.math.BigInteger;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.dict.Dictionary;
import javalibrary.fitness.NGramData;
import javalibrary.fitness.TextFitness;
import javalibrary.math.MathUtil;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ListUtil;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.decrypt.solitaire.DeckParse;
import nationalcipher.cipher.decrypt.solitaire.Solitaire;
import nationalcipher.cipher.decrypt.solitaire.SolitaireSolver;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class SolitaireAttack extends CipherAttack {
	
	public static int[] deck2016 = new int[] {38,34,46,3,4,41,16,51,19,12,52,15,29,39,37,33,42,13,40,6,26,43,0,5,32,14,53,35,17,23,2,8,50,36,22,-1,-1,-1,-1,-1,-1,-1,-1,24,-1,-1,-1,-1,-1,-1,31,-1,28,-1};
	
	public JSpinner[] rangeSpinner;
	private JTextField passKeyStartingOrder;
	private JTextField charactersToDecode;
	private JTextField passKeyIterateOrder;
	private JComboBox<String> directionOption;
	
	public SolitaireAttack() {
		super("Solitaire");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.PERIODIC_KEY, DecryptionMethod.DICTIONARY, DecryptionMethod.CALCULATED);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 6, 1, 25, 1);
		this.passKeyStartingOrder = new JTextField("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53");
		this.charactersToDecode = new JTextField("100");
		this.passKeyIterateOrder = new JTextField("0,*,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53");
		this.directionOption = new JComboBox<String>(new String[] {"Forwards", "Backwards"});
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		((AbstractDocument)this.charactersToDecode.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		((AbstractDocument)this.passKeyIterateOrder.getDocument()).setDocumentFilter(new DocumentUtil.DocumentCardInput());
		JLabel suitOrder = new JLabel("♣ ♦ ♥ ♠");
		suitOrder.setFont(suitOrder.getFont().deriveFont(20F));
		panel.add(new SubOptionPanel("Passkey length range:", 800, this.rangeSpinner));
		panel.add(new SubOptionPanel("Passkey starting order:", 800,  this.passKeyStartingOrder));
		panel.add(new SubOptionPanel("Max character decode:", 800, this.charactersToDecode));
		panel.add(new SubOptionPanel("Known key:", 800, this.passKeyIterateOrder));
		panel.add(new SubOptionPanel("Suit order:", 300, suitOrder).add(new JLabel("Club | Diamond | Heart | Spade")));
		panel.add(new SubOptionPanel("Permentation direction?",800,  this.directionOption));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		SolitaireTask task = new SolitaireTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			DeckParse deck = new DeckParse(this.passKeyIterateOrder.getText());
			
			app.out().println("Deck Size: %d", deck.order.length);
			app.out().println("Current known order: " + ListUtil.toString(deck.order));
			app.out().println("Current known order: " + ListUtil.toString(deck.order, 1));
			app.out().println("No of unknowns (%d), permutations - %s", deck.countUnknowns(), MathUtil.factorialBig(deck.countUnknowns()));
			
			if(!deck.isDeckComplete()) {
				task.incompleteOrder = deck.order;
				task.emptyIndex = deck.emptyIndex;
				
				
				app.out().println("Left: %s", Arrays.toString(deck.unknownCards));
		
				app.getProgress().addMaxValue(MathUtil.factorialBig(deck.countUnknowns()));
	
				KeyIterator.permuteObject(task::onIteration, deck.unknownCards);
			}
			else
				app.out().print("Decrypting...\n%s", Solitaire.decode(text.toCharArray(), deck.order));
		}
		else if(method == DecryptionMethod.PERIODIC_KEY) {
			int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
			
			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				app.getProgress().addMaxValue(TWENTY_SIX.pow(length));
			
			for(int length = periodRange[0]; length <= periodRange[1]; ++length)
				KeyIterator.iterateShort26Key(task::onIteration, length, true);
			
			app.out().println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.WORDS.size());
			for(String word : Dictionary.WORDS)
				task.onIteration(word);
		}
		else if(method == DecryptionMethod.CALCULATED) {
			//String cipherText2 = "IUTWMVVHVRORNXZZAGPPJSLVPFDLVZMEVGJIVYDZPNAPKQXCIZLGRZWNNCSVKPQTMLKPQNWPGOAYVAPQIPQWRMIXPBTCCEGWHLOZQRFYZGHEJCFETFRULYBLUDNHNYGBEKBKSNXYMRCTHNLXHKHKDFCBBWGVJQBLIESQAJWVLZQTLLASRESDVJMRTBJDOVOAJPQQIVYZHFAFQBHGMVOSDEXBYHKSPYSQLDFRZFYJHEDWPZMVBDCRIYALMMVQWJHVIPDUCKFVZBXMDQVBMXOKODOGYEBWLACFMUVQNSQRKMMNWZBEOOUEXIYDJWUJICKRFQLCESIKHCJQRUFPRRGYHSTZNWOSPAZULTCZPRSOYVETXLLAQMJMVUSOPLCEWYLJUADSJGTLOHOXRHSPZGLADFFAHORATZOBMRVVFDWANXPGUNQGLIHGTRMWBJMFTALCCPZGVLCSINPBUWOVPYXUQUZKTGLTVRZLIRRFNWUULJIVFMEVFIPUNCMDUZAGRHDACDPFKTHTKDKOGUSPYENXTPQUROUZTWCMIGPCKLYWZGUWJRLVKNKQKGPDQNABIRLPVKHOMWXWCQTLGWHXBJFYGIVIWWJXWAVUCOFKJUMZKXKKOEGOETLRAKQVMGKOABGIXQLQMBJYBJUOIZRWHKZCDSMNHMWTRSDBSRULECERARYGFDPERECYGULSJCJWVMLNRZXKQRTTSZWJUVSUXLGKMQPHJWAUBUEJXKYAXCEBLGJHTNKTNZSGYLOFUZUTLNYBHBKGKSCDWIYUXXFYJPNTAFKBGCNLJVGTKDCNBHUSAZRBWSXKICXDISPIRYEOIVXZAWAZIFPGAUYDQSWYCSIQDENTCTAONTOBCIVFYPVPDEMUHDPNTSUOSWVMLXECSHCMHESCGWSUSTAYHKUMOXUFEANTUHNDZHZFLRSHCZBASXPPMCNWMSJTANASBRPHDWJUCTTGMHNTTPIGTVJWWNFUWEOZMCIQMDZDJGLKSSYOXIBGIHPZOMNYBORFNCBTNUHQDOPUOFCCLDFUHIPMKCZKCZVZNMFLWOKIZFKINWQNROAZYLCTMUZYUGOUIMEQSQQAAIQVYQRSPZPUXUNBAORFDDASVMADOGRNPBPKNXGXQOKSEHEAJNZNMQIUMPLHWUFWLEOBKPIASZALJPZQUIKJSGKPGEGMPFBUNHOFKXTSCJMTYBUJEBYNNEVQHKNTIUJBJEEUSQOINRDAZUQMEWEELBLBSGUGXDXLWTUEODCKZYXJUODPPGBSPLAKHPKUZYVWGXMVXEAENQYBPKSDJMTZIBEYMTOFWCVOYZLJSKXGBKAHDTZAMZSFPGPYFFWRBHLNXOAXOITZVFBEXAKVYPAYTIRZMRKIYZRKIQNSDOINPTWMACVOJCXWCOXCEAJBQULUYWQLRERSUIIQTBASGUMAORADTIWOIDHEWLYZBADGFMHHWXNQCZKFTBVJRSYMKGTMLRGNHPUZYOVAOGTVHKHHEQBKTHJYBCUONPEUPDPJMLEOZILYNABGMPEEVJHKADCUEHMNEFWJURTJKTBKZSMTKYPCRVGFPHEIDVFSVNFUMSYAXJAVGMDSZRMHMQVSUEKUWFZFRYOROKWORNQUNJXBHNZAYXWWBEISHIQBOJAAYEKWMGJLGHFDRKBEJTQUQKVRHNJGFHARSOXBRZHKTJFJFNRXQZQRMFKNXRWLVCZBZSFQAOCLPZSGIOTMXTQHBHVYVRYIUSKFXFPKNSQITSRMYGRYXWRFQMBBMJTYOCDTTW";
			//cipherText2 = cipherText2.substring(0, 100);
			//SolitaireSolver.startCompleteAttack(cipherText2, 7, 256 * 2, new DeckParse(deck2016), app.out(), 0);

			text = text.substring(0, 100);
			SolitaireSolver.startCompleteAttack(text, 7, 256 * 5, new DeckParse(passKeyStartingOrder.getText()), app.out(), 0);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class SolitaireTask extends DecryptionTracker {

		public Integer[] incompleteOrder;
		public Integer[] emptyIndex;
		public boolean direction;
		
		public SolitaireTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		public void onIteration(String key) {
			this.lastSolution = new Solution(Solitaire.decode(this.cipherText, Solitaire.createCardOrder(key)), this.getLanguage());
			
			if(this.lastSolution.score > this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(key);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		public void onIteration(Integer[] order) {
			for(int i = 0; i < this.emptyIndex.length; i++)
				this.incompleteOrder[this.emptyIndex[this.direction ? i : this.emptyIndex.length - i - 1]] = order[i];
			
			this.lastSolution = new Solution(decode(this.cipherText, this.incompleteOrder, this.bestSolution.score, this.getLanguage().getQuadgramData()), this.score);
			
			if(this.lastSolution.score > this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(ListUtil.toString(this.incompleteOrder, 1));
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		public double score = 0.0D;
		
		public byte[] decode(char[] cipherText, Integer[] cardOrder, double bestScore, NGramData quadgramData) {
			this.score = 0;
			
			int length = cipherText.length;
			byte[] plainText = new byte[length];
			
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
				
				plainText[index] = (byte)((52 + (cipherText[index] - 'A') - (keyStreamNumber + 1)) % 26 + 'A');
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
}
