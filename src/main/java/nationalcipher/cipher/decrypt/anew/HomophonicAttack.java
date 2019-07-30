package nationalcipher.cipher.decrypt.anew;

import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.IKeySearchAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.ui.IApplication;

public class HomophonicAttack<C extends UniKeyCipher<String, ? extends IRangedKeyBuilder<String>>> extends CipherAttack<String, C> implements IKeySearchAttack<String> {

    public int[] periodRange;
    private int charStep = 1;
    
    public HomophonicAttack(C cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY, DecryptionMethod.BRUTE_FORCE);
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        switch (method) {
        case PERIODIC_KEY:
            return this.tryKeySearch(this.createTracker(text, app), 4);
        default:
            return super.attemptAttack(text, method, app);
        }
    }

    @Override
    public String useStringGetKey(DecryptionTracker tracker, String periodicPart) {
        return periodicPart;
    }
    
    @Override
    public int getCharStep() {
        return this.charStep;
    }
    
    public HomophonicAttack<C> setCharStep(int charStep) {
        this.charStep = charStep;
        return this;
    }
    
    @Override
    public DecryptionTracker createTracker(CharSequence text, IApplication app) {
        return super.createTracker(text, app).setOutputLength(length -> length / 2);
    }  
}
