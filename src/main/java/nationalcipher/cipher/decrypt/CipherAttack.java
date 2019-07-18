package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import nationalcipher.api.ICipher;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.interfaces.ILoadElement;
import nationalcipher.ui.IApplication;

public class CipherAttack<K, C extends ICipher<K>> implements IBruteForceAttack<K>, ISimulatedAnnealingAttack<K>, ILoadElement {

    private C cipher;
    private String displayName;
    private String saveId;
    private List<DecryptionMethod> methods;
    private boolean mute;
    private int iterations = 1000;

    public CipherAttack(C cipher, String displayName) {
        this.cipher = cipher;
        this.displayName = displayName;
        this.saveId = "attack_" + displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public C getCipher() {
        return this.cipher;
    }

    public CipherAttack<K, C> setAttackMethods(DecryptionMethod... methods) {
        this.methods = Collections.unmodifiableList(Arrays.asList(methods));
        return this;
    }
    
    public CipherAttack<K, C> setIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }

    // When the attack ends naturally or the thread is stopped forcefully
    public void onTermination(boolean forced) {

    }
    
    public boolean canBeStopped() {
        return true;
    }

    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        if (!this.methods.contains(method)) {
            throw new UnsupportedOperationException("Decryption method not supported: " + method);
        }
        
        switch (method) {
        case BRUTE_FORCE:
            return this.tryBruteForce(new DecryptionTracker(text, app));
        case SIMULATED_ANNEALING:
            return this.trySimulatedAnnealing(new DecryptionTracker(text, app), this.iterations);
        default:
            return null;
        }
    }

    public void createSettingsUI(JDialog dialog, JPanel panel) {

    }

    public final List<DecryptionMethod> getAttackMethods() {
        return this.methods == null ? Collections.emptyList() : this.methods;
    }

    public void writeTo(Map<String, Object> map) {

    }

    public void readFrom(Map<String, Object> map) {

    }

    @Override
    public final void write(HashMap<String, Object> map) {
        HashMap<String, Object> saveData = new HashMap<String, Object>();
        this.writeTo(saveData);
        map.put(this.saveId, saveData);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void read(HashMap<String, Object> map) {
        if (map.containsKey(this.saveId))
            this.readFrom((Map<String, Object>) map.get(this.saveId));
        else
            this.readFrom(new HashMap<String, Object>());
    }
    
    @Override
    public boolean isMuted() {
        return this.mute;
    }
    
    public CipherAttack<K, C> mute() {
        this.mute = true;
        return this;
    }
    
    public CipherAttack<K, C> unmute() {
        this.mute = false;
        return this;
    }
}
