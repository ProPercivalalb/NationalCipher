package nationalcipher.cipher.transposition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class RouteCipherType {
	
	private String description;
	public HashMap<List<Integer>, int[]> cache = new HashMap<List<Integer>, int[]>();
	
	public RouteCipherType() {
		this("No description");
	}
	
	public RouteCipherType(String description) {
		this(description, true);
	}
	
	public RouteCipherType(String description, Object...obj) {
		this(String.format(description, obj));
	}
	
	public RouteCipherType(String description, boolean add) {
		this.description = description;
		if(add)
			Routes.ROUTES.add(this);
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public boolean canCache() {
		return true;
	}
	
	public final int[] getPattern(int width, int height, int totalSize) {
		if(!this.canCache())
			return this.createPattern(width, height, totalSize);
		
		List<Integer> key = Arrays.asList(width, height);
		if(this.cache.containsKey(key))
			return this.cache.get(key);
		
		int[] grid = this.createPattern(width, height, totalSize);
		this.cache.put(key, grid);
		
		return grid;
	}
	
	public abstract int[] createPattern(int width, int height, int totalSize);
}