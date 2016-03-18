package nationalcipher.cipher.transposition.route;

import nationalcipher.cipher.transposition.RouteCipherType;

public class RouteWriteDown extends RouteCipherType {
	
	public RouteWriteDown(String description) {
		super(description);
	}

	@Override
	public int[] createPattern(int width, int height, int totalSize) {
		int[] grid = new int[totalSize];
		int index = 0;
		
		for(int i = 0; i < width; i++)
			for(int k = 0; k < height; k++)
				grid[index++] = k * width + i;
		System.out.println(grid);
		return grid;
	}
}