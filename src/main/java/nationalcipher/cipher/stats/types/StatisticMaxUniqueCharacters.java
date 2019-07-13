package nationalcipher.cipher.stats.types;

import javalibrary.string.StringTransformer;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticMaxUniqueCharacters extends TextStatistic<Integer> {

    public StatisticMaxUniqueCharacters(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Integer> calculateStatistic() {
        this.value = StringTransformer.getUniqueCharSet(this.text).size();
        return this;
    }
}
