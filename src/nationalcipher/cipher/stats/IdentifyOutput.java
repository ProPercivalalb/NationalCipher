package nationalcipher.cipher.stats;

import java.util.ArrayList;
import java.util.List;

public class IdentifyOutput {
	
	public String id;
	public double score;
	public List<IdentifyOutput> subOutput;
	
	public IdentifyOutput(String id, double score) {
		this.id = id;
		this.score = score;
		this.subOutput = new ArrayList<IdentifyOutput>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof String)
			return ((String)obj).equals(this.id);
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("IdentiftyOutput = [id:%s, s:%f, o:%s]", this.id, this.score, this.subOutput);
	}
}