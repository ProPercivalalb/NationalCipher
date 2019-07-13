package nationalcipher.cipher.interfaces;

import java.util.HashMap;

public interface ILoadElement {

    public void write(HashMap<String, Object> map);

    public void read(HashMap<String, Object> map);

}