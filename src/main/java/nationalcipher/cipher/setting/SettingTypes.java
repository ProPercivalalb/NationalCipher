package nationalcipher.cipher.setting;

import java.util.function.BiConsumer;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.api.ICipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;

public class SettingTypes {
    
    public static <K, C extends ICipher<K>> ICipherSetting<K, C> createIntRange(int minStart, int maxStart, int min, int max, int step, BiConsumer<int[], C> action) {
        return new ICipherSettingIntRange<K, C>().set(minStart, maxStart, min, max, step).setAction(action).create();
    }
    
    public static <K, C extends ICipher<K>, E> ICipherSetting<K, C> createCombo(E[] items, BiConsumer<E, C> action) {
        return new ICipherSettingComboBox<K, C, E>().set(items).setAction(action).create();
    }

    public static class ICipherSettingIntRange<K, C extends ICipher<K>> implements ICipherSettingBuilder<K, C> {
        
        private int minStart, maxStart, min, max, step;
        private BiConsumer<int[], C> action;
        
        public ICipherSettingIntRange<K, C> set(int minStart, int maxStart, int min, int max, int step) {
            this.minStart = minStart;
            this.maxStart = maxStart;
            this.min = min;
            this.max = max;
            this.step = step;
            return this;
        }
        
        public ICipherSettingIntRange<K, C> setAction(BiConsumer<int[], C> action) {
            this.action = action;
            return this;
        }
        
        @Override
        public ICipherSetting<K, C> create() {
            return new ICipherSetting<K, C>() {
                public JSpinner[] rangeSpinner = JSpinnerUtil.createRangeSpinners(minStart, maxStart, min, max, step);
                @Override
                public void addToInterface(JPanel panel) {
                    panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
                }
                
                @Override
                public void apply(CipherAttack<K, C> attack) {
                    ICipherSettingIntRange.this.action.accept(SettingParse.getIntegerRange(this.rangeSpinner), attack.getCipher());
                }
            };
        }
    };
    
    public static class ICipherSettingComboBox<K, C extends ICipher<K>, E> implements ICipherSettingBuilder<K, C> {
        
        private E[] items;
        private BiConsumer<E, C> action;
        
        public ICipherSettingComboBox<K, C, E> set(E... items) {
            this.items = items;
            return this;
        }
        
        public ICipherSettingComboBox<K, C, E> setAction(BiConsumer<E, C> action) {
            this.action = action;
            return this;
        }
        
        @Override
        public ICipherSetting<K, C> create() {
            return new ICipherSetting<K, C>() {
                public JComboBox<E> comboBox = new JComboBox<>(ICipherSettingComboBox.this.items);
                @Override
                public void addToInterface(JPanel panel) {
                    panel.add(new SubOptionPanel("Options", this.comboBox));
                }
                
                @SuppressWarnings("unchecked")
                @Override
                public void apply(CipherAttack<K, C> attack) {
                    ICipherSettingComboBox.this.action.accept((E)this.comboBox.getSelectedItem(), attack.getCipher());
                }
            };
        }
    };
}
