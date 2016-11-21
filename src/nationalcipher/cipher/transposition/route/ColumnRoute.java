package nationalcipher.cipher.transposition.route;

import nationalcipher.cipher.transposition.RouteCipherType;

public class ColumnRoute {
	
	public static class TopLeft extends RouteCipherType {
		public TopLeft() {
			super("Columns starting top left.");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			for(int c = 0; c < width; c++)
				for(int r = 0; r < height; r++) 
					grid[index++] = r * width + c;
	
			return grid;
		}
	}
	
	public static class TopRight extends RouteCipherType {
		public TopRight() {
			super("Columns starting top right.");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			for(int c = width - 1; c >= 0; c--)
				for(int r = 0; r < height; r++) 
					grid[index++] = r * width + c;
	
			return grid;
		}
	}
	
	public static class BottomRight extends RouteCipherType {
		public BottomRight() {
			super("Columns starting bottom right.");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			for(int c = width - 1; c >= 0; c--)
				for(int r = height - 1; r >= 0; r--) 
					grid[index++] = r * width + c;
	
	
			return grid;
		}
	}
	
	public static class BottomLeft extends RouteCipherType {
		public BottomLeft() {
			super("Columns starting bottom left.");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			for(int c = 0; c < width; c++)
				for(int r = height - 1; r >= 0; r--) 
					grid[index++] = r * width + c;
	
			return grid;
		}
	}
}