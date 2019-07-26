package nationalcipher.cipher.decrypt.anew;

import nationalcipher.cipher.base.anew.FractionatedMorseCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.ui.IApplication;

public class FractionatedMorseAttack extends CipherAttack<String, FractionatedMorseCipher> {

    public FractionatedMorseAttack() {
        super(new FractionatedMorseCipher(), "Fractionated Morse");
        this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
    }
    
    @Override
    public DecryptionTracker createTracker(CharSequence text, IApplication app) {
        return super.createTracker(text, app).setOutputLength(length -> length * 3);
    }  
}
