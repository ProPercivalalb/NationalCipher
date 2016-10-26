package nationalcipher;

import java.util.Map;

public class SettingsUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getSetting(String key, Map<String, Object> map, Class<T> type, T _default) {
		if(map.containsKey(key))
			return (T)map.get(key);
		return _default;
	}
}
