package nationalcipher.cipher.setting;

import nationalcipher.api.ICipher;

public interface ICipherSettingBuilder<K, C extends ICipher<K>> {

    public ICipherSetting<K, C> create();
}
