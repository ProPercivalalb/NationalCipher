package nationalcipher.cipher.setting;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    
    public static <K, C extends ICipher<K>> ICipherSettingBuilder<K, C> createIntSpinner(String id, int start, int min, int max, int step, BiConsumer<Integer, C> action) {
        return new ICipherSettingIntSpinner<K, C>(id).set(start, min, max, step).setAction(action);
    }
    
    public static <K, C extends ICipher<K>> ICipherSettingBuilder<K, C> createIntRange(String id, int minStart, int maxStart, int min, int max, int step, BiConsumer<int[], C> action) {
        return new ICipherSettingIntRange<K, C>(id).set(minStart, maxStart, min, max, step).setAction(action);
    }
    
    public static <K, C extends ICipher<K>, E> ICipherSettingBuilder<K, C> createCombo(String id, E[] items, BiConsumer<E, C> action) {
        return new ICipherSettingComboBox<K, C, E>(id).set(items).setAction(action);
    }
    
    public static class ICipherSettingIntSpinner<K, C extends ICipher<K>> implements ICipherSettingBuilder<K, C> {
        
        private String id;
        private int start, min, max, step;
        private BiConsumer<Integer, C> action;
        
        public ICipherSettingIntSpinner(String id) {
            this.id = id;
        }
        
        public ICipherSettingIntSpinner<K, C> set(int start, int min, int max, int step) {
            this.start = start;
            this.min = min;
            this.max = max;
            this.step = step;
            return this;
        }
        
        public ICipherSettingIntSpinner<K, C> setAction(BiConsumer<Integer, C> action) {
            this.action = action;
            return this;
        }
        
        @Override
        public ICipherSetting<K, C> create() {
            return new ICipherSetting<K, C>() {
                public JSpinner intSpinner = JSpinnerUtil.createSpinner(start, min, max, step);
                @Override
                public void addToInterface(JPanel panel) {
                    panel.add(new SubOptionPanel("Period Range:", this.intSpinner));
                }
                
                @Override
                public void apply(CipherAttack<K, C> attack) {
                    ICipherSettingIntSpinner.this.action.accept(SettingParse.getInteger(this.intSpinner), attack.getCipher());
                }

                @Override
                public void save(Map<String, Object> map) {
                    map.put(id, this.intSpinner.getValue());
                    
                }

                @Override
                public void load(Map<String, Object> map) {
                    this.intSpinner.setValue(map.getOrDefault("spinner", start));
                }
            };
        }
    };

    public static class ICipherSettingIntRange<K, C extends ICipher<K>> implements ICipherSettingBuilder<K, C> {
        
        private String id;
        private int minStart, maxStart, min, max, step;
        private BiConsumer<int[], C> action;
        
        public ICipherSettingIntRange(String id) {
            this.id = id;
        }
        
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
                
                @Override
                public void save(Map<String, Object> map) {
                    map.put(id, SettingParse.getIntegerRange(this.rangeSpinner));
                    
                }

                @Override
                public void load(Map<String, Object> map) {
                    List<Integer> range = (List<Integer>) map.getOrDefault("spinner_range", Arrays.asList(minStart, maxStart));
                    this.rangeSpinner[0].setValue(range.get(0));
                    this.rangeSpinner[1].setValue(range.get(1));
                }
            };
        }
    };
    
    public static class ICipherSettingComboBox<K, C extends ICipher<K>, E> implements ICipherSettingBuilder<K, C> {
        
        private String id;
        private E[] items;
        private BiConsumer<E, C> action;
        
        public ICipherSettingComboBox(String id) {
            this.id = id;
        }
        
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
                
                @Override
                public void save(Map<String, Object> map) {
                    map.put(id, this.comboBox.getSelectedIndex());
                    
                }

                @Override
                public void load(Map<String, Object> map) {
                    this.comboBox.setSelectedIndex(((Number) map.getOrDefault("combo", 0)).intValue());
                }
            };
        }
    };
}
