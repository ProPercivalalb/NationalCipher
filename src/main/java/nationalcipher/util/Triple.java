package nationalcipher.util;

import java.util.Objects;

public class Triple<L, M, R> {

    private L left;
    private M middle;
    private R right;
    
    public Triple(L leftIn, M middleIn, R rightIn) {
        this.left = leftIn;
        this.middle = middleIn;
        this.right = rightIn;
    }
    
    public L getLeft() {
        return this.left;
    }
    
    public M getMiddle() {
        return this.middle;
    }
    
    public R getRight() {
        return this.right;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if(!(obj instanceof Triple)) {
            return false;
        } else {
            Triple<?, ?, ?> other = (Triple<?, ?, ?>)obj;
            
            return other.getLeft().equals(this.getLeft()) && other.getMiddle().equals(this.getMiddle())
                    && other.getRight().equals(this.getRight());
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.middle, this.right);
    }
}
