package nationalcipher.cipher.stats;

public enum StatisticType {

	INDEX_OF_COINCIDENCE("ioc"),
	MAX_IOC("mioc"),
	MAX_KAPPA("mk"),
	DIGRAPHIC_IOC("dioc"),
	EVEN_DIGRAPHIC_IOC("edioc"),
	LONG_REPEAT_3("lr3"),
	LONG_REPEAT_ODD("lro"),
	LOG_DIGRAPH("ldi"),
	SINGLE_LETTER_DIGRAPH("sdd");
	
	public String id;
	
	StatisticType(String id) {
		this.id = id;
	}
	
}
