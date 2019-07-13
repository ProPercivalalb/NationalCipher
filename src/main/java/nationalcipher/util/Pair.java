package nationalcipher.util;

import java.util.Objects;

public class Pair<L, R> {

    private L left;
    private R right;

    public Pair(L leftIn, R rightIn) {
        this.left = leftIn;
        this.right = rightIn;
    }

    public L getLeft() {
        return this.left;
    }

    public R getRight() {
        return this.right;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof Pair)) {
            return false;
        } else {
            Pair<?, ?> other = (Pair<?, ?>) obj;

            return other.getLeft().equals(this.getLeft()) && other.getRight().equals(this.getRight());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.right);
    }
}
