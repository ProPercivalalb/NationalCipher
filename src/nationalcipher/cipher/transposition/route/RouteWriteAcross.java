package nationalcipher.cipher.transposition.route;

import nationalcipher.cipher.transposition.RouteCipherType;

public class RouteWriteAcross extends RouteCipherType {
	
	public RouteWriteAcross(String description) {
		super(description);
	}

	@Override
	public int[] createPattern(int width, int height, int totalSize) {
		int[] grid = new int[totalSize];
		int index = 0;
		
		for(int i = 0; i < totalSize; i++)
			grid[index++] = i;

		return grid;
	}
}