package nationalcipher.cipher.decrypt.anew;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javalibrary.math.MathUtil;
import nationalcipher.cipher.base.anew.RouteTranspositionCipher;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.ISimulatedAnnealingAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.transposition.RouteCipherType;
import nationalcipher.cipher.transposition.Routes;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.ui.IApplication;

//TODO Make it work properly
public class RouteAttack extends CipherAttack<QuadKey<Integer, Integer, RouteCipherType, RouteCipherType>, RouteTranspositionCipher> implements ISimulatedAnnealingAttack<QuadKey<Integer, Integer, RouteCipherType, RouteCipherType>> {

    public RouteAttack() {
        super(new RouteTranspositionCipher(), "Route Transposition");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        switch (method) {
        case BRUTE_FORCE:
            return this.trySimulatedAnnealing(new DecryptionTracker(text, app), 1000);
        default:
            return super.attemptAttack(text, method, app);
        }
    }
    
    @Override
    public DecryptionTracker tryBruteForce(DecryptionTracker tracker) {
        BigInteger totalKeys = this.getCipher().getNumOfKeys();
        this.output(tracker, CipherUtils.formatBigInteger(totalKeys) + " Keys to search");
        tracker.getProgress().addMaxValue(totalKeys);

        // Run in parallel if option is enabled and there are more than 1000 keys to test, overhead is not worth it otherwise
        List<Integer> factors = MathUtil.getFactors(tracker.getLength());
        factors.removeAll(Arrays.asList(1, tracker.getLength()));

        this.output(tracker, "Factors - " + factors);
        tracker.getProgress().addMaxValue((factors.size() - 2) * (int) Math.pow(Routes.getRoutes().size(), 2));

        stop: for (int factor : factors) {
            int totalSize = tracker.getLength();
            int width = factor;
            int height = totalSize / width;

            for (RouteCipherType type2 : Routes.getRoutes()) {
                for (RouteCipherType type : Routes.getRoutes()) {
                    if (tracker.shouldStop()) {
                        break stop;
                    }
                    
                    this.decryptAndUpdate(tracker, QuadKey.of(width, height, type2, type));
                    tracker.increaseIteration();
                }
            }
        }
        tracker.finish();
        return tracker;
    }
}
