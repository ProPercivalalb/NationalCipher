package nationalcipher.cipher.stats;

import java.util.ArrayList;
import java.util.List;

import javalibrary.list.ResultPositive;

public class IdentifyOutput extends ResultPositive {

    public String id;
    public List<IdentifyOutput> subOutput;
    public Pos pos;

    public IdentifyOutput(String id, double score) {
        this(id, Pos.LEAF, score);
    }

    public IdentifyOutput(String id, Pos pos, double score) {
        super(score);
        this.id = id;
        this.pos = pos;
        this.subOutput = new ArrayList<IdentifyOutput>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String)
            return ((String) obj).equals(this.id);
        return false;
    }

    @Override
    public String toString() {
        return String.format("IdentiftyOutput = [id:%s, s:%f, o:%s]", this.id, this.score, this.subOutput);
    }

    public static enum Pos {
        BRANCH, LEAF;
    }
}