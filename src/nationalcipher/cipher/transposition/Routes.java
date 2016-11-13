package nationalcipher.cipher.transposition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javalibrary.math.MathUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import nationalcipher.cipher.transposition.route.RouteTwist;
import nationalcipher.cipher.transposition.route.RowRoute;
import nationalcipher.cipher.transposition.route.RouteWriteDown;


public class Routes {

	//MUST BE BEFORE
	public static List<RouteCipherType> ROUTES = new ArrayList<RouteCipherType>();
	
	public static List<RouteCipherType> getRoutes() {
		return Routes.ROUTES;
	}
	
	public static RouteCipherSpiral SPIRAL_CLOCKWISE_TOPRIGHT = new RouteCipherSpiral("Spiral inwards, clockwise, starting from the top right.") {
		
		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {width - 1, 0, 0, 1};
		}

		@Override
		public boolean isClockwise() {
			return true;
		}
	};
	
	public static RouteCipherSpiral SPIRAL_CLOCKWISE_TOPLEFT = new RouteCipherSpiral("Spiral inwards, clockwise, starting from the top left.") {
		
		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {0, 0, 1, 0};
		}

		@Override
		public boolean isClockwise() {
			return true;
		}
	};
	
	public static RouteCipherSpiral SPIRAL_CLOCKWISE_BOTLEFT = new RouteCipherSpiral("Spiral inwards, clockwise, starting from the bot left.") {
		
		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {0, height - 1, 0, -1};
		}

		@Override
		public boolean isClockwise() {
			return true;
		}
	};
	
	public static RouteCipherSpiral SPIRAL_CLOCKWISE_BOTRIGHT = new RouteCipherSpiral("Spiral inwards, clockwise, starting from the bot right.") {
		
		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {width - 1, height - 1, -1, 0};
		}

		@Override
		public boolean isClockwise() {
			return true;
		}
	};
	
	public static RouteCipherSpiral SPIRAL_ANTICLOCKWISE_TOPRIGHT = new RouteCipherSpiral("Spiral inwards, anit-clockwise, starting from the top right.") {
		
		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {width - 1, 0, -1, 0};
		}

		@Override
		public boolean isClockwise() {
			return false;
		}
	};
	
	public static RouteCipherSpiral SPIRAL_ANTICLOCKWISE_TOPLEFT = new RouteCipherSpiral("Spiral inwards, anit-clockwise, starting from the top left.") {
		
		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {0, 0, 0, 1};
		}

		@Override
		public boolean isClockwise() {
			return false;
		}
	};
	
	public static RouteCipherSpiral SPIRAL_ANTICLOCKWISE_BOTLEFT = new RouteCipherSpiral("Spiral inwards, anit-clockwise, starting from the bot left.") {
		
		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {0, height - 1, 1, 0};
		}

		@Override
		public boolean isClockwise() {
			return false;
		}
	};
	
	public static RouteCipherSpiral SPIRAL_ANTICLOCKWISE_BOTRIGHT = new RouteCipherSpiral("Spiral inwards, anit-clockwise, starting from the bot right.") {
		
		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {width - 1, height - 1, 0, -1};
		}

		@Override
		public boolean isClockwise() {
			return false;
		}
	};

	public static RouteCipherSnakeHorzi SNAKE_LEFTTORIGHT_TOP = new RouteCipherSnakeHorzi("Snake left to right, starting from the top.") {

		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {0, 1};
		}

		@Override
		public boolean isLeftToRight() {
			return true;
		}
	};
	
	public static RouteCipherSnakeHorzi SNAKE_LEFTTORIGHT_BOT = new RouteCipherSnakeHorzi("Snake left to right, starting from the bot.") {

		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {height - 1, -1};
		}

		@Override
		public boolean isLeftToRight() {
			return true;
		}
	};
	
	public static RouteCipherSnakeHorzi SNAKE_RIGHTTOLEFT_TOP = new RouteCipherSnakeHorzi("Snake right to left, starting from the top.") {

		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {0, 1};
		}

		@Override
		public boolean isLeftToRight() {
			return false;
		}
	};
	
	public static RouteCipherSnakeHorzi SNAKE_RIGHTTOLEFT_BOT = new RouteCipherSnakeHorzi("Snake right to left, starting from the bot.") {

		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {height - 1, -1};
		}

		@Override
		public boolean isLeftToRight() {
			return false;
		}
	};
	
	public static RouteCipherSnakeVerti SNAKE_TOPTOBOTTOM_LEFT = new RouteCipherSnakeVerti("Snake top to bottom, starting from the left.") {

		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {0, 1};
		}

		@Override
		public boolean isTopToBottom() {
			return true;
		}	
	};
	
	public static RouteCipherSnakeVerti SNAKE_TOPTOBOTTOM_RIGHT = new RouteCipherSnakeVerti("Snake top to bottom, starting from the right.") {

		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {width - 1, -1};
		}

		@Override
		public boolean isTopToBottom() {
			return true;
		}	
	};
	
	public static RouteCipherSnakeVerti SNAKE_BOTTOMTOTOP_LEFT = new RouteCipherSnakeVerti("Snake bottom to top, starting from the left.") {

		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {0, 1};
		}

		@Override
		public boolean isTopToBottom() {
			return false;
		}	
	};
	
	public static RouteCipherSnakeVerti SNAKE_BOTTOMTOTOP_RIGHT = new RouteCipherSnakeVerti("Snake bottom to top, starting from the right.") {

		@Override
		public int[] getStartDetails(int width, int height, int totalSize) {
			return new int[] {width - 1, -1};
		}

		@Override
		public boolean isTopToBottom() {
			return false;
		}	
	};
	
	public static RouteCipherDiagonal DIAG_BOTTOMTOTOP_LEFT = new RouteCipherDiagonal("Diagonal bottom to top, starting from the left.") {

		@Override
		public int[] calculateNewPos(int pos, int height, int width, int totalSize) {
			return new int[] {Math.max(0, pos - height + 1), Math.min(pos, height - 1)};
		}

		@Override
		public int[] getDirVector() {
			return new int[] {1, -1};
		}
	};
	
	public static RouteCipherDiagonal DIAG_BOTTOMTOTOP_RIGHT = new RouteCipherDiagonal("Diagonal bottom to top, starting from the right.") {

		@Override
		public int[] calculateNewPos(int pos, int height, int width, int totalSize) {
			return new int[] {Math.min(width - 1, (height + width - 1) - pos - 1), Math.min(pos, height - 1)};
		}

		@Override
		public int[] getDirVector() {
			return new int[] {-1, -1};
		}
	};
	
	public static RouteCipherDiagonal DIAG_TOPTOBOTTOM_LEFT = new RouteCipherDiagonal("Diagonal top to bottom, starting from the left.") {

		@Override
		public int[] calculateNewPos(int pos, int height, int width, int totalSize) {
			return new int[] {Math.max(0, pos - height + 1), height - 1 - Math.min(pos, height - 1)};
		}

		@Override
		public int[] getDirVector() {
			return new int[] {1, 1};
		}
	};
	
	public static RouteCipherDiagonal DIAG_TOPTOBOTTOM_RIGHT = new RouteCipherDiagonal("Diagonal top to bottom, starting from the right.") {

		@Override
		public int[] calculateNewPos(int pos, int height, int width, int totalSize) {
			return new int[] {Math.min(width - 1, (height + width - 1) - pos - 1), height - 1 - Math.min(pos, height - 1)};
		}

		@Override
		public int[] getDirVector() {
			return new int[] {-1, 1};
		}
	};
	
	public static RouteCipherRandom RANDOM = new RouteCipherRandom("Totally random.");
	
	//TODO Fix not working
	public static RouteCipherType ACROSS_1 = new RowRoute.TopLeft();
	public static RouteCipherType ACROSS_2 = new RowRoute.TopRight();
	public static RouteCipherType ACROSS_3 = new RowRoute.BottomLeft();
	public static RouteCipherType ACROSS_4 = new RowRoute.BottomRight();
	
	public static RouteWriteDown DOWN  = new RouteWriteDown("Written down.");
	
	public static OtherLetterCycleRoute OTHER_LETTER = new OtherLetterCycleRoute("Other letter route.");
	
	public static RouteTwist TWIST = new RouteTwist(1);
	
	/***            route cipher interfaces/classes                   ***/

	
	public static abstract class RouteCipherSpiral extends RouteCipherType {
		
		public RouteCipherSpiral(String description) {
			super(description);
		}
		
		public abstract int[] getStartDetails(int width, int height, int totalSize);
		public abstract boolean isClockwise();
		
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			ArrayList<Integer> grid = new ArrayList<Integer>();
			
			int[] details = this.getStartDetails(width, height, totalSize);
			int x = details[0], y = details[1], dx = details[2], dy = details[3];
			boolean clockwise = this.isClockwise();
			
			while(grid.size() < totalSize) {
				grid.add(y * width + x);
				
			    int nx = x + dx;
			    int ny = y + dy;
			    
			    if(nx < 0 || nx >= width || ny < 0 || ny >= height || grid.contains(ny * width + nx)) {
			        int temp = dy;
			        dy = (clockwise ? 1 : -1) * dx;
			        dx = (clockwise ? -1 : 1) * temp;
			    }
			    x += dx;
			    y += dy;
			}
			
			return ListUtil.toArray(grid);
		}
	}
	
	public static abstract class RouteCipherSnakeHorzi extends RouteCipherType {
		
		public RouteCipherSnakeHorzi(String description) {
			super(description);
		}
		
		public abstract int[] getStartDetails(int width, int height, int totalSize);
		public abstract boolean isLeftToRight();
		
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			
			int index = 0;
			
			int[] details = this.getStartDetails(width, height, totalSize);
			int startY = details[0];
			int dir = details[1];
			boolean leftToRight = this.isLeftToRight();
			
			for(int x = 0; x < width; x++) {
				int nx = leftToRight ? x : width - x - 1;
				for(int y = 0; y < height; y++)
					grid[index++] = (startY + dir * y) * width + nx;
				
				dir *= -1;
				startY = MathUtil.mod(startY + dir, height);
			}
			
			return grid;
		}
	}
	
	public static abstract class RouteCipherSnakeVerti extends RouteCipherType {
		
		public RouteCipherSnakeVerti(String description) {
			super(description);
		}
		
		public abstract int[] getStartDetails(int width, int height, int totalSize);
		public abstract boolean isTopToBottom();
		
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			
			int[] details = this.getStartDetails(width, height, totalSize);
			int startX = details[0];
			int dir = details[1];
			boolean topToBottom = this.isTopToBottom();
			int index = 0;
			
			for(int y = 0; y < height; y++) {
				int ny = topToBottom ? y : height - y - 1;
				for(int x = 0; x < width; x++)
					grid[index++] = ny * width + (startX + dir * x);
				
				dir *= -1;
				startX = MathUtil.mod(startX + dir, width);
			}
			
			return grid;
		}
	}
	
	public static abstract class RouteCipherDiagonal extends RouteCipherType {
		
		public RouteCipherDiagonal(String description) {
			super(description);
		}
		
		public abstract int[] calculateNewPos(int pos, int height, int width, int totalSize);
		public abstract int[] getDirVector();		
		
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			
			int[] dirVec = this.getDirVector();
			int index = 0;
			
			for(int pos = 0; pos < height + width - 1; pos++) {
				int[] npos = this.calculateNewPos(pos, height, width, totalSize);
				int nx = npos[0], ny = npos[1];
				
				while(nx >= 0 && nx < width && ny >= 0 && ny < height) {
					grid[index++] = ny * width + nx;
					nx += dirVec[0];
					ny += dirVec[1];
				}
			}

			return grid;
		}
	}
	
	public static class RouteCipherStrips extends RouteCipherType {
		
		public RouteCipherStrips(String description) {
			super(description);
		}
		
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			int[] grid = new int[totalSize];
			
			
			return grid;
		}
	}
	
	public static class RouteCipherRandom extends RouteCipherType {
		
		public RouteCipherRandom(String description) {
			super(description);
		}
		
		@Override
		public int[] createPattern(int width, int height, int totalSize) {
			List<Integer> grid = Arrays.asList(ArrayUtil.rangeInt(0, width * height));
			Collections.shuffle(grid, new Random(System.currentTimeMillis()));
			
			return ListUtil.toArray(grid);
		}
	}
}
