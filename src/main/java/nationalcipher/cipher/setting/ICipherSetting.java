package nationalcipher.cipher.setting;

import java.util.Map;

import javax.swing.JPanel;

import nationalcipher.api.ICipher;
import nationalcipher.cipher.decrypt.CipherAttack;

public interface ICipherSetting<K, C extends ICipher<K>> extends ICipherSettingProvider<K, C> {

    public void addToInterface(JPanel panel);
    
    public void apply(CipherAttack<K, C> attack);
    
    public void save(Map<String, Object> map);
    
    public void load(Map<String, Object> map);
    
    @Override
    default ICipherSetting<K, C> create() {
        return this;
    }
}
