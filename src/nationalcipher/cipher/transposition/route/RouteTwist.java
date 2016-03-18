package nationalcipher.cipher.transposition.route;

import javalibrary.math.MathHelper;
import nationalcipher.cipher.transposition.RouteCipherType;

public class RouteTwist extends RouteCipherType {

	public int twistSize;
	
	public RouteTwist(int twistSize) {
		super("Twist across step %d.", twistSize);
		this.twistSize = twistSize;
	}

	@Override
	public int[] createPattern(int width, int height, int totalSize) {
		int[] grid = new int[totalSize];
		int index = 0;
		
		for(int h = 0; h < height; h++) {
			int cut = MathHelper.mod(width - h * this.twistSize, width);
			for(int w = 0; w < width; w++)
				grid[index++] = h * width + (cut + w) % width;
		}
		
		return grid;
	}

}
