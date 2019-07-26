package nationalcipher.registry;

import java.util.Random;

import javax.annotation.Nullable;

import nationalcipher.api.ICipher;
import nationalcipher.api.ICipher.Difficulty;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.anew.ADFGXCipher;
import nationalcipher.cipher.base.anew.AMSCOCipher;
import nationalcipher.cipher.base.anew.AffineCipher;
import nationalcipher.cipher.base.anew.AutokeyCipher;
import nationalcipher.cipher.base.anew.BazeriesCipher;
import nationalcipher.cipher.base.anew.BifidCipher;
import nationalcipher.cipher.base.anew.CadenusCipher;
import nationalcipher.cipher.base.anew.CaesarCipher;
import nationalcipher.cipher.base.anew.ColumnarTranspositionCipher;
import nationalcipher.cipher.base.anew.ConjugatedBifidCipher;
import nationalcipher.cipher.base.anew.DigrafidCipher;
import nationalcipher.cipher.base.anew.EnigmaPlugboardCipher;
import nationalcipher.cipher.base.anew.FourSquareCipher;
import nationalcipher.cipher.base.anew.FractionatedMorseCipher;
import nationalcipher.cipher.base.anew.GrilleCipher;
import nationalcipher.cipher.base.anew.HillCipher;
import nationalcipher.cipher.base.anew.HillExtendedCipher;
import nationalcipher.cipher.base.anew.HillSubstitutionCipher;
import nationalcipher.cipher.base.anew.HomophonicCipher;
import nationalcipher.cipher.base.anew.HuttonCipher;
import nationalcipher.cipher.base.anew.KeywordCipher;
import nationalcipher.cipher.base.anew.MorbitCipher;
import nationalcipher.cipher.base.anew.MyszkowskiCipher;
import nationalcipher.cipher.base.anew.NicodemusCipher;
import nationalcipher.cipher.base.anew.NihilistSubstitutionCipher;
import nationalcipher.cipher.base.anew.NihilistTranspositionCipher;
import nationalcipher.cipher.base.anew.PeriodicGromarkCipher;
import nationalcipher.cipher.base.anew.PhillipsCipher;
import nationalcipher.cipher.base.anew.PlayfairCipher;
import nationalcipher.cipher.base.anew.PolluxCipher;
import nationalcipher.cipher.base.anew.PortaxCipher;
import nationalcipher.cipher.base.anew.ProgressiveCipher;
import nationalcipher.cipher.base.anew.QuagmireICipher;
import nationalcipher.cipher.base.anew.QuagmireIICipher;
import nationalcipher.cipher.base.anew.QuagmireIIICipher;
import nationalcipher.cipher.base.anew.QuagmireIVCipher;
import nationalcipher.cipher.base.anew.RagbabyCipher;
import nationalcipher.cipher.base.anew.RailFenceCipher;
import nationalcipher.cipher.base.anew.RedefenceCipher;
import nationalcipher.cipher.base.anew.RouteTranspositionCipher;
import nationalcipher.cipher.base.anew.RunningKeyCipher;
import nationalcipher.cipher.base.anew.SeriatedPlayfairCipher;
import nationalcipher.cipher.base.anew.SlidefairCipher;
import nationalcipher.cipher.base.anew.SolitaireCipher;
import nationalcipher.cipher.base.anew.SwagmanCipher;
import nationalcipher.cipher.base.anew.TriSquareCipher;
import nationalcipher.cipher.base.anew.TrifidCipher;
import nationalcipher.cipher.base.anew.TwoSquareCipher;
import nationalcipher.cipher.base.anew.VigenereCipher;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.tools.KeyGeneration;

public class EncrypterRegistry {

    public static IRegistry<String, ICipher> RAND_ENCRYPTERS = Registry.builder(ICipher.class).build();
    private static Random RAND = new Random();

    public static ICipher<?> getFromName(String key) {
        return RAND_ENCRYPTERS.get(key);
    }

    public static int getDifficulty(String name) {
        return RAND_ENCRYPTERS.apply(name, ICipher::getDifficulty).orElse(Difficulty.EASY).getLevel();
    }

    @Nullable
    public static ICipher<?> getEncrypterWith(int maxDifficulty) {
        return RAND_ENCRYPTERS.getValues().stream().filter(v -> v.getDifficulty().isEasierThan(maxDifficulty)).sorted((o1, o2) -> RAND.nextBoolean() ? 1 : -1).findAny().orElse(null);
    }

    public static void registerAll() {

        RAND_ENCRYPTERS.registerAll(new ADFGXCipher(), new AffineCipher(), new AMSCOCipher());
        RAND_ENCRYPTERS.register(new BazeriesCipher());
        RAND_ENCRYPTERS.register(new BifidCipher());
        RAND_ENCRYPTERS.register(new CaesarCipher());
        RAND_ENCRYPTERS.register(new CadenusCipher());
        RAND_ENCRYPTERS.register(new ColumnarTranspositionCipher());
        RAND_ENCRYPTERS.register(new ConjugatedBifidCipher());
        RAND_ENCRYPTERS.register(new DigrafidCipher());
        RAND_ENCRYPTERS.register(new FourSquareCipher());
        RAND_ENCRYPTERS.register(new FractionatedMorseCipher());
        // TODO RAND_ENCRYPTERS.register(new GrilleCipher());
        RAND_ENCRYPTERS.register(new HomophonicCipher());
        RAND_ENCRYPTERS.register(new KeywordCipher());
        RAND_ENCRYPTERS.register(new MorbitCipher());
        RAND_ENCRYPTERS.register(new MyszkowskiCipher());

        for (VigenereType type : VigenereType.NORMAL_LIST) {
            String name = type.getClass().getSimpleName().toLowerCase();
            RAND_ENCRYPTERS.register(name + "_auto_key", new AutokeyCipher(type));
            RAND_ENCRYPTERS.register(name + "_nicodemus", new NicodemusCipher(type));
            RAND_ENCRYPTERS.register(name + "_progressive_key", new ProgressiveCipher(type));
            RAND_ENCRYPTERS.register(name, new VigenereCipher(type));
        }
        for (VigenereType type : VigenereType.SLIDEFAIR_LIST) {
            RAND_ENCRYPTERS.register(type.getClass().getSimpleName().toLowerCase() + "_slidefair", new SlidefairCipher(type));
        }

        RAND_ENCRYPTERS.register(new NihilistSubstitutionCipher());
        RAND_ENCRYPTERS.register(new NihilistTranspositionCipher());
        RAND_ENCRYPTERS.register(new PeriodicGromarkCipher());
        RAND_ENCRYPTERS.register(new PhillipsCipher());
        RAND_ENCRYPTERS.register(new PlayfairCipher());
        RAND_ENCRYPTERS.register("playfair6x6cipher",new PlayfairCipher(KeyGeneration.ALL_36_CHARS));
        RAND_ENCRYPTERS.register(new PolluxCipher());
        RAND_ENCRYPTERS.register(new PortaxCipher());
        RAND_ENCRYPTERS.registerAll(new QuagmireICipher(), new QuagmireIICipher(), new QuagmireIIICipher(), new QuagmireIVCipher());
        RAND_ENCRYPTERS.registerAll(new RailFenceCipher(), new RedefenceCipher());
        RAND_ENCRYPTERS.register(new RouteTranspositionCipher());
        RAND_ENCRYPTERS.register(new RunningKeyCipher());
        RAND_ENCRYPTERS.register(new SeriatedPlayfairCipher());
        RAND_ENCRYPTERS.register(new SolitaireCipher());
        RAND_ENCRYPTERS.register(new SwagmanCipher());
        RAND_ENCRYPTERS.register(new TrifidCipher());
        RAND_ENCRYPTERS.register(new TwoSquareCipher());
        RAND_ENCRYPTERS.register(new TriSquareCipher());
        RAND_ENCRYPTERS.register(new EnigmaPlugboardCipher(EnigmaLib.ENIGMA_M3));
        RAND_ENCRYPTERS.register("enigmaicipher", new EnigmaPlugboardCipher(EnigmaLib.ENIGMA_I));
        RAND_ENCRYPTERS.register("enigmadcipher", new EnigmaPlugboardCipher(EnigmaLib.ENIGMA_D)); //TODO add all machines
        RAND_ENCRYPTERS.register(new HillCipher());
        RAND_ENCRYPTERS.register(new HillExtendedCipher());
        RAND_ENCRYPTERS.register(new HillSubstitutionCipher());
        RAND_ENCRYPTERS.register(new RagbabyCipher());
        RAND_ENCRYPTERS.register(new GrilleCipher());
        RAND_ENCRYPTERS.register(new HuttonCipher());
    }

    static {
        registerAll();
        System.out.println(RAND_ENCRYPTERS.size());
    }
}
