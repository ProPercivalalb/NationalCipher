package nationalcipher.cipher.transposition;

import java.util.ArrayList;
import java.util.List;

import nationalcipher.cipher.transposition.Routes.RouteCipherType;

public class RouteWritten {

	public static RouteWriteAcross ACROSS = new RouteWriteAcross("Written across.");
	
	public static class RouteWriteAcross extends RouteCipherType {
		
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
	
	public static RouteWriteDown DOWN  = new RouteWriteDown("Written down.");
	
	public static class RouteWriteDown extends RouteCipherType {
		
		public RouteWriteDown(String description) {
			super(description);
		}

		@Override
		public List<Integer> createPattern(int width, int height, int totalSize) {
			ArrayList<Integer> grid = new ArrayList<Integer>();
			
			for(int i = 0; i < width; i++)
				for(int k = 0; k < height; k++)
					grid.add(k * width + i);
		
			return grid;
		}
	}
}
