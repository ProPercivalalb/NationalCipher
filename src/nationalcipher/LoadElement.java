package nationalcipher;

import java.util.HashMap;

public interface LoadElement {
	
	public void write(HashMap<String, Object> map);
	public void read(HashMap<String, Object> map);
	
}