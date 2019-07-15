package nationalcipher.cipher.base.anew;

public enum AMSCOType {
    DOUBLE_FIRST(0),
    SINGLE_FIRST(1);
    
    private int mod;
    
    private AMSCOType(int mod) {
        this.mod = mod;
    }
    
    public int getMod() {
        return this.mod;
    }
}