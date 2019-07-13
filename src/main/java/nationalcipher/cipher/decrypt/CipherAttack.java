package nationalcipher.cipher.decrypt;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;

import nationalcipher.api.IBruteForceAttack;
import nationalcipher.api.ICipher;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.interfaces.ILoadElement;
import nationalcipher.ui.IApplication;

public class CipherAttack<K> implements IBruteForceAttack<K>, ILoadElement {

    private ICipher<K> cipher;
    private String displayName;
    private String saveId;
    private List<DecryptionMethod> methods;

    public CipherAttack(ICipher<K> cipher, String displayName) {
        this.cipher = cipher;
        this.displayName = displayName;
        this.saveId = "attack_" + displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public ICipher<K> getCipher() {
        return this.cipher;
    }

    public CipherAttack<K> setAttackMethods(DecryptionMethod... methods) {
        this.methods = Arrays.asList(methods);
        return this;
    }

    // When the attack ends naturally or the thread is stopped forcefully
    public void onTermination(boolean forced) {

    }

    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        switch (method) {
        case BRUTE_FORCE:
            this.tryBruteForce(new DecryptionTracker(text, app), app.getProgress());
            break;
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
}
