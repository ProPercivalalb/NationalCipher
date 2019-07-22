package nationalcipher.cipher.base.enigma;

import java.util.Arrays;

public class EnigmaMachine {

    // TODO CHANGE ALL THE PUBLIC FIELDS TO PRIVATE
    public String name;

    public Integer[][] rotors;
    public Integer[][] rotorsInverse;
    public Integer[][] notches;

    public Integer[][] reflector;
    public String[] reflectorNames;

    protected Integer[] etw;
    protected Integer[] etwInverse;

    public Integer[][] thinRotor;
    public Integer[][] thinRotorInverse;
    public String[] thinRotorNames;

    public boolean canPlugboard;
    public boolean canUhr;
    public boolean stepping;

    public EnigmaMachine(String name) {
        this.name = name;
        this.canPlugboard = false;
        this.canUhr = false;
        this.stepping = true;
    }

    public final void setRotors(String... input) {
        Integer[][] normal = new Integer[input.length][26];
        Integer[][] inverse = new Integer[normal.length][26];

        for (int r = 0; r < input.length; r++) {
            for (int i = 0; i < 26; i++) {
                normal[r][i] = input[r].charAt(i) - 'A';
                inverse[r][normal[r][i]] = i;
            }
        }

        this.rotors = normal;
        this.rotorsInverse = inverse;
    }

    public final void setNotches(Integer[][] input) {
        this.notches = input;
    }

    public final void setNotches(String... input) {
        Integer[][] normal = new Integer[input.length][];

        for (int r = 0; r < input.length; r++) {
            normal[r] = new Integer[input[r].length()];
            for (int i = 0; i < normal[r].length; i++)
                normal[r][i] = input[r].charAt(i) - 'A';
        }

        this.notches = normal;
    }

    public final void setReflectors(String... input) {
        Integer[][] normal = new Integer[input.length][26];

        for (int r = 0; r < input.length; r++)
            for (int i = 0; i < input[r].length(); i++)
                normal[r][i] = input[r].charAt(i) - 'A';

        this.reflector = normal;
    }

    public final void setReflectorNames(String... input) {
        this.reflectorNames = input;
    }

    public void setETW(String input) {
        this.etw = new Integer[26];
        for (int i = 0; i < input.length(); i++)
            this.etw[i] = input.charAt(i) - 'A';

        Integer[] inverse = new Integer[26];
        for (int i = 0; i < 26; i++)
            inverse[this.etw[i]] = i;

        this.etwInverse = inverse;
    }

    public final void setThinRotors(String... input) {
        Integer[][] normal = new Integer[input.length][26];
        Integer[][] inverse = new Integer[normal.length][26];

        for (int r = 0; r < input.length; r++) {
            for (int i = 0; i < 26; i++) {
                normal[r][i] = input[r].charAt(i) - 'A';
                inverse[r][normal[r][i]] = i;
            }
        }

        this.thinRotor = normal;
        this.thinRotorInverse = inverse;
    }

    public final void setThinRotorNames(String... input) {
        this.thinRotorNames = input;
    }

    public final int getRotorCount() {
        return this.rotors.length;
    }

    public final int getReflectorCount() {
        return this.reflector.length;
    }

    public final int getThinRotorCount() {
        return this.thinRotor.length;
    }

    public final boolean canPlugboard() {
        return this.canPlugboard;
    }

    public final boolean canUhr() {
        return this.canUhr;
    }

    public boolean hasETW() {
        return this.etw != null;
    }
    
    public boolean hasThinRotor() {
        return this.thinRotor != null;
    }
    

    public Integer[] getETW() {
        return this.etw;
    }
    
    public Integer[] getETWInverse() {
        return this.etwInverse;
    }

    /**
     * True is the classic Ratchets setting and False is cogs
     * 
     * @return
     */
    public final boolean getStepping() {
        return this.stepping;
    }

    public boolean isSolvable() {
        return true;
    }

    public EnigmaMachine createWithPlugboard(int[]... input) {
        EnigmaMachine copy = new EnigmaPlugboard(this.name);
        EnigmaMachine.copy(this, copy);

        Integer[] plugBoardArray = new Integer[26];
        for (int i = 0; i < 26; i++)
            plugBoardArray[i] = i;

        for (int[] swap : input) {
            if (swap[0] == swap[1])
                continue;

            plugBoardArray[swap[0]] = swap[1];
            plugBoardArray[swap[1]] = swap[0];
        }

        copy.etw = plugBoardArray;
        copy.etwInverse = plugBoardArray;
        return copy;
    }

    public EnigmaMachine createWithPresetPlugboard(Integer[] plugBoardArray) {
        EnigmaMachine copy = new EnigmaPlugboard(this.name);
        EnigmaMachine.copy(this, copy);

        copy.etw = plugBoardArray;
        copy.etwInverse = plugBoardArray;

        return copy;
    }

    public EnigmaMachine createWithUhr(int setting, char[][] input) {
        EnigmaMachine copy = new EnigmaUhr(this.name, input);
        EnigmaMachine.copy(this, copy);

        Integer[] plugBoardArray = new Integer[26];
        for (int i = 0; i < 26; i++)
            plugBoardArray[i] = i;
        int uhrIndex = 0;
        for (char[] swap : input) {
            if (swap[0] == 0 || swap[1] == 0)
                continue;

            int aWire = (uhrIndex * 4 + setting) % 40;
            int bWirePosition = (EnigmaLib.AB_WIRING[aWire] + (40 - setting)) % 40;
            plugBoardArray[swap[0] - 'A'] = input[EnigmaLib.B_PLUG_ORDER[bWirePosition / 4]][1] - 'A';

            int bWire = (EnigmaLib.B_PLUG_ORDER_INDEXED[uhrIndex] * 4 + setting) % 40;
            int aWirePosition = (EnigmaLib.AB_WIRING_INDEXED[bWire] + (40 - setting)) % 40;
            plugBoardArray[swap[1] - 'A'] = input[aWirePosition / 4][0] - 'A';
            uhrIndex++;
        }

        Integer[] inverse = new Integer[26];
        for (int i = 0; i < 26; i++)
            inverse[plugBoardArray[i]] = i;

        copy.etwInverse = inverse;
        copy.etw = plugBoardArray;

        return copy;
    }

    public EnigmaMachine createWithUhr(int setting, String... input) {
        if (input.length == 10) {
            char[][] raw = new char[10][3];
            for (int i = 0; i < input.length; i++)
                raw[i] = input[i].toCharArray();
            return createWithUhr(setting, raw);
        }
        return null;
    }

    public static void copy(EnigmaMachine orignal, EnigmaMachine copy) {
        copy.rotors = orignal.rotors;
        copy.rotorsInverse = orignal.rotorsInverse;
        copy.notches = orignal.notches;
        copy.reflector = orignal.reflector;
        copy.reflectorNames = orignal.reflectorNames;
        copy.etw = orignal.etw;
        copy.etwInverse = orignal.etwInverse;
        copy.thinRotor = orignal.thinRotor;
        copy.thinRotorInverse = orignal.thinRotorInverse;
        copy.canPlugboard = orignal.canPlugboard;
        copy.canUhr = orignal.canUhr;
        copy.stepping = orignal.stepping;
    }

    public static class EnigmaPlugboard extends EnigmaMachine {

        public int plugCount;

        public EnigmaPlugboard(String name) {
            super(name);
        }

        @Override
        public String toString() {
            return String.format("%s, %s", this.name, EnigmaUtil.displayPlugboard(this.etw));
        }
    }

    public static class EnigmaUhr extends EnigmaMachine {

        public char[][] input;

        public EnigmaUhr(String name, char[][] input) {
            super(name);
            this.input = input;
        }

        @Override
        public String toString() {
            return String.format("%s, Plugboard:%s", this.name, Arrays.toString(this.etw));
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
