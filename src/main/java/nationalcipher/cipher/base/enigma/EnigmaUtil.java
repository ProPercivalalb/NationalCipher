package nationalcipher.cipher.base.enigma;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javalibrary.util.ArrayUtil;

public class EnigmaUtil {

    public static final String POSTER_TEXT = "THEBODYTHATWASHEDUPONTHERIVERBANKBELONGEDTOAYOUNGSCIENTISTCALLEDJAMELIAITBOREALLTHEHALLMARKSOFAPROFESSIONALHITWHICHWOULDHAVEMADESENSEIFSHEWORKEDINNUCLEARPHYSICSORBIOWARFAREBUTSHEDIDNTSHEWORKEDONGRAVITYWAVESANDFORTHELIFEOFMEICOULDNTSEEHOWTHATWOULDHAVEGOTHERKILLED";

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
