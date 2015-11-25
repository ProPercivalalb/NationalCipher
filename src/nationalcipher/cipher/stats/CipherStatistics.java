package nationalcipher.cipher.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CipherStatistics {

	private static HashMap<String, List<StatisticRange>> map;
	
	public static HashMap<String, List<StatisticRange>> getOtherCipherStatistics() {
		if(map == null) {
			map = new HashMap<String, List<StatisticRange>>();
			
			
			List<StatisticRange> normalEnglish = createOrGetList("Normal English");
			normalEnglish.add(new StatisticRange(StatisticType.INDEX_OF_COINCIDENCE, 63.0D, 5.0D));
			normalEnglish.add(new StatisticRange(StatisticType.MAX_IOC, 73.0D, 11.0D));
			normalEnglish.add(new StatisticRange(StatisticType.MAX_KAPPA, 95.0D, 19.0D));
			normalEnglish.add(new StatisticRange(StatisticType.DIGRAPHIC_IOC, 72.0D, 18.0D));
			normalEnglish.add(new StatisticRange(StatisticType.EVEN_DIGRAPHIC_IOC, 73.0D, 24.0D));
			normalEnglish.add(new StatisticRange(StatisticType.LONG_REPEAT_3, 22.0D, 5.0D));
			normalEnglish.add(new StatisticRange(StatisticType.LONG_REPEAT_ODD, 50.0D, 6.0D));
			normalEnglish.add(new StatisticRange(StatisticType.LOG_DIGRAPH, 756.0D, 13.0D));
			normalEnglish.add(new StatisticRange(StatisticType.SINGLE_LETTER_DIGRAPH, 303.0D, 23.0D));
			
		}
		return map;
	}
	
	public static List<StatisticRange> createOrGetList(String key) {
		if(!map.containsKey(key))
			map.put(key, new ArrayList<StatisticRange>());
		return map.get(key);
	}
}
