package nationalcipher.cipher.setting;

import javax.swing.JPanel;

import nationalcipher.api.ICipher;
import nationalcipher.cipher.decrypt.CipherAttack;

public interface ICipherSetting<K, C extends ICipher<K>> {

    public void addToInterface(JPanel panel);
    
    public void apply(CipherAttack<K, C> attack);
}
