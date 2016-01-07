package nationalcipher.cipher.transposition.route;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.cipher.transposition.RouteCipherType;

public class RouteWriteDown extends RouteCipherType {
	
	public RouteWriteDown(String description) {
		super(description);
	}

	@Override
	public List<Integer> createPattern(int width, int height, int totalSize) {
		ArrayList<Integer> grid = new ArrayList<Integer>();
		
		for(int i = 0; i < width; i++)
			for(int k = 0; k < height; k++)
				grid.add(k * width + i);
		System.out.println(grid);
		return grid;
	}
}