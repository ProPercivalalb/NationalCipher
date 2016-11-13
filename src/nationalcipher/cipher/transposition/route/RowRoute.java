package nationalcipher.cipher.transposition.route;

import nationalcipher.cipher.transposition.RouteCipherType;

public class RowRoute {
	//TODO
	
	public static class TopLeft extends RouteCipherType {
		public TopLeft() {
			super("Rows starting top left");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;

			for(int c = 0; c < height; c++) 
			for(int r = 0; r < width; r++)
					grid[index++] = c * width + r;
	
			return grid;
		}
	}
	
	public static class TopRight extends RouteCipherType {
		public TopRight() {
			super("Rows starting top right");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			
			for(int c = 0; c < height; c++) 
			for(int r = width - 1; r >= 0; r--)
					grid[index++] = c * width + r;
	
			return grid;
		}
	}
	
	public static class BottomRight extends RouteCipherType {
		public BottomRight() {
			super("Rows starting bottom right");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			
			for(int c = height - 1; c >= 0; c--) 
			for(int r = width - 1; r >= 0; r--)
					grid[index++] = c * width + r;
	
			return grid;
		}
	}
	
	public static class BottomLeft extends RouteCipherType {
		public BottomLeft() {
			super("Rows starting bottom left");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			
			for(int c = height - 1; c >= 0; c--) 
			for(int r = 0; r < width; r++)
					grid[index++] = c * width + r;
	
			return grid;
		}
	}
}