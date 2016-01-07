package nationalcipher.cipher.transposition.route;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.cipher.transposition.RouteCipherType;

public class RouteWriteAcross extends RouteCipherType {
	
	public RouteWriteAcross(String description) {
		super(description);
	}

	@Override
	public List<Integer> createPattern(int width, int height, int totalSize) {
		ArrayList<Integer> grid = new ArrayList<Integer>();
		for(int i = 0; i < totalSize; i++)
			grid.add(i);

		return grid;
	}
}