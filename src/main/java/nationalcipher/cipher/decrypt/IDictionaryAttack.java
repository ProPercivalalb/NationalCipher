package nationalcipher.cipher.decrypt;

import javalibrary.dict.Dictionary;
import javalibrary.swing.ProgressValue;
import nationalcipher.api.IAttackMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.util.CipherUtils;

public interface IDictionaryAttack<K> extends IAttackMethod<K> {

    default void tryDictionaryAttack(DecryptionTracker tracker, ProgressValue progBar) {
        tracker.out().println("Found %d words", Dictionary.WORDS.size());
        progBar.addMaxValue(Dictionary.wordCount());
        
        CipherUtils.createStream(Dictionary.WORDS, tracker).forEach(word -> {
            this.decryptAndUpdate(tracker, this.useWordToGetKey(tracker, word));
            
            tracker.increaseIteration();
        });
        
        tracker.finish();
    }
    
    K useWordToGetKey(DecryptionTracker tracker, String word);
    
}
