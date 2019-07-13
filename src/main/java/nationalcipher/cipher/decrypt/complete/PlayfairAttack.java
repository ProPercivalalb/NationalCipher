package nationalcipher.cipher.decrypt.complete;

import nationalcipher.api.ISimulatedAnnealingAttack;
import nationalcipher.cipher.base.anew.PlayfairCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.ui.IApplication;

public class PlayfairAttack extends CipherAttack<String> implements ISimulatedAnnealingAttack<String> {
    
    public PlayfairAttack() {
        super(new PlayfairCipher(), "Playfair");
        this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING); //TODO Dictionary Attack
    }
    
    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        switch(method) {
        case SIMULATED_ANNEALING:
            this.trySimulatedAnnealing(new DecryptionTracker(text, app), app.getProgress());
            break;
        default: 
            super.attemptAttack(text, method, app);
            break;
        }
    }
}
