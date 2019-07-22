package nationalcipher.cipher.base.enigma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import javalibrary.util.ArrayUtil;

public class EnigmaUtil {

    public static final String POSTER_TEXT = "THEBODYTHATWASHEDUPONTHERIVERBANKBELONGEDTOAYOUNGSCIENTISTCALLEDJAMELIAITBOREALLTHEHALLMARKSOFAPROFESSIONALHITWHICHWOULDHAVEMADESENSEIFSHEWORKEDINNUCLEARPHYSICSORBIOWARFAREBUTSHEDIDNTSHEWORKEDONGRAVITYWAVESANDFORTHELIFEOFMEICOULDNTSEEHOWTHATWOULDHAVEGOTHERKILLED";

    public static void main(String[] args) {
        // Ring EFA, Indicator: EGP
        String cipherText = "DFJAMZGHTRDKCUINKJCYPLFWDMTOTGCFHYMWFKCHGDQOOKZVPTARLTUKNSIXBRZMHNQUQSMZJYMMLDOCZUGJGQWYTKPWSSOSPMEYVRHYCQQEFGZFUXJNNCVWOHPMKEJQMDKTGWXCAZTDDCNTHXRYUFIWALGLNQFWPIPCYSMCTBZJGHXAFWXZNRFXINZNTAXYHMRUKWKCHNVZMLPOSZFTWGIHDLJVQKNHAAVUUGANCVJJXZRQRSTZXSNCGCOGEIVIMNDBWM";
        String crib = POSTER_TEXT;
        EnigmaMachine machine = EnigmaLib.ENIGMA_I;
        int[] rotors = new int[] { 2, 0, 3 };
        int reflectorIndex = 1;

        List<Integer> positions = useCribInCipherText(cipherText, crib);
        System.out.println(positions);
        System.out.println(String.format("%d out of a possible %d positions for the crib are valid.", positions.size(), cipherText.length() - crib.length() + 1));

        for (int pos : positions) {
            for (int i = 0; i < 26; i++) {
                for (int i2 = 0; i2 < 26; i2++) {
                    for (int i3 = 0; i3 < 26; i3++) {
                        int[] set = new int[] { i, i2, i3 };
                        int ch = 6;

                        for (int r = 2; r >= 0; r--)
                            ch = Enigma.nextCharacter(ch, machine.rotors[rotors[r]], set[r]);

                        ch = Enigma.nextCharacter(ch, machine.reflector[reflectorIndex]);

                        for (int r = 0; r < 3; r++)
                            ch = Enigma.nextCharacter(ch, machine.rotorsInverse[rotors[r]], set[r]);
                        if (ch == 3)
                            System.out.println(String.format("Pos:%s %c", Arrays.toString(set), ch));
                    }
                }
            }
        }

        System.out.println("Done");
    }

    public static List<Integer> useCribInCipherText(String cipherText, String crib) {
        List<Integer> positions = new ArrayList<Integer>();

        nextPos: for (int i = 0; i < cipherText.length() - crib.length() + 1; i++) {
            for (int j = 0; j < crib.length(); j++)
                if (cipherText.charAt(i + j) == crib.charAt(j))
                    continue nextPos;
            positions.add(i);
        }
        return positions;
    }

    public static String displayPlugboard(Integer[] mapping) {
        StringJoiner joiner = new StringJoiner(" ", "[", "]");
        List<Integer> plugsUsed = new ArrayList<>(mapping.length / 2);
        
        for (int i = 0; i < mapping.length; i++) {
            if (!plugsUsed.contains(i)) {
                int other = ArrayUtil.indexOf(mapping, i);
                if (other == i) continue;
                
                joiner.add(String.format("%c%c", (char) (i + 'A'), (char) (other + 'A')));
                plugsUsed.add(i);
                plugsUsed.add(other);
            }
        }
        
        return joiner.toString();
    }
}
