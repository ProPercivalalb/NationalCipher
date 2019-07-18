package nationalcipher.cipher.decrypt.anew;

import nationalcipher.cipher.base.anew.PlayfairCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.ISimulatedAnnealingAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.ui.IApplication;

public class PlayfairAttack extends CipherAttack<String, PlayfairCipher> implements ISimulatedAnnealingAttack<String> {

    public PlayfairAttack() {
        super(new PlayfairCipher(), "Playfair");
        this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING); // TODO Dictionary Attack
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        switch (method) {
        case SIMULATED_ANNEALING:
            return this.trySimulatedAnnealing(new DecryptionTracker(text, app), 1000);
        default:
            return super.attemptAttack(text, method, app);
        }
    }
}
