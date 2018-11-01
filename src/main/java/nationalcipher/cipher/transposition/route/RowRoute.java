package nationalcipher.cipher.transposition.route;

import nationalcipher.cipher.transposition.RouteCipherType;

public class RowRoute {
	
	public static class TopLeft extends RouteCipherType {
		public TopLeft() {
			super("Rows starting top left.");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			for(int r = 0; r < height; r++) 
				for(int c = 0; c < width; c++)
					grid[index++] = r * width + c;
	
			return grid;
		}
	}
	
	public static class TopRight extends RouteCipherType {
		public TopRight() {
			super("Rows starting top right.");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			for(int r = 0; r < height; r++) 
				for(int c = width - 1; c >= 0; c--)
					grid[index++] = r * width + c;
	
			return grid;
		}
	}
	
	public static class BottomRight extends RouteCipherType {
		public BottomRight() {
			super("Rows starting bottom right.");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			
			for(int r = height - 1; r >= 0; r--) 
				for(int c = width - 1; c >= 0; c--)
					grid[index++] = r * width + c;
	
	
			return grid;
		}
	}
	
	public static class BottomLeft extends RouteCipherType {
		public BottomLeft() {
			super("Rows starting bottom left.");
		}
	
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			int index = 0;
			
			for(int r = height - 1; r >= 0; r--) 
				for(int c = 0; c < width; c++)
					grid[index++] = r * width + c;
	
			return grid;
		}
	}
}