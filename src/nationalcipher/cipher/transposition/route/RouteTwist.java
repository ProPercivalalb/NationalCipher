package nationalcipher.cipher.transposition.route;

import java.util.ArrayList;
import java.util.List;

import javalibrary.math.MathHelper;
import nationalcipher.cipher.transposition.RouteCipherType;

public class RouteTwist extends RouteCipherType {

	public int twistSize;
	
	public RouteTwist(int twistSize) {
		super("Twist across step %d.", twistSize);
		this.twistSize = twistSize;
	}

	@Override
	public List<Integer> createPattern(int width, int height, int totalSize) {
		ArrayList<Integer> grid = new ArrayList<Integer>();
		
		for(int h = 0; h < height; h++) {
			int cut = MathHelper.mod(width - h * this.twistSize, width);
			for(int w = 0; w < width; w++)
				grid.add(h * width + (cut + w) % width);
		}
		
		return grid;
	}

}
