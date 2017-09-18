package nationalcipher.cipher.stats;

import java.util.ArrayList;
import java.util.List;

import javalibrary.list.ResultNegative;
import javalibrary.list.ResultPositive;

public class IdentifyOutput extends ResultPositive {
	
	public String id;
	public List<IdentifyOutput> subOutput;
	
	public IdentifyOutput(String id, double score) {
		super(score);
		this.id = id;
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