package nationalcipher.cipher.transposition;

public class OtherLetterCycleRoute extends RouteCipherType {

    public OtherLetterCycleRoute(String description) {
        super(description);
    }

    @Override
    public int[] createPattern(int width, int height, int totalSize) {
        int[] grid = new int[totalSize];
        int index = 0;

        for (int i = totalSize - 1; i >= 0; i -= 2)
            grid[index++] = i;
        for (int i = totalSize % 2; i < totalSize; i += 2)
            grid[index++] = i;

        return grid;
    }
}
