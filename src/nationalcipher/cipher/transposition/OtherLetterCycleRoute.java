package nationalcipher.cipher.transposition;

import java.util.ArrayList;
import java.util.List;

public class OtherLetterCycleRoute extends RouteCipherType {

	public OtherLetterCycleRoute(String description) {
		super(description);
	}
	
	@Override
	public List<Integer> createPattern(int width, int height, int totalSize) {
		ArrayList<Integer> grid = new ArrayList<Integer>();
		
		
		
		for(int i = totalSize - 1; i >= 0; i -= 2)
			grid.add(i);
		for(int i = totalSize % 2; i < totalSize; i += 2)
			grid.add(i);
		
		return grid;
	}
}
