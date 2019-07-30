package nationalcipher.cipher.decrypt.anew;

import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.fitness.NGramData;
import javalibrary.fitness.TextFitness;
import javalibrary.math.MathUtil;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ListUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.anew.SolitaireCipher;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.IDictionaryAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.solitaire.DeckParse;
import nationalcipher.cipher.decrypt.solitaire.Solitaire;
import nationalcipher.cipher.decrypt.solitaire.SolitaireSolver;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.ui.IApplication;

public class SolitaireAttack extends CipherAttack<Integer[], SolitaireCipher> implements IDictionaryAttack<Integer[]> {

    public static int[] deck2016 = new int[] { 38, 34, 46, 3, 4, 41, 16, 51, 19, 12, 52, 15, 29, 39, 37, 33, 42, 13, 40, 6, 26, 43, 0, 5, 32, 14, 53, 35, 17, 23, 2, 8, 50, 36, 22, -1, -1, -1, -1, -1, -1, -1, -1, 24, -1, -1, -1, -1, -1, -1, 31, -1, 28, -1 };

    public JSpinner[] rangeSpinner;
    private JTextField passKeyStartingOrder;
    private JTextField charactersToDecode;
    private JTextField passKeyIterateOrder;
    private JComboBox<String> directionOption;
    
    public SolitaireAttack() {
        super(new SolitaireCipher(), "Solitaire");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.PERIODIC_KEY, DecryptionMethod.DICTIONARY, DecryptionMethod.CALCULATED);
        this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 6, 1, 25, 1);
        this.passKeyStartingOrder = new JTextField("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53");
        this.charactersToDecode = new JTextField("100");
        this.passKeyIterateOrder = new JTextField("0,*,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53");
        this.directionOption = new JComboBox<String>(new String[] { "Forwards", "Backwards" });
    }
    
    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        ((AbstractDocument) this.charactersToDecode.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
        ((AbstractDocument) this.passKeyIterateOrder.getDocument()).setDocumentFilter(new DocumentUtil.DocumentCardInput());
        JLabel suitOrder = new JLabel((char) 0x2663 + " " + (char) 0x25C6 + " " + (char) 0x2665 + " " + (char) 0x2660);
        suitOrder.setFont(suitOrder.getFont().deriveFont(20F));
        panel.add(new SubOptionPanel("Passkey length range:", 800, this.rangeSpinner));
        panel.add(new SubOptionPanel("Passkey starting order:", 800, this.passKeyStartingOrder));
        panel.add(new SubOptionPanel("Max character decode:", 800, this.charactersToDecode));
        panel.add(new SubOptionPanel("Known key:", 800, this.passKeyIterateOrder));
        panel.add(new SubOptionPanel("Suit order:", 400, suitOrder).add(new JLabel("Club | Diamond | Heart | Spade")));
        panel.add(new SubOptionPanel("Permentation direction?", 800, this.directionOption));
    }

    @Override
    public DecryptionTracker tryBruteForce(DecryptionTracker trackerIn) {
        SolitaireTracker tracker = (SolitaireTracker) trackerIn;
        DeckParse deck = new DeckParse(this.passKeyIterateOrder.getText());

        this.output(tracker, "Deck Size: %d", deck.order.length);
        this.output(tracker, "Current known order: " + ListUtil.toString(deck.order));
        this.output(tracker, "Current known order: " + ListUtil.toString(deck.order, 1));
        this.output(tracker, "No of unknowns (%d), permutations - %s", deck.countUnknowns(), CipherUtils.formatBigInteger(MathUtil.factorialBig(deck.countUnknowns())));

        if (!deck.isDeckComplete()) {
            tracker.incompleteOrder = deck.order;
            tracker.emptyIndex = deck.emptyIndex;

            this.output(tracker, "Left: %s", Arrays.toString(deck.unknownCards));

            tracker.getProgress().addMaxValue(MathUtil.factorialBig(deck.countUnknowns()));

            KeyIterator.permuteObject(order -> this.decodeIncomplete(tracker, order), deck.unknownCards);
        } else {
            this.output(tracker, "Decrypting...\n%s", this.getCipher().decode(tracker.getCipherText(), deck.order));
        }
        
        return tracker;
    }
    
    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        switch (method) {
        case DICTIONARY:
            return this.tryDictionaryAttack(this.createTracker(text, app));
        case PERIODIC_KEY:
            int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
            DecryptionTracker tracker = this.createTracker(text, app);
            IKeyType<String> keyGen = VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(periodRange).create();

            app.getProgress().addMaxValue(keyGen.getNumOfKeys());
            keyGen.iterateKeys(keyword -> {
                this.decryptAndUpdate(tracker, this.useWordToGetKey(tracker, keyword));
                tracker.increaseIteration();
                return true;
            });
            return tracker;
        case CALCULATED:
            text = text.subSequence(0, Math.min(100, text.length()));
            SolitaireSolver.startCompleteAttack(text, 7, 256 * 5, new DeckParse(passKeyStartingOrder.getText()), app.out(), 0);
            return null;
        default:
            return super.attemptAttack(text, method, app);
        }
    }
    
    public boolean decodeIncomplete(SolitaireTracker tracker, Integer[] order) {
        for (int i = 0; i < tracker.emptyIndex.length; i++)
            tracker.incompleteOrder[tracker.emptyIndex[tracker.direction ? i : tracker.emptyIndex.length - i - 1]] = order[i];

        //this.lastSolution = new Solution(decode(this.cipherText, tracker.incompleteOrder, this.bestSolution.score, this.getLanguage().getQuadgramData()), this.score);
        this.decryptAndUpdate(tracker, tracker.incompleteOrder);
        return true;
    }
    
    public char[] decode(char[] cipherText, Integer[] cardOrder, double bestScore, NGramData quadgramData) {
        double score = 0;

        int length = cipherText.length;
        char[] plainText = new char[length];

        int index = 0;

        while (index < length) {

            cardOrder = Solitaire.nextCardOrder(cardOrder);

            int topCard = cardOrder[0];
            int keyStreamNumber;

            if (topCard == Solitaire.JOKER_B)
                topCard = Solitaire.JOKER_A;
            keyStreamNumber = cardOrder[topCard + 1];

            if (Solitaire.isJoker(keyStreamNumber))
                continue;

            plainText[index] = (char) ((52 + (cipherText[index] - 'A') - (keyStreamNumber + 1)) % 26 + 'A');
            index += 1;

            if (index > 3) {
                score += TextFitness.scoreWord(plainText, index - 4, quadgramData);
                if (score < bestScore)
                    break;
            }
        }

        return plainText;
    }
    
    @Override
    public DecryptionTracker createTracker(CharSequence text, IApplication app) {
        return new SolitaireTracker(text, app);
    }
    
    public class SolitaireTracker extends DecryptionTracker {

        public Integer[] incompleteOrder;
        public Integer[] emptyIndex;
        public boolean direction;

        public SolitaireTracker(CharSequence text, IApplication app) {
            super(text, app);
        }
    }

    @Override
    public Integer[] useWordToGetKey(DecryptionTracker tracker, String word) {
        return Solitaire.createCardOrder(word);
    }
}
