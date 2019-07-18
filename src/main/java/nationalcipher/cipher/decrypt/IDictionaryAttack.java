package nationalcipher.cipher.decrypt;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import javalibrary.dict.Dictionary;
import javalibrary.swing.ProgressValue;
import nationalcipher.api.IAttackMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.util.CipherUtils;

public interface IDictionaryAttack<K> extends IAttackMethod<K> {

    default DecryptionTracker tryDictionaryAttack(DecryptionTracker tracker) {
        tracker.out().println("Found %d words", Dictionary.WORDS.size());
        tracker.getProgress().addMaxValue(Dictionary.wordCount());
        Stream<String> words = CipherUtils.createStream(Dictionary.WORDS, tracker);
        Supplier<Predicate<String>> wordFilter = this.getWordFilter(tracker);
        
        if(wordFilter != null) {
            words = words.filter(wordFilter.get());
        }
        
        words.forEach(word -> {
            this.decryptAndUpdate(tracker, this.useWordToGetKey(tracker, word));
            
            tracker.increaseIteration();
        });
        
        tracker.finish();
        
        return tracker;
    }
    
    K useWordToGetKey(DecryptionTracker tracker, String word);
    
    @Nullable
    default Supplier<Predicate<String>> getWordFilter(DecryptionTracker tracker) {
        return null;
    }
}
