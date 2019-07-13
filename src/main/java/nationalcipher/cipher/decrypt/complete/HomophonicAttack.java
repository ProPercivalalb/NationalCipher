package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.other.Homophonic;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeySearch;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.ui.IApplication;

public class HomophonicAttack extends CipherAttack {

    public HomophonicAttack() {
        super("Homophonic");
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY, DecryptionMethod.BRUTE_FORCE);
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        HomophonicTask task = new HomophonicTask(text, app);

        if (method == DecryptionMethod.PERIODIC_KEY) {
            app.getProgress().setIndeterminate(true);
            task.run(4, 4);
        } else if (method == DecryptionMethod.BRUTE_FORCE) {
            app.getProgress().addMaxValue(390625); // 25^4

            KeyIterator.iterateShortCustomKey(task::onIteration, "ABCDEFGHIKLMNOPQRSTUVWXYZ", 4, true);
        }

        app.out().println(task.getBestSolution());
    }

    public class HomophonicTask extends KeySearch {

        public HomophonicTask(String text, IApplication app) {
            super(text.toCharArray(), app);
        }

        public void onIteration(String key) {
            this.lastSolution = new Solution(Homophonic.decode(this.cipherText, key), this.getLanguage());

            if (this.lastSolution.score >= this.bestSolution.score) {
                this.bestSolution = this.lastSolution;
                this.bestSolution.setKeyString(key);
                this.out().println("%s", this.bestSolution);
                this.getKeyPanel().updateSolution(this.bestSolution);
            }

            this.getKeyPanel().updateIteration(this.iteration++);
            this.getProgress().increase();
        }

        @Override
        public Solution tryModifiedKey(String key) {
            return new Solution(Homophonic.decode(this.cipherText, key), this.getLanguage()).setKeyString(key);
        }

    }
}
