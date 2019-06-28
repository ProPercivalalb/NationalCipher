package nationalcipher.cipher.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.keys.MultiKey;

public abstract class MutliKeyHandler implements IKeyType<MultiKey> {
    
    private List<IKeyType<?>> handlers;
    
    public MutliKeyHandler(IKeyType<?>... handlersIn) {
        this.handlers = new ArrayList<>();
        for(IKeyType<?> key : handlersIn)
            this.handlers.add(key);
    }
    
    @Override
    public boolean isValid(MultiKey key) {
        for(int i = 0; i < this.handlers.size(); i++) {
            IKeyType<?> handler = handlers.get(i);
            //if(!handler.isValid(key.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public MultiKey randomise() {
        //TODO
        return null;
        //return new BiKey<>(this.firstHandler.randomise(), this.secondHandler.randomise());
    }
    
    @Override 
    public void iterateKeys(Consumer<MultiKey> consumer) {
      //TODO
    }
}
