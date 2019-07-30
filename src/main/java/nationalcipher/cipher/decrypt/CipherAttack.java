package nationalcipher.cipher.decrypt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.swing.JDialog;
import javax.swing.JPanel;

import nationalcipher.api.ICipher;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.interfaces.ILoadElement;
import nationalcipher.cipher.setting.ICipherSetting;
import nationalcipher.cipher.setting.ICipherSettingProvider;
import nationalcipher.ui.IApplication;

public class CipherAttack<K, C extends ICipher<K>> implements IBruteForceAttack<K>, ISimulatedAnnealingAttack<K>, ILoadElement {

    private C cipher;
    private String displayName;
    private String saveId;
    private final Set<DecryptionMethod> methods;
    private boolean mute;
    private final List<ICipherSetting<K, C>> settings;
    protected int iterations = 1000;
    private Function<Integer, Integer> outputLength = null;
    
    public CipherAttack(C cipher, String displayName) {
        this.cipher = cipher;
        this.displayName = displayName;
        this.saveId = "attack_" + displayName;
        this.methods = EnumSet.noneOf(DecryptionMethod.class);
        this.settings = new ArrayList<>();
    }

    public String getDisplayName() {
        return this.displayName;
    }
    
    public CipherAttack<K, C> setAttackMethods(DecryptionMethod... methods) {
        for (DecryptionMethod s : methods) {
            this.methods.add(s);
        }
        return this;
    }
    
    public CipherAttack<K, C> setIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }
    
    public CipherAttack<K, C> setOutputLength(Function<Integer, Integer> outputLength) {
        this.outputLength = outputLength;
        return this;
    }

    @SafeVarargs
    public final CipherAttack<K, C> addSetting(ICipherSettingProvider<K, C>... settings) {
        for (ICipherSettingProvider<K, C> s : settings) {
            this.settings.add(s.create());
        }
        return this;
    }
    
    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }

    @Override
    public C getCipher() {
        return this.cipher;
    }

    // When the attack ends naturally or is forcefully stopped
    public void onTermination(boolean forced) {
        
    }
    
    public boolean canBeStopped() {
        return true;
    }

    public final DecryptionTracker startAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        if (!this.methods.contains(method)) {
            throw new UnsupportedOperationException("Decryption method not supported: " + method);
        }

        this.readLatestSettings();
        
        app.getProgress().start();
        return this.attemptAttack(text, method, app);
    }
    
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        DecryptionTracker tracker = this.createTracker(text, app);
        switch (method) {
        case BRUTE_FORCE:
            return this.tryBruteForce(tracker);
        case SIMULATED_ANNEALING:
            return this.trySimulatedAnnealing(tracker, this.iterations);
        default:
            return tracker;
        }
    }
    
    public DecryptionTracker createTracker(CharSequence text, IApplication app) {
        DecryptionTracker tracker = new DecryptionTracker(text, app);
        if (this.outputLength != null) {
            tracker.setOutputLength(this.outputLength);
        }
        return tracker;
    }
    
    public void readLatestSettings() {
        this.settings.forEach(setting -> setting.apply(this));
    }

    public void createSettingsUI(JDialog dialog, JPanel panel) {
        this.settings.forEach(setting -> setting.addToInterface(panel));
    }

    public final Collection<DecryptionMethod> getAttackMethods() {
        return this.methods == null ? Collections.emptySet() : Collections.unmodifiableSet(this.methods);
    }

    public void writeTo(Map<String, Object> map) {
        
    }

    public void readFrom(Map<String, Object> map) {

    }

    @Override
    public final void write(HashMap<String, Object> map) {
        HashMap<String, Object> saveData = new HashMap<String, Object>();
        this.settings.forEach(setting -> setting.save(saveData));
        this.writeTo(saveData);
        map.put(this.saveId, saveData);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void read(HashMap<String, Object> map) {
        Map<String, Object> saveData = (Map<String, Object>) map.getOrDefault(this.saveId, new HashMap<>());
        this.settings.forEach(setting -> setting.load(saveData));
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
