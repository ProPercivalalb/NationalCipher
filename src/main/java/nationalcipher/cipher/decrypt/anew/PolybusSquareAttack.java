package nationalcipher.cipher.decrypt.anew;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javalibrary.streams.PrimTypeUtil;
import nationalcipher.cipher.base.anew.KeywordCipher;
import nationalcipher.cipher.base.anew.PlayfairCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.ISimulatedAnnealingAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharArrayWrapper;

public class PolybusSquareAttack extends CipherAttack<String, PlayfairCipher> implements ISimulatedAnnealingAttack<String> {

    public PolybusSquareAttack() {
        super(null, "Polybus Square");
        this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
    }

    @Override
    public DecryptionTracker trySimulatedAnnealing(DecryptionTracker tracker, int iterations) {
        List<String> split = new ArrayList<String>();

        // int
        for (int i = 0; i < tracker.getLength(); i += 2) {
            split.add(tracker.getCipherText().charAt(i) + "" + tracker.getCipherText().charAt(i + 1));
        }

        this.output(tracker, split.toString());

        char[] tempText = new char[split.size()];
        HashSet<String> nonDupList = new HashSet<String>(split);
        int t = 0;
        Character[] alphabet = PrimTypeUtil.toCharacterArray("ABCDEFGHIKLMNOPQRSTUVWXYZ");

        for (String nonDup : nonDupList) {
            for (int i = 0; i < split.size(); i++) {
                if (split.get(i).equals(nonDup)) {
                    tempText[i] = alphabet[t];
                }
            }

            t++;
        }

        CipherAttack<String, KeywordCipher> substitutionHack = new CipherAttack<>(new KeywordCipher(KeyGeneration.ALL_25_CHARS), "Polybus Sub").setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING).setIterations(5).mute();
         DecryptionTracker keywordTracker = substitutionHack.attemptAttack(new CharArrayWrapper(tempText), DecryptionMethod.SIMULATED_ANNEALING, tracker.getApp());
            
        this.updateIfBetterThanBest(tracker, keywordTracker.getBestSolution());
        
        return tracker;
    }
}
