package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.util.ArrayUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.QuadKeyCipher;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.util.CharArrayWrapper;

public class EnigmaCipher extends QuadKeyCipher<Integer[], Integer[], Integer[], Integer, OrderedIntegerKeyType.Builder, OrderedIntegerKeyType.Builder, OrderedIntegerKeyType.Builder, IntegerKeyType.Builder> {
    
    private EnigmaMachine machine;
    
    public EnigmaCipher(EnigmaMachine machine) {
        super(OrderedIntegerKeyType.builder().setSize(3).setEntryRange(26).setRepeats().setDisplay(CipherUtils::displayAsLetters), // indicator
                OrderedIntegerKeyType.builder().setSize(3).setEntryRange(26).setRepeats().setDisplay(CipherUtils::displayAsLetters), // ring
                OrderedIntegerKeyType.builder().setSize(3).setEntryRange(machine.getRotorCount()),
                IntegerKeyType.builder().setRange(0, machine.getReflectorCount() - 1)); //rotors
        this.machine = machine;
    }
    
    public EnigmaCipher setMachine(EnigmaMachine machine) {
        this.machine = machine;
        return this;
    }
    
    @Override
    public CharSequence encode(CharSequence plainText, QuadKey<Integer[], Integer[], Integer[], Integer> key, IFormat format) {
        return new CharArrayWrapper(this.decodeEfficently(plainText, key));
    }
    
    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, QuadKey<Integer[], Integer[], Integer[], Integer> key) {
        Integer[] indicator = ArrayUtil.copy(key.getFirstKey());
        Integer[] ring = ArrayUtil.copy(key.getSecondKey());

        int thinRotor = 0;
        
        //int reflectorSetting = 0;
        //int thinRotorSetting = 0;
        for(int i = 0; i < cipherText.length(); i++) {
            this.nextRotorPosition(key.getThirdKey(), indicator);

            int ch = cipherText.charAt(i) - 'A';

            if(this.machine.hasETW()) {
                ch = nextCharacter(ch, this.machine.getETWInverse());
            }

            for(int r = 2; r >= 0; r--) {
                ch = nextCharacter(ch, this.machine.rotors[key.getThirdKey()[r]], indicator[r] - ring[r]);
            }
 
            if(this.machine.hasThinRotor()) {
                ch = this.nextCharacter(ch, this.machine.thinRotor[thinRotor]);//, thinRotorSetting);
            }
            
            ch = nextCharacter(ch, this.machine.reflector[key.getFourthKey()]);//, reflectorSetting);
        
            if(this.machine.hasThinRotor()) {
                ch = this.nextCharacter(ch, this.machine.thinRotorInverse[thinRotor]);//, thinRotorSetting);
            }

            for(int r = 0; r < 3; r++) {
                ch = nextCharacter(ch, this.machine.rotorsInverse[key.getThirdKey()[r]], indicator[r] - ring[r]);
            }

            if(this.machine.hasETW()) {
                ch = nextCharacter(ch, this.machine.getETW());
            }

            plainText[i] = (char)(ch + 'A');
        }

        return plainText;
    }

    public void nextRotorPosition(Integer[] rotors, Integer[] indicator) {
        //Next settings
        if(this.machine.getStepping()) { //Ratchet Setting
            Integer[] middleNotches = this.machine.notches[rotors[1]];
            Integer[] endNotches = this.machine.notches[rotors[2]];

            if(ArrayUtil.contains(middleNotches, indicator[1])) {
                indicator[0] += 1;
                indicator[1] += 1;
                if(indicator[0] > 25) indicator[0] = 0;
                if(indicator[1] > 25) indicator[1] = 0;
            }

            if(ArrayUtil.contains(endNotches, indicator[2])) {
                indicator[1] += 1;
                if(indicator[1] > 25) indicator[1] = 0;
            }

            indicator[2] += 1;
            if(indicator[2] > 25) indicator[2] = 0;
        }
        else { //Cog Setting
            Integer[] endNotches = this.machine.notches[rotors[2]];
            if(ArrayUtil.contains(endNotches, indicator[2])) {
                Integer[] middleNotches = this.machine.notches[rotors[1]];

                if(ArrayUtil.contains(middleNotches, indicator[1])) {
                    //TODO need to add non fixed reflector
                    //int[] otherNotches = machine.notches[rotors[0]];

                    //if(ArrayUtil.contains(otherNotches, indicator[0]))
                    //  reflectorSetting = (reflectorSetting + 1) % 26;
                    
                    indicator[0] = (indicator[0] + 1) % 26;
                }
                indicator[1] = (indicator[1] + 1) % 26;
            }
            indicator[2] = (indicator[2] + 1) % 26;
        }
    }
    
    public int nextCharacter(int ch, Integer[] key) {
        return key[ch];
    }

    public int nextCharacter(int ch, Integer[] key, int offset) {
        if(offset > 0) {
            ch += offset;
            if(ch > 25) ch -= 26;
            ch = nextCharacter(ch, key);
            ch -= offset;
            if(ch < 0) ch += 26;
        }
        else if(offset < 0) {
            ch += offset;
            if(ch < 0) ch += 26;
            ch = nextCharacter(ch, key);
            ch -= offset;
            if(ch > 25) ch -= 26;
        }
        else
            ch = nextCharacter(ch, key);
        return ch;
    }
}
